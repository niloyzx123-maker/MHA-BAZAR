package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tournament_matches")
data class TournamentMatch(
    @PrimaryKey val id: String,
    val title: String,
    val gameCategory: String, // "Free Fire", "BGMI", "Clash Squad", "Lone Wolf", "Daily Free"
    val mode: String,         // "Battle Royale Solo", "CS 4v4 Squad", "1v1 Lone Wolf", etc.
    val map: String,          // "Bermuda", "Erangel", "Kalahari", "Purgatory"
    val matchTime: String,
    val entryFee: Int,
    val perKillPrize: Int,
    val totalPrizePool: Int,
    val firstPrize: Int,
    val secondPrize: Int,
    val thirdPrize: Int,
    val maxSlots: Int,
    val joinedSlots: Int,
    val status: String,       // "UPCOMING", "ONGOING", "COMPLETED"
    val roomId: String? = null,
    val roomPassword: String? = null,
    val roomRevealed: Boolean = false,
    val winnerIgn: String? = null,
    val winnerKills: Int? = null,
    val rules: String = "No hackers or emulator scripts allowed. Screenshot proof required for kills. Room details will unlock 15 minutes before start time."
)

@Entity(tableName = "match_participants")
data class MatchParticipant(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val matchId: String,
    val userUid: String,
    val playerName: String,
    val gameUid: String,
    val slotNumber: Int,
    val joinedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "wallet_transactions")
data class WalletTransaction(
    @PrimaryKey val id: String,
    val type: String,        // "DEPOSIT", "WITHDRAWAL", "MATCH_FEE", "MATCH_WINNING", "TOPUP"
    val amount: Double,
    val paymentMethod: String, // "bKash", "Nagad", "Rocket", "Wallet"
    val accountOrPhone: String,
    val trxId: String,
    val status: String,      // "PENDING", "APPROVED", "REJECTED"
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "topup_packages")
data class TopUpPackage(
    @PrimaryKey val id: String,
    val game: String,        // "Free Fire", "BGMI"
    val title: String,
    val itemCount: String,
    val priceBdt: Double,
    val badge: String? = null,
    val type: String         // "diamond", "membership", "uc", "pass"
)

@Entity(tableName = "topup_orders")
data class TopUpOrder(
    @PrimaryKey val id: String,
    val packageTitle: String,
    val game: String,
    val playerUid: String,
    val playerName: String,
    val amountBdt: Double,
    val paymentMethod: String,
    val senderNumber: String? = null,
    val trxId: String? = null,
    val status: String = "PENDING", // "PENDING", "COMPLETED", "REJECTED"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: String = "user_main",
    val name: String = "CyberSniper",
    val phone: String = "01755123456",
    val defaultGameIgn: String = "SHADOW_KILLER",
    val defaultGameUid: String = "8912457812",
    val depositBalance: Double = 120.0,
    val winningBalance: Double = 250.0,
    val matchesPlayed: Int = 14,
    val totalKills: Int = 42,
    val totalWins: Int = 5,
    val isAdmin: Boolean = false
) {
    val totalBalance: Double
        get() = depositBalance + winningBalance
}

@Entity(tableName = "support_tickets")
data class SupportTicket(
    @PrimaryKey val id: String,
    val userName: String,
    val userPhone: String,
    val category: String,
    val message: String,
    val reply: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "OPEN" // "OPEN", "REPLIED", "RESOLVED"
)
