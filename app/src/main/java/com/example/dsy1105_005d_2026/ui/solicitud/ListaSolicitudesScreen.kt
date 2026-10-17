package com.example.dsy1105_005d_2026.ui.solicitud

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaSolicitudesScreen(
    onAgregar: () -> Unit,
    onSeleccionar: (Int) -> Unit,
    vm: SolicitudViewModel = viewModel()
) {
    val solicitudes by vm.solicitudes.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Solicitudes") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregar) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        if (solicitudes.isEmpty()) {
            Text("No hay solicitudes", modifier = Modifier.padding(padding).padding(16.dp))
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp)
            ) {
                items(solicitudes, key = { it.id }) { s ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { onSeleccionar(s.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(s.codigo, style = MaterialTheme.typography.labelLarge)
                            Text(s.titulo, style = MaterialTheme.typography.titleMedium)
                            Text("Categoría: ${s.categoria}")
                            Text("Estado: ${s.estado}")
                        }
                    }
                }
            }
        }
    }
}
