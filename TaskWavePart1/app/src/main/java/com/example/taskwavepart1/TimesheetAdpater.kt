package com.example.taskwavepart1

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView

class TimesheetAdpater : ListAdapter<Timesheet, TimesheetAdpater.TimesheetAdpater>(TimesheetViewHolder())
{
    private var onClickListener: OnClickListener? = null
    class TimesheetAdpater(view : View): RecyclerView.ViewHolder(view)
    {

    }
    override fun onCreateViewHolder(parent : ViewGroup,viewType:Int):TimesheetAdpater
    {
        val inflater = LayoutInflater.from(parent.context)
        return TimesheetAdpater(inflater.inflate(
            R.layout.timesheet_item,parent,false
        ))
    }

    override fun onBindViewHolder(holder: TimesheetAdpater, position: Int)
    {
        val timesheet = getItem(position)
        val imageUri = timesheet.image!!.toUri()
        holder.itemView.findViewById<TextView>(R.id.txtTsDate).text = timesheet.date
        holder.itemView.findViewById<TextView>(R.id.txtTsStartTime).text = timesheet.startTime
        holder.itemView.findViewById<TextView>(R.id.txtTsEndTime).text = timesheet.endTime
        holder.itemView.setOnClickListener{
            if (onClickListener != null){
                onClickListener!!.onClick(position, timesheet)
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Timesheet)
    }
}

class TimesheetViewHolder : DiffUtil.ItemCallback<Timesheet>()
{
    override fun areItemsTheSame(oldItem: Timesheet, newItem: Timesheet): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: Timesheet, newItem: Timesheet): Boolean {
        return areItemsTheSame(oldItem,newItem)
    }
}