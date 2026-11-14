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

// ---------------------------------------------------------------
// 1) ENTITY: el "molde" de una fila de la tabla "registros".
//    No guarda ningún dato personal, solo cuándo se hizo,
//    si ya se entregó y qué soplo se encontró en cada foco.
// ---------------------------------------------------------------
@Entity(tableName = "registros")
data class RegistroEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,   // Nº de registro (automático)
    val fechaMillis: Long,                              // momento en que se hizo
    val estado: String = "PENDIENTE",                   // PENDIENTE o ENTREGADO
    // Texto del soplo detectado en cada foco. null = foco normal.
    val soplo1: String? = null,
    val soplo2: String? = null,
    val soplo3: String? = null,
    val soplo4: String? = null
) {
    // Cuántos focos tienen soplo
    val cantidadSoplos: Int
        get() = listOfNotNull(soplo1, soplo2, soplo3, soplo4).size
}

// ---------------------------------------------------------------
// 2) DAO: las acciones que se pueden hacer con la tabla.
// ---------------------------------------------------------------
@Dao
interface RegistroDao {

    // Todos los registros, del más nuevo al más antiguo.
    // Al ser un Flow, la pantalla se actualiza sola cuando cambian los datos.
    @Query("SELECT * FROM registros ORDER BY id DESC")
    fun todos(): Flow<List<RegistroEntity>>

    // Un registro por su número
    @Query("SELECT * FROM registros WHERE id = :id")
    fun porId(id: Int): Flow<RegistroEntity?>

    // Guarda un registro nuevo y devuelve el número que se le asignó
    @Insert
    suspend fun insertar(registro: RegistroEntity): Long

    // Cambia el estado de entrega (por ejemplo a ENTREGADO)
    @Query("UPDATE registros SET estado = :estado WHERE id = :id")
    suspend fun cambiarEstado(id: Int, estado: String)
}

// ---------------------------------------------------------------
// 3) DATABASE: el archivo que junta todo y se guarda en el celular.
// ---------------------------------------------------------------
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

        // Devuelve siempre la misma base de datos (se crea solo la primera vez)
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