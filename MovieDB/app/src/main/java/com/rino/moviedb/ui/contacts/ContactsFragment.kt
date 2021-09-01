package com.rino.moviedb.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.rino.moviedb.databinding.FragmentContactsBinding
import com.rino.moviedb.entities.Contact
import com.rino.moviedb.utils.sendSms
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactsFragment : Fragment() {
    companion object {
        const val MESSAGE_ARG = "MESSAGE_ARG"

        fun newInstance(message: String) =
            ContactsFragment().apply {
                arguments = Bundle().apply {
                    putString(MESSAGE_ARG, message)
                }
            }
    }

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val contactsViewModel: ContactsViewModel by viewModel()

    private var message: String? = null

    private val onItemClickListener by lazy {
        object : ContactsAdapter.OnItemClickListener {
            override fun onItemClick(contact: Contact) {
                contact.phoneNumber?.let {
                    requireContext().sendSms(it, message ?: "")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { message = it.getString(MESSAGE_ARG) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contactsViewModel.fetchContacts(requireContext())
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.contactsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        contactsViewModel.contacts.observe(viewLifecycleOwner) {
            it?.let {
                binding.contactsRecyclerView.adapter = ContactsAdapter(it, onItemClickListener)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}