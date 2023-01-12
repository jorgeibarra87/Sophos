package com.google.SophosReto.repository.model

import com.jorgeibarra.sophos.data.model.OfficeResponse
import retrofit2.Response
import retrofit2.http.GET


interface APIGetOffices {

    @GET("RS_Oficinas")
    suspend fun getCities()
    : Response<OfficeResponse>

}