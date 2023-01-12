package com.jorgeibarra.sophos.data.network

import com.jorgeibarra.sophos.data.model.DocPostApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiPostDoc {
//This sends the corresponding info to post a doc
    @POST("/RS_Documentos")
    suspend fun postDoc(
        @Body docInfo: DocPostApiResponse
    )
}