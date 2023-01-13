package com.jorgeibarra.sophos.ui.view

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.jorgeibarra.sophos.databinding.FragmentInitBinding
import com.jorgeibarra.sophos.ui.viewmodel.UserViewModel
import java.util.concurrent.Executor


class InitFragment : Fragment() {

    private val userViewModel:UserViewModel by viewModels()

    private var userName:String? = null
    private var useremal:String? = null
    private lateinit var user:String
    private lateinit var password:String

    private lateinit var executor: Executor
    private lateinit var biometricMessage: androidx.biometric.BiometricPrompt
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo

    private var _binding: FragmentInitBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInitBinding.inflate(inflater,container,false)

        validateBiometric()

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
                    Toast.makeText(activity, "credenciales invalidas", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //---------------------------FINGER BUTTON INIT--------------------------------//
        binding.btnFinger.setOnClickListener {
            savePreferencesFinger()
            fingerAuthentication()
            biometricMessage.authenticate(promptInfo)

        }
        //-----------------------------------------------------------------------------//


        binding.etEmail.setText("jorgeibarra87@gmail.com")
        binding.etPass.setText("PIpP0553v049")

        return binding.root
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

    private fun goToStartFragment() {
        view?.findNavController()
            ?.navigate(
                InitFragmentDirections.actionGlobalStartFragment(
                    userName,
                    binding.etEmail.text.toString().trim()
                )
            )
        userViewModel.cleanLiveData()
    }

    //------------------------FINGERPRINT------------------------------//
    private fun savePreferencesFinger() :Boolean{
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val email = sharedPrefs.getString("email", "")
        val password = sharedPrefs.getString("password", "")
        if (email == "" || password == "") {
            Toast.makeText(activity,"Faltan datos", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

        private fun fingerAuthentication() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricMessage = androidx.biometric.BiometricPrompt(this, executor,
        object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(context, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }
            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(context,"Se autentico correctamente" ,Toast.LENGTH_SHORT).show()

                getFingerPreferences()

                val emailUser = binding.etEmail.text?.trim().toString()
                val passwordUser = binding.etPass.text?.trim().toString()
                userViewModel.getUserViewModel(emailUser, passwordUser)
                userViewModel.userApiResponse.observe(viewLifecycleOwner, Observer {
                    if(it.body()?.acceso == true){
                        userName = it.body()!!.nombre
                        view?.findNavController()
                            ?.navigate(
                                InitFragmentDirections.actionGlobalStartFragment(
                                    userName,
                                    binding.etEmail.text.toString().trim()
                                )
                            )
                    } else{
                        Toast.makeText(context,"Datos incorrectos" ,Toast.LENGTH_SHORT).show()
                    }
                })

            }
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context,"Fallo Autenticación" ,Toast.LENGTH_SHORT).show()
            }
        })
        val title = "Biometrico"
        val subtitle = "Ingreso Biometrico"
        val negative = "Iniciar con credenciales"
        promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negative)
            .build()
    }

    private fun getFingerPreferences() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val email = sharedPrefs.getString("email", "")
        val password = sharedPrefs.getString("password", "")

        binding.etEmail.setText(email)
        binding.etPass.setText(password)
    }

    private fun validateBiometric() {

        val biometricManager = androidx.biometric.BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("Sophos", "App can authenticate using biometrics.")
                Toast.makeText(context,"Biometrico Disponible" ,Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("Sophos", "No biometric features available on this device.")
                Toast.makeText(context,"Biometrico no disponible en este dispositivo" ,Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d("Sophos", "Biometric features are currently unavailable.")
                Toast.makeText(context,"Biometrico no disponible actualmente" ,Toast.LENGTH_SHORT).show()
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }
                startActivityForResult(enrollIntent, 100)
            }
        }
    }

    //----------------------------------------------------------------//

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}