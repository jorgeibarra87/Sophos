package com.jorgeibarra.sophos.ui.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEnter1.setOnClickListener{
            findNavController().navigate(R.id.action_startFragment_to_sendDocFragment)
        }
        binding.btnEnter2.setOnClickListener{
            findNavController().navigate(R.id.action_startFragment_to_seeDocFragment)
        }
        binding.btnEnter3.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_officeFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}