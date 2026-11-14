package com.example.cardiocopy

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow


@Entity(tableName = "registros")
data class RegistroEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  
    val fechaMillis: Long,                           
    val estado: String = "PENDIENTE",                   

    val soplo1: String? = null,
    val soplo2: String? = null,
    val soplo3: String? = null,
    val soplo4: String? = null
) {
    
    val cantidadSoplos: Int
        get() = listOfNotNull(soplo1, soplo2, soplo3, soplo4).size
}


@Dao
interface RegistroDao {

    
    @Query("SELECT * FROM registros ORDER BY id DESC")
    fun todos(): Flow<List<RegistroEntity>>

   
    @Query("SELECT * FROM registros WHERE id = :id")
    fun porId(id: Int): Flow<RegistroEntity?>

    
    @Insert
    suspend fun insertar(registro: RegistroEntity): Long

    
    @Query("UPDATE registros SET estado = :estado WHERE id = :id")
    suspend fun cambiarEstado(id: Int, estado: String)
}


@Database(
    entities = [RegistroEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun registroDao(): RegistroDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        
        fun obtener(context: Context): AppDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cardiocori.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build().also { instancia = it }
            }
        }
    }
}
