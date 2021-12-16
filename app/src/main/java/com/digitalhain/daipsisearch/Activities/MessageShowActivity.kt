package com.digitalhain.daipsisearch.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.app.NavUtils
import com.digitalhain.daipsisearch.R



class MessageShowActivity : AppCompatActivity() {

    lateinit var header:TextView
    lateinit var timeStamp:TextView
    lateinit var article:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_show)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        header=findViewById(R.id.header)
        timeStamp=findViewById(R.id.timeStamp)
        article=findViewById(R.id.article)
        //receive data from MyFirebaseMessagingService class
        val title = intent.getStringExtra("title")
        val timeStampString = intent.getStringExtra("timestamp")
        val articleString = intent.getStringExtra("article_data")
        val imageUrl = intent.getStringExtra("image")

        //Set data on UI
//        Picasso.get()
//            .load(imageUrl)
//            .placeholder(R.drawable.image_placeholder)
//            .error(R.drawable.image_placeholder)
//            .into(featureGraphics)

        header.text = title
        timeStamp.text = timeStampString
        article.text = articleString
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home)
            onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = NavUtils.getParentActivityIntent(this)
        startActivity(intent)
    }
}
