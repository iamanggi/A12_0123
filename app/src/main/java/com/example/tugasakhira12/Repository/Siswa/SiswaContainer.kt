package com.example.tugasakhira12.Repository.Siswa

import com.example.tugasakhira12.Service.Siswa.SiswaService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer{
    val siswaRepository: SiswaRepository
}

class SiswaContainer : AppContainer{
    private val baseUrl = "http://10.0.2.2:3000/api/Siswa/" //local host diganti ip klo run dihp
    //10.0.2.2 ip address dari emulator
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl).build()

    private val siswaService: SiswaService by lazy { retrofit.create(SiswaService::class.java) }
    override val siswaRepository:SiswaRepository by lazy { NetworkSiswaRepository(siswaService) }
}