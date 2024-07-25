package com.danial.smartcardreader

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.danial.smartcardreader.ui.screen.card.item.CardsItemScreen
import com.danial.smartcardreader.ui.screen.card.list.CardListViewModel
import com.danial.smartcardreader.ui.screen.card.list.CardsListScreen
import com.danial.smartcardreader.ui.theme.SmartCardReaderTheme
import com.danial.smartcardreader.ui.utils.ContextUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context?) {
        val localeUpdatedContext = newBase?.let {
            ContextUtils.updateLocale(it, Locale("en"))
        }
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            SmartCardReaderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "cardsListNav") {
                        navigation(startDestination = "cardsList", route = "cardsListNav") {
                            composable("cardsList") { backStackEntry ->
                                val cardListEntry = remember(backStackEntry) { navController.getBackStackEntry("cardsListNav") }
                                val cardListViewModel: CardListViewModel = hiltViewModel(cardListEntry)

                                CardsListScreen(
                                    viewModel = cardListViewModel,
                                    onNavigateToCardItemScreen = {
                                        navController.navigate("cardItem/"+it.id)
                                    }
                                )
                            }
                            composable("cardItem/{cardId}") { backStackEntry ->
                                val cardListEntry = remember(backStackEntry) { navController.getBackStackEntry("cardsListNav") }
                                val cardListViewModel: CardListViewModel = hiltViewModel(cardListEntry)

                                val cardId = backStackEntry.arguments?.getString("cardId")
                                val cardItem = cardListViewModel.getCardItem(cardId)

                                CardsItemScreen(cardItem = cardItem)
                            }
                        }
                    }
                }
            }
        }
    }
}