package cl.mmr.prueba3.ui

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


class UBICACIONES (
    val fusedLocationProviderClient: FusedLocationProviderClient

){
    @SuppressLint("MissingPermission")
    fun encontrarUbicacion(
        onExito: (Location) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val prueba = fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            null
        )
        prueba.addOnSuccessListener { onExito(it) }
        prueba.addOnFailureListener { onError(it) }
    }
}



