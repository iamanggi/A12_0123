package com.example.tugasakhira12.ui.PageKursus.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhira12.Repository.Kursus.KursusRepository
import com.example.tugasakhira12.model.Kursus
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class KursusUiState {
    data class Success(val kursus: List<Kursus>) : KursusUiState()
    object Error : KursusUiState()
    object Loading : KursusUiState()
}

class HomeKursusViewModel(private val krs: KursusRepository) : ViewModel() {

    var krsUIState: KursusUiState by mutableStateOf(KursusUiState.Loading)
        private set

    var searchQuery by mutableStateOf("") // Query pencarian
        private set

    private var allKursus: List<Kursus> = listOf()

    init {
        getKrs()
    }

    // Mendapatkan daftar kursus
    fun getKrs() {
        viewModelScope.launch {
            krsUIState = KursusUiState.Loading
            krsUIState = try {
                val kursusList = krs.getKursus()
                allKursus = kursusList // Simpan semua kursus
                KursusUiState.Success(kursusList)
            } catch (e: IOException) {
                KursusUiState.Error
            } catch (e: HttpException) {
                KursusUiState.Error
            }
        }
    }

    // Fungsi untuk menangani perubahan query pencarian
    fun onSearchQueryChanged(query: String) {
        searchQuery = query
        if (krsUIState is KursusUiState.Success) {
            val allKursus = this.allKursus
            val filteredKursus = if (searchQuery.isEmpty()) {
                allKursus
            } else {
                allKursus.filter { kursus ->
                    kursus.namaKursus.contains(searchQuery, ignoreCase = true) ||
                            kursus.kategori.contains(searchQuery, ignoreCase = true) ||
                            kursus.idInstruktur.contains(searchQuery, ignoreCase = true)
                }
            }
            krsUIState = KursusUiState.Success(filteredKursus)
        }
    }

    // Fungsi untuk menghapus kursus
    fun deleteKrs(idKursus: String) {
        viewModelScope.launch {
            try {
                krs.deleteKursus(idKursus)
                getKrs()
            } catch (e: IOException) {
                krsUIState = KursusUiState.Error
            } catch (e: HttpException) {
                krsUIState = KursusUiState.Error
            }
        }
    }
}


