package com.nammashaale.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object AssetList : Screen("asset_list")
    object AddAsset : Screen("add_asset")
    object AssetDetails : Screen("asset_details/{assetId}") {
        fun createRoute(assetId: Int) = "asset_details/$assetId"
    }
    object Audit : Screen("audit")
    object Repair : Screen("repair")
    object Reports : Screen("reports")
    object Profile : Screen("profile")
    object AiAssistant : Screen("ai_assistant")
}
