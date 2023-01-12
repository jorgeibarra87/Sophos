package com.jorgeibarra.sophos.data.network

import com.jorgeibarra.sophos.data.model.UserApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiClient {
    @GET("/RS_Usuarios")
    suspend fun getUser(
        @Query("idUsuario") userEmail:String,
        @Query("clave") userPassword:String
        ):Response<UserApiResponse>
}