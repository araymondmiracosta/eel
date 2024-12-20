package net.araymond.eel

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            initialize()
            Values.scope = rememberCoroutineScope()
            Values.snackbarHostState = remember { SnackbarHostState() }
            applicationNavHost(this)
        }
    }

    /**
     * Reads in any saved ledger or preference data and creates the
     * ArrayList<Transaction> Values.transactions, ArrayList<Transaction> Values.assetTransactions,
     * ArrayList<String> Values.categories, ArrayList<String> Values.accountNames,
     * ArrayList<String> Values.assetNames lists.
     *
     */
    private fun initialize() {
        if (Utility.readLedgerSaveData(this)) {
            Log.d("INFO", "Ledger data read successfully")
        }
        if (Utility.readAssetSaveData(this)) {
            Log.d("INFO", "Asset data read successfully")
        }
        if (Utility.readPreferenceSaveData(this)) {
            Log.d("INFO", "User preferences read successfully")
        }
        Utility.readAll()
    }

    /**
     * Holds the navHostController for the application. All navigation is done through this
     * function and the navHostController is passed to all main screens to achieve this.
     *
     * @param context The main context for this application
     */
    @Composable
    fun applicationNavHost(context: Context) {
        val navHostController = rememberNavController()
        NavHost(navController = navHostController, startDestination = "Main Activity") {
            composable("Main Activity") {
                Views.mainDraw(navHostController, context)
            }
            composable("Edit Account Activity/{accountName}") {
                it.arguments?.getString("accountName")?.let { it1 ->
                    Views.generateAccountCreationView(navHostController, context, it1)
                }
            }
            composable("Search Transactions Activity") {
                Views.generateSearchTransactionsView(navHostController, context)
            }
            composable("New Account Activity") {
                Views.generateAccountCreationView(navHostController, context, "")
            }
            composable("New Transaction Activity") {
                Views.generateNewTransactionView(navHostController, context, 0, null, false)
            }
            composable("New Transaction Activity/{accountName}") {
                it.arguments?.getString("accountName")?.let { accountName ->
                    Views.generateNewTransactionView(navHostController, context, 0, accountName, false)
                }
            }
            composable("View Transaction Activity/{transactionID}", arguments = listOf(navArgument("transactionID") {
                type = NavType.IntType
            })) {
                it.arguments?.getInt("transactionID")?.let { transactionID ->
                    Views.generateNewTransactionView(navHostController, context, transactionID, null, true)
                }
            }
            composable("Settings Activity") {
                Views.generateSettingsView(navHostController, context)
            }
            composable("Account Specific Activity/{accountName}") {
                it.arguments?.getString("accountName")?.let { accountName ->
                    Views.generateAccountSpecificView(navHostController, accountName, context)
                }
            }
            composable("About Activity") {
                Views.generateAboutView(navHostController, context)
            }
            composable("New Asset Activity") {
                Views.generateAssetCreationView(navHostController, context, "")
            }
            composable("Edit Asset Activity/{assetName}") {
                it.arguments?.getString("assetName")?.let{ assetName ->
                    Views.generateAssetCreationView(navHostController, context, assetName)
                }
            }
            composable("Asset Specific Activity/{assetName}") {
                it.arguments?.getString("assetName")?.let { assetName ->
                    Views.generateAssetSpecificView(navHostController, context, assetName)
                }
            }
            composable("Asset Activity") {
                Views.generateAssetView(navHostController, context)
            }
            composable("New Asset Change Point Activity/{assetName}") {
                it.arguments?.getString("assetName")?.let { assetName ->
                    Views.generateNewAssetChangePointView(navHostController, context, 0, assetName, false)
                }
            }
            composable("View Asset Change Point Activity/{assetName}/{transactionID}",
                listOf(
                    navArgument("assetName") {
                        type = NavType.StringType
                    },
                    navArgument("transactionID") {
                        type = NavType.IntType
                    }
                )
            ) {
                var transactionAssetName: String = ""
                var transactionHashCode: Int = 0
                it.arguments?.getString("assetName")?.let { assetName ->
                    transactionAssetName = assetName
                }
                it.arguments?.getInt("transactionID")?.let { transactionID ->
                    transactionHashCode = transactionID
                }
                Views.generateNewAssetChangePointView(navHostController, context, transactionHashCode, transactionAssetName, true)
            }
        }
    }
}