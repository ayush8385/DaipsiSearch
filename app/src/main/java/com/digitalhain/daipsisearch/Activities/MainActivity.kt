package com.digitalhain.daipsisearch.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digitalhain.daipsisearch.BuildConfig
import com.digitalhain.daipsisearch.R


class MainActivity : AppCompatActivity() {
    lateinit var radioGroup: RadioGroup
    lateinit var search: Button
    lateinit var share: Button
    lateinit var help: Button
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
        share = findViewById(R.id.share)
        help = findViewById(R.id.help)


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
            val intent = Intent(this@MainActivity, WebViewActivity::class.java)
            startActivity(intent)

        }
        recyclerView = findViewById(R.id.recyclercard)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        courseArray.add(Subject("Jack", "Android"))
        courseArray.add(Subject("Abhi", "Web Development"))
        courseArray.add(Subject("Jay", "React"))
        courseArray.add(Subject("Ram", "Java"))
        courseArray.add(Subject("John", "Computer"))
        courseArray.add(Subject("Sam", "Php"))
        courseArray.add(Subject("Jam", "C++"))

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