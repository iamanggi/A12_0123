package com.example.tugasakhira12.ui.PagePendaftaran.ViewModel

import com.example.tugasakhira12.model.Siswa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Kursus.KursusRepository
import com.example.tugasakhira12.Repository.Pendaftaran.PendaftaranRepository
import com.example.tugasakhira12.Repository.Siswa.SiswaRepository
import com.example.tugasakhira12.model.Kursus
import com.example.tugasakhira12.model.Pendaftaran
import kotlinx.coroutines.launch

class InsertPendViewModel(
    private val pndf: PendaftaranRepository,
    private val krs: KursusRepository,
    private val mhs: SiswaRepository
): ViewModel() {
    var pendfuiState by mutableStateOf(InsertPendUiState())
        private set

    var listSiswa by mutableStateOf(listOf<Siswa>())
        private set

    var listKursus by mutableStateOf(listOf<Kursus>())
        private set

    init {
        loadData()
    }

    fun updateInsertPendState(insertPendUiEvent: InsertPendUiEvent) {
        pendfuiState = InsertPendUiState(insertPendUiEvent = insertPendUiEvent)
    }


    fun validateFields(): Boolean {
        val event = pendfuiState.insertPendUiEvent
        val errorState = FormErrorStatePendf(
            idPendaftaran = if (event.idPendaftaran.isNotEmpty()) null else "Id Pendaftaran tidak boleh kosong",
            idSiswa = if (event.idSiswa.isNotEmpty()) null else "Nama Siswa tidak boleh kosong",
            idKursus = if (event.idKursus.isNotEmpty()) null else "Nama Kursus tidak boleh kosong",
            tglPendaftaran = if (event.tglPendaftaran.isNotEmpty()) null else "Tgl Pendaftaran tidak boleh kosong",
            status = if (event.status.isNotEmpty()) null else "Status tidak boleh kosong",
        )

        pendfuiState = pendfuiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    //menyimpan data ke repository
    fun saveData() {
        val currentEvent = pendfuiState.insertPendUiEvent
        if (validateFields()) {
            viewModelScope.launch {
                try {
                    pndf.insertPendaftaran(currentEvent.toPendaftaran()) // Memperbaiki penggunaan repository
                    pendfuiState = pendfuiState.copy(
                        snackBarMessage = "Data Berhasil disimpan",
                        insertPendUiEvent = InsertPendUiEvent(),
                        isEntryValid = FormErrorStatePendf()
                    )
                } catch (e: Exception) {
                    pendfuiState = pendfuiState.copy(
                        snackBarMessage = "Data Gagal disimpan"
                    )
                }
            }
        } else {
            pendfuiState = pendfuiState.copy(
                snackBarMessage = "Input tidak valid. periksa kembali data anda"
            )
        }
    }
    //Reset pesan snackbar setelah ditampilkan
    fun resetSnackBarMessage(){
        pendfuiState = pendfuiState.copy(snackBarMessage = null)
    }
    fun clearErrorMessages() {
        // Reset the error state
        pendfuiState = pendfuiState.copy(isEntryValid = FormErrorStatePendf())
    }
    fun loadData() {
        viewModelScope.launch {
            try {
                listSiswa = mhs.getSiswa()
                listKursus = krs.getKursus()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


data class InsertPendUiEvent(
    val idPendaftaran: String = "",
    val idSiswa: String = "",
    val idKursus: String = "",
    val tglPendaftaran: String = "",
    val status: String = "",
    val namaSiswa: String = "",
    val namaKursus: String = "",
    val kategori: String = ""

)

data class InsertPendUiState(
    val insertPendUiEvent: InsertPendUiEvent = InsertPendUiEvent(),
    val isEntryValid: FormErrorStatePendf = FormErrorStatePendf(),
    val snackBarMessage: String? = null,
)

data class FormErrorStatePendf(
    val idPendaftaran: String? = null,
    val idSiswa: String? = null,
    val idKursus: String? = null,
    val tglPendaftaran: String? = null,
    val status: String? = null,
    val katgori: String? = null
){
    fun isValid():Boolean{
        return idPendaftaran == null && idSiswa == null && idKursus == null
                && tglPendaftaran  == null && status  == null
    }
}

fun InsertPendUiEvent.toPendaftaran(): Pendaftaran = Pendaftaran(
    idPendaftaran = idPendaftaran,
    idSiswa = idSiswa,
    idKursus = idKursus,
    tglPendaftaran = tglPendaftaran,
    status = status,
    namaSiswa = namaSiswa,
    namaKursus = namaKursus,
    kategori = kategori

)

fun Pendaftaran.toUiStatePend(): InsertPendUiState = InsertPendUiState(
    insertPendUiEvent = toInsertPendUiEvent()
)

fun Pendaftaran.toInsertPendUiEvent(): InsertPendUiEvent = InsertPendUiEvent(
    idPendaftaran = idPendaftaran,
    idSiswa = idSiswa.toString(),
    idKursus = idKursus.toString(),
    tglPendaftaran = tglPendaftaran,
    status = status,
    namaSiswa = namaSiswa.toString(),
    namaKursus = namaKursus.toString(),
    kategori = kategori.toString()
)