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

    private val baseUrl = "http://10.0.2.2:3000/pam/" // Localhost for emulator
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    //Siswa
    private val siswaService: SiswaService by lazy { retrofit.create(SiswaService::class.java) }
    override val siswaRepository: SiswaRepository by lazy { NetworkSiswaRepository(siswaService) }

    //instruktur
    private val instrukturService: InstrukturService by lazy { retrofit.create(InstrukturService::class.java) }
    override val instrukturRepository: InstrukturRepository by lazy { NetworkInstrukturRepository(instrukturService) }

    //kursus
    private val kursusService: KursusService by lazy { retrofit.create(KursusService::class.java) }
    override val kursusRepository: KursusRepository by lazy { NetworkKursusRepository(kursusService) }

    //pendaftaran
    private val pendaftaranService: PendaftaranService by lazy { retrofit.create(PendaftaranService::class.java) }
    override val pendaftaranRepository: PendaftaranRepository by lazy { NetworkPendRepository(pendaftaranService) }

}





