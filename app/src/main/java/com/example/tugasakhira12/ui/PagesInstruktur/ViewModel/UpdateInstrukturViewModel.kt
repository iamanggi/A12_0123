package com.example.tugasakhira12.ui.PagesInstruktur.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Instruktur.InstrukturRepository
import com.example.tugasakhira12.Repository.Siswa.SiswaRepository
import com.example.tugasakhira12.ui.PagesInstruktur.View.DestinasiUpdateInstruktur
import com.example.tugasakhira12.ui.PagesSiswa.View.DestinasiUpdate
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.InsertUiEvent
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.InsertUiState
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.toSiswa
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.toUiStateSiswa
import kotlinx.coroutines.launch


class UpdateInstrukturViewModel(
    savedStateHandle: SavedStateHandle,
    private val instrukturRepository: InstrukturRepository
) : ViewModel() {

    var uiInstState by mutableStateOf(InsertInsUiState())
        private set

    val idInstruktur: String = checkNotNull(savedStateHandle[DestinasiUpdateInstruktur.IdInstruktur])

    init {
        viewModelScope.launch {
            uiInstState = instrukturRepository.getInstrukturById(idInstruktur).toUiStateInstruktur()
        }
    }

    fun updateInsertInstState(insertInstUiEvent: InsertInstUiEvent) {
        uiInstState = InsertInsUiState(insertInstUiEvent = insertInstUiEvent)
    }

    suspend fun updateInstruktur(){
        viewModelScope.launch {
            try {
                instrukturRepository.updateInstruktur(idInstruktur, uiInstState.insertInstUiEvent.toInstruktur())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun resetSnackBarMessage(){
        uiInstState = uiInstState.copy(snackBarMessage = null)
    }
}