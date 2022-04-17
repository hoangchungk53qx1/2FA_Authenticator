package vn.chungha.authenticator.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import vn.chungha.authenticator.base.BaseFragment
import vn.chungha.authenticator.databinding.FragmentHomeBinding
import vn.chungha.authenticator.ui.MainActivity

@AndroidEntryPoint
class HomeFragment() : BaseFragment<FragmentHomeBinding, MainActivity>() {
    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater)

    override fun setupView() {
//        showLoading(true)
    }

    override fun setupData() {

    }
}