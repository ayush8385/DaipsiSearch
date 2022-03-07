package com.digitalhain.daipsisearch.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digitalhain.daipsisearch.BuildConfig
import com.digitalhain.daipsisearch.R
import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast

import android.net.Uri

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.digitalhain.daipsisearch.Activities.Room.QuestionEntity
import com.digitalhain.daipsisearch.Activities.Room.QuestionViewModel
import com.digitalhain.daipsisearch.Activities.utils.Preferences
import com.google.firebase.messaging.FirebaseMessaging
import com.hellohasan.android_firebase_notification.notification.Configuration
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager

import android.app.PendingIntent
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*
import kotlin.collections.HashMap
import android.content.pm.PackageManager

import android.content.pm.PackageInfo
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import java.lang.reflect.Method
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {
    lateinit var radioGroup: RadioGroup
    lateinit var search: Button
    lateinit var share: ImageButton
    lateinit var help: ImageButton
    lateinit var blog: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: MainAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    var courseArray = arrayListOf<Courses>()
    lateinit var sharedPrefManager:Preferences
    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        radioGroup = findViewById(R.id.groupradio)
        search = findViewById(R.id.search)
        blog = findViewById(R.id.blog)
        share = findViewById(R.id.share)
        help = findViewById(R.id.help)

        sharedPrefManager = Preferences.getInstance(applicationContext)!!

        try {
            val info = packageManager.getPackageInfo(
                "com.digitalhain.daipsisearch",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:",encodeToString(md.digest(), DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createNotificationChannel()
        }

        val intent = Intent(applicationContext, AlarmReceiver::class.java)


        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = System.currentTimeMillis()
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            (220 * 60 * 1000).toLong(),
            pendingIntent
        )

     //   if(getSharedPreferences(Configuration.SHARED_PREF, MODE_PRIVATE).getString("fcm_token","")==""){
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                }

                val token = it.result
                updateToken(token.toString())
                Log.e("token..........",token.toString())
                getSharedPreferences(Configuration.SHARED_PREF, MODE_PRIVATE).edit().putString("fcm_token",token).apply()
            }
     //   }



        help.setOnClickListener {
            val intent = Intent(this@MainActivity, HelpActivity::class.java)
            startActivity(intent)

        }

        share.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Daipsi Search")
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    
                    
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }

        }

        blog.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://daipsi.com/blogs")
            startActivity(i)

        }
        recyclerView = findViewById(R.id.recyclercard)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        courseArray.add(Courses("Gauri Chandrabhatla/Pallavi Bansal", "NEET","https://daipsi.com/courses/neet.php",R.drawable.neet))
        courseArray.add(Courses("Prof. Yogendra Kumar", "JEE","https://daipsi.com/courses/jee.php",R.drawable.jee))
        courseArray.add(Courses("Prof. Prem Gurjar","UPSC","https://daipsi.com/courses/upsc.php",R.drawable.upsc))
        courseArray.add(Courses("Charted Sandeep Sharma", "CA","https://daipsi.com/courses/ca.php",R.drawable.ca))
        courseArray.add(Courses("CS Yash Raj", "CS","https://daipsi.com/courses/cs.php",R.drawable.cs))

        search.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(applicationContext, "Select a Topic", Toast.LENGTH_SHORT).show()
            } else {
                val radbtn: RadioButton = findViewById(selectedId)
                val intent = Intent(this@MainActivity, searchedItemActivity::class.java)
                intent.putExtra("subject", radbtn.text)
                startActivity(intent)
            }
        }

        recyclerAdapter = MainAdapter(this, courseArray)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter

       // checkUpdate()

    }


    private fun createNotificationChannel()
    {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel:NotificationChannel = NotificationChannel(R.string.default_notification_channel_id.toString(), name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun updateToken(token:String){
        val allQuestion = arrayListOf<QuestionEntity>()

        QuestionViewModel(application).allQuestions.observe(this,
            androidx.lifecycle.Observer { list ->
                list?.let {
                    allQuestion.clear()
                    allQuestion.addAll(list)

                    for (quest in allQuestion) {

                        val url = "https://daipsi.com/Android_App_Daipsi/updatetoken.php"

                        val queue = Volley.newRequestQueue(this)
                        val jsonObjectRequest =
                            object : StringRequest(Method.POST, url, Response.Listener {
                                try {
                                    if (it.equals("success")) {

                                        Toast.makeText(this, "Token Updated", Toast.LENGTH_LONG)
                                            .show()
                                        Log.e("repsonse...", it)
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Error Occurred",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }, Response.ErrorListener {
                                Toast.makeText(this, "Volley error occurred!!!", Toast.LENGTH_SHORT)
                                    .show()
                            }) {
                                override fun getParams(): MutableMap<String, String> {
                                    val params = HashMap<String, String>()
                                    params.put("question", quest.question.toString())
                                    params.put("course", quest.course + "_queries")
                                    params.put("token", token)
                                    return params
                                }
                            }
                        queue.add(jsonObjectRequest)
                    }
                }
            })




    }

//    override fun onDestroy() {
//        super.onDestroy()
//        val notificationIntent = Intent(this, RandomNotification::class.java)
//        val contentIntent = PendingIntent.getService(
//            this, 0, notificationIntent,
//            PendingIntent.FLAG_CANCEL_CURRENT
//        )
//        val am = getSystemService(ALARM_SERVICE) as AlarmManager
//        am.cancel(contentIntent)
//        am.setRepeating(
//            AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            System.currentTimeMillis(), 10000, contentIntent
//        )
//    }

//
//    private fun checkUpdate() {
//        private val appUpdateManager: AppUpdateManager? = AppUpdateManagerFactory.create(this)
//        // Returns an intent object that you use to check for an update.
//        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
//        // Checks that the platform will allow the specified type of update.
//        Log.d(TAG, "Checking for updates")
//        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
//                // Request the update.
//                Log.d(TAG, "Update available")
//            } else {
//                Log.d(TAG, "No Update available")
//            }
//        }
//    }





}