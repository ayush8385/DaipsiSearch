package com.digitalhain.daipsisearch.Activities.notification
import android.util.Log

import android.content.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import org.json.JSONException
import android.text.TextUtils
import android.content.Intent
import com.digitalhain.daipsisearch.Activities.AnswerView
import com.digitalhain.daipsisearch.Activities.MessageShowActivity
import com.digitalhain.daipsisearch.Activities.utils.NotificationUtils

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "MessagingService"
    private var notificationUtils: NotificationUtils? = null

     override fun onNewToken(token: String) {
        super.onNewToken(token!!)
        Log.e("newToken", token)
        //Add your token in your sharepreferences
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply()
    }



    //Whenewer you need FCM token, just call this static method to get it.
    fun getToken(context: Context): String? {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fcm_token", "empty")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Data Payload: " + remoteMessage.data.toString())

            try {
                val json = JSONObject(remoteMessage.data.toString())
                handleDataMessage(json)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }

        }
    }

    private fun handleDataMessage(json: JSONObject) {
        Log.e(TAG, "push json: $json")

        try {
            val data = json.getJSONObject("data")

            var subject = data.getString("subject")
            val question = data.getString("question")
            val answer = data.getString("answer")


            Log.e(TAG, "title: " + subject)
            Log.e(TAG, "message: " + question)
            Log.e(TAG, "isBackground: " + answer)


            subject=subject[0].toUpperCase()+subject.substring(1)

            //Send notification data to MessageShowActivity class for showing
            val resultIntent = Intent(applicationContext, AnswerView::class.java)
            resultIntent.putExtra("course",subject)
            resultIntent.putExtra("Ques", question)
            resultIntent.putExtra("Ans", answer)

            // check for image attachment
            showNotificationMessage(applicationContext, subject, question, resultIntent)

        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }

    }

    /**
     * Showing notification with text only
     */
    fun showNotificationMessage(
        context: Context,
        title: String,
        message: String,
        intent: Intent
    ) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils?.showNotificationMessage(title, message, intent)
    }

    /**
     * Showing notification with text and image
     */
    private fun showNotificationMessageWithBigImage(
        context: Context,
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent,
        imageUrl: String
    ) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils?.showNotificationMessage(title, message,  intent)
    }
}