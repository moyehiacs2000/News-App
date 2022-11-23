package com.moyehics.news.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.moyehics.news.R
import com.moyehics.news.databinding.ActivityMainBinding
import com.moyehics.news.util.gone
import com.moyehics.news.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
     val viewModel: NewsViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

     private lateinit var navController  : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        navController   = navHostFrag.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment),binding.drawerLayout)
        binding.activityMainToolbar.setupWithNavController(navController,appBarConfiguration)
        binding.navView.setupWithNavController(navController)

    }
    fun closeDrawer(){
        supportActionBar?.hide()
        binding.activityMainToolbar.gone()
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun openDrawer(){
        supportActionBar?.show()
        binding.activityMainToolbar.show()
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
    override fun onBackPressed() {
        if(binding.drawerLayout.isOpen){
            binding.drawerLayout.close()
        }else{
            super.onBackPressed()
        }
    }
}