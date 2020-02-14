package com.nexters.doctor24.todoc.ui.findload

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.base.BaseDialogFragment
import com.nexters.doctor24.todoc.databinding.LoadMapListDialogBinding
import com.nexters.doctor24.todoc.ext.dpToPixel
import timber.log.Timber


class FindLoadDialog(private val viewModel: FindLoadViewModel) : BaseDialogFragment<LoadMapListDialogBinding>() {
    companion object {
        @JvmField
        val TAG: String = this::class.java.simpleName
    }

    override val layoutResId: Int
        get() = R.layout.load_map_list_dialog

    override fun onDismiss() {}

    override fun onResume() {
        super.onResume()
        val metrics = DisplayMetrics()
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        val dialogWidth = (metrics.widthPixels - 42f.dpToPixel() * 2).toInt()
        dialog?.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.vm = viewModel
        initView()
        initObserve()
    }

    private fun initView() {

    }

    private fun initObserve() {

        viewModel.checkedMapAppEvent.observe(viewLifecycleOwner, Observer {
            Timber.d("MapApps : ${it.scheme}")

            val intent = Intent(Intent.ACTION_VIEW, it.scheme).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val list: List<ResolveInfo> =
                context!!.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list.isEmpty()) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "market://details?id=${it.packageName}".toUri()
                    )
                )
            } else {
                startActivity(intent)
            }
        })

        viewModel.closeEvent.observe(viewLifecycleOwner, Observer {
            dismiss()
        })

    }
}