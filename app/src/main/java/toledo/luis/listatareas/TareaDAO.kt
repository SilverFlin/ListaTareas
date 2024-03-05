package toledo.luis.listatareas

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TareaDAO {
    @Query("SELECT * FROM tareas")
    fun obtenerTareas(): List<Tarea>

    @Insert
    fun agregarTarea(tarea: Tarea)

    @Delete
    fun eliminarTarea(tarea: Tarea)
}