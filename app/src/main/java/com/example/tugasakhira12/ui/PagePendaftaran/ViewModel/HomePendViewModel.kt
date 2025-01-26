package com.example.tugasakhira12.ui.PagePendaftaran.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Kursus.KursusRepository
import com.example.tugasakhira12.Repository.Pendaftaran.PendaftaranRepository
import com.example.tugasakhira12.Repository.Siswa.SiswaRepository
import com.example.tugasakhira12.model.Kursus
import com.example.tugasakhira12.model.Pendaftaran
import com.example.tugasakhira12.model.Siswa
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class PendUiState {
    data class Success(val pendaftaran: List<Pendaftaran>) : PendUiState()
    object Error : PendUiState()
    object Loading : PendUiState()
}

class HomePendViewModel(
    private val pndf: PendaftaranRepository)
    : ViewModel() {

    var pndfUIState: PendUiState by mutableStateOf(PendUiState.Loading)
        private set

    init {
        getPndftrn()
    }

    fun getPndftrn() {
        viewModelScope.launch {
            pndfUIState = PendUiState.Loading
            pndfUIState = try {
                PendUiState.Success(pndf.getPendaftaran())
            } catch (e: IOException) {
                PendUiState.Error
            } catch (e: HttpException) {
                PendUiState.Error
            }
        }
    }

    fun deletePndftrn(idPendaftaran: String) {
        viewModelScope.launch {
            try {
                pndf.deletePendaftaran(idPendaftaran)
                getPndftrn()
            } catch (e: IOException) {
                pndfUIState = PendUiState.Error
            } catch (e: HttpException) {
                pndfUIState = PendUiState.Error
            }
        }
    }
}