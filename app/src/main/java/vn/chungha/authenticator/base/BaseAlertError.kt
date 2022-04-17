package vn.chungha.authenticator.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import vn.chungha.authenticator.R
import vn.chungha.authenticator.databinding.ItemAlertErrorBinding
import vn.chungha.authenticator.extension.onClick

class BaseAlertError(
    val message: String,
    val clickOutSide: Boolean,
    val invoke: (() -> Unit)? = null
) : AlertDialogFragment<ItemAlertErrorBinding>() {
    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ItemAlertErrorBinding = ItemAlertErrorBinding.inflate(inflater)

    override fun onBuildDialog(builder: AlertDialog) {
        builder.window?.setGravity(Gravity.CENTER_VERTICAL)
        if (!clickOutSide) {
            builder.setCancelable(false)
            builder.setCanceledOnTouchOutside(false)
        }
    }

    override fun getTheme(): Int = R.style.MaterialAlertDialog_rounded

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        with(binding) {
            tvMessage.text = message
            tvCancel.onClick {
                invoke?.invoke()
                dialog?.dismiss()
            }
        }
    }

    companion object {
        private const val TAG = "BaseAlertError"
        fun show(
            fm: FragmentManager,
            message: String,
            clickOutSide: Boolean = true,
            invoke: (() -> Unit)? = null
        ) =
            BaseAlertError(message = message, clickOutSide, invoke).show(fm, TAG)
    }
}