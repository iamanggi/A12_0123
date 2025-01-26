package com.example.tugasakhira12.ui.PagePendaftaran.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Pendaftaran.PendaftaranRepository
import com.example.tugasakhira12.model.Pendaftaran
import com.example.tugasakhira12.ui.PagePendaftaran.View.DestinasiDetailPendaftaran
import kotlinx.coroutines.launch
import okio.IOException

sealed class DetailPendUiState{
    data class Success(val pendaftaran: Pendaftaran) : DetailPendUiState()
    object Error : DetailPendUiState()
    object Loading : DetailPendUiState()
}

class DetailPendViewModel(
    savedStateHandle: SavedStateHandle,
    private val pendaftaranRepository: PendaftaranRepository
) : ViewModel() {

    private val idPendaftaran: String = checkNotNull(savedStateHandle[DestinasiDetailPendaftaran.idPendaftaran])
    var detailPendUiState: DetailPendUiState by mutableStateOf(DetailPendUiState.Loading)
        private set

    init {
        getPendaftaranById()
    }

    fun getPendaftaranById(){
        viewModelScope.launch {
            detailPendUiState = DetailPendUiState.Loading
            detailPendUiState = try {
                DetailPendUiState.Success(pendaftaran = pendaftaranRepository.getPendaftaranById(idPendaftaran))
            } catch (e: IOException) {
                DetailPendUiState.Error
            }
        }
    }
}