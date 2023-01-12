package com.jorgeibarra.sophos.data.model

data class UserApiResponse (
    val id: String,
    val nombre:String,
    val apellido:String,
    var acceso:Boolean,
    val admin:Boolean
    )