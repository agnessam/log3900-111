package com.example.colorimagemobile.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.colorimagemobile.ui.login.LoginActivity
import com.example.colorimagemobile.R
import com.example.colorimagemobile.httpresponsehandler.GlobalHandler
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.TextChannelRepository
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.ui.home.fragments.chat.textChannelHandler
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeActivityViewModel
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var globalHandler: GlobalHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        globalHandler = GlobalHandler()
        homeViewModel = ViewModelProvider(this).get(HomeActivityViewModel::class.java)
        sharedPreferencesService = SharedPreferencesService(this)
        setBottomNavigationView()
        AllTextChannel(UserService.getToken())
    }
    // side navigation navbar: upon click, change to new fragment
    private fun setBottomNavigationView() {
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.galleryFragment, R.id.chatFragment, R.id.notificationFragment, R.id.userProfileFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    // add options to Home Navbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_actions_menu, menu)

        if (!UserService.isNull()) {
            val usernameMenuItem: MenuItem = (menu as Menu).findItem(R.id.username_menu_item)
            usernameMenuItem.title = UserService.getUserInfo().username
        } else {
            checkCurrentUser()
        }

        return true
    }

    // when clicked on individual menu icons
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.log_out_menu_item -> {
            logUserOut()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // check if User exists! If not, make HTTP request to init the User and All text channel
    fun checkCurrentUser() {
        // user is null -> GET user
        if (UserService.isNull()) {
            val token = sharedPreferencesService.getItem(Constants.STORAGE_KEY.TOKEN)
            UserService.setToken(token)
            homeViewModel.getUserByToken(token).observe(this, { handleGetUserMe(it) })
        }
    }

    private fun handleGetUserMe(response: DataWrapper<HTTPResponseModel.UserResponse>) {
        UserService.setUserInfo(response.data?.user as UserModel.AllInfo)

        // update username in menu item
        val usernameMenuItem: ActionMenuItemView = findViewById(R.id.username_menu_item)
        usernameMenuItem.text = UserService.getUserInfo().username
    }

    private fun logUserOut() {

        val user = UserModel.Logout(UserService.getUserInfo().username)
        val logOutObserver = homeViewModel.logoutUser(user)
        logOutObserver.observe(this, { handleLogOutResponse(it) })
    }

    private fun handleLogOutResponse(it: DataWrapper<HTTPResponseModel>) {
        printToast(applicationContext, it.message as String)

        // some error occurred during HTTP request
        if (it.isError as Boolean) {
            return
        }

        val token = sharedPreferencesService.getItem(Constants.STORAGE_KEY.TOKEN)
        UserService.setToken(token)
        UserService.setLogHistory(Constants.LAST_LOGOUT_DATE)
        LogHistory()

        // remove items from "local storage"
        sharedPreferencesService.removeItem(Constants.STORAGE_KEY.TOKEN)

        redirectTo(this, LoginActivity::class.java)
    }

    private fun LogHistory(){
        UserService.setLogHistory(Constants.LAST_LOGOUT_DATE)
        val updateObserver = homeViewModel.updateLogHistory(UserService.getUserInfo()._id)
        updateObserver.observe(this, { this?.let { it1 -> globalHandler.response(it1,it) } })
    }

    fun getAllTextChannel(token: String): LiveData<DataWrapper<List<TextChannelModel.AllInfo>>> {
        return TextChannelRepository().getAllTextChannel(token)
    }

    fun AllTextChannel(token: String){
        val updateObserver = getAllTextChannel(token)
        updateObserver.observe(this, { this?.let { it1 -> textChannelHandler.responseGetAll(it1,it) } })
    }


    }