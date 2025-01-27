package com.example.tugasakhira12.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PendDetailResponse(
    val status: Boolean,
    val message : String,
    val data : Pendaftaran
)

@Serializable
data class PendResponse(
    val status: Boolean,
    val message : String,
    val data : List<Pendaftaran>
)

@Serializable
data class Pendaftaran (
    @SerialName("id_pendaftaran")
    val idPendaftaran: String,

    @SerialName("id_siswa")
     val idSiswa: String? = null,

    @SerialName("id_kursus")
    val idKursus: String? = null,

    @SerialName("tgl_pendaftaran")
    val tglPendaftaran: String,

    val status: String,

    @SerialName("nama_siswa")
    val namaSiswa: String? = null,

    @SerialName("nama_kursus")
    val namaKursus: String? = null,

    val kategori: String? = null
)