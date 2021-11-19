package com.example.colorimagemobile.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.colorimagemobile.ui.login.LoginActivity
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.httpresponsehandler.GlobalHandler
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.SearchModel
import com.example.colorimagemobile.repositories.SearchRepository
import com.example.colorimagemobile.services.SearchService
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.socket.SocketManagerService
import com.example.colorimagemobile.ui.home.fragments.gallery.GalleryMenuFragment
import com.example.colorimagemobile.ui.home.fragments.search.SearchFragment
import com.example.colorimagemobile.ui.home.fragments.teams.TeamsMenuFragment
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeActivityViewModel
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var globalHandler: GlobalHandler
    private lateinit var navController: NavController
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        globalHandler = GlobalHandler()
        homeViewModel = ViewModelProvider(this).get(HomeActivityViewModel::class.java)
        sharedPreferencesService = SharedPreferencesService(this)

        navController = findNavController(R.id.fragment)
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        setBottomNavigationView()
    }
    // side navigation navbar: upon click, change to new fragment
    private fun setBottomNavigationView() {
        // remove every socket events
        bottomNav.setOnItemSelectedListener {
            SocketManagerService.disconnectFromAll()
            return@setOnItemSelectedListener true
        }

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.galleryFragment, R.id.chatFragment, R.id.teamsFragment, R.id.userProfileFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)
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

        setSearchIcon(menu)
        return true
    }

    private fun setSearchIcon(menu: Menu?) {
        val searchView = menu?.findItem(R.id.searchIcon)?.actionView as SearchView
        searchView.queryHint = "Quick Search"

        searchView.setOnCloseListener(object: SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                bottomNav.selectedItemId = navController.currentDestination?.id!!
                return false
            }
        })

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNullOrEmpty()) { SearchService.clear() }
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                SearchService.setQuery(query)
                searchView.clearFocus()
                getFilteredData()
                return true
            }
        })
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

    override fun onSupportNavigateUp(): Boolean {
        return when(navController.currentDestination?.id) {
            // back button clicked on Gallery Drawing
            R.id.galleryFragment -> {
                SocketManagerService.leaveDrawingRoom()
                DrawingService.setCurrentDrawingID(null)
                bottomNav.selectedItemId = R.id.galleryFragment
                true
            }
            R.id.teamsFragment -> {
                bottomNav.selectedItemId = R.id.teamsFragment
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

    private fun getFilteredData() {
        val query = SearchService.getQuery()
        if (query.isNullOrEmpty()) return

        SearchRepository().getSearchQuery(query).observe(this, {
            if (it.isError as Boolean) { return@observe }

            val filteredData = it.data as SearchModel
            MyFragmentManager(this).openWithData(R.id.fragment, SearchFragment(), Constants.SEARCH.CURRENT_QUERY, filteredData)
        })
    }
}