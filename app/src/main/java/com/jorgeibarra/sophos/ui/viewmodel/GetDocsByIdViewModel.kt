package com.jorgeibarra.sophos.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeibarra.sophos.core.RetrofitHelper
import com.jorgeibarra.sophos.data.model.DocApiResponse
import com.jorgeibarra.sophos.data.model.DocResponse
import com.jorgeibarra.sophos.data.network.APIGetDocById
import kotlinx.coroutines.launch
import retrofit2.Response


class GetDocsByIdViewModel: ViewModel() {

    var getDocsImgMutableLiveData = MutableLiveData<List<DocApiResponse>>()

    fun getDocsViewModel (idDoc:String) {
        viewModelScope.launch {
            val response : Response<DocResponse> = RetrofitHelper.getRetrofit().create(APIGetDocById::class.java)
                .getSpecificDoc(idDoc)

            val docsInfo = response.body()

            getDocsImgMutableLiveData.postValue(docsInfo?.Items)

            }

}


}