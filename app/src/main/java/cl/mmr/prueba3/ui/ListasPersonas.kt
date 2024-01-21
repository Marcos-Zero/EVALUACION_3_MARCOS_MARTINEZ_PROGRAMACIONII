package cl.mmr.prueba3.ui
import DATA.DATAS
import DATA.DATASDAO
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.mmr.prueba3.AppBanco
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ListasPersonasViewModel(private val personasDAO: DATASDAO) : ViewModel() {

    private val _personas = mutableStateOf<List<DATAS>>(emptyList())
    val personas: List<DATAS> get() = _personas.value

    init {
        actualizarCliente()
    }

    fun ingresarSolicitud(cliente: DATAS) {
        viewModelScope.launch(Dispatchers.IO) {
            personasDAO.ingresarSolicitud(cliente)
            actualizarCliente()
        }
    }

    private fun actualizarCliente() {
        viewModelScope.launch(Dispatchers.IO) {
            _personas.value = personasDAO.DatosPorfechaDeCreacion()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AppBanco)
                ListasPersonasViewModel(app.datasdao)
            }
        }
    }
}