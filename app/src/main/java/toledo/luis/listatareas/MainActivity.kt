package toledo.luis.listatareas

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    lateinit var et_tarea: EditText
    lateinit var btn_agregar: Button
    lateinit var listview_tareas: ListView
    lateinit var lista_tareas: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_tarea = findViewById(R.id.tarea)
        btn_agregar = findViewById(R.id.btn_agregar)
        listview_tareas = findViewById(R.id.listview_tareas)


        lista_tareas = ArrayList()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_tareas)

        btn_agregar.setOnClickListener {
            var tarea = et_tarea.text.toString()
            if (!tarea.isNullOrEmpty()) {
                lista_tareas.add(tarea)

                adapter.notifyDataSetChanged()

                et_tarea.setText("")
            }


        }
        listview_tareas.onItemClickListener = AdapterView.OnItemClickListener{parent, view, position, id ->
            lista_tareas.removeAt(position)
            adapter.notifyDataSetChanged()
        }
    }
}