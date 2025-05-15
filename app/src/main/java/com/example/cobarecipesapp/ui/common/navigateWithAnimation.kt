package com.example.cobarecipesapp.ui.common

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.example.cobarecipesapp.R

fun NavController.navigateWithAnimation(
    destinationId: Int,
    args: Bundle? = null
) {
    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(android.R.anim.slide_out_right)
        .setPopEnterAnim(android.R.anim.slide_in_left)
        .setPopExitAnim(android.R.anim.slide_out_right)
        .build()
    this.navigate(destinationId, args, navOptions)
}

fun NavController.navigateWithAnimation(
    direction: NavDirections,
) {
    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(android.R.anim.slide_out_right)
        .setPopEnterAnim(android.R.anim.slide_in_left)
        .setPopExitAnim(android.R.anim.slide_out_right)
        .build()
    this.navigate(direction, navOptions)
}