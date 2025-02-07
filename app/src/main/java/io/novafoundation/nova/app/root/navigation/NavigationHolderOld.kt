package io.novafoundation.nova.app.root.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController

class NavigationHolderOld {

    var navController: NavController? = null
        private set

    var activity: AppCompatActivity? = null
        private set

    fun attach(navController: NavController, activity: AppCompatActivity) {
        this.navController = navController
        this.activity = activity
    }

    fun detach() {
        navController = null
        activity = null
    }
}

fun NavigationHolderOld.executeBack() {
    val popped = navController!!.popBackStack()

    if (!popped) {
        activity!!.finish()
    }
}
