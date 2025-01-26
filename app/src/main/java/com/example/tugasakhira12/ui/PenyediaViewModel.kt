package com.example.tugasakhira12.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tugasakhira12.PendKursusApp
import com.example.tugasakhira12.ui.PageKursus.ViewModel.DetailKursusViewModel
import com.example.tugasakhira12.ui.PageKursus.ViewModel.HomeKursusViewModel
import com.example.tugasakhira12.ui.PageKursus.ViewModel.InsertKursusViewModel
import com.example.tugasakhira12.ui.PageKursus.ViewModel.UpdateKursusViewModel
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.DetailPendViewModel
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.HomePendViewModel
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.InsertPendViewModel
import com.example.tugasakhira12.ui.PagePendaftaran.ViewModel.UpdatePendViewModel
import com.example.tugasakhira12.ui.PagesInstruktur.ViewModel.DetailInstrukturViewModel
import com.example.tugasakhira12.ui.PagesInstruktur.ViewModel.HomeInstrukturViewModel
import com.example.tugasakhira12.ui.PagesInstruktur.ViewModel.InsertInstrukturViewModel
import com.example.tugasakhira12.ui.PagesInstruktur.ViewModel.UpdateInstrukturViewModel
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.DetailSiswaViewModel
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.HomeSiswaViewModel
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.InsertSiswaViewModel
import com.example.tugasakhira12.ui.PagesSiswa.ViewModel.UpdateSiswaViewModel

object PenyediaViewModel{
    val Factory = viewModelFactory {
        //Siswa
        initializer{ HomeSiswaViewModel(pendKursusApp().mainAppContainer.siswaRepository) }
        initializer{ InsertSiswaViewModel(pendKursusApp().mainAppContainer.siswaRepository) }
        initializer { DetailSiswaViewModel(createSavedStateHandle(), siswaRepository = pendKursusApp().mainAppContainer.siswaRepository) }
        initializer { UpdateSiswaViewModel(createSavedStateHandle(), siswaRepository = pendKursusApp().mainAppContainer.siswaRepository) }

        //Instruktur
        initializer{ HomeInstrukturViewModel(pendKursusApp().mainAppContainer.instrukturRepository)}
        initializer{ InsertInstrukturViewModel(pendKursusApp().mainAppContainer.instrukturRepository)}
        initializer { DetailInstrukturViewModel(createSavedStateHandle(), instrukturRepository = pendKursusApp().mainAppContainer.instrukturRepository) }
        initializer { UpdateInstrukturViewModel(createSavedStateHandle(), instrukturRepository = pendKursusApp().mainAppContainer.instrukturRepository) }

        //Kursus
        initializer{ HomeKursusViewModel(pendKursusApp().mainAppContainer.kursusRepository)}
        initializer{ InsertKursusViewModel(
            pendKursusApp().mainAppContainer.kursusRepository,
            pendKursusApp().mainAppContainer.instrukturRepository)}
        initializer { DetailKursusViewModel(createSavedStateHandle(), kursusRepository = pendKursusApp().mainAppContainer.kursusRepository) }
        initializer { UpdateKursusViewModel(createSavedStateHandle(),
            kursusRepository = pendKursusApp().mainAppContainer.kursusRepository,
            pendKursusApp().mainAppContainer.instrukturRepository) }

        //Pendaftaran
        initializer{ HomePendViewModel(pendKursusApp().mainAppContainer.pendaftaranRepository)}
        initializer{ InsertPendViewModel(
            pendKursusApp().mainAppContainer.pendaftaranRepository,
            pendKursusApp().mainAppContainer.kursusRepository,
            pendKursusApp().mainAppContainer.siswaRepository)
        }
        initializer { DetailPendViewModel(createSavedStateHandle(), pendaftaranRepository = pendKursusApp().mainAppContainer.pendaftaranRepository) }
        initializer { UpdatePendViewModel(createSavedStateHandle(),
            pndf = pendKursusApp().mainAppContainer.pendaftaranRepository,
            pendKursusApp().mainAppContainer.kursusRepository,
            pendKursusApp().mainAppContainer.siswaRepository) }
    }

    fun CreationExtras.pendKursusApp(): PendKursusApp =
        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PendKursusApp)
}