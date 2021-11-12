package com.example.colorimagemobile.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import java.io.Serializable

class MyFragmentManager(activity: FragmentActivity) {
    private var activity: FragmentActivity = activity

    fun closeFragment(fragment: Fragment) {
        activity.supportFragmentManager
            .beginTransaction()
            .remove(fragment)
            .commitAllowingStateLoss()
    }

    // replace and open a fragment with a new one
    fun open(oldFragmentID: Int, newFragmentClass: Fragment) {
        activity.supportFragmentManager
            .beginTransaction()
            .replace(oldFragmentID, newFragmentClass)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commitAllowingStateLoss()
    }

    // open a new fragment by passing data along
    fun openWithData(oldFragmentID: Int, newFragmentClass: Fragment, data: Serializable) {
        val bundle = Bundle()
        bundle.putSerializable("canvas", data)
        newFragmentClass.arguments = bundle

        open(oldFragmentID, newFragmentClass)
    }
}