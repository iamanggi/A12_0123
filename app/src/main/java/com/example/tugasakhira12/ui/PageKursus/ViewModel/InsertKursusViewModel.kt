package com.example.tugasakhira12.ui.PageKursus.ViewModel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Instruktur.InstrukturRepository
import com.example.tugasakhira12.Repository.Kursus.KursusRepository
import com.example.tugasakhira12.model.Instruktur
import com.example.tugasakhira12.model.Kursus
import kotlinx.coroutines.launch

class InsertKursusViewModel(
    private val krs: KursusRepository,
    private val ins: InstrukturRepository
): ViewModel() {
    var krsuiState by mutableStateOf(InsertKursusUiState())
        private set

    var listInstruktur by mutableStateOf(listOf<Instruktur>())
        private set

    init {
loadData()
    }

    fun updateInsertKursusState(insertKursusUiEvent: InsertKursusUiEvent) {
        krsuiState = InsertKursusUiState(insertKursusUiEvent = insertKursusUiEvent)
    }

     fun validateFields(): Boolean {
        val event = krsuiState.insertKursusUiEvent
        val errorState = FormErrorState(
            idKursus = if (event.idKursus.isNotEmpty()) null else "ID Kursus tidak boleh kosong",
            namaKursus = if (event.namaKursus.isNotEmpty()) null else "Nama Kursus tidak boleh kosong",
            deskripsi = if (event.deskripsi.isNotEmpty()) null else "Deskripsi tidak boleh kosong",
            kategori = if (event.kategori.isNotEmpty()) null else "Kategori tidak boleh kosong",
            harga = if (event.harga > 0) event.harga else null,  // Memvalidasi harga
            hargaError = if (event.harga <= 0) "Harga tidak boleh kosong " else null,  // Menyimpan pesan error jika harga tidak valid
            namaInstruktur = if (event.namaInstruktur.isNotEmpty()) null else "Nama Instruktur tidak boleh kosong"
        )

        krsuiState = krsuiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    //menyimpan data ke repository
    fun saveData() {
        val currentEvent = krsuiState.insertKursusUiEvent
        if (validateFields()) {
            viewModelScope.launch {
                try {
                    krs.insertKursus(currentEvent.toKursus()) // Memperbaiki penggunaan repository
                    krsuiState = krsuiState.copy(
                        snackBarMessage = "Data Berhasil disimpan",
                        insertKursusUiEvent = InsertKursusUiEvent(),
                        isEntryValid = FormErrorState()
                    )
                } catch (e: Exception) {
                    krsuiState = krsuiState.copy(
                        snackBarMessage = "Data Gagal disimpan"
                    )
                }
            }
        } else {
            krsuiState = krsuiState.copy(
                snackBarMessage = "Input tidak valid. periksa kembali data anda"
            )
        }
    }
    //Reset pesan snackbar setelah ditampilkan
    fun resetSnackBarMessage(){
        krsuiState = krsuiState.copy(snackBarMessage = null)
    }
    fun clearErrorMessages() {
        // Reset the error state
        krsuiState = krsuiState.copy(isEntryValid = FormErrorState())
    }
    fun loadData() {
        viewModelScope.launch {
            try {
                listInstruktur = ins.getInstruktur()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}


data class InsertKursusUiEvent(
    val idKursus: String = "",
    val namaKursus: String = "",
    val deskripsi: String = "",
    val kategori: String = "",
    val harga: Double = 0.0,
    val idInstruktur: String = "",
    val namaInstruktur: String = ""
)

data class InsertKursusUiState(
    val insertKursusUiEvent: InsertKursusUiEvent = InsertKursusUiEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
    val snackBarMessage: String? = null,
)

data class FormErrorState(
    val idKursus: String? = null,
    val namaKursus: String? = null,
    val deskripsi: String? = null,
    val kategori: String? = null,
    val harga: Double? = null,
    val hargaError: String? = null,
    val namaInstruktur: String? = null,

){
    fun isValid():Boolean{
        return idKursus == null && namaKursus == null && deskripsi == null
                && kategori  == null && hargaError == null && namaInstruktur == null
    }
}

fun InsertKursusUiEvent.toKursus(): Kursus = Kursus(
    idKursus = idKursus,
    namaKursus = namaKursus,
    deskripsi = deskripsi,
    kategori = kategori,
    harga = harga,
    idInstruktur = idInstruktur,
    namaInstruktur = namaInstruktur
)

fun Kursus.toUiStateKursus(): InsertKursusUiState = InsertKursusUiState(
    insertKursusUiEvent = toInsertKrsUiEvent()
)

fun Kursus.toInsertKrsUiEvent(): InsertKursusUiEvent = InsertKursusUiEvent(
    idKursus = idKursus,
    namaKursus = namaKursus,
    deskripsi = deskripsi,
    kategori = kategori,
    harga = harga,
    idInstruktur = idInstruktur,
    namaInstruktur = namaInstruktur
)