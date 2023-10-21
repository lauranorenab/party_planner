package co.edu.udea.compumovil.gr11_20232.partyplanner
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class FeedActivity : AppCompatActivity() {
    val editTextNombreFiesta: EditText = findViewById(R.id.editTextNombreFiesta)
    val buttonFechaFiesta: Button = findViewById(R.id.buttonFechaFiesta)
    val textViewFechaSeleccionada: TextView = findViewById(R.id.textViewFechaSeleccionada)
    val editTextNombreIntegrante: EditText = findViewById(R.id.editTextNombreIntegrante)
    val editTextCorreoIntegrante: EditText = findViewById(R.id.editTextCorreoIntegrante)
    val buttonAgregarIntegrante: Button = findViewById(R.id.buttonAgregarIntegrante)
    val editTextPresupuesto: EditText = findViewById(R.id.editTextPresupuesto)
    val buttonCrearFiesta: Button = findViewById(R.id.buttonCrearFiesta)

    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        // Botón para seleccionar la fecha de la fiesta
        buttonFechaFiesta.setOnClickListener {
            showDatePicker()
        }

        // Botón para agregar un integrante
        buttonAgregarIntegrante.setOnClickListener {
            val nombreIntegrante = editTextNombreIntegrante.text.toString()
            val correoIntegrante = editTextCorreoIntegrante.text.toString()

            // Agregar lógica para guardar el integrante
        }

        // Botón para crear la fiesta
        buttonCrearFiesta.setOnClickListener {
            val nombreFiesta = editTextNombreFiesta.text.toString()
            val presupuesto = editTextPresupuesto.text.toString()

            // Agregar lógica para crear la fiesta
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            textViewFechaSeleccionada.text = "Fecha seleccionada: $selectedDate"
        }, year, month, day)

        datePickerDialog.show()
    }
}
