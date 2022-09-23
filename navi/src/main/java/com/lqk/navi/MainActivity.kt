package com.lqk.navi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.lqk.navi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    ///<editor-fold desc="默认处理">

    ///</editor-fold>



    private lateinit var viewBinder: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main)
        setContentView(viewBinder.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fl_container) as NavHostFragment
        navController = navHostFragment.navController

        viewBinder.bnv.setupWithNavController(navController)

////        DexposedBridge.hookMethod()
//
////        XposedBridge.hookMethod()
//        val hostA = AFragment()
//        val navController = hostA.navController
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.fl_container, hostA)
//            .setPrimaryNavigationFragment(hostA)
//            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
//        return super.onSupportNavigateUp()
    }
}