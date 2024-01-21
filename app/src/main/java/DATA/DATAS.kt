package DATA

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class DATAS(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var nombre: String,
    var Rutificador: String,
    var fecha_de_nacimiento: Long,
    var Correo: String,
    var Fono: Int,
    var Lat: Double,
    var Long: Double,
    var CarnetFrente: ByteArray,
    var CarnetTrasera: ByteArray,
    var fecha_de_Creacion: Long
)
