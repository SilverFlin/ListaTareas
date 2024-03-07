package toledo.luis.listatareas

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        listview_tareas.adapter = adapter

        btn_agregar.setOnClickListener {
            var tarea_str = et_tarea.text.toString()
            if (!tarea_str.isNullOrEmpty()) {
                Toast.makeText(this, tarea_str, Toast.LENGTH_SHORT).show()
                var tarea = Tarea(desc = tarea_str)
                db.tareaDao().agregarTarea(tarea)
                lista_tareas.add(tarea_str)

                adapter.notifyDataSetChanged()

                et_tarea.setText("")
            }


        }
        listview_tareas.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { parent, view, position, id ->
                var tarea_desc = lista_tareas[position]

                var tarea = db.tareaDao().getTarea(tarea_desc)

                db.tareaDao().eliminarTarea(tarea)


                lista_tareas.removeAt(position)
                adapter.notifyDataSetChanged()
                true
            }

        listview_tareas.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                var tarea_desc = lista_tareas[position]

                var tarea = db.tareaDao().getTarea(tarea_desc)

                showInputDialog(this, "Editando tarea: " + tarea_desc, "Ok") { userInput ->
                    tarea.desc = userInput
                    db.tareaDao().modificarTarea(tarea)
                    lista_tareas[position] = userInput
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun cargar_tareas() {
        var lista_db = db.tareaDao().obtenerTareas()

        for (tarea in lista_db) {
            lista_tareas.add(tarea.desc)
        }

    }

    fun showInputDialog(
        context: Context,
        title: String,
        positiveButtonLabel: String,
        onInputReceived: (String) -> Unit
    ) {
        val inputEditText = EditText(context)

        val alertDialogBuilder = AlertDialog.Builder(context)

        alertDialogBuilder.setTitle(title)

        alertDialogBuilder.setView(inputEditText)

        alertDialogBuilder.setPositiveButton(positiveButtonLabel) { dialog, _ ->
            val userInput = inputEditText.text.toString()
            onInputReceived.invoke(userInput)
            dialog.dismiss()
        }

        // Create and show the input dialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}