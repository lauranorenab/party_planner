package co.edu.udea.compumovil.gr11_20232.partyplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.text.InputType
import android.text.TextWatcher
import android.text.Editable
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)

        // Configura el teclado para mostrar la "@" en el campo de correo electrónico
        emailEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        // Agrega una validación para el correo electrónico
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (!isValidEmail(email)) {
                    emailEditText.error = "Correo electrónico no válido"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateInput(email, password)) {
                if (!isUserRegistered(email)) {
                    showRegistrationDialog()
                } else if (areCredentialsIncorrect(email, password)) {
                    showIncorrectCredentialsDialog()
                } else {
                    signInUser(email, password)
                }
            } else {
                Toast.makeText(this, "Ingresa correo y contraseña válidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para validar el correo electrónico
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Función para verificar si el usuario está registrado
    private fun isUserRegistered(email: String): Boolean {
        val user = auth.currentUser
        return user != null && user.email == email
    }

    // Función para verificar si las credenciales son incorrectas
    private fun areCredentialsIncorrect(email: String, password: String): Boolean {
        val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, password)
        try {
            auth.signInWithCredential(credential)
            return false
        } catch (e: Exception) {
            return true
        }
    }

    // Función para mostrar un cuadro de diálogo para el registro
    private fun showRegistrationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Usuario no registrado")
            .setMessage("¿Desea registrarse?")
            .setPositiveButton("Registrar") { _, _ ->
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Función para mostrar un cuadro de diálogo de credenciales incorrectas
    private fun showIncorrectCredentialsDialog() {
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        AlertDialog.Builder(this)
            .setTitle("Usuario o contraseña incorrectos")
            .setMessage("¿Desea intentar de nuevo?")
            .setPositiveButton("Intentar de nuevo") { _, _ ->
                emailEditText.text.clear()
                passwordEditText.text.clear()
            }
            .show()
    }

    // Función para iniciar sesión
    private fun signInUser(email: String, password: String) {
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    emailEditText.text.clear()
                    passwordEditText.text.clear()
                }
            }
    }

    // Función para validar la entrada del usuario
    private fun validateInput(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }
}


