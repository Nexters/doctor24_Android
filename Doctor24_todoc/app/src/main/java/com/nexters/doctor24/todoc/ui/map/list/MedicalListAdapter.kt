package com.nexters.doctor24.todoc.ui.map.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.MarkerEdgeTreatment
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.data.marker.MarkerTypeEnum
import com.nexters.doctor24.todoc.databinding.ItemListHospitalBinding
import com.nexters.doctor24.todoc.databinding.ItemMedicalLastBinding
import com.nexters.doctor24.todoc.ui.map.marker.group.GroupMarkerListDialog.GroupListHospitalUiData

internal class MedicalListAdapter : ListAdapter<GroupListHospitalUiData, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<GroupListHospitalUiData>() {
    override fun areItemsTheSame(oldItem: GroupListHospitalUiData, newItem: GroupListHospitalUiData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroupListHospitalUiData, newItem: GroupListHospitalUiData): Boolean {
        return oldItem.id == newItem.id
    }
}) {

    val TYPE_ITEM = 0
    val TYPE_LAST = 1

    interface MedicalListListener {
        fun onClickListItem(index: Int)
        fun onClickFindLoad(index: Int)
    }

    var listener : MedicalListListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            TYPE_ITEM -> {
                val binding: ItemListHospitalBinding = DataBindingUtil.inflate(inflater, R.layout.item_list_hospital, parent, false)
                MedicalListViewHolder(binding)
            }
            TYPE_LAST -> {
                val binding: ItemMedicalLastBinding = DataBindingUtil.inflate(inflater, R.layout.item_medical_last, parent, false)
                LastItemViewHolder(binding)
            }
            else -> {
                val binding: ItemListHospitalBinding = DataBindingUtil.inflate(inflater, R.layout.item_list_hospital, parent, false)
                MedicalListViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is MedicalListViewHolder -> holder.bind(getItem(position))
            is LastItemViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == currentList.lastIndex) {
            TYPE_LAST
        } else TYPE_ITEM
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

    inner class LastItemViewHolder(private val binding: ItemMedicalLastBinding) : RecyclerView.ViewHolder(binding.root) {
        internal fun bind(item: GroupListHospitalUiData) {
            val medicalTypeTitle = if(item.isPharmacy) MarkerTypeEnum.PHARMACY.title else MarkerTypeEnum.HOSPITAL.title
            binding.type = String.format(binding.root.context.getString(R.string.medical_list_last), medicalTypeTitle)
            binding.executePendingBindings()

        }
    }
}