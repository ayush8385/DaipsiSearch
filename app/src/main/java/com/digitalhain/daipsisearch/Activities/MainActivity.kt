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
import com.digitalhain.daipsisearch.Activities.searchedItemActivity

class MainActivity : AppCompatActivity() {
    lateinit var radioGroup: RadioGroup
    lateinit var search:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        radioGroup = findViewById(R.id.groupradio)
        search = findViewById(R.id.search)

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

//        radioGroup!!.clearCheck()
//        radioGroup!!.setOnCheckedChangeListener { group, checkedId ->
//            val radioButton = group.findViewById<View>(checkedId) as RadioButton
//            when (checkedId) {
//                R.id.radia_eng -> Log.d("RADIO_BTN", "radia_eng Clicked")
//                R.id.radia_med -> Log.d("RADIO_BTN", "radia_med Clicked")
//                R.id.radia_comm -> Log.d("RADIO_BTN", "radia_comm Clicked")
//                R.id.radia_gvt_exam -> Log.d("RADIO_BTN", "radia_gvt_exam Clicked")
//            }
//        }


    }

//    fun gotoNext(view: View?) {
//        val intent = Intent(this@MainActivity, searchedItemActivity::class.java)
//        startActivity(intent)
//    }
}