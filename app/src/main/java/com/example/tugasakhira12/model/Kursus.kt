package com.example.tugasakhira12.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KursusDetailResponse(
    val status: Boolean,
    val message : String,
    val data : Kursus
)

@Serializable
data class KursusResponse(
    val status: Boolean,
    val message : String,
    val data : List<Kursus>
)

@Serializable
data class Kursus (
    @SerialName("id_kursus")
    val idKursus: String,

    @SerialName("nama_kursus")
    val namaKursus: String,

    val deskripsi: String,
    val kategori: String,
    val harga: Double,

    @SerialName("id_instruktur")
    val idInstruktur: String
)





