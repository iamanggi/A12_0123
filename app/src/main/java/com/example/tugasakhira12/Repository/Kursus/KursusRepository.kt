package com.example.tugasakhira12.Repository.Kursus

import com.example.tugasakhira12.Service.KursusService
import com.example.tugasakhira12.model.Kursus
import java.io.IOException

interface KursusRepository{
    suspend fun getKursus(): List<Kursus>
    suspend fun insertKursus(kursus: Kursus)
    suspend fun updateKursus(id_kursus: String, kursus: Kursus)
    suspend fun deleteKursus(id_kursus: String)
    suspend fun getKursusById(id_kursus: String): Kursus
}

class NetworkKursusRepository(
    private val KursusApiService: KursusService
): KursusRepository{
    override suspend fun insertKursus(kursus: Kursus) {
        KursusApiService.insertKursus(kursus)
    }

    override suspend fun updateKursus(id_kursus: String, kursus: Kursus) {
        KursusApiService.updateKursus(id_kursus, kursus)
    }

    override suspend fun deleteKursus(id_kursus: String) {
        try {
            val response = KursusApiService.deleteKursus(id_kursus)
            if (!response.isSuccessful){
                throw IOException("Failed to delete kursus. HTTP Status code: ${response.code()}")
            } else{
                response.message()
                println(response.message())
            }
        } catch (e:Exception){
            throw e
        }
    }

    override suspend fun getKursus(): List<Kursus> {
        return KursusApiService.getKursus().data //karean struktur json sudah berbeda
    }
    override suspend fun getKursusById(id_kursus: String): Kursus {
        return KursusApiService.getKursusByID(id_kursus).data
    }

}