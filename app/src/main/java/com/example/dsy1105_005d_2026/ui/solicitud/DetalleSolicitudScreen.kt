package com.example.dsy1105_005d_2026.ui.solicitud

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleSolicitudScreen(
    id: Int,
    onEditar: (Int) -> Unit,
    onEliminado: () -> Unit,
    vm: SolicitudViewModel = viewModel()
) {
    val solicitud by remember(id) { vm.observar(id) }.collectAsState(initial = null)
    var confirmar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalle de solicitud") }) }
    ) { padding ->
        solicitud?.let { s ->
            val qr = remember(s.codigo) { generarQr(s.codigo).asImageBitmap() }

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(s.titulo, style = MaterialTheme.typography.headlineSmall)
                Text("Código: ${s.codigo}")
                Text("Categoría: ${s.categoria}")
                Text("Servicio relacionado: ${s.servicio}")
                Text("Estado: ${s.estado}")
                Text("Fecha: ${formatoFecha(s.fecha)}")
                Text("Descripción: ${s.descripcion}")

                s.evidenciaUri?.let {
                    Text("Evidencia:")
                    AsyncImage(
                        model = it,
                        contentDescription = "Evidencia",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(220.dp)
                    )
                }

                Text("Código QR de seguimiento")
                Image(
                    bitmap = qr,
                    contentDescription = "QR de ${s.codigo}",
                    modifier = Modifier.size(220.dp)
                )

                Button(
                    onClick = { onEditar(s.id) },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Editar")
                }
                OutlinedButton(
                    onClick = { confirmar = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar")
                }
            }

            if (confirmar) {
                AlertDialog(
                    onDismissRequest = { confirmar = false },
                    title = { Text("Eliminar solicitud") },
                    text = { Text("¿Eliminar ${s.codigo}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            confirmar = false
                            vm.eliminar(s.id, onEliminado)
                        }) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { confirmar = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
