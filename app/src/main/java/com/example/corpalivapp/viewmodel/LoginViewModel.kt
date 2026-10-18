package com.example.corpalivapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.corpalivapp.viewmodel.validation.LoginValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun actualizarCorreo(valor: String){
        _uiState.update { estado -> estado.copy(correo = valor, errorCorreo = null, mensaje = null) }
    }
    fun actualizarClave(valor: String){
        _uiState.update { estado -> estado.copy(
            clave = valor,
            errorClave = null,
            mensaje = null
        ) }
    }
    fun validarFormulario(){
        val estado = _uiState.value
        val errorCorreo = LoginValidator.validarCorreo(estado.correo)
        val errorClave = LoginValidator.validarClave(estado.clave)
        val esValido = errorCorreo == null && errorClave == null

        _uiState.update { actual -> actual.copy(
            errorCorreo = errorCorreo,
            errorClave = errorClave,
            mensaje = if (esValido){
                "Inicio de sesion correcto"
            } else {
                "Error, no se pudo iniciar sesion"
            }
        ) }
    }
}