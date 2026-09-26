package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TopUpOrder
import com.example.data.TopUpPackage
import com.example.data.UserProfile
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberSectionHeader
import com.example.ui.components.PaymentMethodBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyberBg
import com.example.ui.theme.CyberBgDark
import com.example.ui.theme.CyberBgElevated
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGold
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.FreeFireOrange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TopUpScreen(
    packages: List<TopUpPackage>,
    orders: List<TopUpOrder>,
    userProfile: UserProfile,
    currentGame: String,
    onGameChange: (String) -> Unit,
    onSelectPackage: (TopUpPackage) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredPackages = packages.filter { it.game.equals(currentGame, ignoreCase = true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            CyberSectionHeader(
                title = "DIAMOND & UC STORE",
                subtitle = "Instant In-Game Top-Up"
            )
        }

        // Game selector tabs
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberBgDark, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Free Fire", "BGMI").forEach { game ->
                    val isSelected = currentGame.equals(game, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) (if (game == "Free Fire") FreeFireOrange.copy(alpha = 0.25f) else CyberCyan.copy(alpha = 0.25f)) else Color.Transparent)
                            .border(
                                width = if (isSelected) 1.5.dp else 0.dp,
                                color = if (isSelected) (if (game == "Free Fire") FreeFireOrange else CyberCyan) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onGameChange(game) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (game == "Free Fire") Icons.Default.Bolt else Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = if (isSelected) (if (game == "Free Fire") FreeFireOrange else CyberCyan) else CyberTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (game == "Free Fire") "Free Fire Diamonds" else "BGMI UC Pack",
                                color = if (isSelected) CyberTextPrimary else CyberTextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Trust badge banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberCyan.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                    .border(1.dp, CyberCyan.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("⚡ 5-Minute Instant UID Delivery", color = CyberCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("100% safe & official top-up. No login or password required.", color = CyberTextSecondary, fontSize = 11.sp)
                    }
                }
            }
        }

        // Packages Grid/List
        item {
            Text("SELECT PACKAGE", color = CyberTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        // Two items per row or list cards
        items(filteredPackages.chunked(2)) { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                pair.forEach { pack ->
                    Box(modifier = Modifier.weight(1f)) {
                        PackageCard(
                            pack = pack,
                            onBuy = { onSelectPackage(pack) }
                        )
                    }
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Recent Top-Up Orders section
        if (orders.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                CyberSectionHeader(
                    title = "Recent Top-Up Orders",
                    subtitle = "History"
                )
            }

            items(orders.take(5)) { order ->
                OrderCard(order = order)
            }
        }
    }
}

@Composable
fun PackageCard(
    pack: TopUpPackage,
    onBuy: () -> Unit,
    modifier: Modifier = Modifier
) {
    CyberCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onBuy() },
        borderColor = if (pack.badge != null) CyberGold.copy(alpha = 0.7f) else CyberCardBorder
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Badge
            if (pack.badge != null) {
                Box(
                    modifier = Modifier
                        .background(CyberGold, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = pack.badge.uppercase(),
                        color = Color.Black,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            } else {
                Spacer(modifier = Modifier.height(17.dp))
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(CyberCyan.copy(alpha = 0.15f), RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (pack.type) {
                        "membership" -> Icons.Default.Star
                        "pass" -> Icons.Default.EmojiEvents
                        else -> Icons.Default.Diamond
                    },
                    contentDescription = null,
                    tint = CyberCyan,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pack.itemCount,
                color = CyberTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = pack.title,
                color = CyberTextSecondary,
                fontSize = 11.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "৳${pack.priceBdt.toInt()}",
                color = CyberGold,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onBuy,
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .testTag("btn_buy_topup_${pack.id}")
            ) {
                Text("BUY NOW", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun OrderCard(
    order: TopUpOrder,
    modifier: Modifier = Modifier
) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(order.timestamp))

    CyberCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = CyberCardBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(order.packageTitle, color = CyberTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("UID: ${order.playerUid} • $dateStr", color = CyberTextMuted, fontSize = 11.sp)
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    PaymentMethodBadge(order.paymentMethod)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("৳${order.amountBdt.toInt()}", color = CyberCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                StatusBadge(order.status)
            }
        }
    }
}
