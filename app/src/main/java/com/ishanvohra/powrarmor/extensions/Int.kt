package com.ishanvohra.powrarmor.extensions

import android.content.Context
import android.util.DisplayMetrics

/**
 * Converts integer value to value in dp
 */
fun Int.pxToDp(context: Context): Int {
    return this / (context.resources.displayMetrics.densityDpi.toInt() / DisplayMetrics.DENSITY_DEFAULT)
}