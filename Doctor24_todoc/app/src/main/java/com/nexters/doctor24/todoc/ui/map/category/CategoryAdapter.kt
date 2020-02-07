package com.nexters.doctor24.todoc.ui.map.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.databinding.ItemCategoryBinding

internal data class CategoryItem(
    val id : Int,
    @DrawableRes val image : Int,
    @StringRes val name : Int
)

internal class CategoryAdapter(private val context: Context) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    val categoryItemList = arrayListOf<CategoryItem>(
        CategoryItem(0, R.drawable.selector_category_item_all, R.string.category_item_all),
        CategoryItem(1, R.drawable.selector_category_item_baby, R.string.category_item_baby),
        CategoryItem(2, R.drawable.selector_category_item_internal, R.string.category_item_internal),
        CategoryItem(3, R.drawable.selector_category_item_ent, R.string.category_item_ent),
        CategoryItem(4, R.drawable.selector_category_item_skin, R.string.category_item_skin),
        CategoryItem(5, R.drawable.selector_category_item_orthopedy, R.string.category_item_orthopedy),
        CategoryItem(6, R.drawable.selector_category_item_internal, R.string.category_item_internal),
        CategoryItem(7, R.drawable.selector_category_item_eyes, R.string.category_item_eyes)
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
        }
    }
}