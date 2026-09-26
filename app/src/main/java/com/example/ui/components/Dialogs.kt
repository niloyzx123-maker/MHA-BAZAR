package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.TopUpPackage
import com.example.data.TournamentMatch
import com.example.data.UserProfile
import com.example.ui.theme.BKashPink
import com.example.ui.theme.CyberBgDark
import com.example.ui.theme.CyberBgElevated
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
fun JoinMatchDialog(
    match: TournamentMatch,
    userProfile: UserProfile,
    onDismiss: () -> Unit,
    onConfirm: (ign: String, uid: String) -> Unit
) {
    var ign by remember { mutableStateOf(userProfile.defaultGameIgn) }
    var uid by remember { mutableStateOf(userProfile.defaultGameUid) }
    val isFree = match.entryFee == 0
    val canAfford = isFree || (userProfile.totalBalance >= match.entryFee)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            color = CyberCard
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REGISTER FOR MATCH",
                        color = CyberCyan,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = match.title,
                    color = CyberTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${match.gameCategory} • ${match.mode} • ${match.map}",
                    color = CyberTextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                // Cost & Balance box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberBgDark, RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Entry Fee", color = CyberTextMuted, fontSize = 11.sp)
                        Text(
                            text = if (isFree) "FREE ENTRY" else "৳${match.entryFee}",
                            color = if (isFree) CyberGreen else CyberGold,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Your Balance", color = CyberTextMuted, fontSize = 11.sp)
                        Text(
                            text = "৳${userProfile.totalBalance.toInt()}",
                            color = if (canAfford) CyberCyan else CyberRed,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (!canAfford) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⚠ Insufficient balance. Please deposit money first.",
                        color = CyberRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("In-Game Name (IGN)", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = ign,
                    onValueChange = { ign = it },
                    placeholder = { Text("e.g. SHADOW_KILLER", color = CyberTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_ign")
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("Game UID / Character ID", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = uid,
                    onValueChange = { uid = it },
                    placeholder = { Text("e.g. 892341209", color = CyberTextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_uid")
                )

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (ign.isNotBlank() && uid.isNotBlank()) {
                            onConfirm(ign.trim(), uid.trim())
                        }
                    },
                    enabled = canAfford && ign.isNotBlank() && uid.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_confirm_join")
                ) {
                    Text(
                        text = if (isFree) "JOIN FREE MATCH" else "CONFIRM & PAY ৳${match.entryFee}",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MatchDetailsDialog(
    match: TournamentMatch,
    isJoined: Boolean,
    slotNumber: Int?,
    onDismiss: () -> Unit,
    onJoinClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            color = CyberCard
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MATCH DETAILS",
                        color = CyberCyan,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(match.title, color = CyberTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${match.gameCategory} • ${match.mode} • Map: ${match.map}",
                    color = CyberTextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = "Time: ${match.matchTime}",
                    color = CyberGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(14.dp))
                // Prize Distribution Matrix
                Text("PRIZE POOL BREAKDOWN", color = CyberTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberBgDark, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("🥇 1st Place", color = CyberGold, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text("৳${match.firstPrize}", color = CyberGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    if (match.secondPrize > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("🥈 2nd Place", color = CyberTextPrimary, fontSize = 13.sp)
                            Text("৳${match.secondPrize}", color = CyberTextPrimary, fontSize = 13.sp)
                        }
                    }
                    if (match.thirdPrize > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("🥉 3rd Place", color = CyberTextSecondary, fontSize = 13.sp)
                            Text("৳${match.thirdPrize}", color = CyberTextSecondary, fontSize = 13.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("⚔ Per Kill Reward", color = CyberCyan, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text("৳${match.perKillPrize} / Kill", color = CyberCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                // Room credentials area
                if (isJoined) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberCyan.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                            .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.VpnKey, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (match.roomRevealed) "ROOM DETAILS (ASSIGNED SLOT #$slotNumber)" else "ROOM IS CURRENTLY LOCKED 🔒",
                                    color = CyberCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (match.roomRevealed && !match.roomId.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                CopyableTextRow("Room ID", match.roomId, tag = "copy_detail_room_id")
                                Spacer(modifier = Modifier.height(6.dp))
                                CopyableTextRow("Password", match.roomPassword ?: "None", tag = "copy_detail_room_pass")
                            } else {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Room ID and Password will automatically unlock 15 minutes before the match start time. Stay tuned!",
                                    color = CyberTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberBgDark, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = CyberGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Join this match to unlock Room ID & Password!",
                                color = CyberTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("RULES & FAIR PLAY", color = CyberTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = match.rules,
                    color = CyberTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(18.dp))
                if (!isJoined && match.status != "COMPLETED") {
                    Button(
                        onClick = {
                            onDismiss()
                            onJoinClick()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_detail_join")
                    ) {
                        Text(
                            text = if (match.entryFee == 0) "JOIN FREE MATCH" else "REGISTER NOW (৳${match.entryFee})",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberBgElevated),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text("CLOSE", color = CyberTextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun DepositDialog(
    onDismiss: () -> Unit,
    onSubmit: (amount: Double, method: String, senderNumber: String, trxId: String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("bKash") }
    var amountText by remember { mutableStateOf("100") }
    var senderNumber by remember { mutableStateOf("") }
    var trxId by remember { mutableStateOf("") }

    val officialNumber = when (selectedMethod) {
        "bKash" -> "01755123456"
        "Nagad" -> "01855123456"
        else -> "01955123456"
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            color = CyberCard
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DEPOSIT MONEY",
                        color = CyberCyan,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Select Mobile Banking Method", color = CyberTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("bKash" to BKashPink, "Nagad" to NagadOrange, "Rocket" to RocketPurple).forEach { (method, color) ->
                        val isSelected = selectedMethod == method
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) color.copy(alpha = 0.25f) else CyberBgDark)
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.8.dp,
                                    color = if (isSelected) color else CyberCardBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedMethod = method }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = method,
                                color = if (isSelected) color else CyberTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                // Step 1: Send money to official number
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberBgDark, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "1. Send Money (Personal) to this number:",
                            color = CyberTextSecondary,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        CopyableTextRow("$selectedMethod Number", officialNumber, tag = "copy_deposit_official")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Deposit Amount (BDT)", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_deposit_amount")
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("Your $selectedMethod Phone Number", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = senderNumber,
                    onValueChange = { senderNumber = it },
                    placeholder = { Text("01XXXXXXXXX", color = CyberTextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_deposit_sender")
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("Transaction ID (TrxID)", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = trxId,
                    onValueChange = { trxId = it },
                    placeholder = { Text("e.g. 9K2L8MXP0", color = CyberTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_deposit_trxid")
                )

                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = {
                        val amount = amountText.toDoubleOrNull() ?: 0.0
                        if (amount >= 10 && senderNumber.isNotBlank() && trxId.isNotBlank()) {
                            onSubmit(amount, selectedMethod, senderNumber.trim(), trxId.trim())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_deposit")
                ) {
                    Text(
                        text = "SUBMIT DEPOSIT REQUEST",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WithdrawDialog(
    userProfile: UserProfile,
    onDismiss: () -> Unit,
    onSubmit: (amount: Double, method: String, receiverNumber: String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("bKash") }
    var amountText by remember { mutableStateOf("100") }
    var receiverNumber by remember { mutableStateOf(userProfile.phone) }

    val amount = amountText.toDoubleOrNull() ?: 0.0
    val canWithdraw = amount >= 50 && amount <= userProfile.winningBalance && receiverNumber.length >= 11

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyberGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            color = CyberCard
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "WITHDRAW WINNINGS",
                        color = CyberGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                // Balance display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberBgDark, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Withdrawable Balance", color = CyberTextMuted, fontSize = 11.sp)
                            Text("৳${userProfile.winningBalance.toInt()}", color = CyberGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Minimum Limit", color = CyberTextMuted, fontSize = 11.sp)
                            Text("৳50 BDT", color = CyberGold, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Select Method", color = CyberTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("bKash" to BKashPink, "Nagad" to NagadOrange).forEach { (method, color) ->
                        val isSelected = selectedMethod == method
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) color.copy(alpha = 0.25f) else CyberBgDark)
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.8.dp,
                                    color = if (isSelected) color else CyberCardBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedMethod = method }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = method,
                                color = if (isSelected) color else CyberTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Withdrawal Amount (BDT)", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberGold,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_withdraw_amount")
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("$selectedMethod Account Number", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = receiverNumber,
                    onValueChange = { receiverNumber = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberGold,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_withdraw_number")
                )

                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = {
                        if (canWithdraw) {
                            onSubmit(amount, selectedMethod, receiverNumber.trim())
                        }
                    },
                    enabled = canWithdraw,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberGold),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_withdraw")
                ) {
                    Text(
                        text = "INSTANT WITHDRAW ৳${amount.toInt()}",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TopUpCheckoutDialog(
    pack: TopUpPackage,
    userProfile: UserProfile,
    onDismiss: () -> Unit,
    onSubmit: (pack: TopUpPackage, uid: String, name: String, method: String, sender: String?, trx: String?) -> Unit
) {
    var playerUid by remember { mutableStateOf(userProfile.defaultGameUid) }
    var playerName by remember { mutableStateOf(userProfile.defaultGameIgn) }
    var paymentOption by remember { mutableStateOf(if (userProfile.totalBalance >= pack.priceBdt) "Wallet" else "bKash") }
    var senderNumber by remember { mutableStateOf("") }
    var trxId by remember { mutableStateOf("") }

    val officialNumber = if (paymentOption == "Nagad") "01855123456" else "01755123456"

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            color = CyberCard
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("CHECKOUT TOP-UP", color = CyberCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                // Package Summary Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberBgDark, RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(pack.game, color = CyberTextMuted, fontSize = 11.sp)
                        Text(pack.title, color = CyberTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("৳${pack.priceBdt.toInt()}", color = CyberCyan, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Player UID", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = playerUid,
                    onValueChange = { playerUid = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("e.g. 192348571", color = CyberTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_topup_uid")
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("Player / Character Name (Optional)", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it },
                    placeholder = { Text("In-game name", color = CyberTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text("Select Payment Method", color = CyberTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Wallet", "bKash", "Nagad").forEach { option ->
                        val isSelected = paymentOption == option
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CyberCyan.copy(alpha = 0.2f) else CyberBgDark)
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.8.dp,
                                    color = if (isSelected) CyberCyan else CyberCardBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { paymentOption = option }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (option == "Wallet") "Wallet (৳${userProfile.totalBalance.toInt()})" else option,
                                color = if (isSelected) CyberCyan else CyberTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                if (paymentOption != "Wallet") {
                    Spacer(modifier = Modifier.height(10.dp))
                    CopyableTextRow("$paymentOption Number", officialNumber, tag = "copy_topup_official")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = senderNumber,
                        onValueChange = { senderNumber = it },
                        placeholder = { Text("Sender phone number", color = CyberTextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CyberTextPrimary,
                            unfocusedTextColor = CyberTextPrimary,
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = CyberCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = trxId,
                        onValueChange = { trxId = it },
                        placeholder = { Text("Transaction ID (TrxID)", color = CyberTextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CyberTextPrimary,
                            unfocusedTextColor = CyberTextPrimary,
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = CyberCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = {
                        if (playerUid.isNotBlank()) {
                            onSubmit(pack, playerUid.trim(), playerName.trim(), paymentOption, senderNumber, trxId)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_topup")
                ) {
                    Text("CONFIRM ORDER ৳${pack.priceBdt.toInt()}", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileDialog(
    userProfile: UserProfile,
    onDismiss: () -> Unit,
    onToggleAdmin: (Boolean) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            color = CyberCard
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("PLAYER PROFILE", color = CyberCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                // Avatar & Name Card
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberBgDark, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(CyberCyan.copy(alpha = 0.2f), RoundedCornerShape(25.dp))
                            .border(1.5.dp, CyberCyan, RoundedCornerShape(25.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(28.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(userProfile.name, color = CyberTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("IGN: ${userProfile.defaultGameIgn}", color = CyberCyan, fontSize = 12.sp)
                        Text("UID: ${userProfile.defaultGameUid}", color = CyberTextMuted, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                // Stats Grid
                Text("CAREER STATS", color = CyberTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(CyberBgDark, RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${userProfile.matchesPlayed}", color = CyberCyan, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("Matches", color = CyberTextMuted, fontSize = 11.sp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(CyberBgDark, RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${userProfile.totalKills}", color = CyberGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("Total Kills", color = CyberTextMuted, fontSize = 11.sp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(CyberBgDark, RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${userProfile.totalWins}", color = CyberGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("Wins \uD83C\uDFC6", color = CyberTextMuted, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                // Admin Toggle Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberBgDark, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Admin Mode Control", color = CyberTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Manage rooms, winners & verify deposits", color = CyberTextMuted, fontSize = 11.sp)
                    }
                    Switch(
                        checked = userProfile.isAdmin,
                        onCheckedChange = onToggleAdmin,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CyberCyan,
                            checkedTrackColor = CyberCyan.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.testTag("switch_admin_mode")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberBgElevated),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CLOSE", color = CyberTextPrimary)
                }
            }
        }
    }
}
