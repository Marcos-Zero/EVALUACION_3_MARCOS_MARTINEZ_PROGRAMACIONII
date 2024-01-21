package DATA

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Update

import androidx.room.Query


@Dao
interface DATASDAO {


    @Query("SELECT * FROM DATAS ORDER BY fecha_de_Creacion DESC")
    suspend fun DatosPorfechaDeCreacion(): List<DATAS>


    @Query ("SELECT * FROM DATAS WHERE id = :id")
    suspend fun encontrarPorId(id:Long): DATAS


    @Insert
    suspend fun ingresarSolicitud(datas: DATAS)

    @Query("INSERT INTO DATAS (id, Fono, Rutificador, nombre, Correo, fecha_de_nacimiento, Lat, Long, CarnetFrente, CarnetTrasera, fecha_de_Creacion) VALUES (:nuevoid, :telefono, :rut, :nombreCompleto, :email, :fechaDeNacimiento, :latitud, :longitud, :imagenFrente, :imagenTrasera, :fechaDeCreacion)")
    suspend fun insertarsolicitud(
        nuevoid: Int,
        telefono: Int,
        rut: String,
        nombreCompleto: String,
        email: String,
        fechaDeNacimiento: Long,
        latitud: Double,
        longitud: Double,
        imagenFrente: ByteArray,
        imagenTrasera: ByteArray,
        fechaDeCreacion: Long
    )

    @Query("DELETE FROM DATAS WHERE ID = :IDborrado")
    suspend fun eliminarSolicitud(IDborrado: Int)
}