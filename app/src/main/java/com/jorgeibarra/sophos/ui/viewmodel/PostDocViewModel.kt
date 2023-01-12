package com.jorgeibarra.sophos.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.SophosReto.repository.model.APIGetOffices
import com.jorgeibarra.sophos.core.RetrofitHelper
import com.jorgeibarra.sophos.data.model.DocApiResponse
import com.jorgeibarra.sophos.data.model.DocPostApiResponse
import com.jorgeibarra.sophos.data.network.ApiPostDoc
import kotlinx.coroutines.launch


class PostDocViewModel : ViewModel() {

    init {
        getCities()
    }

    private var _citiesLiveData = MutableLiveData<MutableList<String>>()
    val citiesLiveData: LiveData<MutableList<String>>
    get() = _citiesLiveData


    val docModel = MutableLiveData<DocApiResponse>()


    fun postDoc(DocInput: DocPostApiResponse) {
        viewModelScope.launch {
            RetrofitHelper.getRetrofit().create(ApiPostDoc::class.java)
                .postDoc(DocInput)

        }

    }

    fun getCities() {
        viewModelScope.launch {
            val response = RetrofitHelper.getRetrofit().create(APIGetOffices::class.java)
                .getCities()

            val cities = response.body()?.Items

            //brings the cities available in the API
            if (cities != null) {
                val citiesList = mutableSetOf<String>()
                for (city in cities.indices) {
                    citiesList.add(cities[city].Ciudad)
                }
                _citiesLiveData.postValue(citiesList.toMutableList())
            }
        }

    }

}