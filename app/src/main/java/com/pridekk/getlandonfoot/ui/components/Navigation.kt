package com.pridekk.getlandonfoot.ui.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pridekk.getlandonfoot.Screen
import com.pridekk.getlandonfoot.ui.viewmodels.ProfileViewModel

@Composable
fun Navigation(
    logout: () -> Unit
) {

    val navController = rememberNavController()
    val items = listOf(
        Screen.Profile,
        Screen.Main,
        Screen.Map
    )
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ){

        NavHost(navController = navController, startDestination = "main" ){

            composable(Screen.Main.route){
                Main(navController = navController)
            }
            composable(Screen.Profile.route){
                Profile(navController = navController, logout)
            }
            composable(Screen.Map.route){
                MyMap(navController = navController){ googleMap ->
                    val sydney = LatLng(-33.852, 151.211)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(sydney)
                            .title("Marker in Sydney")
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
            }


        }
    }

}