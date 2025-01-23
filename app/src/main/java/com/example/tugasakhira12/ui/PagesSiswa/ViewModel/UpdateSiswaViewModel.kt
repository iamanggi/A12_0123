package com.example.tugasakhira12.ui.PagesSiswa.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Siswa.SiswaRepository
import com.example.tugasakhira12.ui.PagesSiswa.View.DestinasiUpdate
import kotlinx.coroutines.launch


class UpdateSiswaViewModel(
    savedStateHandle: SavedStateHandle,
    private val siswaRepository: SiswaRepository
) : ViewModel() {

    var uiState by mutableStateOf(InsertUiState())
        private set

    val idSiswa: String = checkNotNull(savedStateHandle[DestinasiUpdate.idSiswa])

    init {
        viewModelScope.launch {
            uiState = siswaRepository.getSiswaById(idSiswa).toUiStateSiswa()
        }
    }

    fun updateInsertMhsState(insertUiEvent: InsertUiEvent) {
        uiState = InsertUiState(insertUiEvent = insertUiEvent)
    }

    suspend fun updateSiswa(){
        viewModelScope.launch {
            try {
                siswaRepository.updateSiswa(idSiswa, uiState.insertUiEvent.toSiswa())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun resetSnackBarMessage(){
        uiState = uiState.copy(snackBarMessage = null)
    }
}