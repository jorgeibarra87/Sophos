package com.jorgeibarra.sophos.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jorgeibarra.sophos.R
import com.jorgeibarra.sophos.data.model.OfficeApiResponse
import com.jorgeibarra.sophos.databinding.FragmentOfficeBinding
import com.jorgeibarra.sophos.ui.viewmodel.GetOfficesViewModel


class OfficeFragment : Fragment(), OnMapReadyCallback {

    private val getOfficesViewModel: GetOfficesViewModel by viewModels()

    private var citiesObserved: MutableList<OfficeApiResponse> = mutableListOf()


    private val REQUEST_CODE_LOCATION = 0

    private lateinit var map: GoogleMap

    private var _binding: FragmentOfficeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOfficeBinding.inflate(inflater,container,false)

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.setTitle("Regresar")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)


        getOfficesViewModel.citiesLiveData.observe(viewLifecycleOwner, Observer {
            getOfficesViewModel.getOffices()
            for (cities in getOfficesViewModel.citiesLiveData.value!!) {
                citiesObserved.add(cities)
            }
            createMarker()
        })
        createFragment()

        return binding.root
    }

    private fun createFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        enableLocation()

    }

    private fun createMarker() {

        for (cities in citiesObserved) {
            val marker = MarkerOptions()
                .position(LatLng(cities.Latitud.toDouble(), cities.Longitud.toDouble()))
                .title(cities.Nombre)
            map.addMarker(marker)


        }
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
            //the camera location
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(4.7109886, -74.072092), 12f), 4000, null
            )
        } else {
            requestLocationPermission()
        }

    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            //String for toast
            val locationPermission = "Habilitar permiso de localización en configuración"
            Toast.makeText(context, locationPermission, Toast.LENGTH_SHORT)
                .show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true

            } else {
                val locationPermission = "Habilitar permiso de localización en configuración"
                Toast.makeText(
                    context,
                    locationPermission,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_global_startFragment -> {
                view?.findNavController()
                    ?.navigate(
                        OfficeFragmentDirections.actionGlobalStartFragment(
                            arguments?.getString("user_email"),
                            arguments?.getString("user_name")
                        )
                    )
                true
            }
            R.id.sendDocFragment -> {
                view?.findNavController()
                    ?.navigate(
                        OfficeFragmentDirections.actionGlobalSendDocFragment(
                            arguments?.getString("user_email"),
                            arguments?.getString("user_name")
                        )
                    )
                true
            }
            R.id.seeDocFragment -> {
                view?.findNavController()?.navigate(
                    OfficeFragmentDirections.actionGlobalSeeDocFragment(
                        arguments?.getString("user_email"),
                        arguments?.getString("user_name")
                    )
                )
                true
            }
            R.id.officeFragment -> {
                view?.findNavController()?.navigate(
                    OfficeFragmentDirections.actionGlobalOfficeFragment(
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