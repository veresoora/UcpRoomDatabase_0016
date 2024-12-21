package com.nadia.pertemuan9.ui.navigation

interface AlamatNavigasi {
    val route: String
}

object AlamatTambahDokter : AlamatNavigasi {
    override val route = "insert_dokter"
}

object AlamatHomeDokter : AlamatNavigasi {
    override val route = "home_dokter"
}

object AlamatTambahJadwal : AlamatNavigasi {
    override val route = "insert_jadwal"
}

object AlamatHomeJadwal : AlamatNavigasi {
    override val route = "home_jadwal"
}

object AlamatDetailJadwal : AlamatNavigasi {
    override val route = "detail"
    const val IDJWL = "idjadwal"
    val routeWithArg = "$route/{${IDJWL}}"
}

object AlamatEditJadwal : AlamatNavigasi {
    override val route = "update"
    const val IDJWL = "idjadwal"
    val routeWithArg = "$route/{$IDJWL}"
}