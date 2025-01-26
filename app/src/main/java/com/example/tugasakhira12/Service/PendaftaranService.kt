package com.example.tugasakhira12.Service

import com.example.tugasakhira12.model.PendDetailResponse
import com.example.tugasakhira12.model.PendResponse
import com.example.tugasakhira12.model.Pendaftaran
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PendaftaranService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )

    @GET("pendaftaran/.")
    suspend fun getPendaftaran(): PendResponse

    @GET("pendaftaran/{id_pendaftaran}")
    suspend fun getPendaftaranByID(@Path("id_pendaftaran")id_pendaftaran: String): PendDetailResponse

    @POST("pendaftaran/add")
    suspend fun insertPendaftaran(@Body pendaftaran: Pendaftaran)

    @PUT("pendaftaran/{id_pendaftaran}")
    suspend fun updatePendaftaran(@Path("id_pendaftaran")id_pendaftaran: String, @Body pendaftaran: Pendaftaran)

    @DELETE("pendaftaran/{id_pendaftaran}")
    suspend fun deletePendaftaran(@Path("id_pendaftaran")id_pendaftaran: String): Response<Void>

}