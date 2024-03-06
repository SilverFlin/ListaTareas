package toledo.luis.listatareas

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.room.Room
import androidx.room.Room.databaseBuilder

class MainActivity : AppCompatActivity() {
    lateinit var et_tarea: EditText
    lateinit var btn_agregar: Button
    lateinit var listview_tareas: ListView
    lateinit var lista_tareas: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_tarea = findViewById(R.id.tarea)
        btn_agregar = findViewById(R.id.btn_agregar)
        listview_tareas = findViewById(R.id.listview_tareas)


        lista_tareas = ArrayList()

        db = databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "lista-tareas"
        ).allowMainThreadQueries().build()

        cargar_tareas()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_tareas)

        btn_agregar.setOnClickListener {
            var tarea_str = et_tarea.text.toString()
            if (!tarea_str.isNullOrEmpty()) {
                var tarea = Tarea(desc = tarea_str)
                db.tareaDao().agregarTarea(tarea)
                lista_tareas.add(tarea_str)

                adapter.notifyDataSetChanged()

                et_tarea.setText("")
            }


        }
        listview_tareas.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                var tarea_desc = lista_tareas[position]

                var tarea = db.tareaDao().getTarea(tarea_desc)

                db.tareaDao().eliminarTarea(tarea)


                lista_tareas.removeAt(position)
                adapter.notifyDataSetChanged()
            }
    }

    private fun cargar_tareas() {
        var lista_db = db.tareaDao().obtenerTareas()

        for (tarea in lista_db){
            lista_tareas.add(tarea.desc)
        }

    }
}