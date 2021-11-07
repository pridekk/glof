package com.pridekk.getlandonfoot

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pridekk.getlandonfoot.ui.components.Login
import com.pridekk.getlandonfoot.ui.components.Main
import com.pridekk.getlandonfoot.ui.components.MyMap
import com.pridekk.getlandonfoot.ui.components.Profile
import com.pridekk.getlandonfoot.ui.theme.GetLandOnFootTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import timber.log.Timber


class MainActivity : ComponentActivity(){

    val viewModel: MainViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            GetLandOnFootTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Login(viewModel::login)
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.loginUiState.collect {
                when(it) {
                    is MainViewModel.LoginUiState.Success -> {
                        setContent {
                            GetLandOnFootTheme {
                                // A surface container using the 'background' color from the theme
                                Surface(color = MaterialTheme.colors.background) {
                                    Navigation()
                                }

                            }
                        }
                    }
                }
            }
        }



    }



}





@Composable
fun Navigation() {

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
                Profile(navController = navController)
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




