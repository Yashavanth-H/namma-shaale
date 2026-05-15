package com.nammashaale.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nammashaale.ui.theme.StatusBroken
import com.nammashaale.ui.theme.StatusMissing
import com.nammashaale.ui.theme.StatusNeedsRepair
import com.nammashaale.ui.theme.StatusWorking

@Composable
fun StatusChip(status: String, modifier: Modifier = Modifier) {
    val backgroundColor = when (status) {
        "Working" -> StatusWorking.copy(alpha = 0.15f)
        "Needs Repair" -> StatusNeedsRepair.copy(alpha = 0.15f)
        "Broken" -> StatusBroken.copy(alpha = 0.15f)
        "Missing" -> StatusMissing.copy(alpha = 0.15f)
        else -> Color.Gray.copy(alpha = 0.15f)
    }
    
    val textColor = when (status) {
        "Working" -> StatusWorking
        "Needs Repair" -> StatusNeedsRepair
        "Broken" -> StatusBroken
        "Missing" -> StatusMissing
        else -> Color.Gray
    }

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}
