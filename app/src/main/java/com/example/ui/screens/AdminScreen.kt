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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TopUpOrder
import com.example.data.TournamentMatch
import com.example.data.UserProfile
import com.example.data.WalletTransaction
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
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberRed
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary

@Composable
fun AdminScreen(
    userProfile: UserProfile,
    matches: List<TournamentMatch>,
    transactions: List<WalletTransaction>,
    orders: List<TopUpOrder>,
    onToggleAdmin: (Boolean) -> Unit,
    onPublishRoomClick: (TournamentMatch) -> Unit,
    onDeclareWinnerClick: (TournamentMatch) -> Unit,
    onApproveDeposit: (String) -> Unit,
    onRejectDeposit: (String) -> Unit,
    onCompleteOrder: (String) -> Unit,
    onRejectOrder: (String) -> Unit,
    onCreateMatchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var adminSection by remember { mutableStateOf("MATCHES") }

    val pendingDeposits = transactions.filter { it.type == "DEPOSIT" && it.status == "PENDING" }
    val pendingOrders = orders.filter { it.status == "PENDING" }
    val activeMatches = matches.filter { it.status != "COMPLETED" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Admin Top Toggle Card
        item {
            CyberCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (userProfile.isAdmin) CyberGold else CyberCardBorder,
                glow = userProfile.isAdmin
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(CyberGold.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = CyberGold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("ADMIN CONTROL PANEL", color = CyberGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (userProfile.isAdmin) "Admin privileges active" else "Toggle ON for manager actions",
                                color = CyberTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Switch(
                        checked = userProfile.isAdmin,
                        onCheckedChange = onToggleAdmin,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CyberGold,
                            checkedTrackColor = CyberGold.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.testTag("admin_switch_toggle")
                    )
                }
            }
        }

        if (!userProfile.isAdmin) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = CyberTextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Admin Mode is currently inactive", color = CyberTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Enable the switch above to manage tournaments, publish room details, and approve deposits.", color = CyberTextSecondary, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 24.dp), lineHeight = 16.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onToggleAdmin(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberGold)
                        ) {
                            Text("ACTIVATE ADMIN MODE", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // Admin Section Selector
            item {
                val sections = listOf(
                    "MATCHES" to "Manage Matches (${activeMatches.size})",
                    "DEPOSITS" to "Deposits (${pendingDeposits.size})",
                    "ORDERS" to "Top-Up Orders (${pendingOrders.size})"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(sections) { (key, label) ->
                        val isSelected = adminSection == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) CyberGold.copy(alpha = 0.2f) else CyberBgElevated)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) CyberGold else CyberCardBorder,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { adminSection = key }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) CyberGold else CyberTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Section 1: Matches Management
            if (adminSection == "MATCHES") {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CyberSectionHeader("Tournament Matches", "Publish & Finalize")
                        Button(
                            onClick = onCreateMatchClick,
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp).testTag("btn_admin_create_match")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New Match", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                items(matches, key = { it.id }) { match ->
                    AdminMatchCard(
                        match = match,
                        onPublishRoom = { onPublishRoomClick(match) },
                        onDeclareWinner = { onDeclareWinnerClick(match) }
                    )
                }
            }

            // Section 2: Deposit Approvals
            if (adminSection == "DEPOSITS") {
                item {
                    CyberSectionHeader("Pending Deposits", "${pendingDeposits.size} Waiting")
                }

                if (pendingDeposits.isEmpty()) {
                    item {
                        Text("No pending deposits waiting for review.", color = CyberTextMuted, fontSize = 13.sp)
                    }
                } else {
                    items(pendingDeposits, key = { it.id }) { tx ->
                        AdminDepositCard(
                            tx = tx,
                            onApprove = { onApproveDeposit(tx.id) },
                            onReject = { onRejectDeposit(tx.id) }
                        )
                    }
                }
            }

            // Section 3: Top-up Orders
            if (adminSection == "ORDERS") {
                item {
                    CyberSectionHeader("Pending Top-Up Orders", "${pendingOrders.size} Waiting")
                }

                if (pendingOrders.isEmpty()) {
                    item {
                        Text("No pending top-up orders.", color = CyberTextMuted, fontSize = 13.sp)
                    }
                } else {
                    items(pendingOrders, key = { it.id }) { order ->
                        AdminOrderCard(
                            order = order,
                            onComplete = { onCompleteOrder(order.id) },
                            onReject = { onRejectOrder(order.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMatchCard(
    match: TournamentMatch,
    onPublishRoom: () -> Unit,
    onDeclareWinner: () -> Unit,
    modifier: Modifier = Modifier
) {
    CyberCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(match.id, color = CyberCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                StatusBadge(match.status)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(match.title, color = CyberTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("${match.gameCategory} • ${match.mode} • ${match.matchTime}", color = CyberTextSecondary, fontSize = 11.sp)
            Text("Registered: ${match.joinedSlots}/${match.maxSlots} • 1st: ৳${match.firstPrize} • Per Kill: ৳${match.perKillPrize}", color = CyberGold, fontSize = 11.sp)

            if (match.roomRevealed && !match.roomId.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text("Room: ${match.roomId} / Pass: ${match.roomPassword}", color = CyberGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (match.status != "COMPLETED") {
                    Button(
                        onClick = onPublishRoom,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(36.dp).testTag("btn_admin_room_${match.id}")
                    ) {
                        Icon(Icons.Default.VpnKey, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (match.roomRevealed) "Edit Room" else "Publish Room", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onDeclareWinner,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(36.dp).testTag("btn_admin_winner_${match.id}")
                    ) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Declare Winner", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text("Winner: ${match.winnerIgn} (${match.winnerKills} kills)", color = CyberGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AdminDepositCard(
    tx: WalletTransaction,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier
) {
    CyberCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PaymentMethodBadge(tx.paymentMethod)
                Text("৳${tx.amount.toInt()}", color = CyberCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text("Sender Phone: ${tx.accountOrPhone}", color = CyberTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text("TrxID: ${tx.trxId}", color = CyberGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(36.dp).testTag("btn_approve_tx_${tx.id}")
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("APPROVE", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberRed.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(36.dp).border(1.dp, CyberRed, RoundedCornerShape(8.dp)).testTag("btn_reject_tx_${tx.id}")
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = CyberRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("REJECT", color = CyberRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AdminOrderCard(
    order: TopUpOrder,
    onComplete: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier
) {
    CyberCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${order.game} • ${order.packageTitle}", color = CyberCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("৳${order.amountBdt.toInt()}", color = CyberGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text("Player UID: ${order.playerUid}", color = CyberTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            if (order.playerName.isNotBlank()) {
                Text("Player Name: ${order.playerName}", color = CyberTextSecondary, fontSize = 12.sp)
            }
            Text("Payment: ${order.paymentMethod} (Sender: ${order.senderNumber ?: "N/A"} | Trx: ${order.trxId ?: "N/A"})", color = CyberTextMuted, fontSize = 11.sp)

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onComplete,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(36.dp).testTag("btn_complete_order_${order.id}")
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("COMPLETE", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberRed.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(36.dp).border(1.dp, CyberRed, RoundedCornerShape(8.dp)).testTag("btn_reject_order_${order.id}")
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = CyberRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("REJECT", color = CyberRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
