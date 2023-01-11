package com.jorgeibarra.sophos.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.jorgeibarra.sophos.R
<<<<<<<< HEAD:app/src/main/java/com/jorgeibarra/sophos/view/LoginFragment.kt
import com.jorgeibarra.sophos.databinding.FragmentLoginBinding
========
import com.jorgeibarra.sophos.databinding.FragmentInitBinding
>>>>>>>> temp-branch:app/src/main/java/com/jorgeibarra/sophos/view/InitFragment.kt

class InitFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEnter.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_startFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}