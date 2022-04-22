package vn.chungha.authenticator.ui.create

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            val i = Intent(requireContext(), ShareQrCodeActivity::class.java)
            i.putExtra(EditActivity.EXTRA_TOKEN_ID, token.id)
            activity?.startActivity(i)
        }
    }

    companion object {
        const val TAG_SELECT_BOTTOM_SHEET = "TAG_SELECT_BOTTOM_SHEET"
    }
}