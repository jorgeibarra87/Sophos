package com.jorgeibarra.sophos.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeibarra.sophos.core.RetrofitHelper
import com.jorgeibarra.sophos.data.model.UserApiResponse
import com.jorgeibarra.sophos.data.network.UserApiClient
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel:ViewModel() {
    private val _userResponse : MutableLiveData<Response<UserApiResponse>> = MutableLiveData()
    val userApiResponse: LiveData<Response<UserApiResponse>>
    get() = _userResponse

    fun getUserViewModel(user:String, pass:String) =
        viewModelScope.launch {
            _userResponse.value =
                RetrofitHelper.getRetrofit().create(UserApiClient::class.java)
                    .getUser(user, pass)
        }

    fun cleanLiveData (){
        _userResponse.value?.body()?.acceso = false
    }

}