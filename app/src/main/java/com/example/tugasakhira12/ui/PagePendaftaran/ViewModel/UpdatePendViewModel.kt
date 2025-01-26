package com.example.tugasakhira12.ui.PagePendaftaran.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Kursus.KursusRepository
import com.example.tugasakhira12.Repository.Pendaftaran.PendaftaranRepository
import com.example.tugasakhira12.Repository.Siswa.SiswaRepository
import com.example.tugasakhira12.model.Kursus
import com.example.tugasakhira12.model.Siswa
import com.example.tugasakhira12.ui.PagePendaftaran.View.DestinasiUpdatePendaftaran
import kotlinx.coroutines.launch

class UpdatePendViewModel(
    savedStateHandle: SavedStateHandle,
    private val pndf: PendaftaranRepository,
    private val krs: KursusRepository,
    private val mhs: SiswaRepository
) : ViewModel() {

    var penUiState by mutableStateOf(InsertPendUiState())
        private set
    var listSiswa by mutableStateOf(listOf<Siswa>())
        private set

    var listKursus by mutableStateOf(listOf<Kursus>())
        private set

    val idPendaftaran: String = checkNotNull(savedStateHandle[DestinasiUpdatePendaftaran.IdPendaftaran])

    init {
        viewModelScope.launch {
            penUiState =  pndf.getPendaftaranById(idPendaftaran).toUiStatePend()
        }
        loadPendaftaran()
    }

    fun updateInsertPendState(insertPendUiEvent: InsertPendUiEvent) {
        penUiState = InsertPendUiState(insertPendUiEvent = insertPendUiEvent)
    }

    suspend fun updateKursus(){
        viewModelScope.launch {
            try {
                pndf.updatePendaftaran(idPendaftaran, penUiState.insertPendUiEvent.toPendaftaran())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun loadPendaftaran() {
        viewModelScope.launch {
            try {
                listKursus = krs.getKursus()
                listSiswa = mhs.getSiswa()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun resetSnackBarMessage(){
        penUiState = penUiState.copy(snackBarMessage = null)
    }
}