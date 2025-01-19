package com.example.tugasakhira12.Repository.Siswa

import com.example.tugasakhira12.Service.Siswa.SiswaService
import com.example.tugasakhira12.model.Siswa
import okio.IOException

interface SiswaRepository{
    suspend fun getSiswa(): List<Siswa>
    suspend fun insertSiswa(siswa: Siswa)
    suspend fun updateSiswa(id_siswa: String, siswa: Siswa)
    suspend fun deleteSiswa(id_siswa: String)
    suspend fun getSiswaById(id_siswa: String): Siswa
}

class NetworkSiswaRepository(
    private val MahasiswaApiService: SiswaService
): SiswaRepository{
    override suspend fun insertSiswa(siswa: Siswa) {
        MahasiswaApiService.insertSiswa(siswa)
    }

    override suspend fun updateSiswa(id_siswa: String, siswa: Siswa) {
        MahasiswaApiService.updateSiswa(id_siswa, siswa)
    }

    override suspend fun deleteSiswa(id_siswa: String) {
        try {
            val response = MahasiswaApiService.deleteSiswa(id_siswa)
            if (!response.isSuccessful){
                throw IOException("Failed to delete mahasiswa. HTTP Status code: ${response.code()}")
            } else{
                response.message()
                println(response.message())
            }
        } catch (e:Exception){
            throw e
        }
    }

    override suspend fun getSiswa(): List<Siswa> {
        return MahasiswaApiService.getSiswa().data //karean struktur json sudah berbeda
    }
    override suspend fun getSiswaById(id_siswa: String): Siswa {
        return MahasiswaApiService.getSiswaByID(id_siswa).data
    }

}