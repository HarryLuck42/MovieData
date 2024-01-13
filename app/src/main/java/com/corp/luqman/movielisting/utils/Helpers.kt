package com.corp.luqman.movielisting.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.corp.luqman.movielisting.R
import com.corp.luqman.movielisting.databinding.DialogGeneralOkBinding

object Helpers {

    fun customViewDialog(context: Context, layout: Int, binding : ViewBinding, cancelAble: Boolean): MaterialDialog {
        val mDialog = MaterialDialog(context)
            .customView(layout, binding.root, scrollable = false,
                noVerticalPadding = true,
                dialogWrapContent = true,
                horizontalPadding = false)
            .cornerRadius(8f)
            .cancelable(cancelAble)

        return mDialog

    }

    fun showGeneralOkDialog(mContext: Context, mTitle: String = "", mDesc: String = ""): MaterialDialog {
        val binding = DialogGeneralOkBinding.inflate(LayoutInflater.from(mContext))
        val mDialog = MaterialDialog(mContext).customView(
            R.layout.dialog_general_ok,
            binding.root,
            scrollable = false,
            noVerticalPadding = true,
            dialogWrapContent = true,
            horizontalPadding = false
        ).cornerRadius(8f)

        binding.tvDialogTitle.text = mTitle.trim()
        binding.tvDialogDesc.text = mDesc.trim()
        binding.btnDialogSubmit.text = mContext.getText(R.string.ok)
        binding.btnDialogSubmit.setOnClickListener{
            mDialog.dismiss()
        }

        return mDialog.show{
            cancelable(true)
        }
    }

}