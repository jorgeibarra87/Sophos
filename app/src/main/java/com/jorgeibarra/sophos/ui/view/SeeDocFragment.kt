package com.jorgeibarra.sophos.ui.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jorgeibarra.sophos.databinding.FragmentSeeDocBinding
import com.jorgeibarra.sophos.ui.adapter.ItemsDocsAdapter
import com.jorgeibarra.sophos.ui.viewmodel.GetDocsByIdViewModel
import com.jorgeibarra.sophos.ui.viewmodel.GetDocsViewModel


class SeeDocFragment : Fragment() {

    private val getDocsModel: GetDocsViewModel by viewModels()
    private val getDocsByIdViewModel: GetDocsByIdViewModel by viewModels()
    private var _binding: FragmentSeeDocBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSeeDocBinding.inflate(inflater,container,false)
        val view = binding.root

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = arguments?.getString("user_name")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)



        val email = arguments?.getString("user_email")!!

        getDocsModel.getDocsModelLiveData.observe(viewLifecycleOwner, Observer {
            getDocsModel.getDocsList(email)
            initRecyclerView()

        })

        getDocsModel.getDocsList(email)

        getDocsByIdViewModel.getDocsImgMutableLiveData.observe(viewLifecycleOwner, Observer {
            val imageBase64 = getDocsByIdViewModel.getDocsImgMutableLiveData.value?.get(0)?.Adjunto
            if(imageBase64?.contains("9j",true) == true){ //Makes sure it is an img in base64
                val imgConverted = decodePicString(imageBase64)
                binding.ivSeeDocsImage.setImageBitmap(imgConverted)
            }
            else{
                Toast.makeText(context, "NO IMAGE", Toast.LENGTH_SHORT).show()
            }

        })


        return view
    }
    private fun initRecyclerView() {
        val manager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, manager.orientation)


        binding.rvDocList.layoutManager = manager
        binding.rvDocList.adapter = ItemsDocsAdapter(getDocsModel.getDocsModelLiveData.value)

        {
            getDocsByIdViewModel.getDocsViewModel(it.IdRegistro)
        }

        binding.rvDocList.addItemDecoration(decoration)
    }

    fun decodePicString(encodedString: String): Bitmap {
        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}