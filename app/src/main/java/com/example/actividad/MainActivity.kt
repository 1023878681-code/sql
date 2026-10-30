package com.example.actividad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    InterfazSQLite(dbHelper)
                }
            }
        }
    }
}

@Composable
fun InterfazSQLite(dbHelper: DatabaseHelper) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var listaUsuarios by remember { mutableStateOf(dbHelper.obtenerUsuarios()) }

    InterfazUI(
        nombre = nombre,
        email = email,
        listaUsuarios = listaUsuarios,
        onNombreChange = { nombre = it },
        onEmailChange = { email = it },
        onGuardarClick = {
            if (nombre.isNotBlank() && email.isNotBlank()) {
                dbHelper.insertarUsuario(Usuario(nombre = nombre, email = email))
                listaUsuarios = dbHelper.obtenerUsuarios()
                nombre = ""
                email = ""
            }
        }
    )
}

@Composable
fun InterfazUI(
    nombre: String,
    email: String,
    listaUsuarios: List<Usuario>,
    onNombreChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onGuardarClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Mi Aplicacion",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onGuardarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Lista de usuarios (${listaUsuarios.size}):",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(listaUsuarios) { usuario ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "${usuario.id}. ${usuario.nombre}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = usuario.email, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInterfaz() {
    MaterialTheme {
        InterfazUI(
            nombre = "Juan",
            email = "juan@correo.com",
            listaUsuarios = listOf(
                Usuario(1, "Ana Pérez", "ana@correo.com"),
                Usuario(2, "Juan Pérez", "juan@correo.com")
            ),
            onNombreChange = {},
            onEmailChange = {},
            onGuardarClick = {}
        )
    }
}