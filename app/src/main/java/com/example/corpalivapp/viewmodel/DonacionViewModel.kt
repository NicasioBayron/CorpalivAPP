package com.example.corpalivapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.corpalivapp.notifications.Notificaciones
import com.example.corpalivapp.data.Donacion
import com.example.corpalivapp.data.DonacionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.util.Locale
import java.time.LocalDate
import java.io.File
import com.example.corpalivapp.data.CatalogoComunas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.corpalivapp.viewmodel.validation.DonacionValidator

class DonacionViewModel(application: Application): AndroidViewModel(application) {
    private val repositorio = DonacionRepository(application)
    private val _uiState = MutableStateFlow(DonacionUiState())
    val uiState = _uiState.asStateFlow()

    fun actualizarNombre(valor: String){
        if (_uiState.value.guardando) return
        _uiState.update {
            it.copy(nombre = valor, errorNombre = null, mensaje = null)
        }
    }

    fun actualizarCorreo(valor: String){
        if (_uiState.value.guardando) return
        _uiState.update {
            it.copy(correo = valor, errorCorreo = null, mensaje = null)
        }
    }

    fun actualizarMonto(valor: String) {
        if (_uiState.value.guardando) return
        _uiState.update {
            it.copy(monto = valor, errorMonto = null, mensaje = null)
        }
    }

    fun actualizarFecha(valor: LocalDate) {
        if (_uiState.value.guardando) return
        _uiState.update {
            it.copy(fecha = valor, errorFecha = null, mensaje = null)
        }
    }

    fun actualizarCondiciones(valor: Boolean) {
        if (_uiState.value.guardando) return
        _uiState.update {
            it.copy(
                aceptaCondiciones = valor,
                errorCondiciones = null,
                mensaje = null
            )
        }
    }

    fun actualizarSeguimiento(valor: Boolean) {
        if (_uiState.value.guardando) return
        _uiState.update {
            it.copy(recibirSeguimiento = valor, mensaje = null)
        }
    }

    fun validar(): Boolean {
        val datos = _uiState.value

        val errorNombre =
            DonacionValidator.validarNombre(datos.nombre)

        val errorCorreo =
            DonacionValidator.validarCorreo(datos.correo)

        val errorMonto =
            DonacionValidator.validarMonto(datos.monto)

        val errorFecha =
            DonacionValidator.validarFecha(datos.fecha)

        val errorCondiciones =
            DonacionValidator.validarCondiciones(
                datos.aceptaCondiciones
            )

        val errorComuna =
            DonacionValidator.validarComuna(datos.comuna)

        val valido = listOf(
            errorNombre,
            errorCorreo,
            errorMonto,
            errorFecha,
            errorCondiciones,
            errorComuna
        ).all { it == null }

        _uiState.update {
            it.copy(
                errorNombre = errorNombre,
                errorCorreo = errorCorreo,
                errorMonto = errorMonto,
                errorFecha = errorFecha,
                errorCondiciones = errorCondiciones,
                errorComuna = errorComuna,
                mensaje = null
            )
        }
        return valido
    }
    fun guardar(usuarioId: Int?) {
        if (_uiState.value.guardando) return

        if (usuarioId == null) {
            _uiState.update {
                it.copy(mensaje = "Inicia sesión para registrar un aporte.", esError = true)
            }
            return
        }

        if (!validar()) return

        val datos = _uiState.value
        val rutaFoto = datos.fotoComprobante

        if (
            rutaFoto.isNullOrBlank() ||
            !File(rutaFoto).isFile ||
            File(rutaFoto).length() == 0L
        ) {
            _uiState.update {
                it.copy(
                    errorFoto = "Debes tomar y confirmar una foto del documento de prueba."
                )
            }
            return
        }
        val monto = datos.monto.trim().toIntOrNull() ?: return
        val fecha = datos.fecha ?: return

        val donacion = Donacion(
            usuarioId = usuarioId,
            nombre = datos.nombre.trim(),
            correo = datos.correo.trim().lowercase(Locale.ROOT),
            monto = monto,
            fechaAporte = fecha.toString(),
            aceptaCondiciones = datos.aceptaCondiciones,
            recibirSeguimiento = datos.recibirSeguimiento,
            fotoComprobante = datos.fotoComprobante,
            fechaRegistro = System.currentTimeMillis(),
            region = CatalogoComunas.REGION,
            comuna = datos.comuna,
        )

        _uiState.update {
            it.copy(guardando = true, mensaje = null, esError = false)
        }

        viewModelScope.launch {
            try {
                val id = repositorio.registrar(donacion)

                // Un aviso bloqueado nunca convierte un guardado correcto en un fallo.
                runCatching {
                    Notificaciones.enviarAporteRegistrado(getApplication(), id)
                }

                _uiState.value = DonacionUiState(
                    mensaje = "Aporte de prueba #$id registrado. No se realizó ningún cobro."
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        mensaje = "No se pudo guardar el aporte. Intenta nuevamente.",
                        esError = true
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(guardando = false)
                }
            }
        }
    }
    fun actualizarFoto(ruta: String) {
        if (_uiState.value.guardando) return

        _uiState.update {
            it.copy(
                fotoComprobante = ruta,
                errorFoto = null,
                mensaje = null
            )
        }
    }

    fun actualizarBusquedaComuna(valor: String) {
        if (_uiState.value.guardando) return

        _uiState.update {
            it.copy(
                busquedaComuna = valor,
                comuna = "",
                errorComuna = null,
                mensaje = null
            )
        }
    }

    fun seleccionarComuna(valor: String) {
        if (_uiState.value.guardando) return
        if (valor !in CatalogoComunas.comunas) return

        _uiState.update {
            it.copy(
                busquedaComuna = valor,
                comuna = valor,
                errorComuna = null,
                mensaje = null
            )
        }
    }
}