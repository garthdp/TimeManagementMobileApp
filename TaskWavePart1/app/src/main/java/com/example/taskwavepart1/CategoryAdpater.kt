package com.example.taskwavepart1

import android.content.Intent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter : ListAdapter<Category, CategoryAdapter.CategoryAdapter>(CategoryViewHolder())
{
    private var onClickListener: OnClickListener? = null
    class CategoryAdapter(view : View): RecyclerView.ViewHolder(view)
    {

    }
    override fun onCreateViewHolder(parent : ViewGroup,viewType:Int):CategoryAdapter
    {
        val inflater = LayoutInflater.from(parent.context)
        return CategoryAdapter(inflater.inflate(
            R.layout.category_item,parent,false
        ))
    }

    override fun onBindViewHolder(holder: CategoryAdapter, position: Int)
    {
        val category = getItem(position)
        holder.itemView.findViewById<TextView>(R.id.txtCateItemName).text = category.name
        holder.itemView.findViewById<TextView>(R.id.txtCateItemMinHrs).text = "Min: " + category.minHours.toString() + "hrs"
        holder.itemView.findViewById<TextView>(R.id.txtCateItemMaxHrs).text = "Min: " + category.maxHours.toString() + "hrs"
        holder.itemView.setOnClickListener{
            if (onClickListener != null){
                onClickListener!!.onClick(position, category)
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Category)
    }
}

class CategoryViewHolder : DiffUtil.ItemCallback<Category>()
{
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return areItemsTheSame(oldItem,newItem)
    }
}