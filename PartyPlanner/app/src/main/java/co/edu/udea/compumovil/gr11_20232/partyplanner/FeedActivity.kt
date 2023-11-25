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
    var nombre: String? = "",
    var fecha: String? = "",
    var presupuesto: String? = "",
    var creadorUid: String? = ""

)

class FeedActivity : AppCompatActivity() {
    private lateinit var editTextNombreFiesta: EditText
    private lateinit var buttonFechaFiesta: Button
    private lateinit var textViewFechaSeleccionada: TextView
    private lateinit var editTextNombreIntegrante: EditText
    private lateinit var editTextCorreoIntegrante: EditText
    private lateinit var buttonAgregarIntegrante: Button
    private lateinit var editTextPresupuesto: EditText
    private lateinit var buttonCrearFiesta: Button
    private lateinit var datePicker: DatePicker
    private lateinit var buttonAceptarFecha: Button
    private lateinit var buttonCerrarSesion: Button
    private lateinit var buttonIrAListFiestas: Button

    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        editTextNombreFiesta = findViewById(R.id.editTextNombreFiesta)
        buttonFechaFiesta = findViewById(R.id.buttonFechaFiesta)
        textViewFechaSeleccionada = findViewById(R.id.textViewFechaSeleccionada)
        editTextNombreIntegrante = findViewById(R.id.editTextNombreIntegrante)
        editTextCorreoIntegrante = findViewById(R.id.editTextCorreoIntegrante)
        buttonAgregarIntegrante = findViewById(R.id.buttonAgregarIntegrante)
        editTextPresupuesto = findViewById(R.id.editTextPresupuesto)
        buttonCrearFiesta = findViewById(R.id.buttonCrearFiesta)
        datePicker = findViewById(R.id.datePicker)
        buttonAceptarFecha = findViewById(R.id.buttonAceptarFecha)
        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion)
        buttonIrAListFiestas = findViewById(R.id.buttonIrAListFiestas)

        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val buttonAceptarFecha = findViewById<Button>(R.id.buttonAceptarFecha)
        val buttonCerrarSesion = findViewById<Button>(R.id.buttonCerrarSesion)
        val buttonIrAListFiestas = findViewById<Button>(R.id.buttonIrAListFiestas)
        val buttonFechaFiesta = findViewById<Button>(R.id.buttonFechaFiesta)

        // Botón para seleccionar la fecha de la fiesta
        buttonFechaFiesta.setOnClickListener {
            datePicker.visibility = View.VISIBLE
        }

        // Botón para agregar un integrante
        buttonAgregarIntegrante.setOnClickListener {
            val nombreIntegrante = editTextNombreIntegrante.text.toString()
            val correoIntegrante = editTextCorreoIntegrante.text.toString()

            // Verifica que los campos no estén vacíos
            if (nombreIntegrante.isNotEmpty() && isEmailValid(correoIntegrante)) {
                // Obtén una referencia a la base de datos de Firebase
                val database = FirebaseDatabase.getInstance()
                val ref = database.getReference("party-planner-15590/integrantes-59c22")

                // Crea un nuevo objeto Integrante con los datos ingresados
                val integrante = Integrante(nombreIntegrante, correoIntegrante)

                // Genera una nueva clave única para el integrante
                val nuevaKey = ref.push().key

                if (nuevaKey != null) {
                    // Guarda el integrante en la ubicación deseada en la base de datos
                    ref.child("integrantes").child(nuevaKey).setValue(integrante)
                    Toast.makeText(this, "Integrante agregado correctamente", Toast.LENGTH_SHORT)
                        .show()
                    editTextNombreIntegrante.text.clear()
                    editTextCorreoIntegrante.text.clear()
                } else {
                    Toast.makeText(this, "Error al agregar el integrante", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Asegúrate de completar todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }
        }





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

        // Botón para crear la fiesta
        buttonCrearFiesta.setOnClickListener {
            val nombreFiesta = editTextNombreFiesta.text.toString()
            val presupuesto = editTextPresupuesto.text.toString()
            val selectedDate = textViewFechaSeleccionada.text.toString()

            val currentUser = FirebaseAuth.getInstance().currentUser
            val creadorUid = currentUser?.uid

            if (creadorUid != null) {
                // Verifica que tenga nombre y presupuesto
                if (nombreFiesta.isEmpty()) {
                    Toast.makeText(this, "Ingresa un nombre de fiesta", Toast.LENGTH_SHORT).show()
                } else if (presupuesto.isEmpty()) {
                    Toast.makeText(this, "Ingresa un presupuesto", Toast.LENGTH_SHORT).show()
                } else {
                    val nuevaFiesta = Fiesta(nombreFiesta, selectedDate, presupuesto, creadorUid)
                    val database = FirebaseDatabase.getInstance()
                    val fiestasReference = database.getReference("party-planner-15590/fiestas")
                    fiestasReference.child(nombreFiesta).setValue(nuevaFiesta)
                    Toast.makeText(this, "Fiesta creada exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ListFiestasActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                // El usuario no está autenticado o no se pudo obtener el creadorUid
                // Debes manejar esta situación según tu lógica de la aplicación
                Toast.makeText(this, "No se pudo obtener el creadorUid", Toast.LENGTH_SHORT).show()
            }


        }

        buttonCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Cierra la sesión del usuario
            val intent = Intent(
                this,
                MainActivity::class.java
            ) // Reemplaza "LoginActivity" con el nombre de tu actividad de inicio de sesión
            startActivity(intent) // Inicia la actividad de inicio de sesión
            finish() // Cierra la actividad actual
        }

        buttonIrAListFiestas.setOnClickListener {
            val intent = Intent(
                this,
                ListFiestasActivity::class.java
            ) // Reemplaza "ListFiestasActivity" con el nombre de tu actividad de lista de fiestas
            startActivity(intent) // Inicia la actividad de lista de fiestas
        }


    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"
                textViewFechaSeleccionada.text = "Fecha seleccionada: $selectedDate"
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    fun aceptarFecha(view: View) {
        val day = datePicker.dayOfMonth
        val month = datePicker.month + 1
        val year = datePicker.year
        val fechaSeleccionada = "$day/$month/$year"
        textViewFechaSeleccionada.text = fechaSeleccionada
        datePicker.visibility = View.GONE
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }
}
