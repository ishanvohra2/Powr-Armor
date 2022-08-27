package com.ishanvohra.powrarmor.utils

import android.content.Context
import com.ishanvohra.powrarmor.extensions.pxToDp

enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

/**
 * Get the current window size class
 * If the width is greater than 840dp, the function returns EXPANDED
 * If the width is greater than 600dp and less than 840dp, the function returns MEDIUM
 * if the width is less than 600 dp, the function returns COMPACT
 */
fun getWindowSizeClass(context: Context): WindowSizeClass {
    val width = context.resources.displayMetrics.widthPixels.pxToDp(context)
    return when {
        width < 600 -> { WindowSizeClass.COMPACT }
        width in 600..839 -> { WindowSizeClass.MEDIUM }
        width >= 840 -> { WindowSizeClass.EXPANDED }
        else -> { WindowSizeClass.COMPACT }
    }
}

/**
 * Calculate number of columns in the grid based on the screen width and card width
 */
fun getNumberOfColumns(cardWidth: Int, cardHorizontalSpacing: Int, context: Context): Int =
    (context.resources.displayMetrics.widthPixels.pxToDp(context)) / (cardWidth + cardHorizontalSpacing)