package com.example.tugasakhira12.Service

import com.example.tugasakhira12.model.InstDetailResponse
import com.example.tugasakhira12.model.InstResponse
import com.example.tugasakhira12.model.Instruktur
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface InstrukturService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )

    @GET("instruktur/.")
    suspend fun getInstruktur(): InstResponse

    @GET("instruktur/{id_instruktur}")
    suspend fun getInstrukturByID(@Path("id_instruktur")id_instruktur: String): InstDetailResponse

    @POST("instruktur/add")
    suspend fun insertInstruktur(@Body instruktur: Instruktur)

    @PUT("instruktur/{id_instruktur}")
    suspend fun updateInstruktur(@Path("id_instruktur")id_instruktur: String, @Body instruktur: Instruktur)

    @DELETE("instruktur/{id_instruktur}")
    suspend fun deleteInstruktur(@Path("id_instruktur")id_instruktur: String): Response<Void>

}