package co.edu.udea.compumovil.gr11_20232.partyplanner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FiestaAdapter(private val context: Context, private val fiestas: List<Fiesta>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return fiestas.size
    }

    override fun getItem(position: Int): Any {
        return fiestas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_fiesta, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val fiesta = getItem(position) as Fiesta

        viewHolder.nombreFiestaTextView.text = "Nombre: ${fiesta.nombre}"
        viewHolder.fechaFiestaTextView.text = "Fecha: ${fiesta.fecha}"
        viewHolder.presupuestoTextView.text = "Presupuesto: ${fiesta.presupuesto}"
        // Agregar lógica para el botón de editar
        viewHolder.buttonEditFiesta.setOnClickListener {
            // Lógica para manejar el clic del botón de editar
            // Puedes obtener el ID de la fiesta usando fiesta.id o alguna propiedad similar
            val idFiesta = fiesta.toString()
            // Aquí puedes abrir una nueva actividad para editar la fiesta, o realizar alguna otra acción
            Toast.makeText(context, "Editando fiesta con ID: $idFiesta", Toast.LENGTH_SHORT).show()
        }

        // Agregar lógica para el botón de eliminar
        viewHolder.buttonDeleteFiesta.setOnClickListener {
            // Lógica para manejar el clic del botón de eliminar
            val databaseInstance = FirebaseDatabase.getInstance()
            val ref = databaseInstance.getReference("party-planner-15590/fiestas")
            // Genera una nueva clave única para el integrante
            val nuevaKey = fiesta.nombre
            val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            if (nuevaKey != null && fiesta.creadorUid == currentUser?.uid) {
                // Elimina la fiesta en la ubicación deseada en la base de datos
                ref.child(nuevaKey).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Eliminando fiesta con ID: $nuevaKey",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "falló ID: $nuevaKey", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Error al eliminar la fiesta", Toast.LENGTH_SHORT).show()
            }
            // Puedes obtener el ID de la fiesta usando fiesta.id o alguna propiedad similar

            // Aquí puedes eliminar la fiesta de la base de datos u realizar alguna otra acción
        }

        return view
    }

    private class ViewHolder(view: View) {
        val nombreFiestaTextView: TextView = view.findViewById(R.id.textNombreFiesta)
        val fechaFiestaTextView: TextView = view.findViewById(R.id.textFechaFiesta)
        val presupuestoTextView: TextView = view.findViewById(R.id.textPresupuesto)
        val buttonEditFiesta: ImageButton = view.findViewById(R.id.buttonEditFiesta)
        val buttonDeleteFiesta: ImageButton = view.findViewById(R.id.buttonDeleteFiesta)
    }
}


class ListFiestasActivity : AppCompatActivity() {
    private lateinit var listViewFiestas: ListView
    private lateinit var fiestasList: MutableList<Fiesta>
    private lateinit var adapter: FiestaAdapter  // Agrega un campo para el adaptador

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_fiestas)

        listViewFiestas = findViewById(R.id.listViewFiestas)
        fiestasList = mutableListOf()
        adapter = FiestaAdapter(this, fiestasList)  // Crea el adaptador aquí

        // Conecta con la base de datos de Firebase
        val database = FirebaseDatabase.getInstance()
        val fiestasReference = database.getReference("party-planner-15590/fiestas")
        val buttonAddFiesta = findViewById<Button>(R.id.buttonAddFiesta)

        // Agrega un escuchador para obtener las fiestas del usuario
        fiestasReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                fiestasList.clear()

                val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

                if (currentUser != null) {
                    for (snapshot in dataSnapshot.children) {
                        val fiesta = snapshot.getValue(Fiesta::class.java)

                        // Verificar si la fiesta pertenece al usuario actual comparando con el UID del creador
                        if (fiesta != null && fiesta.creadorUid == currentUser.uid) {
                            fiestasList.add(fiesta)
                        }
                    }
                }

                // Después de llenar fiestasList
                if (fiestasList.isEmpty()) {
                    val textNoFiestas = findViewById<TextView>(R.id.textNoFiestas)
                    textNoFiestas.visibility = View.VISIBLE
                } else {
                    // Configura el adaptador en el ListView
                    listViewFiestas.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores de la base de datos si es necesario
                Log.e("FirebaseDatabase", "Error de Firebase Database: ${databaseError.message}")
            }

        })
        buttonAddFiesta.setOnClickListener {
            val intent = Intent(
                this,
                FeedActivity::class.java
            ) // Reemplaza "ListFiestasActivity" con el nombre de tu actividad de lista de fiestas
            startActivity(intent) // Inicia la actividad de lista de fiestas
        }


    }

}
