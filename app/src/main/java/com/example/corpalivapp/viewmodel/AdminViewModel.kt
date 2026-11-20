package com.example.corpalivapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.data.DatabaseProvider
import com.example.corpalivapp.data.Donacion
import com.example.corpalivapp.data.DonacionRepository
import com.example.corpalivapp.data.PasswordUtils
import com.example.corpalivapp.data.Usuario
import com.example.corpalivapp.network.repository.DonacionNetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AdminUiState(
    val cargando: Boolean = true,
    val todasLasDonaciones: List<Donacion> = emptyList(),
    val donacionesFiltradas: List<Donacion> = emptyList(),
    val filtroEstado: String = "TODAS",
    val mensaje: String? = null,
    val error: String? = null
) {
    val totalRecaudado: Long
        get() = todasLasDonaciones.filter { it.estado.equals("APROBADA", ignoreCase = true) }
            .fold(0L) { acc, d -> acc + d.monto }

    val totalRecaudadoGeneral: Long
        get() = todasLasDonaciones.fold(0L) { acc, d -> acc + d.monto }

    val cantidadPendientes: Int
        get() = todasLasDonaciones.count { it.estado.equals("PENDIENTE", ignoreCase = true) }

    val cantidadAprobadas: Int
        get() = todasLasDonaciones.count { it.estado.equals("APROBADA", ignoreCase = true) }

    val cantidadRechazadas: Int
        get() = todasLasDonaciones.count { it.estado.equals("RECHAZADA", ignoreCase = true) }
}

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = DonacionRepository(application)
    private val networkRepo = DonacionNetworkRepository()

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observarDonaciones()
    }

    fun observarDonaciones() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }

            // Intentar sincronizar todas las donaciones desde Retrofit
            runCatching {
                val remota = withContext(Dispatchers.IO) { networkRepo.listarTodas() }
                if (remota.isSuccess) {
                    val listaRemota = remota.getOrNull().orEmpty()
                    withContext(Dispatchers.IO) {
                        val daoUsuario = DatabaseProvider.obtenerDatabase(getApplication()).usuarioDao()
                        for (d in listaRemota) {
                            val idRemoto = d.id?.toInt() ?: continue
                            val uId = d.usuarioId?.toInt() ?: 1
                            if (daoUsuario.buscarPorId(uId) == null) {
                                val salt = PasswordUtils.generarSalt()
                                daoUsuario.registrarUsuario(
                                    Usuario(
                                        id = uId,
                                        nombre = d.nombreDonante ?: "Donante",
                                        correo = d.correoDonante ?: "donante@corpaliv.cl",
                                        claveHash = PasswordUtils.generarHash("Temporal123!", salt),
                                        claveSalt = salt
                                    )
                                )
                            }
                            repositorio.registrarOActualizar(
                                Donacion(
                                    id = idRemoto,
                                    usuarioId = uId,
                                    nombre = d.nombreDonante ?: "Donante",
                                    correo = d.correoDonante ?: "donante@corpaliv.cl",
                                    monto = d.monto ?: 0,
                                    fechaAporte = d.fechaAporte ?: "",
                                    aceptaCondiciones = true,
                                    recibirSeguimiento = false,
                                    fotoComprobante = d.fotoComprobante,
                                    fechaRegistro = d.fechaRegistro ?: System.currentTimeMillis(),
                                    region = d.region,
                                    comuna = d.comuna,
                                    estado = d.estado ?: "PENDIENTE"
                                )
                            )
                        }
                    }
                }
            }

            repositorio.observarTodas()
                .catch { ex ->
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            error = ex.message ?: "Error al cargar donaciones"
                        )
                    }
                }
                .collect { lista ->
                    _uiState.update { estado ->
                        estado.copy(
                            cargando = false,
                            todasLasDonaciones = lista,
                            donacionesFiltradas = filtrar(lista, estado.filtroEstado)
                        )
                    }
                }
        }
    }

    fun cambiarFiltro(filtro: String) {
        _uiState.update { estado ->
            estado.copy(
                filtroEstado = filtro,
                donacionesFiltradas = filtrar(estado.todasLasDonaciones, filtro)
            )
        }
    }

    fun cambiarEstado(id: Int, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                // 1. Actualizar en Room local
                repositorio.actualizarEstado(id, nuevoEstado)

                // 2. Intentar actualizar en el microservicio vía Retrofit
                val res = withContext(Dispatchers.IO) {
                    networkRepo.cambiarEstado(id.toLong(), nuevoEstado)
                }

                val msg = if (res.isSuccess) {
                    "Donación #$id actualizada a $nuevoEstado (servidor y local)"
                } else {
                    "Donación #$id actualizada localmente a $nuevoEstado"
                }

                _uiState.update {
                    it.copy(mensaje = msg)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "No se pudo actualizar el estado: ${e.message}")
                }
            }
        }
    }

    fun limpiarMensaje() {
        _uiState.update { it.copy(mensaje = null, error = null) }
    }

    private fun filtrar(lista: List<Donacion>, filtro: String): List<Donacion> {
        return when (filtro.uppercase()) {
            "PENDIENTE" -> lista.filter { it.estado.equals("PENDIENTE", ignoreCase = true) }
            "APROBADA" -> lista.filter { it.estado.equals("APROBADA", ignoreCase = true) }
            "RECHAZADA" -> lista.filter { it.estado.equals("RECHAZADA", ignoreCase = true) }
            else -> lista
        }
    }
}
