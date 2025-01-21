package com.example.tugasakhira12.Service

import com.example.tugasakhira12.model.Siswa
import com.example.tugasakhira12.model.SiswaDetailResponse
import com.example.tugasakhira12.model.SiswaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface SiswaService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )

    @GET(".")
    suspend fun getSiswa(): SiswaResponse

    @GET("{id_siswa}")
    suspend fun getSiswaByID(@Path("id_siswa")id_siswa: String): SiswaDetailResponse

    @POST("add")
    suspend fun insertSiswa(@Body siswa: Siswa)

    @PUT("{id_siswa}")
    suspend fun updateSiswa(@Path("id_siswa")id_siswa: String,@Body siswa: Siswa)

    @DELETE("{id_siswa}")
    suspend fun deleteSiswa(@Path("id_siswa")id_siswa: String): Response<Void>

}