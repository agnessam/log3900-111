package com.example.colorimagemobile.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.colorimagemobile.ui.login.LoginActivity
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.services.socket.SocketManagerService
import com.example.colorimagemobile.ui.home.fragments.chat.ChatFragment
import com.example.colorimagemobile.ui.home.fragments.chat.ChatFragmentDirections
import com.example.colorimagemobile.ui.home.fragments.gallery.GalleryFragmentDirections
import com.example.colorimagemobile.ui.home.fragments.gallery.GalleryMenuFragment
import com.example.colorimagemobile.ui.home.fragments.teams.TeamsFragmentDirections
import com.example.colorimagemobile.ui.home.fragments.teams.TeamsMenuFragment
import com.example.colorimagemobile.ui.home.fragments.userProfile.ShowUserProfileFragmentDirections
import com.example.colorimagemobile.ui.home.fragments.userProfile.UserProfileFragmentDirections
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_show_user_profile.*

class HomeActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeActivityViewModel
    private lateinit var sharedPreferencesService: SharedPreferencesService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        homeViewModel = ViewModelProvider(this).get(HomeActivityViewModel::class.java)
        sharedPreferencesService = SharedPreferencesService(this)

        setBottomNavigationView()
    }
    // side navigation navbar: upon click, change to new fragment
    private fun setBottomNavigationView() {
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // remove every socket events
        navView.setOnItemSelectedListener {
            SocketManagerService.disconnectFromAll()
            return@setOnItemSelectedListener true
        }

        val navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.galleryFragment, R.id.chatFragment, R.id.teamsFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    // add options to Home Navbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_actions_menu, menu)
        if (!UserService.isNull()) {
            // username
            val usernameMenuItem: MenuItem = (menu as Menu).findItem(R.id.username_menu_item)
            usernameMenuItem.title = UserService.getUserInfo().username

            //avatar
            val avatarmenuItem : MenuItem = (menu as Menu).findItem(R.id.useravatar_menu_item)
            val view: View = avatarmenuItem.getActionView()
            val profileImage : ImageView = view.findViewById(R.id.toolbar_profile_avatar)
            CommonFun.loadUrl( UserService.getUserInfo().avatar.imageUrl, profileImage)

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
        R.id.show_profile -> {
            showUserProfile()
            true
        }
        R.id.Settings -> {
            showSettings()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {

         val navController = this.findNavController(R.id.fragment)
        return when(navController.currentDestination?.id) {
            // back button clicked on Gallery Drawing
            R.id.galleryFragment -> {
                SocketManagerService.leaveDrawingRoom()
                MyFragmentManager(this).open(R.id.main_gallery_fragment, GalleryMenuFragment())
                true
            }
            R.id.teamsFragment -> {
                MyFragmentManager(this).open(R.id.teamsMenuFrameLayout, TeamsMenuFragment())
                true
            }
            R.id.chatFragment -> {
                MyFragmentManager(this).open(R.id.chatChannelFragment, ChatFragment())
                true
            }

            else -> navController.navigateUp()
        }
    }

    // check if User exists! If not, make HTTP request to init the User and All text channel
    private fun checkCurrentUser() {
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

        // refresh menu Item by calling back onCreateOptionsMenu
        invalidateOptionsMenu()
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

        // remove items from "local storage"
        sharedPreferencesService.removeItem(Constants.STORAGE_KEY.TOKEN)

        redirectTo(this, LoginActivity::class.java)
    }

    // menu item show user profile
    private fun showUserProfile(){
        // define path
        val showUserProfileFromGallery = GalleryFragmentDirections.actionGalleryFragmentToShowUserProfileFragment()
        val showUserProfileFromTeam = TeamsFragmentDirections.actionTeamsFragmentToShowUserProfileFragment()
        val showUserProfileFromChat = ChatFragmentDirections.actionChatFragmentToShowUserProfileFragment()
        val showUserProfileFromSettings = UserProfileFragmentDirections.actionUserProfileFragmentToShowUserProfileFragment()

        // redirect from actual page to userProfile
        when (this.findNavController(R.id.fragment).currentDestination?.id) {
            R.id.galleryFragment-> this.findNavController(R.id.fragment).navigate(showUserProfileFromGallery)
            R.id.teamsFragment  -> this.findNavController(R.id.fragment).navigate(showUserProfileFromTeam)
            R.id.chatFragment ->this.findNavController(R.id.fragment).navigate(showUserProfileFromChat)
            R.id.userProfileFragment ->this.findNavController(R.id.fragment).navigate(showUserProfileFromSettings)
        }
    }

    // menu item show settings
    private fun showSettings(){
        //define path
        val showSettingsFromGallery = GalleryFragmentDirections.actionGalleryFragmentToUserProfileFragment()
        val showSettingsFromTeam = TeamsFragmentDirections.actionTeamsFragmentToUserProfileFragment()
        val showSettingsFromChat = ChatFragmentDirections.actionChatFragmentToUserProfileFragment()
        val showSettingsFromProfileView = ShowUserProfileFragmentDirections.actionShowUserProfileFragmentToUserProfileFragment()

        // redirect from actual page to settings
        when (this.findNavController(R.id.fragment).currentDestination?.id) {
            R.id.galleryFragment-> this.findNavController(R.id.fragment).navigate(showSettingsFromGallery)
            R.id.teamsFragment  -> this.findNavController(R.id.fragment).navigate(showSettingsFromTeam)
            R.id.chatFragment ->this.findNavController(R.id.fragment).navigate(showSettingsFromChat)
            R.id.showUserProfileFragment -> this.findNavController(R.id.fragment).navigate(showSettingsFromProfileView)
        }
    }

}