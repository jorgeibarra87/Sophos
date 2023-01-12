package com.jorgeibarra.sophos.data.model

data class DocResponse(
    var Items: List<DocApiResponse>,
    var Count: Int,
    var ScannedCount: Int
)
