package co.edu.udea.compumovil.gr11_20232.partyplanner
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val fiestaListView = findViewById<ListView>(R.id.fiestaListView)
        val noFiestasMessage = findViewById<TextView>(R.id.noFiestasMessage)
        val crearFiestaButton = findViewById<Button>(R.id.crearFiestaButton)

        // Define una lista de fiestas y un adaptador para la lista
        val fiestas = mutableListOf<Fiesta>()
        val adapter = FiestaAdapter(this, R.layout.fiesta_item, fiestas)
        fiestaListView.adapter = adapter

        // Manejar la selección de una fiesta en la lista
        fiestaListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedFiesta = fiestas[position]
            // Abre una nueva actividad o fragmento para mostrar detalles de la fiesta seleccionada
        }

        // Manejar clic en el botón para crear una fiesta
        crearFiestaButton.setOnClickListener {
            // Abre una nueva actividad o fragmento para crear una nueva fiesta
        }
    }

    fun logout(view: View) {
        // Implementa la lógica para cerrar la sesión del usuario
        // Por ejemplo, utilizando Firebase Authentication
    }

    fun crearFiesta(view: View) {
        // Implementa la lógica para crear una nueva fiesta
        // Puede abrir una actividad o fragmento para ingresar detalles de la fiesta
    }
}

