package com.example.appinterfaces.ui

import com.example.appinterfaces.ui.contact.form.ContactFormScreen
import com.example.appinterfaces.ui.contact.list.ContactListScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppContactNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "list"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = "list") {
            ContactListScreen(
                onAddPressed = {
                    navController.navigate("form")
                },
                onContactPressed = { contact ->
                    navController.navigate("form?id=${contact.id}")
                }
            )
        }
        composable(
            route = "form?id={id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            ContactFormScreen(
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}