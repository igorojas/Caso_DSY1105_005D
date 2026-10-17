package com.example.dsy1105_005d_2026.ui.solicitud

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(
    onEncontrado: (Int) -> Unit,
    vm: SolicitudViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var permiso by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    var mensaje by remember { mutableStateOf("Apunta la cámara al QR de una solicitud") }
    val ocupado = remember { booleanArrayOf(false) }
    val ejecutor = remember { Executors.newSingleThreadExecutor() }

    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        permiso = it
    }

    LaunchedEffect(Unit) {
        if (!permiso) pedirPermiso.launch(Manifest.permission.CAMERA)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Leer código QR") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(mensaje, modifier = Modifier.padding(bottom = 12.dp))

            if (permiso) {
                AndroidView(
                    factory = { ctx ->
                        val vista = PreviewView(ctx)
                        val futuro = ProcessCameraProvider.getInstance(ctx)
                        futuro.addListener({
                            val proveedor = futuro.get()
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(vista.surfaceProvider)
                            }
                            val scanner = BarcodeScanning.getClient(
                                BarcodeScannerOptions.Builder()
                                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                                    .build()
                            )
                            val analisis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                            analisis.setAnalyzer(ejecutor) { proxy ->
                                if (ocupado[0]) {
                                    proxy.close()
                                    return@setAnalyzer
                                }
                                val imagen = InputImage.fromBitmap(
                                    proxy.toBitmap(),
                                    proxy.imageInfo.rotationDegrees
                                )
                                scanner.process(imagen)
                                    .addOnSuccessListener { codigos ->
                                        val valor = codigos.firstOrNull()?.rawValue
                                        if (valor != null && !ocupado[0]) {
                                            ocupado[0] = true
                                            vm.buscarPorCodigo(valor) { id ->
                                                if (id != null) {
                                                    onEncontrado(id)
                                                } else {
                                                    mensaje = "Código no registrado: $valor"
                                                    ocupado[0] = false
                                                }
                                            }
                                        }
                                    }
                                    .addOnCompleteListener { proxy.close() }
                            }
                            proveedor.unbindAll()
                            proveedor.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                analisis
                            )
                        }, ContextCompat.getMainExecutor(ctx))
                        vista
                    },
                    modifier = Modifier.fillMaxWidth().height(360.dp)
                )
            } else {
                Button(onClick = { pedirPermiso.launch(Manifest.permission.CAMERA) }) {
                    Text("Permitir cámara")
                }
            }
        }
    }
}
