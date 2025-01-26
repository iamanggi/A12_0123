package com.example.tugasakhira12.ui.PageKursus.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Kursus.KursusRepository
import com.example.tugasakhira12.model.Kursus
import com.example.tugasakhira12.ui.PageKursus.View.DestinasiDetailKursus
import kotlinx.coroutines.launch
import okio.IOException

sealed class DetailKursusUiState{
    data class Success(val kursus: Kursus) : DetailKursusUiState()
    object Error : DetailKursusUiState()
    object Loading : DetailKursusUiState()
}

class DetailKursusViewModel(
    savedStateHandle: SavedStateHandle,
    private val kursusRepository: KursusRepository
) : ViewModel() {

    private val idKursus: String = checkNotNull(savedStateHandle[DestinasiDetailKursus.idKursus])
    var detailKursusUiState: DetailKursusUiState by mutableStateOf(DetailKursusUiState.Loading)
        private set

    init {
        getKursusById()
    }

    fun getKursusById(){
        viewModelScope.launch {
            detailKursusUiState = DetailKursusUiState.Loading
            detailKursusUiState = try {
                DetailKursusUiState.Success(kursus = kursusRepository.getKursusById(idKursus))
            } catch (e: IOException) {
                DetailKursusUiState.Error
            }
        }
    }
}