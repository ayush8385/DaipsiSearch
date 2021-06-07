package com.digitalhain.daipsisearch.Activities

import androidx.appcompat.app.AppCompatActivity
import android.widget.RadioGroup
import android.os.Bundle
import com.digitalhain.daipsisearch.R
import android.widget.RadioButton
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digitalhain.daipsisearch.Activities.searchedItemActivity

class MainActivity : AppCompatActivity() {
    lateinit var radioGroup: RadioGroup
    lateinit var search:Button
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter:MainAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    var courseArray = arrayListOf<Subject>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        radioGroup = findViewById(R.id.groupradio)
        search = findViewById(R.id.search)

        recyclerView=findViewById(R.id.recyclercard)
        layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        courseArray.add(Subject("Jack","Android"))
        courseArray.add(Subject("Abhi","Web Development"))
        courseArray.add(Subject("Jay","React"))
        courseArray.add(Subject("Ram","Java"))
        courseArray.add(Subject("Name","Computer"))
        courseArray.add(Subject("Sam","Php"))
        courseArray.add(Subject("Jam","C++"))

        search.setOnClickListener{
            val selectedId = radioGroup.checkedRadioButtonId
            if(selectedId == -1){
                Toast.makeText(applicationContext,"Select a Topic",Toast.LENGTH_SHORT).show()
            }else{
                val radbtn:RadioButton = findViewById(selectedId)
                val intent = Intent(this@MainActivity,searchedItemActivity::class.java)
                intent.putExtra("subject",radbtn.text)
                startActivity(intent)
            }
        }

        recyclerAdapter= MainAdapter(this,courseArray)
        recyclerView.layoutManager=layoutManager
        recyclerView.adapter=recyclerAdapter



    }
}