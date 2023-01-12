package com.jorgeibarra.sophos.data.network

import com.jorgeibarra.sophos.data.model.DocResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIGetDocById {
    @GET("/RS_Documentos")
    suspend fun getSpecificDoc(
        @Query("idRegistro") idRegister:String
    ): Response<DocResponse>
}