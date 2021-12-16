package com.digitalhain.daipsisearch.Activities

//import pl.droidsonroids.gif.GifImageView

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayush.livesearch.ApiClient
import com.ayush.livesearch.ApiInterface
import com.digitalhain.daipsisearch.R
import pl.droidsonroids.gif.GifImageView
import retrofit2.Call
import retrofit2.Callback
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.digitalhain.daipsisearch.Activities.Room.QuestionEntity
import com.digitalhain.daipsisearch.Activities.Room.QuestionViewModel
import com.digitalhain.daipsisearch.Activities.utils.Preferences
import com.hellohasan.android_firebase_notification.notification.Configuration


class searchedItemActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var ll_center: LinearLayout
    lateinit var recyclerAdapter:SearchAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var textse:TextView
    lateinit var noDataText:TextView
    lateinit var url:String
    lateinit var wait:TextView
    lateinit var searchView: SearchView
    lateinit var gif: GifImageView
    lateinit var apiInterface:ApiInterface
    lateinit var subjectArray:ArrayList<Subject>
    lateinit var sharedPreferences:Preferences

    val handler = Handler()
    val filteredlist:ArrayList<com.digitalhain.daipsisearch.Activities.Subject> = ArrayList()
    var str=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_item)

        ll_center = findViewById(R.id.ll_center)
        textse=findViewById(R.id.text_ser)
        noDataText=findViewById(R.id.noDataText)
        searchView=findViewById(R.id.search_bar)
        gif=findViewById(R.id.gif)
        wait=findViewById(R.id.wait_text)

        val sub=intent.getStringExtra("subject")

        supportActionBar!!.title=sub

        sharedPreferences= Preferences.getInstance(applicationContext)!!


        recyclerView=findViewById(R.id.recyclermain)
        layoutManager=LinearLayoutManager(this@searchedItemActivity)
        recyclerView.layoutManager=layoutManager
        recyclerView.setHasFixedSize(true)


        if(sub=="Engineering"){
            str="engineering"
        }
        else if(sub=="Medical"){
            str="medical"
        }
        else if(sub=="Commerce"){
            str="commerce"
        }
        else{
            str="govtexams"
        }




        gif.visibility=View.GONE
        wait.visibility=View.GONE
        searchView.visibility=View.VISIBLE

        searchElement()

        fetchQuestion("","")

    }

    private fun searchElement() {

        recyclerAdapter= SearchAdapter(this@searchedItemActivity)
        recyclerView.adapter=recyclerAdapter

        searchView.queryHint="Search your question..."
        searchView.setIconifiedByDefault(false)
        val searchIcon:ImageView = searchView.findViewById(R.id.search_mag_icon);
        searchIcon.visibility= View.GONE
        searchIcon.setImageDrawable(null)
        val closeIcon:ImageView = searchView.findViewById(R.id.search_close_btn);
        closeIcon.setColorFilter(Color.BLACK)
        val theTextArea = searchView.findViewById<View>(R.id.search_src_text) as SearchAutoComplete
        theTextArea.setTextColor(Color.BLACK)
        theTextArea.setHintTextColor(Color.DKGRAY)//or any color that you want

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                if(query.length>1){
                    fetchQuest(query,str)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    if(newText.length>1){
                        fetchQuestion(newText.replace("\\s".toRegex(), "").replace("\\?".toRegex(), ""),str)
                    }
                    else{
                        filteredlist.clear()
                        recyclerAdapter.filterList(filteredlist)
                    }
                }, 500)
                return false

            }

        })

    }

    fun fetchQuest(key:String, course:String){

        apiInterface= ApiClient().getApiClient().create(ApiInterface::class.java)
        val call: Call<List<Subject>> =apiInterface.getQuestions(key.replace("\\s".toRegex(), "").replace("\\?".toRegex(), ""),course)

        call.enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: retrofit2.Response<List<Subject>>) {
                textse.visibility=View.VISIBLE
                subjectArray= (response.body() as ArrayList<Subject>?)!!

                recyclerAdapter.filterList(subjectArray as ArrayList<Subject>)

                var m=0
                for(quest in subjectArray){
                    if(quest.ques!!.toLowerCase().contains(key.toLowerCase())){
                        m=1
                        break
                    }
                }

                if(m==0){
                    askQuestion(str,key)
                }
            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                gif.visibility=View.GONE
                wait.visibility=View.GONE
                searchView.visibility=View.VISIBLE

                textse.visibility=View.VISIBLE
                Toast.makeText(this@searchedItemActivity,"Error on :"+t.toString(),Toast.LENGTH_LONG).show()
            }

        })
    }

    fun askQuestion(str: String, newText: String) {

        url="https://daipsi.com/Android_App_Daipsi/"+str+".php/"
        val queue= Volley.newRequestQueue(this)

            val jsonObjectRequest=object : StringRequest(Method.POST,url,Response.Listener {
                try{
                    if(it.equals("success")){

//                        sharedPreferences.addQuestion(applicationContext,Subject(str,newText))

                        QuestionViewModel(application).inserQuestion(QuestionEntity(str,newText))


                        Toast.makeText(this,"Your Answer will be available in next 60 mins", Toast.LENGTH_LONG).show()
                        Log.d("repsonse...",it)
                    }
                    else{
                        Toast.makeText(this,"Error While Saving Question", Toast.LENGTH_LONG).show()
                    }
                }
                catch (e:Exception){
                    Toast.makeText(applicationContext,"Error Occurred",Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
            }){
                override fun getParams(): MutableMap<String, String> {
                    val params=HashMap<String,String>()
                    params.put("question",newText)
                    params.put("course",str)
                    params.put("asked_by", getSharedPreferences(Configuration.SHARED_PREF,
                        MODE_PRIVATE).getString("fcm_token","")!!
                    )
                    return params
                }
            }
            queue.add(jsonObjectRequest)
    }

    fun fetchQuestion(key:String, course:String){

        apiInterface= ApiClient().getApiClient().create(ApiInterface::class.java)
        val call: Call<List<Subject>> =apiInterface.getQuestions(key,course)

        call.enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: retrofit2.Response<List<Subject>>) {
                textse.visibility=View.VISIBLE
                subjectArray= (response.body() as ArrayList<Subject>?)!!


                recyclerAdapter.filterList(subjectArray)


            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                gif.visibility=View.GONE
                wait.visibility=View.GONE
                searchView.visibility=View.VISIBLE

                textse.visibility=View.VISIBLE
                Toast.makeText(this@searchedItemActivity,"Error on :"+t.toString(),Toast.LENGTH_LONG).show()
            }

        })

    }

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
        finishAffinity()
        super.onBackPressed()
    }


}