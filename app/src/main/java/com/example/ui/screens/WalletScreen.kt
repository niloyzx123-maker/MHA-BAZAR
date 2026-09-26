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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SportsEsports
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
import com.example.data.UserProfile
import com.example.data.WalletTransaction
import com.example.ui.components.CopyableTextRow
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    userProfile: UserProfile,
    transactions: List<WalletTransaction>,
    currentFilter: String,
    onFilterChange: (String) -> Unit,
    onOpenDeposit: () -> Unit,
    onOpenWithdraw: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredTransactions = transactions.filter { tx ->
        if (currentFilter == "ALL") true else tx.type.equals(currentFilter, ignoreCase = true)
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
                title = "WALLET & PAYMENTS",
                subtitle = "bKash • Nagad • Rocket"
            )
        }

        // Cyber Neon Wallet Card
        item {
            CyberCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wallet_main_card"),
                borderColor = CyberCyan,
                glow = true
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Total Available Balance", color = CyberTextMuted, fontSize = 12.sp)
                    Text(
                        "৳${userProfile.totalBalance.toInt()}",
                        color = CyberCyan,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberBgDark, RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Deposit Balance", color = CyberTextMuted, fontSize = 11.sp)
                            Text("৳${userProfile.depositBalance.toInt()}", color = CyberTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("For Match Fees & Top-up", color = CyberTextMuted, fontSize = 10.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Winning Balance", color = CyberTextMuted, fontSize = 11.sp)
                            Text("৳${userProfile.winningBalance.toInt()}", color = CyberGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Instant Withdrawable", color = CyberGreen, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenDeposit,
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_wallet_deposit")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Deposit", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Button(
                            onClick = onOpenWithdraw,
                            colors = ButtonDefaults.buttonColors(containerColor = CyberGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("btn_wallet_withdraw")
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Withdraw Win", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Official Send Money Numbers
        item {
            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Official Send Money Numbers", color = CyberTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Use 'Send Money' option in your app, then submit TrxID", color = CyberTextSecondary, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    CopyableTextRow("bKash Personal", "01755123456", tag = "copy_wallet_bkash")
                    Spacer(modifier = Modifier.height(6.dp))
                    CopyableTextRow("Nagad Personal", "01855123456", tag = "copy_wallet_nagad")
                    Spacer(modifier = Modifier.height(6.dp))
                    CopyableTextRow("Rocket Personal", "01955123456", tag = "copy_wallet_rocket")
                }
            }
        }

        // Transaction History Section
        item {
            CyberSectionHeader("Transaction History", "Statements")
        }

        // Filter chips
        item {
            val filters = listOf(
                "ALL" to "All",
                "DEPOSIT" to "Deposits",
                "WITHDRAWAL" to "Withdrawals",
                "MATCH_FEE" to "Match Fees",
                "MATCH_WINNING" to "Winnings",
                "TOPUP" to "Top-Up"
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
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) CyberCyan else CyberTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        if (filteredTransactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No transactions found", color = CyberTextMuted, fontSize = 13.sp)
                }
            }
        }

        items(filteredTransactions, key = { it.id }) { tx ->
            TransactionItemCard(tx = tx)
        }
    }
}

@Composable
fun TransactionItemCard(
    tx: WalletTransaction,
    modifier: Modifier = Modifier
) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(tx.timestamp))
    val isCredit = tx.type in listOf("DEPOSIT", "MATCH_WINNING")
    val sign = if (isCredit) "+" else "-"
    val amountColor = if (isCredit) CyberGreen else CyberTextPrimary

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            when (tx.type) {
                                "DEPOSIT" -> CyberCyan.copy(alpha = 0.15f)
                                "WITHDRAWAL" -> CyberGold.copy(alpha = 0.15f)
                                "MATCH_WINNING" -> CyberGreen.copy(alpha = 0.15f)
                                "MATCH_FEE" -> CyberRed.copy(alpha = 0.15f)
                                else -> CyberCyan.copy(alpha = 0.15f)
                            },
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (tx.type) {
                            "DEPOSIT" -> Icons.Default.ArrowDownward
                            "WITHDRAWAL" -> Icons.Default.ArrowUpward
                            "MATCH_WINNING" -> Icons.Default.EmojiEvents
                            "MATCH_FEE" -> Icons.Default.SportsEsports
                            else -> Icons.Default.Diamond
                        },
                        contentDescription = null,
                        tint = when (tx.type) {
                            "DEPOSIT" -> CyberCyan
                            "WITHDRAWAL" -> CyberGold
                            "MATCH_WINNING" -> CyberGreen
                            "MATCH_FEE" -> CyberRed
                            else -> CyberCyan
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = when (tx.type) {
                            "DEPOSIT" -> "Deposit (${tx.paymentMethod})"
                            "WITHDRAWAL" -> "Withdrawal (${tx.paymentMethod})"
                            "MATCH_WINNING" -> "Tournament Winning"
                            "MATCH_FEE" -> "Match Entry Fee"
                            "TOPUP" -> "Diamond / UC Top-Up"
                            else -> tx.type
                        },
                        color = CyberTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Trx: ${tx.trxId} • $dateStr",
                        color = CyberTextMuted,
                        fontSize = 11.sp
                    )
                    if (tx.note.isNotBlank()) {
                        Text(tx.note, color = CyberTextSecondary, fontSize = 10.sp)
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$sign৳${tx.amount.toInt()}",
                    color = amountColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                StatusBadge(tx.status)
            }
        }
    }
}
