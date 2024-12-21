package com.nadia.pertemuan9

import android.app.Application
import com.nadia.pertemuan9.dependeciesinjection.ContainerApp

class KlinikApp : Application() { //Fungsinya untuk menyimpan instance
    lateinit var containerApp: ContainerApp
    override fun onCreate() {
        super.onCreate()
        containerApp = ContainerApp(this) //Membuat instance ContainerApp
        // instance adalah object yang dibuat dari class
    }
}