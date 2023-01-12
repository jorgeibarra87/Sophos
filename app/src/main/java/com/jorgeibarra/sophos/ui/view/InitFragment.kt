package com.jorgeibarra.sophos.ui.view

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.jorgeibarra.sophos.R
import com.jorgeibarra.sophos.databinding.FragmentInitBinding
import com.jorgeibarra.sophos.ui.viewmodel.UserViewModel


class InitFragment : Fragment() {

    private val userViewModel:UserViewModel by viewModels()

    private var userName:String? = null

    private var _binding: FragmentInitBinding? = null
    private val binding get() = _binding!!
    private lateinit var user:String
    private lateinit var password:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInitBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEnter.setOnClickListener {
        user = binding.etEmail.text.toString().trim()
        password = binding.etPass.text.toString().trim()

        userViewModel.getUserViewModel(user,password)

        userViewModel.userApiResponse.observe(viewLifecycleOwner, Observer {
            if(it.body()?.acceso == true) {
                binding.btnEnter.isEnabled = false
                savePreferences()
                userName = it.body()!!.nombre
                goToStartFragment()

                } else{
                    Toast.makeText(activity, "credentials invalid", Toast.LENGTH_SHORT).show()

                }

        })

        }

    }

    private fun goToStartFragment() {
        view?.findNavController()
            ?.navigate(
                InitFragmentDirections.actionInitFragmentToStartFragment(
                    userName,
                    binding.etEmail.text?.trim().toString()
                )
            )

        userViewModel.cleanLiveData()
    }

    private fun savePreferences() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPass.text.toString().trim()
        val data = sharedPrefs.edit()
        data.putString("email", email)
        data.putString("password", password)
        data.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}