package com.shamtube.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val ShamTypography = Typography().run {
    copy(
        headlineLarge = headlineLarge.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold),
        headlineMedium = headlineMedium.copy(fontSize = 23.sp, fontWeight = FontWeight.Bold),
        titleLarge = titleLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        titleMedium = titleMedium.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
        bodyLarge = bodyLarge.copy(fontSize = 16.sp),
        bodyMedium = bodyMedium.copy(fontSize = 14.sp),
        labelLarge = labelLarge.copy(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
    )
}