package cl.mmr.prueba3

import DATA.BD
import android.app.Application
import androidx.room.Room

class AppBanco: Application() {
    val db by lazy { Room.databaseBuilder(this, BD::class.java, "PERSONAS.db").build()  }
    val datasdao by lazy { db.DATASDAO() }
}