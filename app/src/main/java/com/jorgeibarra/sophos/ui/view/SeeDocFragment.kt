package com.jorgeibarra.sophos.ui.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jorgeibarra.sophos.R
import com.jorgeibarra.sophos.databinding.FragmentSeeDocBinding
import com.jorgeibarra.sophos.ui.adapter.ItemsDocsAdapter
import com.jorgeibarra.sophos.ui.viewmodel.GetDocsByIdViewModel
import com.jorgeibarra.sophos.ui.viewmodel.GetDocsViewModel


class SeeDocFragment : Fragment(R.layout.fragment_see_doc) {

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


        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Regresar")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        val email = arguments?.getString("user_email")!!

        getDocsModel.getDocsModelLiveData.observe(viewLifecycleOwner, Observer {
            getDocsModel.getDocsList(email)
            initRecyclerView()
        })

        getDocsModel.getDocsList(email)

        getDocsByIdViewModel.getDocsImgMutableLiveData.observe(viewLifecycleOwner, Observer {
            val imageBase64 = getDocsByIdViewModel.getDocsImgMutableLiveData.value?.get(0)?.Adjunto
            if(imageBase64?.contains("9j",true) == true){
                val imgConverted = decodePicString(imageBase64)
                binding.ivSeeDocsImage.setImageBitmap(imgConverted)
            }
            else{
                Toast.makeText(context, "NO IMAGE", Toast.LENGTH_SHORT).show()
            }

        })


        return binding.root
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sendDocFragment -> {
                view?.findNavController()
                    ?.navigate(
                        SeeDocFragmentDirections.actionGlobalSendDocFragment(
                            arguments?.getString("user_email"),
                            arguments?.getString("user_name")
                        )
                    )
                true
            }
            R.id.seeDocFragment -> {
                view?.findNavController()?.navigate(
                    SeeDocFragmentDirections.actionGlobalSeeDocFragment(
                        arguments?.getString("user_email"),
                        arguments?.getString("user_name")
                    )
                )
                true
            }
            R.id.officeFragment -> {
                view?.findNavController()?.navigate(
                    SeeDocFragmentDirections.actionGlobalOfficeFragment(
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