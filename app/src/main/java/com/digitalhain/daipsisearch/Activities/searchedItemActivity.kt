package com.digitalhain.daipsisearch.Activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.digitalhain.daipsisearch.R
import org.json.JSONArray
import org.json.JSONException
import javax.security.auth.Subject

class searchedItemActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter:SearchAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    var subjectArray = arrayListOf<com.digitalhain.daipsisearch.Activities.Subject>()
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_item)

        progressBar=findViewById(R.id.progressbar)
        progressBar.visibility= View.VISIBLE

        val url="https://daipsi.com/api/find-qna.php"
        val queue= Volley.newRequestQueue(this)

        recyclerView=findViewById(R.id.recyclermain)
        layoutManager= LinearLayoutManager(this)

            if(ConnectionManager().checkconnectivity(this)){
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        Log.d("HEloooo,...","Hellll")
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

                            recyclerAdapter= SearchAdapter(this,subjectArray)

                            recyclerView.layoutManager=layoutManager
                            recyclerView.adapter=recyclerAdapter
                            progressBar.visibility=View.GONE
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


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.search_menu, menu)

        val search: MenuItem = menu!!.findItem(R.id.search)

        val searchView: SearchView = search.actionView as SearchView
        searchView.queryHint = "Search People"


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterr(newText!!)
                return true
            }

        })



        return super.onCreateOptionsMenu(menu)
    }


    fun filterr(text:String){
        val filteredlist:ArrayList<com.digitalhain.daipsisearch.Activities.Subject> = ArrayList()

        for(item in subjectArray){
            if(item.ques!!.toLowerCase().contains(text.toLowerCase())){
                //recyclerView.scrollToPosition(mChatlist.indexOf(item))
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()){
            Toast.makeText(applicationContext,"No Data found", Toast.LENGTH_SHORT).show()
            recyclerAdapter.filterList(filteredlist)
        }
        else{
            recyclerAdapter.filterList(filteredlist)
        }


    }

}