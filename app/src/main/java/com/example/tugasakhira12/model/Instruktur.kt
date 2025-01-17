package com.example.tugasakhira12.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstDetailResponse(
    val status: Boolean,
    val message : String,
    val data : Instruktur
)

@Serializable
data class InstResponse(
    val status: Boolean,
    val message : String,
    val data : List<Instruktur>
)

@Serializable
data class Instruktur (
    @SerialName("id_instruktur")
    val idInstruktur: String,

    @SerialName("nama_instruktur")
    val namaInstruktur: String,

    val email: String,

    @SerialName("no_telepon")
    val noTelepon: String,

    val deskripsi: String
)