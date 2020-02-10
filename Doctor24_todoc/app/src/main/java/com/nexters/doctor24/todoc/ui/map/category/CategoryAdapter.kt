package com.nexters.doctor24.todoc.ui.map.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.databinding.ItemCategoryBinding
import timber.log.Timber

internal data class CategoryItem(
    val id : Int,
    @DrawableRes val image : Int,
    @StringRes val name : Int,
    var selected : Boolean = false
)

internal val categoryItemList = arrayListOf<CategoryItem>(
    CategoryItem(0, R.drawable.selector_category_item_all, R.string.category_item_all, true),
    CategoryItem(1, R.drawable.selector_category_item_baby, R.string.category_item_baby),
    CategoryItem(2, R.drawable.selector_category_item_internal, R.string.category_item_internal),
    CategoryItem(3, R.drawable.selector_category_item_ent, R.string.category_item_ent),
    CategoryItem(4, R.drawable.selector_category_item_skin, R.string.category_item_skin),
    CategoryItem(5, R.drawable.selector_category_item_orthopedy, R.string.category_item_orthopedics),
    CategoryItem(6, R.drawable.selector_category_item_eyes, R.string.category_item_eyes),
    CategoryItem(7, R.drawable.selector_category_item_dental, R.string.category_item_dental),
    CategoryItem(8, R.drawable.selector_category_item_oriental, R.string.category_item_oriental),
    CategoryItem(9, R.drawable.selector_category_item_maternity, R.string.category_item_maternity),
    CategoryItem(10, R.drawable.selector_category_item_urology, R.string.category_item_urology),
    CategoryItem(11, R.drawable.selector_category_item_psychiatry, R.string.category_item_psychiatry),
    CategoryItem(12, R.drawable.selector_category_item_plastic, R.string.category_item_plastic),
    CategoryItem(13, R.drawable.selector_category_item_family, R.string.category_item_family),
    CategoryItem(14, R.drawable.selector_category_item_surgery, R.string.category_item_surgery),
    CategoryItem(15, R.drawable.selector_category_item_neurosurgery, R.string.category_item_neurosurgery),
    CategoryItem(16, R.drawable.selector_category_item_anesthesia, R.string.category_item_anesthesia),
    CategoryItem(17, R.drawable.selector_category_item_neurology, R.string.category_item_neurology)
)

internal class CategoryAdapter : ListAdapter<CategoryItem, CategoryAdapter.CategoryViewHolder>(
    object : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {

    var listener : CategoryListener? = null
    val childViewList: MutableList<CheckBox> = mutableListOf()

    interface CategoryListener {
        fun onClickCategory(position : Int)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    inner class CategoryViewHolder(private val binding : ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item : CategoryItem) {
            binding.item = item
            if(item.id == 0) binding.checkboxCategory.isChecked = true
            binding.checkboxCategory.apply {
                setOnCheckedChangeListener { compoundButton, b ->
                    if(b) {
                        childViewList.forEachIndexed { index, checkBox ->
                            if (checkBox == this) {
                                Timber.d("CategoryViewHolder : ${context.resources.getString(item.name)} 이것은 선택되었다.")
                                listener?.onClickCategory(item.id)
                            } else {
                                checkBox.isChecked = false
                            }
                        }
                    } else {
                        if(childViewList.none { it.isChecked }) childViewList[0].isChecked = true
                    }
                }
                childViewList.add(this)
            }
            binding.executePendingBindings()
        }
    }
}