package co.edu.udea.compumovil.gr11_20232.partyplanner
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import com.google.firebase.database.FirebaseDatabase


data class Integrante(
    val nombre: String,
    val correo: String
)

data class Fiesta(
    val nombre: String,
    val fecha: String, // Debes decidir el formato que prefieres
    val presupuesto: String
)



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

            // Verifica que los campos no estén vacíos
            if (nombreIntegrante.isNotEmpty() && correoIntegrante.isNotEmpty()) {
                // Obtén una referencia a la base de datos de Firebase
                val database = FirebaseDatabase.getInstance()
                val ref = database.getReference("fiestas") // Reemplaza "fiestas" con la ubicación donde deseas almacenar los integrantes

                // Crea un nuevo objeto Integrante con los datos ingresados
                val integrante = Integrante(nombreIntegrante, correoIntegrante)

                // Genera una nueva clave única para el integrante
                val nuevaKey = ref.push().key

                if (nuevaKey != null) {
                    // Guarda el integrante en la base de datos con la clave generada
                    ref.child("tu_id_de_fiesta").child("integrantes").child(nuevaKey).setValue(integrante)
                    Toast.makeText(this, "Integrante agregado correctamente", Toast.LENGTH_SHORT).show()
                    editTextNombreIntegrante.text.clear()
                    editTextCorreoIntegrante.text.clear()
                } else {
                    Toast.makeText(this, "Error al agregar el integrante", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Asegúrate de completar todos los campos", Toast.LENGTH_SHORT).show()
            }
        }


        // Botón para crear la fiesta
        // En tu función onCreate o donde lo necesites
        val datePicker = findViewById<DatePicker>(R.id.datePicker)

// Botón para crear la fiesta
        buttonCrearFiesta.setOnClickListener {
            val nombreFiesta = editTextNombreFiesta.text.toString()

            // Obtén la fecha seleccionada del DatePicker
            val dia = datePicker.dayOfMonth
            val mes = datePicker.month + 1  // Suma 1 porque enero es 0
            val anio = datePicker.year

            // Formatea la fecha como desees (por ejemplo, en formato "dd/MM/yyyy")
            val fechaFiesta = String.format("%02d/%02d/%04d", dia, mes, anio)

            val presupuesto = editTextPresupuesto.text.toString()

            val nuevaFiesta = Fiesta(nombreFiesta, fechaFiesta, presupuesto)
            val database = FirebaseDatabase.getInstance()
            val fiestasReference = database.getReference("nombre_de_tu_base_de_datos/fiestas")
            fiestasReference.push().setValue(nuevaFiesta)
            Toast.makeText(this, "Fiesta creada exitosamente", Toast.LENGTH_SHORT).show()


            // Puedes agregar una notificación o mensaje de éxito aquí
        }

        val buttonFechaFiesta = findViewById<Button>(R.id.buttonFechaFiesta)

        buttonFechaFiesta.setOnClickListener {
            // Mostrar el DatePicker cuando se toca el botón
            datePicker.visibility = View.VISIBLE
        }

// Puedes agregar un botón "Aceptar" en tu diseño y utilizarlo para confirmar la fecha seleccionada
        val buttonAceptarFecha = findViewById<Button>(R.id.buttonFechaFiesta)

        buttonAceptarFecha.setOnClickListener {
            // Aquí obtén la fecha seleccionada del DatePicker
            val day = datePicker.dayOfMonth
            val month = datePicker.month + 1 // El mes comienza desde 0, así que sumamos 1
            val year = datePicker.year

            // Puedes mostrar la fecha en un TextView o realizar la lógica necesaria
            val fechaSeleccionada = "$day/$month/$year"
            textViewFechaSeleccionada.text = fechaSeleccionada

            // Ocultar el DatePicker después de seleccionar la fecha
            datePicker.visibility = View.GONE
        }

        val buttonCerrarSesion = findViewById<Button>(R.id.buttonCerrarSesion)
        val buttonIrAListFiestas = findViewById<Button>(R.id.buttonIrAListFiestas)

        buttonCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Cierra la sesión del usuario
            val intent = Intent(this, MainActivity::class.java) // Reemplaza "LoginActivity" con el nombre de tu actividad de inicio de sesión
            startActivity(intent) // Inicia la actividad de inicio de sesión
            finish() // Cierra la actividad actual
        }

        buttonIrAListFiestas.setOnClickListener {
            val intent = Intent(this, ListFiestasActivity::class.java) // Reemplaza "ListFiestasActivity" con el nombre de tu actividad de lista de fiestas
            startActivity(intent) // Inicia la actividad de lista de fiestas
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
