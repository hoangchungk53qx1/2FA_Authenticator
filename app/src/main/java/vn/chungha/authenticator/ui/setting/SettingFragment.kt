package vn.chungha.authenticator.ui.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import vn.chungha.authenticator.base.BaseFragment
import vn.chungha.authenticator.databinding.FragmentSettingBinding
import vn.chungha.authenticator.ui.MainActivity

class SettingFragment() : BaseFragment<FragmentSettingBinding,MainActivity>() {
    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding = FragmentSettingBinding.inflate(inflater)

    override fun setupView() {

    }

    override fun setupData() {

    }

}