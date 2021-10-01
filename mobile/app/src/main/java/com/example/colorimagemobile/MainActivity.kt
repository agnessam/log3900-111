package com.example.colorimagemobile

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.colorimagemobile.databinding.ActivityMainBinding
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants.Companion.LOCAL_STORAGE_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_TOKEN_KEY

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences(LOCAL_STORAGE_KEY, Context.MODE_PRIVATE)
        val token = sharedPref.getString(SHARED_TOKEN_KEY, "").toString()

        if (token == "" || token == null) {
            // redirect to /login
            redirectTo(this, LoginActivity::class.java)
        } else {
            // redirect to /chat
            redirectTo(this, ChatActivity::class.java)
        }

//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }
}