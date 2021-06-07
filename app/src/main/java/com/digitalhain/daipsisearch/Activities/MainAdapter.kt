package com.digitalhain.daipsisearch.Activities

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.digitalhain.daipsisearch.R

class MainAdapter(val context: Context, var itemList:ArrayList<Subject>) : RecyclerView.Adapter<MainAdapter.MViewHolder>(){
    class MViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView =view.findViewById(R.id.name)
        val teacher: TextView =view.findViewById(R.id.teacher)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MainAdapter.MViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_course,parent,false)

        return MViewHolder(view)
    }

    override fun onBindViewHolder(holder:MainAdapter.MViewHolder, position: Int) {
        val item=itemList[position]
        holder.name.text=item.ques
        holder.teacher.text=item.ans
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
}