package vn.chungha.authenticator.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import vn.chungha.authenticator.R
import vn.chungha.authenticator.base.BaseActivity
import vn.chungha.authenticator.databinding.ActivityMainBinding
import vn.chungha.authenticator.utils.SharePreference
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var sharePreference: SharePreference
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        createTokenFab.setOnClickListener {
            navController?.navigateUp() // to clear previous navigation history
            navController?.navigate(R.id.createTokenFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController?.navigateUp() // to clear previous navigation history
                navController?.navigate(R.id.createTokenFragment)
                true
            }
            R.id.action_setting -> {
//                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}