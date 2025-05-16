package com.example.projecttdm.firebase
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.projecttdm.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class FirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "🎯 Nouveau token FCM : $token")

        // Stockez le token localement
        val sharedPrefs = getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("fcm_token", token).apply()

        // Si l'utilisateur est connecté, envoyez le token au backend
        val userId = getUserIdFromLocalStorage()
        if (userId != null) {
            sendTokenToBackend(token, userId)
        } else {
            Log.d("FCM", "⚠️ Token mis à jour mais utilisateur non connecté, le token sera envoyé à la prochaine connexion")
        }
    }

    private fun getUserIdFromLocalStorage(): String? {
        val sharedPrefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPrefs.getString("user_id", null)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.notification?.title ?: "Notification"
        val body = remoteMessage.notification?.body ?: "Contenu vide"

        Log.d("FCM", "📥 Notification reçue : $title - $body")
        showNotification(title, body)
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "doctor_notifications"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Créer le canal (obligatoire pour Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notifications de l'application",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Construire la notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.default_profil)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Afficher
        notificationManager.notify(0, notification)
    }


    companion object {
        fun sendTokenToBackend(token: String, userId: String) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://172.20.10.3:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(FCMApi::class.java)
            val body = mapOf("userId" to userId, "token" to token)

            api.registerToken(body).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("FCM", "✅ Token envoyé après login")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("FCM", "❌ Échec d'envoi du token après login", t)
                }
            })
        }
    }

}

interface FCMApi {
    @POST("fcm/register-token")
    fun registerToken(@Body body: Map<String, String>): Call<ResponseBody>
}