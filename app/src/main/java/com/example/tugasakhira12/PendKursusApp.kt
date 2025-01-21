package com.example.tugasakhira12

import android.app.Application
import com.example.tugasakhira12.Repository.Siswa.AppContainer
import com.example.tugasakhira12.Repository.Siswa.SiswaContainer

class PendKursusApp: Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container= SiswaContainer()
    }
}