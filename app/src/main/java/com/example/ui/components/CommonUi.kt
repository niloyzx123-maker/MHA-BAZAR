package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BKashPink
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGold
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberRed
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.RocketPurple

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberCardBorder,
    glow: Boolean = false,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .border(
                width = if (glow) 1.5.dp else 1.dp,
                color = if (glow) CyberCyan else borderColor,
                shape = RoundedCornerShape(14.dp)
            )
            .clip(RoundedCornerShape(14.dp)),
        color = CyberCard,
        content = content
    )
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status.uppercase()) {
        "APPROVED", "COMPLETED", "RESOLVED", "UPCOMING" -> Triple(
            CyberGreen.copy(alpha = 0.18f),
            CyberGreen,
            if (status.uppercase() == "UPCOMING") "LIVE SOON" else status
        )
        "PENDING", "OPEN" -> Triple(
            CyberGold.copy(alpha = 0.18f),
            CyberGold,
            status
        )
        "REJECTED" -> Triple(
            CyberRed.copy(alpha = 0.18f),
            CyberRed,
            status
        )
        "ONGOING" -> Triple(
            CyberCyan.copy(alpha = 0.2f),
            CyberCyan,
            "ONGOING"
        )
        else -> Triple(
            CyberTextMuted.copy(alpha = 0.2f),
            CyberTextSecondary,
            status
        )
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(6.dp))
            .border(0.5.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun PaymentMethodBadge(
    method: String,
    modifier: Modifier = Modifier
) {
    val (color, name) = when {
        method.contains("bKash", ignoreCase = true) -> Pair(BKashPink, "bKash")
        method.contains("Nagad", ignoreCase = true) -> Pair(NagadOrange, "Nagad")
        method.contains("Rocket", ignoreCase = true) -> Pair(RocketPurple, "Rocket")
        else -> Pair(CyberCyan, "Wallet")
    }

    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.18f), RoundedCornerShape(6.dp))
            .border(0.8.dp, color.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
            .padding(horizontal = 7.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CopyableTextRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    tag: String = "copy_row"
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .testTag(tag)
            .background(Color(0xFF090D17), RoundedCornerShape(8.dp))
            .border(1.dp, CyberCyan.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
            .clickable {
                copyToClipboard(context, label, value)
            }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: ",
            color = CyberTextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = CyberCyan,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy $label",
            tint = CyberCyan,
            modifier = Modifier.size(16.dp)
        )
    }
}

fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "$label copied to clipboard!", Toast.LENGTH_SHORT).show()
}

@Composable
fun CyberSectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .size(width = 4.dp, height = 20.dp)
                .background(
                    Brush.verticalGradient(listOf(CyberCyan, CyberGold)),
                    RoundedCornerShape(2.dp)
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = title,
                color = CyberTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.3.sp
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = subtitle,
                    color = CyberTextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        if (action != null) {
            action()
        }
    }
}
