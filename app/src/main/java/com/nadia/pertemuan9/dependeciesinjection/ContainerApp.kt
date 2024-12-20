package com.nadia.pertemuan9.dependeciesinjection

import android.content.Context
import com.nadia.pertemuan9.data.database.KlinikDatabase
import com.nadia.pertemuan9.repository.LocalRepositoryDokter
import com.nadia.pertemuan9.repository.LocalRepositoryJadwal
import com.nadia.pertemuan9.repository.RepositoryDokter
import com.nadia.pertemuan9.repository.RepositoryJadwal

interface InterfaceContainerApp {
    val repositoryDokter: RepositoryDokter
    val repositoryJadwal: RepositoryJadwal
}

class ContainerApp (private val context: Context) : InterfaceContainerApp{
    override val repositoryDokter: RepositoryDokter by lazy {
        LocalRepositoryDokter(KlinikDatabase.getDatabase(context).dokterDao())
    }

    override val repositoryJadwal: RepositoryJadwal by lazy {
        LocalRepositoryJadwal(KlinikDatabase.getDatabase(context).jadwalDao())
    }
}