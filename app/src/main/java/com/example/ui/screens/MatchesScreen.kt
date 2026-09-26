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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.MatchParticipant
import com.example.data.TournamentMatch
import com.example.data.UserProfile
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
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.FreeFireOrange

@Composable
fun MatchesScreen(
    matches: List<TournamentMatch>,
    participants: List<MatchParticipant>,
    userProfile: UserProfile,
    currentFilter: String,
    onFilterChange: (String) -> Unit,
    onJoinMatch: (TournamentMatch) -> Unit,
    onViewMatchDetails: (TournamentMatch) -> Unit,
    modifier: Modifier = Modifier
) {
    val myJoinedMatchIds = participants.filter { it.userUid == userProfile.id }.map { it.matchId }.toSet()

    val filteredMatches = matches.filter { match ->
        when (currentFilter) {
            "ALL" -> true
            "MY_JOINED" -> myJoinedMatchIds.contains(match.id)
            else -> match.gameCategory.equals(currentFilter, ignoreCase = true)
        }
    }

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
                title = "TOURNAMENT HUB",
                subtitle = "${filteredMatches.size} Matches Found"
            )
        }

        // Filter chips bar
        item {
            val filters = listOf(
                "ALL" to "All Matches",
                "MY_JOINED" to "Joined (${myJoinedMatchIds.size})",
                "Free Fire" to "Free Fire",
                "BGMI" to "BGMI / PUBG",
                "Clash Squad" to "CS 4v4",
                "Lone Wolf" to "Lone Wolf",
                "Daily Free" to "Daily Free"
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filters) { (key, label) ->
                    val isSelected = currentFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) CyberCyan.copy(alpha = 0.2f) else CyberBgElevated)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) CyberCyan else CyberCardBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { onFilterChange(key) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) CyberCyan else CyberTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        if (filteredMatches.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No matches found in this category", color = CyberTextSecondary, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { onFilterChange("ALL") },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                        ) {
                            Text("VIEW ALL MATCHES", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        items(filteredMatches, key = { it.id }) { match ->
            val isJoined = myJoinedMatchIds.contains(match.id)
            val participant = participants.find { it.matchId == match.id && it.userUid == userProfile.id }

            MatchCard(
                match = match,
                isJoined = isJoined,
                slotNumber = participant?.slotNumber,
                onJoin = { onJoinMatch(match) },
                onDetails = { onViewMatchDetails(match) }
            )
        }
    }
}

@Composable
fun MatchCard(
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
            // Header Row
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadge(match.status)
                    Spacer(modifier = Modifier.width(6.dp))
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
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(match.title, color = CyberTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text("${match.mode} • Time: ${match.matchTime}", color = CyberTextSecondary, fontSize = 12.sp)

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
                    Text("Total Prize", color = CyberTextMuted, fontSize = 10.sp)
                    Text("৳${match.totalPrizePool}", color = CyberGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("1st Place", color = CyberTextMuted, fontSize = 10.sp)
                    Text("৳${match.firstPrize}", color = CyberTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Per Kill", color = CyberTextMuted, fontSize = 10.sp)
                    Text("৳${match.perKillPrize}", color = CyberCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            // Slot progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Joined Slots: ${match.joinedSlots}/${match.maxSlots}", color = CyberTextSecondary, fontSize = 11.sp)
                Text(
                    text = if (match.joinedSlots >= match.maxSlots) "SLOTS FULL" else "${match.maxSlots - match.joinedSlots} slots open",
                    color = if (match.joinedSlots >= match.maxSlots) CyberGold else CyberCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { (match.joinedSlots.toFloat() / match.maxSlots.toFloat()).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CyberCyan,
                trackColor = CyberBgDark
            )

            // Winner badge if completed
            if (match.status == "COMPLETED" && !match.winnerIgn.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberGold.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                        .border(1.dp, CyberGold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = CyberGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("WINNER: ${match.winnerIgn}", color = CyberGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Kills: ${match.winnerKills ?: 0} • Prize Disbursed", color = CyberTextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Room Credentials Box (if joined)
            if (isJoined && match.status != "COMPLETED") {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberCyan.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .border(1.dp, CyberCyan.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "REGISTERED • YOUR ASSIGNED SLOT: #$slotNumber",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (match.roomRevealed && !match.roomId.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            CopyableTextRow("Room ID", match.roomId, tag = "copy_match_room_id_${match.id}")
                            Spacer(modifier = Modifier.height(4.dp))
                            CopyableTextRow("Password", match.roomPassword ?: "None", tag = "copy_match_room_pass_${match.id}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "⚡ Open game -> Custom Room -> Enter ID/Pass -> Sit in slot #$slotNumber",
                                color = CyberGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = CyberGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Room ID & Pass will be revealed 15 minutes before start.",
                                    color = CyberTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
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
                    Text("Rules & Details", color = CyberTextSecondary, fontSize = 12.sp)
                }

                if (!isJoined && match.status != "COMPLETED") {
                    val isFull = match.joinedSlots >= match.maxSlots
                    Button(
                        onClick = onJoin,
                        enabled = !isFull,
                        colors = ButtonDefaults.buttonColors(containerColor = if (isFull) CyberCardBorder else CyberCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .testTag("btn_matches_join_${match.id}")
                    ) {
                        Text(
                            text = if (isFull) "FULL" else if (match.entryFee == 0) "JOIN FREE" else "JOIN NOW (৳${match.entryFee})",
                            color = if (isFull) CyberTextMuted else Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
