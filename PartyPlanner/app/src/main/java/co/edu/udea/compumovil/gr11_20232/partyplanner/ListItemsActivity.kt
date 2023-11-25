package co.edu.udea.compumovil.gr11_20232.partyplanner

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ItemAdapter(private val context: Context, private val items: List<Item>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
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

        val item = getItem(position) as Item

        viewHolder.nombreFiestaTextView.text = "${item.nombre}"
        viewHolder.fechaFiestaTextView.text = "${item.cantidad}"
        viewHolder.presupuestoTextView.text = "${item.precio}"

        // Agregar lógica para el botón de editar
        viewHolder.buttonEditFiesta.setOnClickListener {
            // Lógica para manejar el clic del botón de editar
            // Puedes obtener el ID de la fiesta usando fiesta.id o alguna propiedad similar
            val idFiesta = item.nombre
            // Aquí puedes abrir una nueva actividad para editar la fiesta, o realizar alguna otra acción
            val intent = Intent(context, ListItemsActivity::class.java)
            context.startActivity(intent)
        }

        // Agregar lógica para el botón de eliminar
        viewHolder.buttonDeleteFiesta.setOnClickListener {
            // Lógica para manejar el clic del botón de eliminar
            val databaseInstance = FirebaseDatabase.getInstance()
            val ref = databaseInstance.getReference("party-planner-15590/fiestas")
            // Genera una nueva clave única para el integrante
            val nuevaKey = item.nombre
            val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            if (nuevaKey != null) {
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

data class Item(
    var nombre: String? = "",
    var cantidad: String? = "",
    var precio: String? = "",
)

class ListItemsActivity : AppCompatActivity() {
    private lateinit var listViewItems: ListView
    private lateinit var itemList: MutableList<Item>
    private lateinit var adapter: ItemAdapter  // Agrega un campo para el adaptador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_items)
        val buttonIrAListFiestas = findViewById<Button>(R.id.buttonIrAListFiestas)

        listViewItems = findViewById(R.id.listViewItems)
        itemList = mutableListOf()
        adapter = ItemAdapter(this, itemList)  // Crea el adaptador aquí
        val buttonAddItem = findViewById<Button>(R.id.buttonNuevoItem)

        //Renderiza los objetos.
        itemList.clear()
        itemList.add(Item("Cerveza", "10", "10000"))
        itemList.add(Item("Carne", "1", "50000"))
        itemList.add(Item("Vodka", "2", "75000"))
        itemList.add(Item("Whisky", "5", "80000"))
        itemList.add(Item("Tequila", "1", "150000"))

        // Después de llenar fiestasList
        if (itemList.isEmpty()) {
            val textNoFiestas = findViewById<TextView>(R.id.textNoFiestas)
            textNoFiestas.visibility = View.VISIBLE
        } else {
            // Configura el adaptador en el ListView
            listViewItems.adapter = adapter
        }

        buttonAddItem.setOnClickListener {
            val intent = Intent(
                this,
                FeedActivity::class.java
            ) // Reemplaza "ListFiestasActivity" con el nombre de tu actividad de lista de fiestas
            startActivity(intent) // Inicia la actividad de lista de fiestas
        }

        buttonIrAListFiestas.setOnClickListener {
            val intent = Intent(
                this,
                ListFiestasActivity::class.java
            ) // Reemplaza "ListFiestasActivity" con el nombre de tu actividad de lista de fiestas
            startActivity(intent) // Inicia la actividad de lista de fiestas
        }


    }
}
