package com.example.tugasakhira12.Service

import com.example.tugasakhira12.model.Kursus
import com.example.tugasakhira12.model.KursusDetailResponse
import com.example.tugasakhira12.model.KursusResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface KursusService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )

    @GET(".")
    suspend fun getKursus(): KursusResponse

    @GET("{id_kursus}")
    suspend fun getKursusByID(@Path("id_kursus")id_kursus: String): KursusDetailResponse

    @POST("add")
    suspend fun insertKursus(@Body kursus: Kursus)

    @PUT("{id_kursus}")
    suspend fun updateKursus(@Path("id_kursus")id_kursus: String, @Body kursus: Kursus)

    @DELETE("{id_kursus}")
    suspend fun deleteKursus(@Path("id_kursus")id_kursus: String): Response<Void>

}