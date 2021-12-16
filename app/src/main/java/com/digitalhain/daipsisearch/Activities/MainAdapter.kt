package com.digitalhain.daipsisearch.Activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.digitalhain.daipsisearch.R
import androidx.core.content.ContextCompat.startActivity




class MainAdapter(val context: Context, var itemList:ArrayList<Courses>) : RecyclerView.Adapter<MainAdapter.MViewHolder>(){
    class MViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView =view.findViewById(R.id.name)
        val teacher: TextView =view.findViewById(R.id.teacher)
        val parent:LinearLayout=view.findViewById(R.id.parent)
        val image:ImageView=view.findViewById(R.id.course_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MainAdapter.MViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_course,parent,false)

        return MViewHolder(view)
    }

    override fun onBindViewHolder(holder:MainAdapter.MViewHolder, position: Int) {
        val item=itemList[position]
        holder.name.text=item.course
        holder.teacher.text=item.teacher

        holder.image.setImageResource(item.image)

        holder.parent.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(item.link)
            context.startActivity(i)
        }


    }
    override fun getItemCount(): Int {
        return itemList.size
    }
}