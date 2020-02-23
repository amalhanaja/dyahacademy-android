package com.amalcodes.dyahacademy.android.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

import com.amalcodes.dyahacademy.android.R
import kotlinx.android.synthetic.main.dialog_finish_quiz_confirmation.*

class ConfirmationDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_finish_quiz_confirmation, container, false)
    }

    private val viewModel: ConfirmationDialogViewModel by activityViewModels {
        ConfirmationDialogViewModel.Factory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.shape_round_white_xl)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        isCancelable = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindToView(requireNotNull(viewModel.viewEntity))
    }

    private fun bindToView(viewEntity: ConfirmationDialogViewEntity) {
        mtv_confirmation_title?.text = viewEntity.title
        mtv_confirmation_description?.text = viewEntity.description
        mb_confirmation_ok?.text = viewEntity.confirmButton.first
        mb_confirmation_cancel?.text = viewEntity.cancelButton.first
        mb_confirmation_ok?.setOnClickListener {
            dismiss()
            viewEntity.confirmButton.second(it)
        }
        mb_confirmation_cancel?.setOnClickListener {
            dismiss()
            viewEntity.cancelButton.second(it)
        }
        iv_confirmation?.isGone = viewEntity.drawableRes == 0
        iv_confirmation?.setImageResource(viewEntity.drawableRes)
        iv_confirmation?.imageTintList = viewEntity.drawableTint
    }

}
