package com.nexters.doctor24.todoc.ui.map.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.databinding.ItemListHospitalBinding
import com.nexters.doctor24.todoc.ui.map.marker.group.GroupMarkerListDialog.GroupListHospitalUiData

internal class MedicalListAdapter() : ListAdapter<GroupListHospitalUiData, MedicalListAdapter.MedicalListViewHolder>(
    object : DiffUtil.ItemCallback<GroupListHospitalUiData>() {
    override fun areItemsTheSame(oldItem: GroupListHospitalUiData, newItem: GroupListHospitalUiData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroupListHospitalUiData, newItem: GroupListHospitalUiData): Boolean {
        return oldItem.id == newItem.id
    }
}) {

    interface MedicalListListener {
        fun onClickListItem(index: Int)
        fun onClickFindLoad(index: Int)
    }

    var listener : MedicalListListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalListViewHolder {
        val binding: ItemListHospitalBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_hospital, parent, false)
        return MedicalListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicalListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MedicalListViewHolder(private val binding : ItemListHospitalBinding) : RecyclerView.ViewHolder(binding.root) {

        internal fun bind(item: GroupListHospitalUiData) {
            binding.item = item
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                listener?.onClickListItem(adapterPosition)
            }
            binding.ivFindLoad.setOnClickListener {
                listener?.onClickFindLoad(adapterPosition)
            }
        }
    }

}