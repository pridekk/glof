package com.pridekk.getlandonfoot.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.pridekk.getlandonfoot.MainViewModel
import com.pridekk.getlandonfoot.utils.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalCoroutinesApi
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Navigation(
    isTracking: MutableLiveData<Boolean>,
    toggleTracking: () -> Unit,
    logout: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {

    val trackingState = isTracking.observeAsState()
    val lastLocationState = viewModel.lastLocations.observeAsState()
    val navController = rememberNavController()
    val items = listOf(
        Screen.Profile,
        Screen.Main,
        Screen.Market,
        Screen.Map,
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

        NavHost(navController = navController, startDestination = "profile" ){
            composable(Screen.Main.route){
                Text("main")
            }

            composable(Screen.Profile.route){
                Profile(viewModel.token, trackingState.value, toggleTracking, lastLocationState.value, logout)
            }
            composable(Screen.Market.route){
                Market()
            }
            composable(Screen.Map.route){
                MyMap(null){ googleMap ->
                    val sydney = LatLng(-33.852, 151.211)

                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
            }


        }
    }

}

fun trackingLocation(context: Context): Unit  {

    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.

    }
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location : Location? ->
            location?.let {
                Timber.d("MainLocation $location")
            }
        }
}