package com.jorgeibarra.sophos.ui.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jorgeibarra.sophos.data.model.DocApiResponse
import com.jorgeibarra.sophos.databinding.ItemSeeDocsBinding

class ItemsDocsViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    val binding = ItemSeeDocsBinding.bind(view)

    fun render(getDocsViewModel: DocApiResponse?, onItemSelected: (DocApiResponse) -> Unit) {
        binding.tvAttachedType.text = getDocsViewModel?.TipoAdjunto
        binding.tvItemFecha.text = getDocsViewModel?.Fecha
        binding.tvNameItem.text = getDocsViewModel?.Nombre
        binding.tvLastNameItem.text = getDocsViewModel?.Apellido

        binding.root.setOnClickListener {
            if (getDocsViewModel != null) {
                onItemSelected(getDocsViewModel)

            }
        }
    }
}