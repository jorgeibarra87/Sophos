package com.jorgeibarra.sophos.ui.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jorgeibarra.sophos.R
import com.jorgeibarra.sophos.data.model.DocPostApiResponse
import com.jorgeibarra.sophos.databinding.FragmentSendDocBinding
import com.jorgeibarra.sophos.databinding.FragmentStartBinding
import com.jorgeibarra.sophos.ui.viewmodel.PostDocViewModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SendDocFragment : Fragment(R.layout.fragment_send_doc), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentSendDocBinding? = null

    private val postDocViewModel: PostDocViewModel by viewModels()

    lateinit var arrayAdapterTypeDocs: ArrayAdapter<String>

    lateinit var arrayAdapterCities: ArrayAdapter<String>

    private val PERMISSION_CAMARA: Int = 100
    private val CAMARA_REQUEST_CODE: Int = 101
    private val PERMISSION_EXTERNAL_STORAGE: Int = 100
    private val IMAGE_REQUEST_CODE: Int = 102
    private lateinit var citySelected: String
    private lateinit var typeDocsSelected: String
    private val calendar: Calendar = Calendar.getInstance()
    private val currentDate = SimpleDateFormat("d/M/yyyy").format(calendar.time)

    private var imageTakenBase64 = ""

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSendDocBinding.inflate(inflater,container,false)


        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Regresar")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        arrayAdapterTypeDocs =
            ArrayAdapter<String>(requireContext(), R.layout.spinner)

        arrayAdapterCities = ArrayAdapter<String>(requireContext(), R.layout.spinner)

        val typeID = "Tipo de documento"

        arrayAdapterTypeDocs.addAll(Arrays.asList(typeID, "CC", "TI", "PA", "CE"))

        postDocViewModel.citiesLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            val city: String = "Ciudad"
            arrayAdapterCities.addAll(
                Arrays.asList(
                    city,
                    postDocViewModel.citiesLiveData.value?.get(0).toString(),
                    postDocViewModel.citiesLiveData.value?.get(1).toString(),
                    postDocViewModel.citiesLiveData.value?.get(2).toString(),
                    postDocViewModel.citiesLiveData.value?.get(3).toString(),
                    postDocViewModel.citiesLiveData.value?.get(4).toString(),
                    postDocViewModel.citiesLiveData.value?.get(5).toString()
                )

            )
        })

        binding.spDocType.adapter = arrayAdapterTypeDocs
        binding.spDocCity.adapter = arrayAdapterCities

        binding.spDocType.onItemSelectedListener = this
        binding.spDocCity.onItemSelectedListener = this

        binding.etEmailSendDocs.setText(arguments?.getString("user_email"))

        binding.ivTakePhotoDocs.setOnClickListener {
            askForCameraPermission()
        }

        binding.btnAttachDoc.setOnClickListener {
            askForFilesPermission()
        }

        postDocViewModel.docModel.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            postDocViewModel.postDoc(getInformationForPosting())
        })

        binding.btnSendDoc.setOnClickListener {
            val documentSent: String = "Documento Enviado"
            val documentSentFail: String = "Debe llenar todos los datos"

            when (validateData()) {
                true -> {
                    postDocViewModel.postDoc(getInformationForPosting())
                    showMessage(documentSent)
                    println(getInformationForPosting())
                    goToFragmentSendDocs()
                }
                else -> showMessage(documentSentFail)
            }

        }

        return binding.root
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (parent?.id) {
            R.id.spDocType -> typeDocsSelected = arrayAdapterTypeDocs.getItem(position).toString()
            R.id.spDocCity -> citySelected = arrayAdapterCities.getItem(position).toString()
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun askForCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                takePhoto()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                val permissionDeniedMessage = "Se nego el permiso, habilítelo en configuracion"
                showMessage(permissionDeniedMessage)

            }
            else -> {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), PERMISSION_CAMARA)
            }

        }
    }

    private fun askForFilesPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> {
                uploadPhoto()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                val permissionDeniedMessage = "Se nego el permiso, habilítelo en configuracion"
                showMessage(permissionDeniedMessage)

            }
            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_EXTERNAL_STORAGE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMARA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto()
                }
            }

            PERMISSION_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    uploadPhoto()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }

    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMARA_REQUEST_CODE)
    }

    private fun uploadPhoto() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes =
                arrayOf("images/jpeg", "images/jpg", "images/png") //only allows this format
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, IMAGE_REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMARA_REQUEST_CODE -> {
                if (resultCode != Activity.RESULT_OK) {
                    val photoNotTaken = "no se tomo la foto"
                    showMessage(photoNotTaken)
                } else {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    imageTakenBase64 = convertBitmapToBase64(bitmap)

                }
            }
            IMAGE_REQUEST_CODE -> {
                if (requestCode == IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {

                    val bitmap = convertUriToBitmap(data?.data)
                    imageTakenBase64 = convertBitmapToBase64(bitmap)

                } else {
                    //String for message
                    val imgNotUploaded = "no cargo la imagen"
                    showMessage(imgNotUploaded)
                }
            }
        }
    }


    fun convertBitmapToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val image = stream.toByteArray()
        return android.util.Base64.encodeToString(image, android.util.Base64.DEFAULT)
    }

    fun convertUriToBitmap(uri: Uri?): Bitmap {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(requireContext().contentResolver, uri!!)
            ImageDecoder.decodeBitmap(source)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun getInformationForPosting(): DocPostApiResponse {
        return DocPostApiResponse(
            currentDate,
            typeDocsSelected,
            binding.etIDNumberSendDocs.text.toString().trim(),
            binding.etNamesSendDocs.text.toString().trim(),
            binding.etLastNameSendDocs.text.toString().trim(),
            citySelected,
            binding.etEmailSendDocs.text.toString().trim(),
            binding.etDocTypeSendDocs.text.toString().trim(),
            imageTakenBase64

        )
    }

    private fun validateData(): Boolean {
        if (getInformationForPosting().TipoId == "Tipo de documento" ||
            getInformationForPosting().Identificacion == "" ||
            getInformationForPosting().Nombre == "" ||
            getInformationForPosting().Apellido == "" ||
            getInformationForPosting().Correo == "" ||
            getInformationForPosting().Ciudad == "Ciudad" ||
            getInformationForPosting().TipoAdjunto == "" ||
            getInformationForPosting().Adjunto == "" ||
            getInformationForPosting().TipoAdjunto == ""
        ) {
            return false
        }
        return true
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
                        SendDocFragmentDirections.actionGlobalStartFragment(
                            arguments?.getString("user_email"),
                            arguments?.getString("user_name")
                        )
                    )
                true
            }
            R.id.sendDocFragment -> {
                view?.findNavController()
                    ?.navigate(
                        SendDocFragmentDirections.actionGlobalSendDocFragment(
                            arguments?.getString("user_email"),
                            arguments?.getString("user_name")
                        )
                    )
                true
            }
            R.id.seeDocFragment -> {
                view?.findNavController()?.navigate(
                    SendDocFragmentDirections.actionGlobalSeeDocFragment(
                        arguments?.getString("user_email"),
                        arguments?.getString("user_name")
                    )
                )
                true
            }
            R.id.officeFragment -> {
                view?.findNavController()?.navigate(
                    SendDocFragmentDirections.actionGlobalOfficeFragment(
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

    private fun goToFragmentSendDocs() {
        view?.findNavController()
            ?.navigate(
                SendDocFragmentDirections.actionGlobalSendDocFragment(
                    arguments?.getString("user_name"),
                    arguments?.getString("user_email")
                )
            )
    }

    //-----------------------------------FINGERPRINT---------------------------------------//

    //-------------------------------------------------------------------------------------//

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}