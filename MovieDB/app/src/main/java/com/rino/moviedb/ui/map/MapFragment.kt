package com.rino.moviedb.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.rino.moviedb.R
import com.rino.moviedb.databinding.FragmentMapBinding
import com.rino.moviedb.utils.hideKeyboard
import com.rino.moviedb.utils.openAppSystemSettings
import com.rino.moviedb.utils.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment() {

    companion object {
        const val ADDRESS_TAG = "ADDRESS_TAG"

        fun newInstance(address: String) = MapFragment().apply {
            arguments?.putString(ADDRESS_TAG, address)
        }
    }

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel: MapViewModel by viewModel()

    private var defaultAddress: String? = null
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

        defaultAddress?.let { mapViewModel.searchByLocationName(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let { defaultAddress = it.getString(ADDRESS_TAG) }
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
                mapViewModel.searchByLocationName(query ?: defaultAddress)
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}