package com.digitalhain.daipsisearch.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digitalhain.daipsisearch.R


class MainActivity : AppCompatActivity() {
    lateinit var radioGroup: RadioGroup
    lateinit var search: Button
    lateinit var blog: TextView

    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: MainAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    var courseArray = arrayListOf<Subject>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        radioGroup = findViewById(R.id.groupradio)
        search = findViewById(R.id.search)
        blog = findViewById(R.id.blog)

        blog.setOnClickListener {
            val intent = Intent(this@MainActivity, WebViewActivity::class.java)
            startActivity(intent)

        }
        recyclerView = findViewById(R.id.recyclercard)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        courseArray.add(Subject("Jack", "NEET"))
        courseArray.add(Subject("Johny", "JEE"))
        courseArray.add(Subject("July", "CA"))
        courseArray.add(Subject("justine", "CS"))
        courseArray.add(Subject("Jack", "NEET"))
        courseArray.add(Subject("Johny", "JEE"))
        courseArray.add(Subject("July", "CA"))
        courseArray.add(Subject("justine", "CS"))
        courseArray.add(Subject("Jack", "NEET"))
        courseArray.add(Subject("Johny", "JEE"))
        courseArray.add(Subject("July", "CA"))
        courseArray.add(Subject("justine", "CS"))

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


    }
}