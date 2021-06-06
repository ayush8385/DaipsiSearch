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

class SearchAdapter(val context: Context, var itemList:ArrayList<Subject>) : RecyclerView.Adapter<SearchAdapter.MainViewHolder>(){
    class MainViewHolder(view: View): RecyclerView.ViewHolder(view){
        val question: TextView =view.findViewById(R.id.question)
        val answer: TextView =view.findViewById(R.id.answer)
        val parent:LinearLayout = view.findViewById(R.id.parent)

    }

    fun filterList(filterlist:ArrayList<Subject>){
        itemList=filterlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_res_search,parent,false)

        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item=itemList[position]
        holder.question.text=item.ques
        holder.answer.text=item.ans

        holder.parent.setOnClickListener{
            val intent = Intent(context,AnswerView::class.java)
            intent.putExtra("Ques",item.ques)
            intent.putExtra("Ans",item.ans)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
}