package DATA
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [DATAS::class], version = 1)

@TypeConverters(LocalDataConverter::class)
abstract class BD : RoomDatabase() {
    abstract fun DATASDAO(): DATASDAO
}