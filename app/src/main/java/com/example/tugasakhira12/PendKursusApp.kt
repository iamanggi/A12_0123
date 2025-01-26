package com.example.tugasakhira12

import android.app.Application
import com.example.tugasakhira12.Repository.MainAppContainer


class PendKursusApp: Application(){
    lateinit var mainAppContainer: MainAppContainer
    override fun onCreate() {
        super.onCreate()
        mainAppContainer =MainAppContainer()
    }
}