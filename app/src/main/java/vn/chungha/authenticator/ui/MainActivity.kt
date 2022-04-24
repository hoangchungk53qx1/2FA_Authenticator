package vn.chungha.authenticator.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import vn.chungha.authenticator.R
import vn.chungha.authenticator.base.BaseActivity
import vn.chungha.authenticator.databinding.ActivityMainBinding
import vn.chungha.authenticator.extension.onClick
import vn.chungha.authenticator.extension.visibleIf
import vn.chungha.authenticator.ui.create.SelectAddToken
import vn.chungha.authenticator.ui.create.SelectAddToken.Companion.TAG_SELECT_BOTTOM_SHEET
import vn.chungha.authenticator.utils.SharePreference
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var sharePreference: SharePreference
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupColorApp()
        setupNavigation()
    }

    override fun navigateUpTo(upIntent: Intent?): Boolean =
        navController?.navigateUp() ?: false

    override fun onInflateView(inflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflater)

    override fun setupData(savedInstanceState: Bundle?) {
    }

    private fun setupNavigation() = binding.run {
        navController = findNavController(R.id.nav_host_fragment)
        setSupportActionBar(bottomAppBar)
        createTokenFab.onClick {
            val fabDialog = SelectAddToken()
            fabDialog.show(supportFragmentManager,TAG_SELECT_BOTTOM_SHEET)
        }
    }

//    fun showBottomBar(isShow : Boolean) = binding.run {
//        bottomAppBar.visibleIf(isShow)
//        createTokenFab.visibleIf(isShow)
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
//                navController?.navigateUp() // to clear previous navigation history
//                navController?.navigate(R.id.createTokenFragment)\
                    val fabDialog = SelectAddToken()
                    fabDialog.show(supportFragmentManager,TAG_SELECT_BOTTOM_SHEET)
                true
            }
            R.id.action_setting -> {
//                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun setupColorApp() {
//        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(applicationContext,R.color.color_primary_variant)
        window.navigationBarColor = ContextCompat.getColor(applicationContext,R.color.white)
    }
}