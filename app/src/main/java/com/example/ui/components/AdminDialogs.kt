package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.TournamentMatch
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGold
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary

@Composable
fun PublishRoomDialog(
    match: TournamentMatch,
    onDismiss: () -> Unit,
    onPublish: (matchId: String, roomId: String, pass: String) -> Unit
) {
    var roomId by remember { mutableStateOf(match.roomId ?: "") }
    var password by remember { mutableStateOf(match.roomPassword ?: "") }

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
                    Text("PUBLISH ROOM CREDENTIALS", color = CyberCyan, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(match.title, color = CyberTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("Players registered: ${match.joinedSlots}/${match.maxSlots}", color = CyberTextSecondary, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(14.dp))
                Text("Custom Room ID", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = roomId,
                    onValueChange = { roomId = it },
                    placeholder = { Text("e.g. 7821940", color = CyberTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_admin_room_id")
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("Room Password", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("e.g. 1234", color = CyberTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_admin_room_pass")
                )

                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = {
                        if (roomId.isNotBlank() && password.isNotBlank()) {
                            onPublish(match.id, roomId.trim(), password.trim())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_publish_room")
                ) {
                    Text("PUBLISH TO ALL PLAYERS", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DeclareWinnerDialog(
    match: TournamentMatch,
    onDismiss: () -> Unit,
    onDeclare: (matchId: String, winnerIgn: String, kills: Int) -> Unit
) {
    var winnerIgn by remember { mutableStateOf("") }
    var killsText by remember { mutableStateOf("5") }

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
                    Text("DECLARE WINNER & PRIZE", color = CyberGold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(match.title, color = CyberTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("1st Prize: ৳${match.firstPrize} • Per Kill: ৳${match.perKillPrize}", color = CyberCyan, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(14.dp))
                Text("Winner Player IGN", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = winnerIgn,
                    onValueChange = { winnerIgn = it },
                    placeholder = { Text("e.g. SHADOW_KILLER", color = CyberTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberGold,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_winner_ign")
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("Total Kills by Winner", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = killsText,
                    onValueChange = { killsText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberGold,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_winner_kills")
                )

                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = {
                        val kills = killsText.toIntOrNull() ?: 0
                        if (winnerIgn.isNotBlank()) {
                            onDeclare(match.id, winnerIgn.trim(), kills)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberGold),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_confirm_winner")
                ) {
                    Text("DISTRIBUTE PRIZE & FINISH", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CreateMatchDialog(
    onDismiss: () -> Unit,
    onCreate: (TournamentMatch) -> Unit
) {
    var title by remember { mutableStateOf("Free Fire Elite Scrims") }
    var gameCategory by remember { mutableStateOf("Free Fire") }
    var mode by remember { mutableStateOf("Battle Royale Solo") }
    var map by remember { mutableStateOf("Bermuda") }
    var matchTime by remember { mutableStateOf("Tonight, 10:00 PM") }
    var entryFeeText by remember { mutableStateOf("30") }
    var perKillText by remember { mutableStateOf("15") }
    var totalPrizeText by remember { mutableStateOf("800") }
    var firstPrizeText by remember { mutableStateOf("400") }
    var maxSlotsText by remember { mutableStateOf("48") }

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
                    Text("CREATE NEW MATCH", color = CyberCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("Match Title", color = CyberTextSecondary, fontSize = 12.sp)
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
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
                Text("Category (Free Fire / BGMI / Clash Squad / Lone Wolf / Daily Free)", color = CyberTextSecondary, fontSize = 11.sp)
                OutlinedTextField(
                    value = gameCategory,
                    onValueChange = { gameCategory = it },
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
                Text("Mode & Map", color = CyberTextSecondary, fontSize = 11.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = mode,
                        onValueChange = { mode = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CyberTextPrimary,
                            unfocusedTextColor = CyberTextPrimary,
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = CyberCardBorder
                        )
                    )
                    OutlinedTextField(
                        value = map,
                        onValueChange = { map = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CyberTextPrimary,
                            unfocusedTextColor = CyberTextPrimary,
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = CyberCardBorder
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Match Time", color = CyberTextSecondary, fontSize = 11.sp)
                OutlinedTextField(
                    value = matchTime,
                    onValueChange = { matchTime = it },
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Entry Fee (৳)", color = CyberTextSecondary, fontSize = 11.sp)
                        OutlinedTextField(
                            value = entryFeeText,
                            onValueChange = { entryFeeText = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = CyberTextPrimary,
                                unfocusedTextColor = CyberTextPrimary,
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = CyberCardBorder
                            )
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Per Kill (৳)", color = CyberTextSecondary, fontSize = 11.sp)
                        OutlinedTextField(
                            value = perKillText,
                            onValueChange = { perKillText = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = CyberTextPrimary,
                                unfocusedTextColor = CyberTextPrimary,
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = CyberCardBorder
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("1st Prize (৳)", color = CyberTextSecondary, fontSize = 11.sp)
                        OutlinedTextField(
                            value = firstPrizeText,
                            onValueChange = { firstPrizeText = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = CyberTextPrimary,
                                unfocusedTextColor = CyberTextPrimary,
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = CyberCardBorder
                            )
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Max Slots", color = CyberTextSecondary, fontSize = 11.sp)
                        OutlinedTextField(
                            value = maxSlotsText,
                            onValueChange = { maxSlotsText = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = CyberTextPrimary,
                                unfocusedTextColor = CyberTextPrimary,
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = CyberCardBorder
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = {
                        val fee = entryFeeText.toIntOrNull() ?: 0
                        val kill = perKillText.toIntOrNull() ?: 0
                        val first = firstPrizeText.toIntOrNull() ?: 0
                        val total = totalPrizeText.toIntOrNull() ?: (first * 2)
                        val slots = maxSlotsText.toIntOrNull() ?: 48
                        val match = TournamentMatch(
                            id = "M-${System.currentTimeMillis().toString().takeLast(4)}",
                            title = title,
                            gameCategory = gameCategory,
                            mode = mode,
                            map = map,
                            matchTime = matchTime,
                            entryFee = fee,
                            perKillPrize = kill,
                            totalPrizePool = total,
                            firstPrize = first,
                            secondPrize = first / 2,
                            thirdPrize = first / 4,
                            maxSlots = slots,
                            joinedSlots = 0,
                            status = "UPCOMING"
                        )
                        onCreate(match)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("CREATE TOURNAMENT", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun NewTicketDialog(
    onDismiss: () -> Unit,
    onSubmit: (category: String, message: String) -> Unit
) {
    var category by remember { mutableStateOf("Match Room") }
    var message by remember { mutableStateOf("") }

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
                    Text("NEW SUPPORT TICKET", color = CyberCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Issue Category", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Match Room", "Deposit", "Withdrawal", "Top-Up").forEach { cat ->
                        val isSelected = category == cat
                        Button(
                            onClick = { category = cat },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) CyberCyan.copy(alpha = 0.25f) else Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    if (isSelected) CyberCyan else CyberCardBorder,
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            Text(
                                text = cat,
                                color = if (isSelected) CyberCyan else CyberTextSecondary,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Describe your problem in detail", color = CyberTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text("e.g. Room password didn't unlock / Sent bKash deposit but pending...", color = CyberTextMuted) },
                    minLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CyberTextPrimary,
                        unfocusedTextColor = CyberTextPrimary,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_ticket_message")
                )

                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = {
                        if (message.isNotBlank()) {
                            onSubmit(category, message.trim())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_submit_ticket")
                ) {
                    Text("SEND TICKET TO SUPPORT", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
