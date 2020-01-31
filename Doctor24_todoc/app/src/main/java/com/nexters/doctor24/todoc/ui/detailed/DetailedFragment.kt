package com.nexters.doctor24.todoc.ui.detailed

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nexters.doctor24.todoc.R

class DetailedFragment : Fragment() {

    companion object {
        fun newInstance() = DetailedFragment()
    }

    private lateinit var viewModel: DetailedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detailed_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
