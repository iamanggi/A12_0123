package com.example.tugasakhira12.ui.PagesSiswa.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Siswa.SiswaRepository
import com.example.tugasakhira12.model.Siswa
import com.example.tugasakhira12.ui.PagesSiswa.View.DestinasiDetail
import kotlinx.coroutines.launch
import okio.IOException

sealed class DetailSiswaUiState{
    data class Success(val siswa: Siswa) : DetailSiswaUiState()
    object Error : DetailSiswaUiState()
    object Loading : DetailSiswaUiState()
}

class DetailSiswaViewModel(
    savedStateHandle: SavedStateHandle,
    private val siswaRepository: SiswaRepository
) : ViewModel() {

    private val idSiswa: String = checkNotNull(savedStateHandle[DestinasiDetail.idSiswa])
    var detailSiswaUiState: DetailSiswaUiState by mutableStateOf(DetailSiswaUiState.Loading)
        private set

    init {
        getSiswaById()
    }

    fun getSiswaById(){
        viewModelScope.launch {
            detailSiswaUiState = DetailSiswaUiState.Loading
            detailSiswaUiState = try {
                DetailSiswaUiState.Success(siswa = siswaRepository.getSiswaById(idSiswa))
            } catch (e: IOException) {
                DetailSiswaUiState.Error
            }
        }
    }
}