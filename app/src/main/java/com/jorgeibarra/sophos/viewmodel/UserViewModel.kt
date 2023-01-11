package com.jorgeibarra.sophos.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jorgeibarra.sophos.model.UserModel

class UserViewModel:ViewModel() {
    val userModel = MutableLiveData<UserModel>()
}