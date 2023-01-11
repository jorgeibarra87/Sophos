package com.jorgeibarra.sophos.model

data class UserModel (
    val id: String,
    val nombre:String,
    val apellido:String,
    val acceso:Boolean,
    val admin:Boolean
    )