package com.nexters.doctor24.todoc.ui.mask.preview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naver.maps.geometry.LatLng
import com.nexters.doctor24.todoc.R
import com.nexters.doctor24.todoc.data.marker.MaskStateEnum
import com.nexters.doctor24.todoc.data.mask.response.StoreSale
import com.nexters.doctor24.todoc.databinding.PreviewMaskFragmentBinding
import com.nexters.doctor24.todoc.ui.findload.FindLoadDialog
import com.nexters.doctor24.todoc.ui.findload.FindLoadViewModel
import com.nexters.doctor24.todoc.util.stockAt
import com.nexters.doctor24.todoc.util.toDistance
import kotlinx.android.synthetic.main.preview_mask_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class PreviewMaskFragment : BottomSheetDialogFragment() {
    companion object {
        @JvmField
        val TAG: String = this::class.java.simpleName

        const val KEY_MEDICAL: String = "KEY_MEDICAL"
        const val KEY_MY_LOCATION: String = "KEY_MY_LOCATION"
    }

    private lateinit var binding: PreviewMaskFragmentBinding
    private var previewMaskData: PreviewMaskUiData? = null
    private lateinit var currentLocation: LatLng
    private val previewMaskViewModel: PreviewMaskViewModel by sharedViewModel()
    private val findLoadViewModel: FindLoadViewModel by viewModel()
    private val findLoadDialog: FindLoadDialog by lazy { FindLoadDialog(findLoadViewModel) }

    interface PreviewListener {
        fun onClosedPreview()
    }

    var listener: PreviewListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = arguments?.let {
            it.getParcelable<StoreSale>(KEY_MEDICAL)
        }
        val loc = arguments?.let {
            it.getDoubleArray(KEY_MY_LOCATION)
        }

        data?.let {
            val from = loc?.let { LatLng(it[0], it[1]) }
            val to = LatLng(data.lat.toDouble(), data.lng.toDouble())
            previewMaskData = PreviewMaskUiData(
                id = it.code,
                type = it.type,
                placeName = it.name,
                stockAt = stockAt(it.stockAt),
                address = it.addr,
                distance = to.toDistance(from),
                state = it.state!!
            )
            findLoadViewModel.centerName = it.name
            findLoadViewModel.determineLocation = to
            findLoadViewModel.currentLocation = from
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate<PreviewMaskFragmentBinding>(
            inflater,
            R.layout.preview_mask_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setCanceledOnTouchOutside(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.item = previewMaskData

        binding.tvPreviewMaskGotoMap.setOnClickListener {
            findLoadDialog.show(childFragmentManager, FindLoadDialog.TAG)
        }

        drawMaskIcon(previewMaskData!!.state)

    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.onClosedPreview()
    }

    private fun drawMaskIcon(state: String) {
        iv_preview_mask_stock.setImageResource(MaskStateEnum.getMaskState(state).drawableMask)
    }


    data class PreviewMaskUiData(
        val id: String,
        val type: String,
        val placeName: String = "",
        val stockAt: String = "",
        val address: String = "",
        val distance: String = "",
        val state : String = ""
    )
}