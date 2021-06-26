package com.digitalhain.daipsisearch.Activities

//import pl.droidsonroids.gif.GifImageView

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.appcompat.widget.SearchView.VIEW_LOG_TAG
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.digitalhain.daipsisearch.R
import org.json.JSONArray
import org.json.JSONException
import pl.droidsonroids.gif.GifImageView


class searchedItemActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter:SearchAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var textse:TextView
    lateinit var noDataText:TextView
    lateinit var url:String
    lateinit var searchView:SearchView
    lateinit var gif: GifImageView
    var subjectArray = arrayListOf<com.digitalhain.daipsisearch.Activities.Subject>()
    val filteredlist:ArrayList<com.digitalhain.daipsisearch.Activities.Subject> = ArrayList()
    var str=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_item)

        textse=findViewById(R.id.text_ser)
        noDataText=findViewById(R.id.noDataText)
        searchView=findViewById(R.id.search_bar)
        gif=findViewById(R.id.gif)

        val sub=intent.getStringExtra("subject")

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

        url="https://daipsi.com/api/"+str+".php/"
        val queue= Volley.newRequestQueue(this)

        recyclerView=findViewById(R.id.recyclermain)
        layoutManager= LinearLayoutManager(this)

            if(ConnectionManager().checkconnectivity(this)){
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        try {
                            //converting the string to json array object
                            val array = JSONArray(response)


                            //traversing through all the object
                            for (i in 0 until array.length()) {

                                //getting product object from json array
                                val product = array.getJSONObject(i)

                                val itemjsonRequest=com.digitalhain.daipsisearch.Activities.Subject(
                                    product.getString("Question"),
                                    product.getString("Answer")
                                )
                                subjectArray.add(itemjsonRequest)

                                //adding the product to product list
                            }


                            recyclerAdapter= SearchAdapter(this,filteredlist)
                            recyclerView.layoutManager=layoutManager
                            recyclerView.adapter=recyclerAdapter
                            textse.visibility=View.VISIBLE
                            gif.visibility = View.GONE
                            noDataText.visibility = View.GONE

                            //creating adapter object and setting it to recyclerview


                        } catch (e: JSONException) {
                            Toast.makeText(applicationContext,"Error found", Toast.LENGTH_LONG).show()
                        }
                    }
                ) { }
                queue.add(stringRequest)
            }
           else{
                Toast.makeText(applicationContext,"Not connected to internet", Toast.LENGTH_LONG).show()
            }

        searchElement()

    }

    private fun searchElement() {

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

        val manager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                filtering(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterr(newText!!)
                return true
            }

        })
    }

    private fun filtering(text: String) {
        val filtered:ArrayList<com.digitalhain.daipsisearch.Activities.Subject> = ArrayList()
        for(item in subjectArray){
            if(item.ques!!.toLowerCase().contains(text!!.toLowerCase()) && text!=""){
                filtered.add(item)
            }
        }
        if(filtered.isEmpty()){

            val queue=Volley.newRequestQueue(this)

            val jsonObjectRequest=object : StringRequest(Method.POST,url,Response.Listener {
                try{
                    if(it.equals("success")){
                        Toast.makeText(this,"Question Saved to Database", Toast.LENGTH_LONG).show()
                        Log.d("repsonse...",it)
                    }
                    else{
                        Toast.makeText(this,"Error While placing Order", Toast.LENGTH_LONG).show()
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
                    params.put("question",text)
                    params.put("course",str)
                    return params
                }
            }
            queue.add(jsonObjectRequest)
        }
    }


    fun filterr(text:String){
        val filtered:ArrayList<com.digitalhain.daipsisearch.Activities.Subject> = ArrayList()
        for(item in subjectArray){
            if(item.ques!!.toLowerCase().contains(text.toLowerCase()) && text!=""){
                filtered.add(item)
            }
        }
        if (filtered.isEmpty()){
            textse.visibility=View.GONE
           // gif.visibility = View.VISIBLE
            noDataText.visibility = View.VISIBLE
         //   Toast.makeText(applicationContext,"No Data found", Toast.LENGTH_SHORT).show()
            recyclerAdapter.filterList(filtered)
        }
        else{
            recyclerAdapter.filterList(filtered)

        }


    }

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
        finishAffinity()
        super.onBackPressed()
    }


}