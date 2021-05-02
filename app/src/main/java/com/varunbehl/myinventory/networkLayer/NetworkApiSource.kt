package com.varunbehl.myinventory.networkLayer

import com.varunbehl.myinventory.datamodel.MyExcelResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

 interface NetworkApiSource {
    @GET("api?id=1ToVasqEKcaQ7reGd-iinEARdQA29xYeA1EBf6GchplQ&sheet=1")
    fun getMyApiResponse() : Deferred<MyExcelResponse>
 }
