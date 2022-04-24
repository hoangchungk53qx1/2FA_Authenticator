package vn.chungha.authenticator.ui.create

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import vn.chungha.authenticator.base.BaseFragment
import vn.chungha.authenticator.databinding.FragmentCreateTokenBinding
import vn.chungha.authenticator.ui.MainActivity

@AndroidEntryPoint
class CreateTokenFragment() : BaseFragment<FragmentCreateTokenBinding,MainActivity>() {
    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateTokenBinding = FragmentCreateTokenBinding.inflate(inflater)

    override fun setupView() {

    }

    override fun setupData() {

    }
}