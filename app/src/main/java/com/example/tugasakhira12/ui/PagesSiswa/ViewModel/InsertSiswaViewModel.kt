package com.example.tugasakhira12.ui.PagesSiswa.ViewModel

import com.example.tugasakhira12.Repository.Siswa.SiswaRepository
import com.example.tugasakhira12.model.Siswa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class InsertSiswaViewModel(private val mhs: SiswaRepository): ViewModel() {
    var uiState by mutableStateOf(InsertUiState())
        private set

    fun updateInsertSiswaState(insertUiEvent: InsertUiEvent) {
        uiState = InsertUiState(insertUiEvent = insertUiEvent)
    }

    suspend fun insertSiswa() {
        viewModelScope.launch {
            try {
                mhs.insertSiswa(uiState.insertUiEvent.toSiswa())
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun validateFields(): Boolean {
        val event = uiState.insertUiEvent
        val errorState = FormErrorStateSiswa(
            idSiswa = if (event.idSiswa.isNotEmpty()) null else "ID Siswa tidak boleh kosong",
            namaSiswa = if (event.namaSiswa.isNotEmpty()) null else "Nama Siswa tidak boleh kosong",
            email = if (event.email.isNotEmpty()) null else "Email tidak boleh kosong",
            nomorTlp = if (event.nomorTlp.isNotEmpty()) null else "Nomor Telepon tidak boleh kosong",
        )

        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    //menyimpan data ke repository
    fun saveData() {
        val currentEvent = uiState.insertUiEvent
        if (validateFields()) {
            viewModelScope.launch {
                try {
                    mhs.insertSiswa(currentEvent.toSiswa()) // Memperbaiki penggunaan repository
                    uiState = uiState.copy(
                        snackBarMessage = "Data Berhasil disimpan",
                        insertUiEvent = InsertUiEvent(),
                        isEntryValid = FormErrorStateSiswa()
                    )
                } catch (e: Exception) {
                    uiState = uiState.copy(
                        snackBarMessage = "Data Gagal disimpan"
                    )
                }
            }
        } else {
            uiState = uiState.copy(
                snackBarMessage = "Input tidak valid. periksa kembali data anda"
            )
        }
    }
    //Reset pesan snackbar setelah ditampilkan
    fun resetSnackBarMessage(){
        uiState = uiState.copy(snackBarMessage = null)
    }
    fun clearErrorMessages() {
        // Reset the error state
        uiState = uiState.copy(isEntryValid = FormErrorStateSiswa())
    }
}


data class InsertUiEvent(
    val idSiswa: String = "",
    val namaSiswa: String = "",
    val email: String = "",
    val nomorTlp: String = ""
)

data class InsertUiState(
    val insertUiEvent: InsertUiEvent = InsertUiEvent(),
    val isEntryValid: FormErrorStateSiswa = FormErrorStateSiswa(),
    val snackBarMessage: String? = null,
)

data class FormErrorStateSiswa(
    val idSiswa: String? = null,
    val namaSiswa: String? = null,
    val email: String? = null,
    val nomorTlp: String? = null,
){
    fun isValid():Boolean{
        return idSiswa == null && namaSiswa == null && email == null
                && nomorTlp  == null
    }
}



fun InsertUiEvent.toSiswa(): Siswa = Siswa(
    idSiswa = idSiswa,
    namaSiswa = namaSiswa,
    email = email,
    nomorTlp = nomorTlp
)

fun Siswa.toUiStateSiswa(): InsertUiState = InsertUiState(
    insertUiEvent = toInsertUiEvent()
)

fun Siswa.toInsertUiEvent(): InsertUiEvent = InsertUiEvent(
    idSiswa = idSiswa,
    namaSiswa = namaSiswa,
    email = email,
    nomorTlp = nomorTlp
)