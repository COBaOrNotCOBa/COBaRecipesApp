package com.example.cobarecipesapp.ui.common

//noinspection SuspiciousImport
import android.R
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateWithAnimation(
    destinationId: Int,
    args: Bundle? = null
) {
    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_left)
        .setExitAnim(R.anim.slide_out_right)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()
    this.navigate(destinationId, args, navOptions)
}