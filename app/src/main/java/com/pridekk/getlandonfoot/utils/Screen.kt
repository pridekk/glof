package com.pridekk.getlandonfoot.utils

import androidx.annotation.StringRes
import com.pridekk.getlandonfoot.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Map : Screen("map", R.string.map)
    object Main : Screen("main", R.string.main)
    object Profile : Screen("profile", R.string.profile)
    object Market: Screen("market", R.string.market)
}
