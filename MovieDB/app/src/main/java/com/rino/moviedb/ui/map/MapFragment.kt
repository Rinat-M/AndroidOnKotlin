package com.rino.moviedb.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rino.moviedb.R
import com.rino.moviedb.databinding.FragmentMapBinding
import com.rino.moviedb.utils.hideKeyboard
import com.rino.moviedb.utils.openAppSystemSettings
import com.rino.moviedb.utils.showSnackBar
import com.rino.moviedb.utils.showToast
import com.rino.moviedb.wrappers.GeofencesWrapper
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment() {

    companion object {
        private const val TAG = "MapFragment"
        private const val GEOFENCE_RADIUS = 200.0f

        fun newInstance(address: String) = MapFragment().apply {
            arguments = MapFragmentArgs(address).toBundle()
        }
    }

    private val args: MapFragmentArgs by navArgs()

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel: MapViewModel by viewModel()

    private lateinit var map: GoogleMap

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                binding.root.showSnackBar(
                    R.string.need_permissions_to_location,
                    actionStringId = R.string.settings,
                    action = { context?.openAppSystemSettings() }
                )
            }
        }

    @SuppressLint("MissingPermission")
    private val onMapReadyCallback = OnMapReadyCallback { googleMap ->
        map = googleMap

        with(map.uiSettings) {
            isZoomControlsEnabled = true
            isMyLocationButtonEnabled = true
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        args.address?.let { mapViewModel.searchByLocationName(it) }

        map.setOnMapLongClickListener { latLng ->
            if (Build.VERSION.SDK_INT >= 29) {
                // need background permission to work with geofence
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    addAndDrawGeofence(latLng)
                } else {
                    permissionResult.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }

            } else {
                addAndDrawGeofence(latLng)
            }
        }
    }

    private fun addAndDrawGeofence(latLng: LatLng) {
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("${latLng.latitude},${latLng.longitude}")
        )
        addCircle(latLng)
        addGeofence(latLng)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(onMapReadyCallback)

        mapViewModel.markerOptions.observe(viewLifecycleOwner) { markerOptions ->
            markerOptions?.let {
                val marker = map.addMarker(it)
                marker?.let { markerNotNull ->
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            markerNotNull.position,
                            15f
                        )
                    )
                }
            }
        }

        binding.mapSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mapViewModel.searchByLocationName(query ?: args.address)
                context?.hideKeyboard(binding.root)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = true
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.map_menu_mode_normal -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.map_menu_mode_satellite -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.map_menu_mode_terrain -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        R.id.map_menu_traffic -> {
            map.isTrafficEnabled = !map.isTrafficEnabled
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun addCircle(latLng: LatLng) {
        val circleOptions = CircleOptions().apply {
            center(latLng)
            radius(GEOFENCE_RADIUS.toDouble())
            strokeColor(ContextCompat.getColor(requireContext(), R.color.red))
            fillColor(ContextCompat.getColor(requireContext(), R.color.red_with_alpha))
            strokeWidth(4f)
        }
        map.addCircle(circleOptions)
    }

    private fun addGeofence(latLng: LatLng) {
        val geofences = listOf(latLng)
        GeofencesWrapper(context)
            .addGeofences(geofences, GEOFENCE_RADIUS)
            ?.run {
                addOnSuccessListener {
                    // Geofences added
                    Log.d(TAG, "Geofences added")
                }
                addOnFailureListener { exception ->
                    // Failed to add geofences
                    val errorMessage = GeofencesWrapper.getErrorString(exception)
                    errorMessage?.let { context?.showToast(it) }
                    Log.e(TAG, "Failed to add geofences with error: $errorMessage", exception)
                }
            }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}