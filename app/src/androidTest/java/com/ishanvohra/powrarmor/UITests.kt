package com.ishanvohra.powrarmor

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ishanvohra.powrarmor.models.ArmorResponseItem
import com.ishanvohra.powrarmor.models.Assets
import com.ishanvohra.powrarmor.models.Defense
import com.ishanvohra.powrarmor.models.Slot
import com.ishanvohra.powrarmor.ui.activities.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITests {

    @get: Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val dummyList = listOf(
        ArmorResponseItem(
            assets = Assets("", ""),
            defense = Defense(base = 12),
            id = 1,
            name = "Demo armor",
            slots = listOf(Slot(12))
        ),
        ArmorResponseItem(
            assets = Assets("", ""),
            defense = Defense(base = 12),
            id = 2,
            name = "Demo armor",
            slots = listOf(Slot(12))
        ),
        ArmorResponseItem(
            assets = Assets("", ""),
            defense = Defense(base = 12),
            id = 3,
            name = "Demo armor",
            slots = listOf(Slot(12))
        )
    )

    @Test
    fun testSuccessState(){
        composeTestRule.activity.setContent {
            MainActivity().SuccessState(
                dummyList
            )
        }
        composeTestRule
            .onNodeWithTag("SuccessState")
            .onChildren()
            .assertCountEquals(dummyList.size)
    }

    @Test
    fun testErrorState(){
        composeTestRule.activity.setContent {
            MainActivity().ErrorState()
        }
        composeTestRule
            .onNodeWithTag("ErrorState")
            .assertExists()
    }

    @Test
    fun testLoadingState(){
        composeTestRule.activity.setContent {
            MainActivity().LoadingState()
        }
        composeTestRule
            .onNodeWithTag("LoadingState")
            .assertExists()
    }

}