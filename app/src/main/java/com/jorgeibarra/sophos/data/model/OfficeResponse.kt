package com.jorgeibarra.sophos.data.model

data class OfficeResponse(
    var Items : List<OfficeApiResponse>,
    var Count : Int,
    var ScannedCount: Int
)
