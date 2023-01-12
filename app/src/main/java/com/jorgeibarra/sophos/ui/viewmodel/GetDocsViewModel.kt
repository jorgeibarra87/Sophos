package com.jorgeibarra.sophos.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeibarra.sophos.core.RetrofitHelper
import com.jorgeibarra.sophos.data.network.APIGetDocs
import com.jorgeibarra.sophos.data.model.DocResponse
import com.jorgeibarra.sophos.data.model.DocApiResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response


class GetDocsViewModel : ViewModel() {

    val getDocsModelLiveData = MutableLiveData<List<DocApiResponse>>()

    fun getDocsList(emailLogin: String) {
        viewModelScope.launch {
            val response: Response<DocResponse> = RetrofitHelper.getRetrofit().create(APIGetDocs::class.java)
                .getDocs(emailLogin)
            val docsInfo = response.body()
            getDocsModelLiveData.postValue(docsInfo?.Items)
            viewModelScope.cancel()
        }
    }


}