package com.ishanvohra.armorx.extensions

import android.content.Context
import android.util.DisplayMetrics

fun Int.pxToDp(context: Context): Int {
    return this / (context.resources.displayMetrics.densityDpi.toInt() / DisplayMetrics.DENSITY_DEFAULT)
}