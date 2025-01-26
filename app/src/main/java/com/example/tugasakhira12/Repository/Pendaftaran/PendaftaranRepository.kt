package com.example.tugasakhira12.Repository.Pendaftaran

import com.example.tugasakhira12.Service.PendaftaranService
import com.example.tugasakhira12.model.Pendaftaran
import okio.IOException

interface PendaftaranRepository{
    suspend fun getPendaftaran(): List<Pendaftaran>
    suspend fun insertPendaftaran(pendaftaran: Pendaftaran)
    suspend fun updatePendaftaran(id_pendaftaran: String, pendaftaran: Pendaftaran)
    suspend fun deletePendaftaran(id_pendaftaran: String)
    suspend fun getPendaftaranById(id_pendaftaran: String): Pendaftaran
}

class NetworkPendRepository(
    private val PendaftaranApiService: PendaftaranService
): PendaftaranRepository{

    override suspend fun insertPendaftaran(pendaftaran: Pendaftaran) {
        PendaftaranApiService.insertPendaftaran(pendaftaran)
    }

    override suspend fun updatePendaftaran(id_pendaftaran: String, pendaftaran: Pendaftaran) {
        PendaftaranApiService.updatePendaftaran(id_pendaftaran, pendaftaran)
    }

    override suspend fun deletePendaftaran(id_pendaftaran: String) {
        try {
            val response = PendaftaranApiService.deletePendaftaran(id_pendaftaran)
            if (!response.isSuccessful){
                throw IOException("Failed to delete pendaftaran. HTTP Status code: ${response.code()}")
            } else{
                response.message()
                println(response.message())
            }
        } catch (e:Exception){
            throw e
        }
    }

    override suspend fun getPendaftaran(): List<Pendaftaran> {
        return PendaftaranApiService.getPendaftaran().data //karena struktur json sudah berbeda
    }
    override suspend fun getPendaftaranById(id_pendaftaran: String): Pendaftaran {
        return PendaftaranApiService.getPendaftaranByID(id_pendaftaran).data
    }

}