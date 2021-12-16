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
import android.content.ContentValues.TAG
import android.content.Context
import android.widget.Toast

import android.content.IntentFilter
import android.net.Uri

import android.text.TextUtils

import android.util.Log
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.digitalhain.daipsisearch.Activities.Room.QuestionEntity
import com.digitalhain.daipsisearch.Activities.Room.QuestionViewModel
import com.digitalhain.daipsisearch.Activities.notification.MyFirebaseMessagingService
import com.digitalhain.daipsisearch.Activities.utils.Preferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.hellohasan.android_firebase_notification.notification.Configuration

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



//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
////            val msg = getString(R.string.msg_token_fmt, token)
//            Log.d(TAG, token.toString())
//            Toast.makeText(baseContext, token.toString(), Toast.LENGTH_SHORT).show()
//        })
////
//        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//
//                // checking for type intent filter
//                if (intent.action == com.digitalhain.daipsisearch.Activities.Config.TOPI) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
//                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)
//                    displayFirebaseRegId()
//                } else if (intent.action == Config.PUSH_NOTIFICATION) {
//                    // new push notification is received
//                    val message = intent.getStringExtra("message")
//                    Toast.makeText(
//                        applicationContext,
//                        "Push notification: $message",
//                        Toast.LENGTH_LONG
//                    ).show()
//
//                }
//            }
//        }

  //      displayFirebaseRegId()


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

        courseArray.add(Courses("Gauri Chandrabhatla/Pallavi Singhla", "NEET","https://daipsi.com/courses/neet.php",R.drawable.neet))
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

    fun updateToken(token:String){
        val allQuestion = arrayListOf<QuestionEntity>()

        QuestionViewModel(application).allQuestions.observe(this, Observer { list->
            list?.let {
                allQuestion.clear()
                allQuestion.addAll(list)

                for(quest in allQuestion){

                    val url="https://daipsi.com/Android_App_Daipsi/updatetoken.php"

                    val queue= Volley.newRequestQueue(this)
                    val jsonObjectRequest=object : StringRequest(Method.POST,url, Response.Listener {
                        try{
                            if(it.equals("success")){

                                Toast.makeText(this,"Token Updated", Toast.LENGTH_LONG).show()
                                Log.e("repsonse...",it)
                            }
                        }
                        catch (e:Exception){
                            Toast.makeText(applicationContext,"Error Occurred",Toast.LENGTH_SHORT).show()
                        }
                    },Response.ErrorListener {
                        Toast.makeText(this, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
                    }){
                        override fun getParams(): MutableMap<String, String> {
                            val params=HashMap<String,String>()
                            params.put("question", quest.question.toString())
                            params.put("course", quest.course+"_queries")
                            params.put("token",token)
                            return params
                        }
                    }
                    queue.add(jsonObjectRequest)
                }
            }
        })




    }

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