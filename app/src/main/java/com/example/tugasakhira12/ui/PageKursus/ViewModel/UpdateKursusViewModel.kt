package com.example.tugasakhira12.ui.PageKursus.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Instruktur.InstrukturRepository
import com.example.tugasakhira12.Repository.Kursus.KursusRepository
import com.example.tugasakhira12.model.Instruktur
import com.example.tugasakhira12.ui.PageKursus.View.DestinasiUpdateKursus
import kotlinx.coroutines.launch


class UpdateKursusViewModel(
    savedStateHandle: SavedStateHandle,
    private val kursusRepository: KursusRepository,
    private val ins : InstrukturRepository
) : ViewModel() {

    var listInstruktur by mutableStateOf(listOf<Instruktur>())
        private set

    var krsUiState by mutableStateOf(InsertKursusUiState())
        private set

    val idKursus: String = checkNotNull(savedStateHandle[DestinasiUpdateKursus.IdKursus])

    init {
        viewModelScope.launch {
            krsUiState = kursusRepository.getKursusById(idKursus).toUiStateKursus()
        }
        loadInstruktur()
    }

    fun updateInsertKursusState(insertKursusUiEvent: InsertKursusUiEvent) {
        krsUiState = InsertKursusUiState(insertKursusUiEvent = insertKursusUiEvent)
    }

    suspend fun updateKursus(){
        viewModelScope.launch {
            try {
                kursusRepository.updateKursus(idKursus, krsUiState.insertKursusUiEvent.toKursus())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun loadInstruktur() {
        viewModelScope.launch {
            try {
                listInstruktur = ins.getInstruktur()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun resetSnackBarMessage(){
        krsUiState = krsUiState.copy(snackBarMessage = null)
    }
}