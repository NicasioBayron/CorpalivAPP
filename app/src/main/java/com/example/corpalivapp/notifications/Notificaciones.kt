package com.example.corpalivapp.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.corpalivapp.MainActivity
import com.example.corpalivapp.R

object Notificaciones{
    private const val CANAL_ID = "recordatorios_corpaliv"
    private const val NOTIFICACION_ID = 100

    fun crearCanal(context: Context){
        val canal = NotificationChannel(
            CANAL_ID,
            "Recordatorios",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Avisos y recordatorios de CORPALIV"
        }

        val administrador = context.getSystemService(
            NotificationManager::class.java
        )
        administrador.createNotificationChannel(canal)
    }

    fun enviarPrueba(context: Context): Boolean{
        crearCanal(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val permiso = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permiso != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }

        val administrador = NotificationManagerCompat.from(context)
        if (!administrador.areNotificationsEnabled()){
            return false
        }
        val administradorSistema = context.getSystemService(
            NotificationManager::class.java
        )
        if (
            administradorSistema.getNotificationChannel(CANAL_ID)
                ?.importance == NotificationManager.IMPORTANCE_NONE
        ){
            return false
        }
        val abrirApp = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val accion = PendingIntent.getActivity(
            context,
            0,
            abrirApp,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("CORPALIV")
            .setContentText("Este es un recordatorio de prueba")
            .setContentIntent(accion)
            .setAutoCancel(true)
            .build()

        return try {
            administrador.notify(NOTIFICACION_ID, notificacion)
            true
        } catch (error: SecurityException){
            false
        }
    }

    fun cancelarPrueba(context: Context){
        NotificationManagerCompat.from(context)
            .cancel(NOTIFICACION_ID)
    }
}