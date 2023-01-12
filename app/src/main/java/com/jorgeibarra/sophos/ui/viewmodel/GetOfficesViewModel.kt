package com.jorgeibarra.sophos.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.SophosReto.repository.model.APIGetOffices
import com.jorgeibarra.sophos.core.RetrofitHelper
import com.jorgeibarra.sophos.data.model.OfficeApiResponse
import com.jorgeibarra.sophos.data.model.OfficeResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response


class GetOfficesViewModel : ViewModel() {

    init {
        getOffices()
    }

    val citiesLiveData = MutableLiveData<List<OfficeApiResponse>>()

    fun getOffices (){
        viewModelScope.launch {
            val response: Response<OfficeResponse> = RetrofitHelper.getRetrofit().create(
                APIGetOffices::class.java)
                .getCities()

            val citiesInfo = response.body()

            citiesLiveData.postValue(citiesInfo?.Items)

            viewModelScope.cancel()

        }
    }
}