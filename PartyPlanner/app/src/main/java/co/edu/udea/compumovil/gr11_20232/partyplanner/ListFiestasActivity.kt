package co.edu.udea.compumovil.gr11_20232.partyplanner

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
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.ArrayList

class FiestaAdapter(private val context: Context, private val fiestas: List<Fiesta>) : BaseAdapter() {

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

        return view
    }

    private class ViewHolder(view: View) {
        val nombreFiestaTextView: TextView = view.findViewById(R.id.textNombreFiesta)
        val fechaFiestaTextView: TextView = view.findViewById(R.id.textFechaFiesta)
        val presupuestoTextView: TextView = view.findViewById(R.id.textPresupuesto)
    }
}


class ListFiestasActivity : AppCompatActivity() {
    private lateinit var listViewFiestas: ListView
    private lateinit var fiestasList: MutableList<Fiesta>
    private lateinit var adapter: FiestaAdapter  // Agrega un campo para el adaptador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_fiestas)

        listViewFiestas = findViewById(R.id.listViewFiestas)
        fiestasList = mutableListOf()
        adapter = FiestaAdapter(this, fiestasList)  // Crea el adaptador aquí

        // Conecta con la base de datos de Firebase
        val database = FirebaseDatabase.getInstance()
        val fiestasReference = database.getReference("party-planner-15590/fiestas")
        val ButtonAddFiesta = findViewById<Button>(R.id.buttonAddFiesta)

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
                }
                else {
                    // Configura el adaptador en el ListView
                    listViewFiestas.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores de la base de datos si es necesario
                Log.e("FirebaseDatabase", "Error de Firebase Database: ${databaseError.message}")
            }

        })
        ButtonAddFiesta.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java) // Reemplaza "ListFiestasActivity" con el nombre de tu actividad de lista de fiestas
            startActivity(intent) // Inicia la actividad de lista de fiestas
        }

    }

}
