package cl.mmr.prueba3

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.PreviewView.ScaleType
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}


    private lateinit var cameraExecutor: Executor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        cameraExecutor = Executors.newSingleThreadExecutor()

        setContent {

            UbicacionUI()

            val imageCapture = remember {
                ImageCapture.Builder().build()

            }
            Box(modifier = Modifier.fillMaxSize()) {
                CameraPreview(imageCapture = imageCapture)
                IconButton(onClick = { TomarFoto(imageCapture) }) {
                    Image(
                        painter = painterResource(id = androidx.core.R.drawable.ic_call_decline),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                            .size(300.dp)
                    )

                }
            }


        }
    }

    private fun TomarFoto(imageCapture: ImageCapture) {
        val file = File.createTempFile("img", ".jpg")
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(
            outputFileOptions,
            cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback(),
                ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    println("el Uri es ${outputFileResults.savedUri}")
                }
            })
    }


    @Composable
    fun CameraPreview(
        scaleType: ScaleType = ScaleType.FILL_CENTER,
        cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
        imageCapture: ImageCapture

    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val coroutineScope = rememberCoroutineScope()
        AndroidView(factory = { context ->
            val previewView = PreviewView(context).apply {

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                this.scaleType = scaleType
            }


            val previewUseCase = Preview.Builder().build()


            val surfaceProvider = previewView.createAccessibilityNodeInfo()

            previewUseCase.setSurfaceProvider(surfaceProvider)

            coroutineScope.launch {
                val cameraProvider = context.cameraProvider()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    previewUseCase,
                    imageCapture
                )
            }




            previewView


        })
    }


    suspend fun Context.cameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->


        val listenableFuture = ProcessCameraProvider.getInstance(this)
        listenableFuture.addListener({

            continuation.resume(listenableFuture.get())

        }, ContextCompat.getMainExecutor(this))

    }

    @Preview(showSystemUi = true)
    @Composable
    fun FormIngresoUI() {
        val (usuario, setUsuario) = remember { mutableStateOf("") }
        val (contrasena, setContrasena) = remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.padding(60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "IplaBank",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextField(
                    value = usuario,
                    onValueChange = { setUsuario(it) },
                    label = { Text("Usuario") }
                )
                TextField(
                    value = contrasena,
                    onValueChange = { setContrasena(it) },
                    label = { Text("Contraseña") }
                )
                Spacer(modifier = Modifier.height(40.dp))

                // Primer botón
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Ingresar")
                }

                // Separador
                Spacer(modifier = Modifier.height(16.dp))

                // Segundo botón
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Solicitar Cuenta")
                }
            }
        }
    }

    @Composable
    fun FormCuenta() {
        var nombre by remember { mutableStateOf<String>("") }
        var Rutificador by remember { mutableStateOf<String>("") }
        var fecha_de_nacimiento by remember { mutableStateOf<String>("") }
        var Correo by remember { mutableStateOf<String>("") }
        var Fono by remember { mutableStateOf<String>("") }
        var CarnetFrente by remember { mutableStateOf<String>("") }
        var CarnetTrasera by remember { mutableStateOf<String>("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = Rutificador,
                onValueChange = { Rutificador = it },
                label = { Text("RUT") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = fecha_de_nacimiento,
                onValueChange = { fecha_de_nacimiento = it },
                label = { Text("Fecha de Nacimiento") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = Correo,
                onValueChange = { Correo = it },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = Fono,
                onValueChange = { Fono = it },
                label = { Text("Teléfono") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = CarnetFrente,
                onValueChange = { CarnetFrente = it },
                label = { Text("Imagen de la cédula frontal") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = CarnetTrasera,
                onValueChange = { CarnetTrasera = it },
                label = { Text("Imagen de la cédula trasera") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { /* Realizar acción al enviar el formulario */ }) {
                Text(text = "Enviar Solicitud")
            }
        }
    }

    @Composable
    private fun AppBankUI(ListaPersonas: ListasPersonasViewModel) {
        LazyColumn {
            items(ListaPersonas.personas) { persona ->
                // Ajusta esta línea según la estructura de tu clase DATAS
                Text(persona.nombre)
            }
        }
    }

    @Composable
    fun UbicacionUI() {
        val contexto = LocalContext.current
        var mensaje by rememberSaveable { mutableStateOf("ubicacion: ") }
        val lanzadorPermisos = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = {
                if (it.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false)
                    ||
                    it.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false)
                ) {

                    val locationServices = LocationServices.getFusedLocationProviderClient(contexto)
                    UBICACIONES(locationServices)

                } else {

                    mensaje = "otorgar permisos"
                }
            }
        )
        Column {
            Text(mensaje)
            Button(onClick = {
                // Realizar acción al solicitar ubicación
            }) {
                Text("Ubicacion")
            }
        }
    }
}

