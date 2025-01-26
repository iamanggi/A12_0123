package com.example.tugasakhira12.Repository.Instruktur

import com.example.tugasakhira12.Service.InstrukturService
import com.example.tugasakhira12.model.Instruktur
import java.io.IOException

interface InstrukturRepository{
    suspend fun getInstruktur(): List<Instruktur>
    suspend fun insertInstruktur(instruktur: Instruktur)
    suspend fun updateInstruktur(id_instruktur: String, instruktur: Instruktur)
    suspend fun deleteInstruktur(id_instruktur: String)
    suspend fun getInstrukturById(id_instruktur: String): Instruktur
}

class NetworkInstrukturRepository(
    private val InstrukturApiService: InstrukturService
): InstrukturRepository{
    override suspend fun insertInstruktur(instruktur: Instruktur) {
        InstrukturApiService.insertInstruktur(instruktur)
    }

    override suspend fun updateInstruktur(id_instruktur: String, instruktur: Instruktur) {
        InstrukturApiService.updateInstruktur(id_instruktur, instruktur)
    }

    override suspend fun deleteInstruktur(id_instruktur: String) {
        try {
            val response = InstrukturApiService.deleteInstruktur(id_instruktur)
            if (!response.isSuccessful){
                throw IOException("Failed to delete Instruktur. HTTP Status code: ${response.code()}")
            } else{
                response.message()
                println(response.message())
            }
        } catch (e:Exception){
            throw e
        }
    }

    override suspend fun getInstruktur(): List<Instruktur> {
        return InstrukturApiService.getInstruktur().data //karean struktur json sudah berbeda
    }
    override suspend fun getInstrukturById(id_instruktur: String): Instruktur {
        return InstrukturApiService.getInstrukturByID(id_instruktur).data
    }

}