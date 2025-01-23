package com.example.tugasakhira12.ui.PagesInstruktur.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Instruktur.InstrukturRepository
import com.example.tugasakhira12.model.Instruktur
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class InstrukturUiState {
    data class Success(val instruktur: List<Instruktur>) : InstrukturUiState()
    object Error : InstrukturUiState()
    object Loading : InstrukturUiState()
}

class HomeInstrukturViewModel(private val ins: InstrukturRepository) : ViewModel() {

    var insUIState: InstrukturUiState by mutableStateOf(InstrukturUiState.Loading)
        private set

    init {
        getInstruktur()
    }

    fun getInstruktur() {
        viewModelScope.launch {
            insUIState = InstrukturUiState.Loading
            insUIState = try {
                InstrukturUiState.Success(ins.getInstruktur())
            } catch (e: IOException) {
                InstrukturUiState.Error
            } catch (e: HttpException) {
                InstrukturUiState.Error
            }
        }
    }

    fun deleteInstruktur(idInstruktur: String) {
        viewModelScope.launch {
            try {
                ins.deleteInstruktur(idInstruktur)
                getInstruktur()
            } catch (e: IOException) {
                insUIState = InstrukturUiState.Error
            } catch (e: HttpException) {
                insUIState = InstrukturUiState.Error
            }
        }
    }
}