package com.jorgeibarra.sophos.ui.view

import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.jorgeibarra.sophos.R
import com.jorgeibarra.sophos.databinding.FragmentStartBinding



class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStartBinding.inflate(inflater,container,false)
        val view = binding.root
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.title = arguments?.getString("user_name")

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.btnEnter1.setOnClickListener{
            view?.findNavController()
                ?.navigate(
                    StartFragmentDirections.actionStartFragmentToSendDocFragment(
                        arguments?.getString("user_email"),
                        arguments?.getString("user_name")
                    )
                )
        }
        binding.btnEnter2.setOnClickListener{
            view?.findNavController()?.navigate(
                StartFragmentDirections.actionStartFragmentToSeeDocFragment(
                    arguments?.getString("user_email"),
                    arguments?.getString("user_name")
                )
            )
        }
        binding.btnEnter3.setOnClickListener {
            view?.findNavController()?.navigate(
                StartFragmentDirections.actionStartFragmentToOfficeFragment(
                    arguments?.getString("user_email"),
                    arguments?.getString("user_name")
                )
            )
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sendDocFragment -> {
                view?.findNavController()
                    ?.navigate(
                        StartFragmentDirections.actionGlobalSendDocFragment(
                            arguments?.getString("user_email"),
                            arguments?.getString("user_name")
                        )
                    )
                true
            }
            R.id.seeDocFragment -> {
                view?.findNavController()?.navigate(
                    StartFragmentDirections.actionGlobalSeeDocFragment(
                        arguments?.getString("user_email"),
                        arguments?.getString("user_name")
                    )
                )
                true
            }
            R.id.officeFragment -> {
                view?.findNavController()?.navigate(
                    StartFragmentDirections.actionGlobalOfficeFragment(
                        arguments?.getString("user_email"),
                        arguments?.getString("user_name")
                    )
                )
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}