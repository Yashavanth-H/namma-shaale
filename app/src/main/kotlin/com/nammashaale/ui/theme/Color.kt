package com.nammashaale.ui.theme

import androidx.compose.ui.graphics.Color

val PrimaryBlue = Color(0xFF1565C0)
val SecondaryTeal = Color(0xFF26A69A)
val BackgroundGray = Color(0xFFF5F7FA)

// Status Colors
val StatusWorking = Color(0xFF4CAF50)
val StatusNeedsRepair = Color(0xFFFF9800)
val StatusBroken = Color(0xFFF44336)
val StatusMissing = Color(0xFF9E9E9E)

val LightColors = androidx.compose.material3.lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryTeal,
    background = BackgroundGray,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)
