package vn.chungha.authenticator.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import vn.chungha.authenticator.base.BaseFragment
import vn.chungha.authenticator.databinding.FragmentHomeBinding
import vn.chungha.authenticator.model.Token
import vn.chungha.authenticator.ui.MainActivity

@AndroidEntryPoint
class HomeFragment() : BaseFragment<FragmentHomeBinding, MainActivity>() {
    private var fakeListToken = mutableListOf<Token>()
    private val appAdapter by lazy(LazyThreadSafetyMode.NONE) {
        HomeAdapter(::clickToken)
    }

    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (0..20).forEach {
            fakeListToken.add(Token(it.toLong(), it.toLong()))
        }
    }

    override fun setupView() {
        setupRecyclerView()
        appAdapter.submitList(fakeListToken)
    }

    override fun setupData() {

    }

    override fun onResume() {
        super.onResume()
        showBottomBar(true)
    }

    private fun clickToken(token: Token) {
        Toast.makeText(requireContext(), "Item click $token", Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView() = binding.recyclerView.run {
        setHasFixedSize(true)
        adapter = appAdapter
    }
}