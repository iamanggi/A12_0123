package com.example.tugasakhira12.ui.PagesInstruktur.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Instruktur.InstrukturRepository
import com.example.tugasakhira12.model.Instruktur
import kotlinx.coroutines.launch

class InsertInstrukturViewModel(private val ins: InstrukturRepository): ViewModel() {
    var uiInstState by mutableStateOf(InsertInsUiState())
        private set

    fun updateInsertInstrukturState(insertInstUiEvent: InsertInstUiEvent) {
        uiInstState = InsertInsUiState(insertInstUiEvent = insertInstUiEvent)
    }

    suspend fun insertInstruktur() {
        viewModelScope.launch {
            try {
                ins.insertInstruktur(uiInstState.insertInstUiEvent.toInstruktur())
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun validateFields(): Boolean {
        val event = uiInstState.insertInstUiEvent
        val errorState = FormErrorStateInst(
            idInstruktur = if (event.idInstruktur.isNotEmpty()) null else "ID Instruktur tidak boleh kosong",
            namaInstruktur = if (event.idInstruktur.isNotEmpty()) null else "Nama Siswa tidak boleh kosong",
            email = if (event.email.isNotEmpty()) null else "Email tidak boleh kosong",
            noTelepon = if (event.noTelepon.isNotEmpty()) null else "Nomor Telepon tidak boleh kosong",
            deskripsi = if (event.deskripsi.isNotEmpty()) null else "Deskripsi tidak boleh kosong"
            )

        uiInstState = uiInstState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    //menyimpan data ke repository
    fun saveData() {
        val currentEvent = uiInstState.insertInstUiEvent
        if (validateFields()) {
            viewModelScope.launch {
                try {
                    ins.insertInstruktur(currentEvent.toInstruktur()) // Memperbaiki penggunaan repository
                    uiInstState = uiInstState.copy(
                        snackBarMessage = "Data Berhasil disimpan",
                        insertInstUiEvent = InsertInstUiEvent(),
                        isEntryValid = FormErrorStateInst()
                    )
                } catch (e: Exception) {
                    uiInstState = uiInstState.copy(
                        snackBarMessage = "Data Gagal disimpan"
                    )
                }
            }
        } else {
            uiInstState = uiInstState.copy(
                snackBarMessage = "Input tidak valid. periksa kembali data anda"
            )
        }
    }
    //Reset pesan snackbar setelah ditampilkan
    fun resetSnackBarMessage(){
        uiInstState = uiInstState.copy(snackBarMessage = null)
    }
    fun clearErrorMessages() {
        // Reset the error state
        uiInstState = uiInstState.copy(isEntryValid = FormErrorStateInst())
    }
}


data class InsertInstUiEvent(
    val idInstruktur: String = "",
    val namaInstruktur: String = "",
    val email: String = "",
    val noTelepon: String = "",
    val deskripsi: String = ""
)

data class InsertInsUiState(
    val insertInstUiEvent: InsertInstUiEvent = InsertInstUiEvent(),
    val isEntryValid: FormErrorStateInst = FormErrorStateInst(),
    val snackBarMessage: String? = null,
)

data class FormErrorStateInst(
    val idInstruktur: String? = null,
    val namaInstruktur: String? = null,
    val email: String? = null,
    val noTelepon: String? = null,
    val deskripsi: String? = null
){
    fun isValid():Boolean{
        return idInstruktur == null && namaInstruktur == null && email == null
                && noTelepon  == null && deskripsi  == null
    }
}

fun InsertInstUiEvent.toInstruktur(): Instruktur = Instruktur(
    idInstruktur = idInstruktur,
    namaInstruktur = namaInstruktur,
    email = email,
    noTelepon = noTelepon,
    deskripsi = deskripsi
)

fun Instruktur.toUiStateInstruktur(): InsertInsUiState = InsertInsUiState(
    insertInstUiEvent = toInsertInstUiEvent()
)

fun Instruktur.toInsertInstUiEvent(): InsertInstUiEvent = InsertInstUiEvent(
    idInstruktur = idInstruktur,
    namaInstruktur = namaInstruktur,
    email = email,
    noTelepon = noTelepon,
    deskripsi = deskripsi
)