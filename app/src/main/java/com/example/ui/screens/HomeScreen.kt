package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.MatchParticipant
import com.example.data.TournamentMatch
import com.example.data.UserProfile
import com.example.ui.AppTab
import com.example.ui.components.CopyableTextRow
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberSectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyberBg
import com.example.ui.theme.CyberBgDark
import com.example.ui.theme.CyberBgElevated
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGold
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberRed
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.FreeFireOrange

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    matches: List<TournamentMatch>,
    participants: List<MatchParticipant>,
    onNavigateTab: (AppTab) -> Unit,
    onCategorySelected: (String) -> Unit,
    onOpenDeposit: () -> Unit,
    onOpenWithdraw: () -> Unit,
    onOpenProfile: () -> Unit,
    onJoinMatch: (TournamentMatch) -> Unit,
    onViewMatchDetails: (TournamentMatch) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val upcomingMatches = matches.filter { it.status == "UPCOMING" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onOpenProfile() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.2f))
                            .border(1.5.dp, CyberCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(38.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ARENAZONE",
                                color = CyberCyan,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ESPORTS",
                                color = CyberGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = userProfile.defaultGameIgn,
                            color = CyberTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Balance Pill + Profile Icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(CyberBgElevated)
                            .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .clickable { onNavigateTab(AppTab.WALLET) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "৳${userProfile.totalBalance.toInt()}",
                                color = CyberTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onOpenProfile,
                        modifier = Modifier
                            .size(36.dp)
                            .background(CyberBgElevated, CircleShape)
                            .border(1.dp, CyberCardBorder, CircleShape)
                    ) {
                        Icon(
                            if (userProfile.isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = if (userProfile.isAdmin) CyberGold else CyberTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Hero Tournament Banner
        item {
            CyberCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner"),
                borderColor = CyberCyan.copy(alpha = 0.5f),
                glow = true
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.esports_hero_banner),
                        contentDescription = "Esports Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        CyberBg.copy(alpha = 0.7f),
                                        CyberBgDark
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(CyberPink, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "🔥 BANGLADESH #1 TOURNAMENTS",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Play Scrims & Win bKash / Nagad",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.3.sp
                        )
                        Text(
                            text = "Daily Free Fire & BGMI Matches • Instant Room Unlock",
                            color = CyberCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onNavigateTab(AppTab.MATCHES) },
                                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text(
                                    "BROWSE MATCHES",
                                    color = Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = { onNavigateTab(AppTab.TOPUP) },
                                colors = ButtonDefaults.buttonColors(containerColor = CyberBgElevated),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .height(36.dp)
                                    .border(1.dp, CyberGold, RoundedCornerShape(8.dp))
                            ) {
                                Text(
                                    "TOP-UP STORE",
                                    color = CyberGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Wallet Quick Action Card
        item {
            CyberCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CyberCardBorder
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Balance", color = CyberTextMuted, fontSize = 11.sp)
                            Text(
                                "৳${userProfile.totalBalance.toInt()}",
                                color = CyberCyan,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Deposit", color = CyberTextMuted, fontSize = 11.sp)
                                Text("৳${userProfile.depositBalance.toInt()}", color = CyberTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Withdrawable", color = CyberTextMuted, fontSize = 11.sp)
                                Text("৳${userProfile.winningBalance.toInt()}", color = CyberGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onOpenDeposit,
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .border(1.dp, CyberCyan, RoundedCornerShape(8.dp))
                                .testTag("btn_home_deposit")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Deposit Money", color = CyberCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = onOpenWithdraw,
                            colors = ButtonDefaults.buttonColors(containerColor = CyberGold.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .border(1.dp, CyberGold, RoundedCornerShape(8.dp))
                                .testTag("btn_home_withdraw")
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = CyberGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Withdraw", color = CyberGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Game Category Filter Chips
        item {
            CyberSectionHeader("Tournament Categories", "Select Game")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val categories = listOf(
                    "All" to Icons.Default.SportsEsports,
                    "Free Fire" to Icons.Default.Bolt,
                    "BGMI" to Icons.Default.EmojiEvents,
                    "Clash Squad" to Icons.Default.SportsEsports,
                    "Lone Wolf" to Icons.Default.Bolt,
                    "Daily Free" to Icons.Default.Diamond
                )
                items(categories) { (cat, icon) ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(CyberBgElevated)
                            .border(1.dp, CyberCyan.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                            .clickable {
                                onCategorySelected(if (cat == "All") "ALL" else cat)
                                onNavigateTab(AppTab.MATCHES)
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(icon, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(cat, color = CyberTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Live & Upcoming Tournament Matches
        item {
            CyberSectionHeader(
                title = "Featured Matches",
                subtitle = "Join & Win",
                action = {
                    Row(
                        modifier = Modifier.clickable { onNavigateTab(AppTab.MATCHES) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("See All", color = CyberCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                    }
                }
            )
        }

        items(upcomingMatches.take(3)) { match ->
            val isJoined = participants.any { it.matchId == match.id && it.userUid == userProfile.id }
            val myParticipant = participants.find { it.matchId == match.id && it.userUid == userProfile.id }

            HomeMatchCard(
                match = match,
                isJoined = isJoined,
                slotNumber = myParticipant?.slotNumber,
                onJoin = { onJoinMatch(match) },
                onDetails = { onViewMatchDetails(match) }
            )
        }

        // Diamond & UC Top-Up Store Promo
        item {
            CyberCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateTab(AppTab.TOPUP) },
                borderColor = CyberGold.copy(alpha = 0.6f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(CyberGold.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .border(1.dp, CyberGold, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Diamond, contentDescription = null, tint = CyberGold, modifier = Modifier.size(28.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Top-Up Diamond & UC Store", color = CyberGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Free Fire 115 💎 @ ৳80 • BGMI 60 UC @ ৳95", color = CyberTextSecondary, fontSize = 11.sp)
                        Text("Instant Delivery via Player UID • bKash/Nagad", color = CyberCyan, fontSize = 10.sp)
                    }
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = CyberGold, modifier = Modifier.size(20.dp))
                }
            }
        }

        // WhatsApp & Telegram Support Banner
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Official Support Desk", color = CyberTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Need help joining custom room or deposit? Contact us 24/7", color = CyberTextSecondary, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/8801755123456"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(38.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/arenazone_esports"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0088CC)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(38.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Telegram", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeMatchCard(
    match: TournamentMatch,
    isJoined: Boolean,
    slotNumber: Int?,
    onJoin: () -> Unit,
    onDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    CyberCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = if (isJoined) CyberCyan else CyberCardBorder,
        glow = isJoined
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Category, Map, Status, Entry Fee
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (match.gameCategory.contains("Free Fire")) FreeFireOrange.copy(alpha = 0.2f) else CyberCyan.copy(alpha = 0.2f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            match.gameCategory,
                            color = if (match.gameCategory.contains("Free Fire")) FreeFireOrange else CyberCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("• ${match.map}", color = CyberTextMuted, fontSize = 11.sp)
                }

                Box(
                    modifier = Modifier
                        .background(if (match.entryFee == 0) CyberGreen.copy(alpha = 0.2f) else CyberGold.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (match.entryFee == 0) "FREE" else "৳${match.entryFee}",
                        color = if (match.entryFee == 0) CyberGreen else CyberGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(match.title, color = CyberTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text("Mode: ${match.mode} • Time: ${match.matchTime}", color = CyberTextSecondary, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(10.dp))
            // 3-Pillar Matrix: Prize Pool, 1st Prize, Per Kill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberBgDark, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Prize Pool", color = CyberTextMuted, fontSize = 10.sp)
                    Text("৳${match.totalPrizePool}", color = CyberGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("1st Prize", color = CyberTextMuted, fontSize = 10.sp)
                    Text("৳${match.firstPrize}", color = CyberTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Per Kill", color = CyberTextMuted, fontSize = 10.sp)
                    Text("৳${match.perKillPrize}", color = CyberCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            // Slots Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Slots: ${match.joinedSlots}/${match.maxSlots}",
                    color = CyberTextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = "${match.maxSlots - match.joinedSlots} left",
                    color = CyberCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { (match.joinedSlots.toFloat() / match.maxSlots.toFloat()).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CyberCyan,
                trackColor = CyberBgDark
            )

            // If user joined: show Room credentials Box!
            if (isJoined) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberCyan.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "REGISTERED (SLOT #$slotNumber)",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (match.roomRevealed && !match.roomId.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            CopyableTextRow("Room ID", match.roomId, tag = "copy_home_room_id")
                            Spacer(modifier = Modifier.height(4.dp))
                            CopyableTextRow("Password", match.roomPassword ?: "None", tag = "copy_home_room_pass")
                        } else {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "🔒 Room ID & Password will unlock 15m before match.",
                                color = CyberTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onDetails,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberBgElevated),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
                ) {
                    Text("Details", color = CyberTextSecondary, fontSize = 12.sp)
                }

                if (!isJoined && match.status != "COMPLETED") {
                    Button(
                        onClick = onJoin,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .testTag("btn_join_match_${match.id}")
                    ) {
                        Text(
                            text = if (match.entryFee == 0) "JOIN FREE" else "JOIN (৳${match.entryFee})",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
