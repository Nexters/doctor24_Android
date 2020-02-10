package com.nexters.doctor24.todoc.ui.detailed

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseFragment
import com.nexters.doctor24.todoc.databinding.DetailedFragmentBinding
import com.nexters.doctor24.todoc.ui.detailed.adapter.DayAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class DetailedFragment : BaseFragment<DetailedFragmentBinding, DetailedViewModel>() {

    override val layoutResId: Int
        get() = R.layout.detailed_fragment
    override val viewModel: DetailedViewModel by viewModel()
    private val dayAdapter by lazy { DayAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.reqDetailedInfo("hospital", "A1119764")

        binding.apply {
            vm = viewModel
        }
        setMovieRecyclerView()
    }

    private fun setMovieRecyclerView() {

        with(binding) {
            rvDetailedFragTime.apply {
                layoutManager = GridLayoutManager(context,2)
                adapter = dayAdapter
            }
        }
    }

}
