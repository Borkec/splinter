package com.sintegra.splinter.ui.navigation

import androidx.annotation.Keep
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sintegra.splinter.ui.mainscreen.EditableWaveGraph
import com.sintegra.splinter.ui.mainscreen.Octave
import com.sintegra.splinter.ui.mainscreen.SplinterArea
import com.sintegra.splinter.ui.mainscreen.MainSplinterScreen
import kotlinx.serialization.Serializable

@Composable
fun MainController() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainScreen(MainScreenType.SPLINTER_AREA),
    ) {
        composable<MainScreen> {
            val screen: MainScreen = it.toRoute()

            MainSplinterScreen(
                switchState = screen.type == MainScreenType.PIANO_KEYS,
                onSwitchChange = {
                    when (screen.type) {
                        MainScreenType.SPLINTER_AREA -> navController.navigate(MainScreen(MainScreenType.PIANO_KEYS))
                        MainScreenType.PIANO_KEYS -> navController.navigate(MainScreen(MainScreenType.SPLINTER_AREA))
                    }
                },
                onCustomWaveScreenOpened = {
                    navController.navigate(CustomWaveEditorScreen)
                },
                content = {
                    when (screen.type) {
                        MainScreenType.SPLINTER_AREA -> SplinterArea()
                        MainScreenType.PIANO_KEYS -> Octave(Modifier.fillMaxSize())
                    }
                }
            )
        }
        composable<CustomWaveEditorScreen> {
            EditableWaveGraph(onCloseGraphScreen = {
                navController.popBackStack()
            })
        }
    }
}

@Keep
enum class MainScreenType {
    SPLINTER_AREA, PIANO_KEYS
}

@Serializable
data class MainScreen(val type: MainScreenType)

@Serializable
object CustomWaveEditorScreen