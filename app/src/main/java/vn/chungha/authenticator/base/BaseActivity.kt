package vn.chungha.authenticator.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import vn.chungha.authenticator.R

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: B
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = onInflateView(layoutInflater)
        setContentView(binding.root)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        loadingDialog = LoadingDialog(this)
        setupData(savedInstanceState)
    }

    protected abstract fun onInflateView(inflater: LayoutInflater): B

    protected abstract fun setupData(savedInstanceState: Bundle?)

    override fun setContentView(view: View?) {
        super.setContentView(view)
    }

    fun showLoading(isShow: Boolean) {
        if (isShow) {
            loadingDialog?.show()
        } else {
            loadingDialog?.dismiss()
        }
    }

    fun hideLoading() = loadingDialog?.dismiss()

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    override fun onBackPressed() {
        if (
            Build.VERSION.SDK_INT == Build.VERSION_CODES.Q &&
            isTaskRoot &&
            supportFragmentManager.backStackEntryCount == 0
        ) {
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }
}
