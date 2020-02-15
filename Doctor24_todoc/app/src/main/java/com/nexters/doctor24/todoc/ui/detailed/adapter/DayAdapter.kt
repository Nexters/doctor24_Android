package com.nexters.doctor24.todoc.ui.detailed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.databinding.RvItemOpenTimeBlackBinding
import com.nexters.doctor24.todoc.databinding.RvItemOpenTimeBlueBinding
import com.nexters.doctor24.todoc.databinding.RvItemOpenTimeRedBinding

class DayAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun clearData() {
        data.clear()
    }

    fun addItem(DayList: List<DayData>?) {
        if (DayList == null) return

        with(data) {
            addAll(DayList)
        }
        notifyDataSetChanged()
    }

    class BlackDayViewHolder(val binding: RvItemOpenTimeBlackBinding) :
        RecyclerView.ViewHolder(binding.root)

    class BlueDayViewHolder(val binding: RvItemOpenTimeBlueBinding) :
        RecyclerView.ViewHolder(binding.root)

    class RedDayViewHolder(val binding: RvItemOpenTimeRedBinding) :
        RecyclerView.ViewHolder(binding.root)

    var data = arrayListOf<DayData>()

    override fun getItemViewType(position: Int): Int {
        val detailedInfoData = data[position]

        return if (detailedInfoData.weekday == "SUNDAY" || detailedInfoData.weekday == "HOLIDAY") {
            2
        } else if (detailedInfoData.weekday == "SATURDAY") {
            1
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        lateinit var viewHolder: RecyclerView.ViewHolder

        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_item_open_time_black, parent, false)

                BlackDayViewHolder(RvItemOpenTimeBlackBinding.bind(view))
            }
            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_item_open_time_blue, parent, false)

                BlueDayViewHolder(RvItemOpenTimeBlueBinding.bind(view))
            }
            2 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_item_open_time_red, parent, false)

                RedDayViewHolder(RvItemOpenTimeRedBinding.bind(view))
            }
            else -> {
                viewHolder
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is BlackDayViewHolder -> {
                holder.binding.dayData = data[position]
            }
            is BlueDayViewHolder -> {
                holder.binding.dayData = data[position]
            }
            is RedDayViewHolder -> {
                holder.binding.dayData = data[position]
            }
        }
    }
}