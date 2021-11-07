package com.pridekk.getlandonfoot

import androidx.annotation.StringRes

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Map : Screen("map", R.string.map)
    object Main : Screen("main", R.string.main)
    object Profile : Screen("profile", R.string.profile)
}
