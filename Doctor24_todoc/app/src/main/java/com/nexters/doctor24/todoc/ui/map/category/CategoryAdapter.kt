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

internal data class CategoryItem(
    val id : Int,
    @DrawableRes val image : Int,
    @StringRes val name : Int,
    var selected : Boolean = false
)

internal class CategoryAdapter(private val context: Context) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    val categoryItemList = arrayListOf<CategoryItem>(
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

    val listener : CategoryListener? = null

    interface CategoryListener {
        fun onClickCategory(position : Int)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(categoryItemList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categoryItemList.size

    inner class CategoryViewHolder(private val binding : ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item : CategoryItem) {
            binding.item = item
            binding.checkboxCategory.apply {
                isChecked = item.selected
                setOnCheckedChangeListener { compoundButton, b ->
                    categoryItemList[adapterPosition].selected = b
                }
            }
        }
    }
}