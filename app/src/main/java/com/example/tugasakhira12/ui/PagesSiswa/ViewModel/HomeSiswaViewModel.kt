package com.example.tugasakhira12.ui.PagesSiswa.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Siswa.SiswaRepository
import com.example.tugasakhira12.model.Siswa
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class SiswaUiState {
    data class Success(val siswa: List<Siswa>) : SiswaUiState()
    object Error : SiswaUiState()
    object Loading : SiswaUiState()
}

class HomeSiswaViewModel(private val mhs: SiswaRepository) : ViewModel() {

    var mhsUIState: SiswaUiState by mutableStateOf(SiswaUiState.Loading)
        private set

    init {
        getSiswa()
    }

    fun getSiswa() {
        viewModelScope.launch {
            mhsUIState = SiswaUiState.Loading
            mhsUIState = try {
                SiswaUiState.Success(mhs.getSiswa())
            } catch (e: IOException) {
                SiswaUiState.Error
            } catch (e: HttpException) {
                SiswaUiState.Error
            }
        }
    }

    fun deleteMhs(idSiswa: String) {
        viewModelScope.launch {
            try {
                mhs.deleteSiswa(idSiswa)
                getSiswa()
            } catch (e: IOException) {
                mhsUIState = SiswaUiState.Error
            } catch (e: HttpException) {
                mhsUIState = SiswaUiState.Error
            }
        }
    }
}