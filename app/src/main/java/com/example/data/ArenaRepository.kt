package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID

class ArenaRepository(private val db: AppDatabase) {
    val matches: Flow<List<TournamentMatch>> = db.matchDao().getAllMatches()
    val participants: Flow<List<MatchParticipant>> = db.matchDao().getAllParticipants()
    val transactions: Flow<List<WalletTransaction>> = db.walletDao().getAllTransactions()
    val packages: Flow<List<TopUpPackage>> = db.topUpDao().getAllPackages()
    val topUpOrders: Flow<List<TopUpOrder>> = db.topUpDao().getAllOrders()
    val userProfile: Flow<UserProfile?> = db.userDao().getUser()
    val supportTickets: Flow<List<SupportTicket>> = db.supportDao().getAllTickets()

    suspend fun initializeDefaultData() {
        val user = db.userDao().getUserSync()
        if (user == null) {
            db.userDao().insertUser(
                UserProfile(
                    id = "user_main",
                    name = "ProGamer_BD",
                    phone = "01712345678",
                    defaultGameIgn = "SHADOW_KILLER",
                    defaultGameUid = "982341254",
                    depositBalance = 150.0,
                    winningBalance = 320.0,
                    matchesPlayed = 18,
                    totalKills = 54,
                    totalWins = 6,
                    isAdmin = false
                )
            )
        }

        val existingMatches = db.matchDao().getAllMatches().first()
        if (existingMatches.isEmpty()) {
            val initialMatches = listOf(
                TournamentMatch(
                    id = "FF-501",
                    title = "Free Fire Grand Clash #501",
                    gameCategory = "Free Fire",
                    mode = "Battle Royale Solo",
                    map = "Bermuda",
                    matchTime = "Today, 08:30 PM",
                    entryFee = 35,
                    perKillPrize = 20,
                    totalPrizePool = 1200,
                    firstPrize = 600,
                    secondPrize = 300,
                    thirdPrize = 150,
                    maxSlots = 48,
                    joinedSlots = 42,
                    status = "UPCOMING",
                    roomId = "8923019",
                    roomPassword = "772",
                    roomRevealed = true,
                    rules = "No hack, no emulator. Flare gun allowed. Room ID published below!"
                ),
                TournamentMatch(
                    id = "BGMI-202",
                    title = "BGMI / PUBG Royale War #202",
                    gameCategory = "BGMI",
                    mode = "Squad TPP",
                    map = "Erangel",
                    matchTime = "Today, 09:45 PM",
                    entryFee = 50,
                    perKillPrize = 30,
                    totalPrizePool = 2400,
                    firstPrize = 1200,
                    secondPrize = 600,
                    thirdPrize = 300,
                    maxSlots = 25,
                    joinedSlots = 19,
                    status = "UPCOMING",
                    roomId = "7718290",
                    roomPassword = "990",
                    roomRevealed = false,
                    rules = "Only mobile players. Emulators will be kicked with no refund. Room details unlock 15m before start."
                ),
                TournamentMatch(
                    id = "CS-303",
                    title = "Clash Squad 4v4 High Stakes",
                    gameCategory = "Clash Squad",
                    mode = "CS 4v4",
                    map = "Kalahari",
                    matchTime = "Today, 10:30 PM",
                    entryFee = 40,
                    perKillPrize = 15,
                    totalPrizePool = 800,
                    firstPrize = 500,
                    secondPrize = 200,
                    thirdPrize = 100,
                    maxSlots = 8,
                    joinedSlots = 6,
                    status = "UPCOMING",
                    roomId = null,
                    roomPassword = null,
                    roomRevealed = false,
                    rules = "Standard CS Rules: Unlimited ammo OFF, Grenade limit 1, No character skill restrictions."
                ),
                TournamentMatch(
                    id = "LW-404",
                    title = "Lone Wolf 1v1 Sniper Duel",
                    gameCategory = "Lone Wolf",
                    mode = "1v1 Duel",
                    map = "Iron Cage",
                    matchTime = "Tomorrow, 07:00 PM",
                    entryFee = 25,
                    perKillPrize = 25,
                    totalPrizePool = 350,
                    firstPrize = 250,
                    secondPrize = 100,
                    thirdPrize = 0,
                    maxSlots = 2,
                    joinedSlots = 1,
                    status = "UPCOMING",
                    roomId = null,
                    roomPassword = null,
                    roomRevealed = false,
                    rules = "AWM / M82B only sniper rounds. First to 5 rounds win."
                ),
                TournamentMatch(
                    id = "FREE-101",
                    title = "Daily Free Fire Scrims [FREE ENTRY]",
                    gameCategory = "Daily Free",
                    mode = "Battle Royale Solo",
                    map = "Purgatory",
                    matchTime = "Tomorrow, 04:00 PM",
                    entryFee = 0,
                    perKillPrize = 5,
                    totalPrizePool = 250,
                    firstPrize = 100,
                    secondPrize = 50,
                    thirdPrize = 25,
                    maxSlots = 48,
                    joinedSlots = 35,
                    status = "UPCOMING",
                    roomId = null,
                    roomPassword = null,
                    roomRevealed = false,
                    rules = "100% Free entry daily match for community practicing! Win real withdrawable bKash prize."
                ),
                TournamentMatch(
                    id = "FF-499",
                    title = "Free Fire Pro Series #499",
                    gameCategory = "Free Fire",
                    mode = "Squad BR",
                    map = "Bermuda",
                    matchTime = "Yesterday, 09:00 PM",
                    entryFee = 30,
                    perKillPrize = 15,
                    totalPrizePool = 1000,
                    firstPrize = 500,
                    secondPrize = 250,
                    thirdPrize = 100,
                    maxSlots = 48,
                    joinedSlots = 48,
                    status = "COMPLETED",
                    roomId = "8112340",
                    roomPassword = "331",
                    roomRevealed = true,
                    winnerIgn = "SHADOW_KILLER",
                    winnerKills = 9,
                    rules = "Match finished. Winner awarded prize to wallet."
                )
            )
            db.matchDao().insertMatches(initialMatches)

            // Auto-join the user to FF-501 so room ID revealing is tested out-of-the-box
            db.matchDao().insertParticipant(
                MatchParticipant(
                    matchId = "FF-501",
                    userUid = "user_main",
                    playerName = "SHADOW_KILLER",
                    gameUid = "982341254",
                    slotNumber = 42
                )
            )
        }

        val existingPackages = db.topUpDao().getAllPackages().first()
        if (existingPackages.isEmpty()) {
            val packs = listOf(
                // Free Fire
                TopUpPackage("ff-1", "Free Fire", "115 Diamonds", "115 \uD83D\uDC8E", 80.0, "Popular", "diamond"),
                TopUpPackage("ff-2", "Free Fire", "240 Diamonds", "240 \uD83D\uDC8E", 160.0, null, "diamond"),
                TopUpPackage("ff-3", "Free Fire", "355 Diamonds", "355 \uD83D\uDC8E", 240.0, "Best Deal", "diamond"),
                TopUpPackage("ff-4", "Free Fire", "610 Diamonds", "610 \uD83D\uDC8E", 410.0, "Bonus +60", "diamond"),
                TopUpPackage("ff-5", "Free Fire", "Weekly Membership", "Weekly VIP", 185.0, "Hot VIP", "membership"),
                TopUpPackage("ff-6", "Free Fire", "Monthly Membership", "Monthly VIP", 790.0, "Mega Savings", "membership"),
                TopUpPackage("ff-7", "Free Fire", "Level Up Pass", "Level Pass", 160.0, "One-Time", "pass"),

                // BGMI / PUBG
                TopUpPackage("bg-1", "BGMI", "60 UC Pack", "60 UC", 95.0, null, "uc"),
                TopUpPackage("bg-2", "BGMI", "325 UC Pack", "325 UC", 420.0, "Popular", "uc"),
                TopUpPackage("bg-3", "BGMI", "660 UC Pack", "660 UC", 790.0, "Best Value", "uc"),
                TopUpPackage("bg-4", "BGMI", "Royale Pass Elite", "Royale Pass", 450.0, "Season Pass", "pass"),
                TopUpPackage("bg-5", "BGMI", "Elite Plus Pass", "Elite Plus", 990.0, "Full Unlock", "pass")
            )
            db.topUpDao().insertPackages(packs)
        }

        val existingTx = db.walletDao().getAllTransactions().first()
        if (existingTx.isEmpty()) {
            val txs = listOf(
                WalletTransaction(
                    id = "TX-9901",
                    type = "MATCH_WINNING",
                    amount = 350.0,
                    paymentMethod = "Wallet",
                    accountOrPhone = "In-App Tournament",
                    trxId = "WIN-FF-499",
                    status = "APPROVED",
                    note = "1st Place & 9 kills in FF Pro Series #499",
                    timestamp = System.currentTimeMillis() - 86400000L
                ),
                WalletTransaction(
                    id = "TX-9902",
                    type = "DEPOSIT",
                    amount = 200.0,
                    paymentMethod = "bKash",
                    accountOrPhone = "01712345678",
                    trxId = "BK9A8721X4",
                    status = "APPROVED",
                    note = "Deposit via bKash personal",
                    timestamp = System.currentTimeMillis() - 43200000L
                ),
                WalletTransaction(
                    id = "TX-9903",
                    type = "MATCH_FEE",
                    amount = 35.0,
                    paymentMethod = "Wallet",
                    accountOrPhone = "Entry Fee",
                    trxId = "FEE-FF-501",
                    status = "APPROVED",
                    note = "Joined FF Grand Clash #501",
                    timestamp = System.currentTimeMillis() - 7200000L
                )
            )
            db.walletDao().insertTransactions(txs)
        }

        val existingTickets = db.supportDao().getAllTickets().first()
        if (existingTickets.isEmpty()) {
            db.supportDao().insertTicket(
                SupportTicket(
                    id = "TCK-101",
                    userName = "ProGamer_BD",
                    userPhone = "01712345678",
                    category = "Match Room",
                    message = "When will room password be given for match #501?",
                    reply = "Hello! Room ID & Password is now unlocked in your match card. Please join room within 10 minutes!",
                    status = "REPLIED",
                    timestamp = System.currentTimeMillis() - 3600000L
                )
            )
        }
    }

    suspend fun joinMatch(
        match: TournamentMatch,
        playerName: String,
        gameUid: String
    ): Result<String> {
        val user = db.userDao().getUserSync() ?: return Result.failure(Exception("User not found"))
        if (match.joinedSlots >= match.maxSlots) {
            return Result.failure(Exception("Match is full! All slots occupied."))
        }

        val alreadyJoined = db.matchDao().getParticipant(match.id, user.id)
        if (alreadyJoined != null) {
            return Result.failure(Exception("You have already registered for this match!"))
        }

        val fee = match.entryFee.toDouble()
        if (fee > 0) {
            if (user.totalBalance < fee) {
                return Result.failure(Exception("Insufficient balance! Please deposit money to join (Fee: ৳${match.entryFee})."))
            }

            // Deduct fee: prioritize depositBalance, then winningBalance
            val newDeposit: Double
            val newWinning: Double
            if (user.depositBalance >= fee) {
                newDeposit = user.depositBalance - fee
                newWinning = user.winningBalance
            } else {
                val remainder = fee - user.depositBalance
                newDeposit = 0.0
                newWinning = user.winningBalance - remainder
            }

            db.userDao().updateBalances(newDeposit, newWinning)

            // Record transaction
            db.walletDao().insertTransaction(
                WalletTransaction(
                    id = "TX-" + UUID.randomUUID().toString().take(8).uppercase(),
                    type = "MATCH_FEE",
                    amount = fee,
                    paymentMethod = "Wallet",
                    accountOrPhone = "Match Fee",
                    trxId = "FEE-" + match.id,
                    status = "APPROVED",
                    note = "Entry fee for ${match.title}",
                    timestamp = System.currentTimeMillis()
                )
            )
        }

        // Add participant
        val newSlot = match.joinedSlots + 1
        db.matchDao().insertParticipant(
            MatchParticipant(
                matchId = match.id,
                userUid = user.id,
                playerName = playerName,
                gameUid = gameUid,
                slotNumber = newSlot
            )
        )
        db.matchDao().incrementJoined(match.id)

        // Update user default IGN/UID if empty
        db.userDao().updateUser(
            user.copy(
                defaultGameIgn = playerName,
                defaultGameUid = gameUid,
                matchesPlayed = user.matchesPlayed + 1
            )
        )

        return Result.success("Successfully registered! Slot #$newSlot allocated.")
    }

    suspend fun requestDeposit(
        amount: Double,
        method: String,
        senderNumber: String,
        trxId: String
    ): Result<String> {
        if (amount < 10.0) {
            return Result.failure(Exception("Minimum deposit amount is ৳10"))
        }
        if (senderNumber.length < 11) {
            return Result.failure(Exception("Please enter a valid 11-digit sender phone number"))
        }
        if (trxId.length < 5) {
            return Result.failure(Exception("Please enter a valid Transaction ID (TrxID)"))
        }

        val tx = WalletTransaction(
            id = "TX-" + UUID.randomUUID().toString().take(8).uppercase(),
            type = "DEPOSIT",
            amount = amount,
            paymentMethod = method,
            accountOrPhone = senderNumber,
            trxId = trxId.uppercase().trim(),
            status = "PENDING",
            note = "Deposit via $method (Awaiting admin approval)",
            timestamp = System.currentTimeMillis()
        )
        db.walletDao().insertTransaction(tx)
        return Result.success("Deposit request of ৳$amount submitted! Verification takes 2-10 minutes.")
    }

    suspend fun requestWithdrawal(
        amount: Double,
        method: String,
        receiverNumber: String
    ): Result<String> {
        val user = db.userDao().getUserSync() ?: return Result.failure(Exception("User not found"))
        if (amount < 50.0) {
            return Result.failure(Exception("Minimum withdrawal limit is ৳50"))
        }
        if (receiverNumber.length < 11) {
            return Result.failure(Exception("Please enter a valid 11-digit mobile banking number"))
        }
        if (amount > user.winningBalance) {
            return Result.failure(Exception("Insufficient winning balance! Withdrawable: ৳${user.winningBalance}"))
        }

        // Deduct from winning balance immediately to lock funds
        val newWinning = user.winningBalance - amount
        db.userDao().updateBalances(user.depositBalance, newWinning)

        val tx = WalletTransaction(
            id = "TX-" + UUID.randomUUID().toString().take(8).uppercase(),
            type = "WITHDRAWAL",
            amount = amount,
            paymentMethod = method,
            accountOrPhone = receiverNumber,
            trxId = "WTH-" + UUID.randomUUID().toString().take(6).uppercase(),
            status = "PENDING",
            note = "Withdrawal to $method ($receiverNumber)",
            timestamp = System.currentTimeMillis()
        )
        db.walletDao().insertTransaction(tx)
        return Result.success("Withdrawal request of ৳$amount placed! Money will be sent to $receiverNumber within 30 minutes.")
    }

    suspend fun purchaseTopUp(
        pack: TopUpPackage,
        playerUid: String,
        playerName: String,
        paymentMethod: String,
        senderNumber: String? = null,
        trxId: String? = null
    ): Result<String> {
        if (playerUid.length < 5) {
            return Result.failure(Exception("Please enter a valid Player UID"))
        }

        val user = db.userDao().getUserSync() ?: return Result.failure(Exception("User not found"))

        if (paymentMethod == "Wallet") {
            if (user.totalBalance < pack.priceBdt) {
                return Result.failure(Exception("Insufficient wallet balance! Cost: ৳${pack.priceBdt}, Balance: ৳${user.totalBalance}"))
            }

            // Deduct
            val newDeposit: Double
            val newWinning: Double
            if (user.depositBalance >= pack.priceBdt) {
                newDeposit = user.depositBalance - pack.priceBdt
                newWinning = user.winningBalance
            } else {
                val rem = pack.priceBdt - user.depositBalance
                newDeposit = 0.0
                newWinning = user.winningBalance - rem
            }
            db.userDao().updateBalances(newDeposit, newWinning)

            // Record transaction
            db.walletDao().insertTransaction(
                WalletTransaction(
                    id = "TX-" + UUID.randomUUID().toString().take(8).uppercase(),
                    type = "TOPUP",
                    amount = pack.priceBdt,
                    paymentMethod = "Wallet",
                    accountOrPhone = "UID: $playerUid",
                    trxId = "TOP-" + UUID.randomUUID().toString().take(6).uppercase(),
                    status = "APPROVED",
                    note = "${pack.game} ${pack.title} top-up",
                    timestamp = System.currentTimeMillis()
                )
            )

            // Order is directly completed for instant wallet payment
            val order = TopUpOrder(
                id = "ORD-" + UUID.randomUUID().toString().take(8).uppercase(),
                packageTitle = pack.title,
                game = pack.game,
                playerUid = playerUid,
                playerName = playerName,
                amountBdt = pack.priceBdt,
                paymentMethod = "Wallet",
                status = "COMPLETED",
                timestamp = System.currentTimeMillis()
            )
            db.topUpDao().insertOrder(order)
            return Result.success("Top-up successful! ${pack.itemCount} sent to UID: $playerUid.")
        } else {
            // Direct Mobile Banking with TrxID
            if (senderNumber.isNullOrBlank() || senderNumber.length < 11) {
                return Result.failure(Exception("Please enter your $paymentMethod sender phone number"))
            }
            if (trxId.isNullOrBlank() || trxId.length < 5) {
                return Result.failure(Exception("Please enter the Transaction ID (TrxID)"))
            }

            val order = TopUpOrder(
                id = "ORD-" + UUID.randomUUID().toString().take(8).uppercase(),
                packageTitle = pack.title,
                game = pack.game,
                playerUid = playerUid,
                playerName = playerName,
                amountBdt = pack.priceBdt,
                paymentMethod = paymentMethod,
                senderNumber = senderNumber,
                trxId = trxId.uppercase().trim(),
                status = "PENDING",
                timestamp = System.currentTimeMillis()
            )
            db.topUpDao().insertOrder(order)
            return Result.success("Order submitted! Diamonds/UC will be delivered to $playerUid after payment check.")
        }
    }

    suspend fun createSupportTicket(
        category: String,
        message: String
    ): Result<String> {
        val user = db.userDao().getUserSync() ?: return Result.failure(Exception("User not found"))
        if (message.isBlank()) {
            return Result.failure(Exception("Please describe your issue"))
        }

        val ticket = SupportTicket(
            id = "TCK-" + UUID.randomUUID().toString().take(6).uppercase(),
            userName = user.name,
            userPhone = user.phone,
            category = category,
            message = message,
            status = "OPEN",
            timestamp = System.currentTimeMillis()
        )
        db.supportDao().insertTicket(ticket)
        return Result.success("Support ticket created! Our team replies within 15 minutes.")
    }

    // --- Admin Operations ---
    suspend fun toggleAdminMode(enable: Boolean) {
        db.userDao().setAdmin(enable)
    }

    suspend fun publishRoomCredentials(
        matchId: String,
        roomId: String,
        roomPassword: String
    ): Result<String> {
        if (roomId.isBlank() || roomPassword.isBlank()) {
            return Result.failure(Exception("Room ID and Password cannot be empty!"))
        }
        db.matchDao().updateRoom(matchId, roomId, roomPassword, true)
        return Result.success("Room ID & Password published successfully to all joined players!")
    }

    suspend fun declareMatchWinner(
        matchId: String,
        winnerIgn: String,
        kills: Int
    ): Result<String> {
        val match = db.matchDao().getMatchById(matchId) ?: return Result.failure(Exception("Match not found"))
        db.matchDao().setWinner(matchId, winnerIgn, kills)

        // Calculate prize distribution
        val totalPrizeWon = match.firstPrize + (kills * match.perKillPrize).toDouble()

        // Credit to current user if matches IGN or award to balance
        val user = db.userDao().getUserSync()
        if (user != null && (user.defaultGameIgn.equals(winnerIgn, ignoreCase = true) || user.name.equals(winnerIgn, ignoreCase = true))) {
            val newWinning = user.winningBalance + totalPrizeWon
            db.userDao().updateBalances(user.depositBalance, newWinning)
            db.userDao().updateUser(
                user.copy(
                    winningBalance = newWinning,
                    totalWins = user.totalWins + 1,
                    totalKills = user.totalKills + kills
                )
            )

            db.walletDao().insertTransaction(
                WalletTransaction(
                    id = "TX-" + UUID.randomUUID().toString().take(8).uppercase(),
                    type = "MATCH_WINNING",
                    amount = totalPrizeWon,
                    paymentMethod = "Wallet",
                    accountOrPhone = winnerIgn,
                    trxId = "WIN-" + match.id,
                    status = "APPROVED",
                    note = "1st Place (৳${match.firstPrize}) + $kills Kills (৳${kills * match.perKillPrize}) in ${match.title}",
                    timestamp = System.currentTimeMillis()
                )
            )
        }

        return Result.success("Match marked COMPLETED! ৳$totalPrizeWon awarded to $winnerIgn.")
    }

    suspend fun approveDeposit(txId: String): Result<String> {
        val tx = db.walletDao().getTransactionById(txId) ?: return Result.failure(Exception("Transaction not found"))
        if (tx.status != "PENDING") {
            return Result.failure(Exception("Transaction is already ${tx.status}"))
        }

        db.walletDao().updateTransactionStatus(txId, "APPROVED")
        val user = db.userDao().getUserSync()
        if (user != null) {
            val newDeposit = user.depositBalance + tx.amount
            db.userDao().updateBalances(newDeposit, user.winningBalance)
        }
        return Result.success("Deposit approved! ৳${tx.amount} credited to user wallet.")
    }

    suspend fun rejectDeposit(txId: String): Result<String> {
        db.walletDao().updateTransactionStatus(txId, "REJECTED")
        return Result.success("Deposit rejected.")
    }

    suspend fun approveWithdrawal(txId: String): Result<String> {
        db.walletDao().updateTransactionStatus(txId, "APPROVED")
        return Result.success("Withdrawal marked APPROVED! Money disbursed.")
    }

    suspend fun rejectWithdrawal(txId: String): Result<String> {
        val tx = db.walletDao().getTransactionById(txId) ?: return Result.failure(Exception("Transaction not found"))
        db.walletDao().updateTransactionStatus(txId, "REJECTED")
        // Refund back to winning balance
        val user = db.userDao().getUserSync()
        if (user != null) {
            val newWinning = user.winningBalance + tx.amount
            db.userDao().updateBalances(user.depositBalance, newWinning)
        }
        return Result.success("Withdrawal rejected. ৳${tx.amount} refunded to winning balance.")
    }

    suspend fun completeTopUpOrder(orderId: String): Result<String> {
        db.topUpDao().updateOrderStatus(orderId, "COMPLETED")
        return Result.success("Top-up order marked COMPLETED!")
    }

    suspend fun rejectTopUpOrder(orderId: String): Result<String> {
        db.topUpDao().updateOrderStatus(orderId, "REJECTED")
        return Result.success("Top-up order rejected.")
    }

    suspend fun createNewMatch(match: TournamentMatch): Result<String> {
        db.matchDao().insertMatch(match)
        return Result.success("Tournament match created successfully!")
    }

    suspend fun replySupportTicket(ticketId: String, reply: String): Result<String> {
        db.supportDao().replyTicket(ticketId, reply, "REPLIED")
        return Result.success("Reply sent to customer ticket!")
    }
}
