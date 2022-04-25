package vn.chungha.authenticator.ui.create

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import vn.chungha.authenticator.R
import vn.chungha.authenticator.base.BaseBottomSheet
import vn.chungha.authenticator.databinding.FragmentSelectAddTokenBinding
import vn.chungha.authenticator.extension.onClick

class SelectAddToken() : BaseBottomSheet<FragmentSelectAddTokenBinding>() {
    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelectAddTokenBinding = FragmentSelectAddTokenBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() = binding.apply {
        neuCamera.onClick {
            findNavController().navigate(R.id.scanQrCodeTokenFragment)
            dismissAllowingStateLoss()
        }
    }

    companion object {
        const val TAG_SELECT_BOTTOM_SHEET = "TAG_SELECT_BOTTOM_SHEET"
    }
}