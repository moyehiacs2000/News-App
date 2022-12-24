package com.moyehics.news.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.material.appbar.MaterialToolbar
import com.moyehics.news.R
import com.moyehics.news.databinding.ActivityMainBinding
import com.moyehics.news.util.gone
import com.moyehics.news.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
     val viewModel: NewsViewModel by viewModels()
    lateinit var  toolBar : MaterialToolbar
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController  : NavController
    val END_SCALE = 0.7f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // FacebookSdk.sdkInitialize(getApplicationContext());
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        toolBar=binding.activityMainToolbar
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        navController   = navHostFrag.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment),binding.drawerLayout)
        binding.activityMainToolbar.setupWithNavController(navController,appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.activityMainToolbar.inflateMenu(R.menu.options_menu)
        animateNaviagationDrawer()
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
    fun shareData(url : String){
       val  sendIntent: Intent = Intent().apply {
           action = Intent.ACTION_SEND
           putExtra(Intent.EXTRA_TEXT, url)
           type = "text/plain"
       }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }


     fun animateNaviagationDrawer() {
        binding.drawerLayout.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                val diffScaledoffset: Float = slideOffset * (1 - END_SCALE)
                val offsetScale = 1 - diffScaledoffset
                binding.container.setScaleX(offsetScale)
                binding.container.setScaleY(offsetScale)
                val xOffset: Float = drawerView.getWidth() * slideOffset
                val xOffsetDiff: Float = binding.container.getWidth() * diffScaledoffset / 2
                val xTranslation = xOffset - xOffsetDiff
                binding.container.setTranslationX(xTranslation)
            }
        })
    }

}