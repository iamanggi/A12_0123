package com.example.tugasakhira12.Repository

import com.example.tugasakhira12.Repository.Instruktur.InstrukturRepository
import com.example.tugasakhira12.Repository.Instruktur.NetworkInstrukturRepository
import com.example.tugasakhira12.Repository.Kursus.KursusRepository
import com.example.tugasakhira12.Repository.Kursus.NetworkKursusRepository
import com.example.tugasakhira12.Repository.Pendaftaran.NetworkPendRepository
import com.example.tugasakhira12.Repository.Pendaftaran.PendaftaranRepository
import com.example.tugasakhira12.Repository.Siswa.NetworkSiswaRepository
import com.example.tugasakhira12.Repository.Siswa.SiswaRepository
import com.example.tugasakhira12.Service.InstrukturService
import com.example.tugasakhira12.Service.KursusService
import com.example.tugasakhira12.Service.PendaftaranService
import com.example.tugasakhira12.Service.SiswaService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val siswaRepository: SiswaRepository
    val instrukturRepository: InstrukturRepository
    val kursusRepository: KursusRepository
    val pendaftaranRepository: PendaftaranRepository
}

class MainAppContainer : AppContainer {
    private val siswaContainer = SiswaContainer()
    private val instrukturContainer = InstrukturContainer()
    private val kursusContainer = KursusContainer()
    private val pendaftaranContainer = PendaftaranContainer()


    override val siswaRepository: SiswaRepository
        get() = siswaContainer.siswaRepository

    override val instrukturRepository: InstrukturRepository
        get() = instrukturContainer.instrukturRepository

    override val kursusRepository: KursusRepository
        get() = kursusContainer.kursusRepository

    override val pendaftaranRepository: PendaftaranRepository
        get() = pendaftaranContainer.pendaftaranRepository
}

class SiswaContainer  {
    private val baseUrl = "http://10.0.2.2:3000/pam/Siswa/" // Localhost for emulator
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val siswaService: SiswaService by lazy { retrofit.create(SiswaService::class.java) }
     val siswaRepository: SiswaRepository by lazy { NetworkSiswaRepository(siswaService) }
}

class InstrukturContainer {
    private val baseUrl = "http://10.0.2.2:3000/pam/Instruktur/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val instrukturService: InstrukturService by lazy { retrofit.create(InstrukturService::class.java) }
     val instrukturRepository: InstrukturRepository by lazy { NetworkInstrukturRepository(instrukturService) }
}

class KursusContainer {
    private val baseUrl = "http://10.0.2.2:3000/pam/Kursus/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val kursusService: KursusService by lazy { retrofit.create(KursusService::class.java) }
    val kursusRepository: KursusRepository by lazy { NetworkKursusRepository(kursusService) }
}

class PendaftaranContainer {
    private val baseUrl = "http://10.0.2.2:3000/pam/Pendaftaran/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val pendaftaranService: PendaftaranService by lazy { retrofit.create(PendaftaranService::class.java) }
    val pendaftaranRepository: PendaftaranRepository by lazy { NetworkPendRepository(pendaftaranService) }
}