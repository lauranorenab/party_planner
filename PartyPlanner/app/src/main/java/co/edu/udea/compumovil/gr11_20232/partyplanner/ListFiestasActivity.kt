package co.edu.udea.compumovil.gr11_20232.partyplanner

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class ListFiestasActivity : AppCompatActivity() {

    val listViewFiestas: ListView = findViewById(R.id.listViewFiestas)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_fiestas)

        // Simula la lista de fiestas (debes cargarla desde Firebase)
        val fiestas = listOf("Fiesta 1", "Fiesta 2", "Fiesta 3")

        // Adaptador para la lista de fiestas
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fiestas)
        listViewFiestas.adapter = adapter

        // Maneja la selección de una fiesta y agrega lógica para mostrar detalles
        listViewFiestas.setOnItemClickListener { _, _, position, _ ->
            val selectedFiesta = fiestas[position]
            // Agrega lógica para mostrar los detalles de la fiesta seleccionada
        }
    }
}
