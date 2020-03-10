package com.fevgenson.timetable.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

@Navigator.Name(value = "save_state")
class BottomSaveStateNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) :
    FragmentNavigator(context, manager, containerId) {

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val tag = destination.id.toString()
        val transaction = manager.beginTransaction()
        //check current fragment
        val currentFragment = manager.primaryNavigationFragment
        val initialNavigate = if (currentFragment != null) {
            transaction.detach(currentFragment)
            false
        } else
            true
        //set destination fragment
        var destinationFragment = manager.findFragmentByTag(tag)
        if (destinationFragment == null) {
            destinationFragment =
                manager.fragmentFactory.instantiate(context.classLoader, destination.className)
            transaction.add(containerId, destinationFragment, tag)
        } else
            transaction.attach(destinationFragment)

        transaction.setPrimaryNavigationFragment(destinationFragment)
        transaction.setReorderingAllowed(true)
        transaction.commitNow()

        return if (initialNavigate) destination else null
    }
}