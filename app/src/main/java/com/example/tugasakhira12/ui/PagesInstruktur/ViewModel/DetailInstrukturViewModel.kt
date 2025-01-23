package com.example.tugasakhira12.ui.PagesInstruktur.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Instruktur.InstrukturRepository
import com.example.tugasakhira12.model.Instruktur
import com.example.tugasakhira12.ui.PagesInstruktur.View.DestinasiDetailInstruktur
import kotlinx.coroutines.launch
import okio.IOException

sealed class DetailInstrukturUiState{
    data class Success(val instruktur: Instruktur) : DetailInstrukturUiState()
    object Error : DetailInstrukturUiState()
    object Loading : DetailInstrukturUiState()
}

class DetailInstrukturViewModel(
    savedStateHandle: SavedStateHandle,
    private val instrukturRepository: InstrukturRepository
) : ViewModel() {

    private val idInstruktur: String = checkNotNull(savedStateHandle[DestinasiDetailInstruktur.idInstruktur])
    var detailInstrukturUiState: DetailInstrukturUiState by mutableStateOf(DetailInstrukturUiState.Loading)
        private set

    init {
        getInstrukturById()
    }

    fun getInstrukturById(){
        viewModelScope.launch {
            detailInstrukturUiState = DetailInstrukturUiState.Loading
            detailInstrukturUiState = try {
                DetailInstrukturUiState.Success(instruktur = instrukturRepository.getInstrukturById(idInstruktur))
            } catch (e: IOException) {
                DetailInstrukturUiState.Error
            }
        }
    }
}