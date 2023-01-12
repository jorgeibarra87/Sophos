package com.jorgeibarra.sophos.data.network

import com.jorgeibarra.sophos.data.model.DocResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIGetDocs {
    //this brings the complete list of documents
    @GET("/RS_Documentos")
    suspend fun getDocs(
        @Query("correo") email:String
    ): Response<DocResponse>


}