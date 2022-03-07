package com.digitalhain.daipsisearch.Activities

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.digitalhain.daipsisearch.R
import android.app.NotificationChannel
import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.digitalhain.daipsisearch.Activities.notification.MyFirebaseMessagingService
import org.json.JSONObject
import java.lang.Exception


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val url ="https://daipsi.com/Android_App_Daipsi/randomques.php/"
        val queue = Volley.newRequestQueue(context)


        val channelId = context.getString(R.string.default_notification_channel_id)


        val stringRequest: JsonObjectRequest = JsonObjectRequest(Request.Method.POST,url,null,Response.Listener{
            try{

                val subject:String = it["subject"] as String
                val question:String =it["ques"] as String
                val answer:String=it["ans"] as String

                val resultIntent = Intent(context, AnswerView::class.java)
                resultIntent.putExtra("course",subject)
                resultIntent.putExtra("Ques", question)
                resultIntent.putExtra("Ans", answer)

                // check for image attachment
                MyFirebaseMessagingService().showNotificationMessage(context, subject, question, resultIntent)

//                resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                val resultPendingIntent = PendingIntent.getActivity(
//                    context,
//                    0,
//                    resultIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT
//                )
//
//                val inboxStyle = NotificationCompat.InboxStyle()
//
//                inboxStyle.addLine(question)
//
//                val mBuilder = NotificationCompat.Builder(
//                    context,
//                    channelId
//                )
//
//                val alarmSound = Uri.parse(
//                    ContentResolver.SCHEME_ANDROID_RESOURCE
//                            + "://" + context.packageName + "/raw/notification"
//                )
//
//                val notification: Notification
//                notification = mBuilder.setSmallIcon(R.drawable.app_logo).setTicker(subject).setWhen(System.currentTimeMillis())
//                    .setAutoCancel(true)
//                    .setContentTitle(subject)
//                    .setContentIntent(resultPendingIntent)
//                    .setSound(alarmSound)
//                    .setStyle(inboxStyle)
//                    .setSmallIcon(R.drawable.app_logo)
//                    .setColor(R.color.purple_500)
//                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.app_logo))
//                    .setContentText(question)
//                    .build()
//
//                val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                manager.notify(1, notification)
            }catch (e:Exception){
                Toast.makeText(context,"Error in Random Notification",Toast.LENGTH_SHORT).show()
            }
        },Response.ErrorListener{

        })

        queue.add(stringRequest)


    }


}