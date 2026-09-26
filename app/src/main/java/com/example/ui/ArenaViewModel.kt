package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ArenaRepository
import com.example.data.MatchParticipant
import com.example.data.SupportTicket
import com.example.data.TopUpOrder
import com.example.data.TopUpPackage
import com.example.data.TournamentMatch
import com.example.data.UserProfile
import com.example.data.WalletTransaction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    HOME, MATCHES, TOPUP, WALLET, SUPPORT, ADMIN
}

class ArenaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ArenaRepository(AppDatabase.getInstance(application))

    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _matchCategoryFilter = MutableStateFlow("ALL")
    val matchCategoryFilter: StateFlow<String> = _matchCategoryFilter.asStateFlow()

    private val _topUpGameFilter = MutableStateFlow("Free Fire")
    val topUpGameFilter: StateFlow<String> = _topUpGameFilter.asStateFlow()

    private val _walletTxFilter = MutableStateFlow("ALL")
    val walletTxFilter: StateFlow<String> = _walletTxFilter.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .combine(MutableStateFlow(Unit)) { user, _ ->
            user ?: UserProfile()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    val allMatches: StateFlow<List<TournamentMatch>> = repository.matches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val participants: StateFlow<List<MatchParticipant>> = repository.participants
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<WalletTransaction>> = repository.transactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topUpPackages: StateFlow<List<TopUpPackage>> = repository.packages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topUpOrders: StateFlow<List<TopUpOrder>> = repository.topUpOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val supportTickets: StateFlow<List<SupportTicket>> = repository.supportTickets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dialog & Interaction states
    var selectedMatchForJoin = MutableStateFlow<TournamentMatch?>(null)
    var selectedMatchForDetails = MutableStateFlow<TournamentMatch?>(null)
    var selectedPackageForCheckout = MutableStateFlow<TopUpPackage?>(null)
    var showDepositDialog = MutableStateFlow(false)
    var showWithdrawDialog = MutableStateFlow(false)
    var showNewTicketDialog = MutableStateFlow(false)
    var showProfileDialog = MutableStateFlow(false)

    // Admin Dialog states
    var adminSelectedMatchForRoom = MutableStateFlow<TournamentMatch?>(null)
    var adminSelectedMatchForWinner = MutableStateFlow<TournamentMatch?>(null)
    var showCreateMatchDialog = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            repository.initializeDefaultData()
        }
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setMatchCategoryFilter(category: String) {
        _matchCategoryFilter.value = category
    }

    fun setTopUpGameFilter(game: String) {
        _topUpGameFilter.value = game
    }

    fun setWalletTxFilter(filter: String) {
        _walletTxFilter.value = filter
    }

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(message)
        }
    }

    fun joinMatch(match: TournamentMatch, ign: String, uid: String) {
        viewModelScope.launch {
            val result = repository.joinMatch(match, ign, uid)
            result.onSuccess {
                showSnackbar(it)
                selectedMatchForJoin.value = null
            }.onFailure {
                showSnackbar(it.message ?: "Failed to join match")
            }
        }
    }

    fun submitDeposit(amount: Double, method: String, senderNumber: String, trxId: String) {
        viewModelScope.launch {
            val result = repository.requestDeposit(amount, method, senderNumber, trxId)
            result.onSuccess {
                showSnackbar(it)
                showDepositDialog.value = false
            }.onFailure {
                showSnackbar(it.message ?: "Deposit request failed")
            }
        }
    }

    fun submitWithdrawal(amount: Double, method: String, receiverNumber: String) {
        viewModelScope.launch {
            val result = repository.requestWithdrawal(amount, method, receiverNumber)
            result.onSuccess {
                showSnackbar(it)
                showWithdrawDialog.value = false
            }.onFailure {
                showSnackbar(it.message ?: "Withdrawal request failed")
            }
        }
    }

    fun submitTopUpCheckout(
        pack: TopUpPackage,
        playerUid: String,
        playerName: String,
        paymentMethod: String,
        senderNumber: String? = null,
        trxId: String? = null
    ) {
        viewModelScope.launch {
            val result = repository.purchaseTopUp(pack, playerUid, playerName, paymentMethod, senderNumber, trxId)
            result.onSuccess {
                showSnackbar(it)
                selectedPackageForCheckout.value = null
            }.onFailure {
                showSnackbar(it.message ?: "Top-up failed")
            }
        }
    }

    fun submitSupportTicket(category: String, message: String) {
        viewModelScope.launch {
            val result = repository.createSupportTicket(category, message)
            result.onSuccess {
                showSnackbar(it)
                showNewTicketDialog.value = false
            }.onFailure {
                showSnackbar(it.message ?: "Failed to send ticket")
            }
        }
    }

    // --- Admin Functions ---
    fun toggleAdminMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.toggleAdminMode(enabled)
            showSnackbar(if (enabled) "Admin Mode Activated!" else "Switched to Player Mode")
        }
    }

    fun publishRoom(matchId: String, roomId: String, pass: String) {
        viewModelScope.launch {
            val result = repository.publishRoomCredentials(matchId, roomId, pass)
            result.onSuccess {
                showSnackbar(it)
                adminSelectedMatchForRoom.value = null
            }.onFailure {
                showSnackbar(it.message ?: "Failed to publish room")
            }
        }
    }

    fun declareWinner(matchId: String, winnerIgn: String, kills: Int) {
        viewModelScope.launch {
            val result = repository.declareMatchWinner(matchId, winnerIgn, kills)
            result.onSuccess {
                showSnackbar(it)
                adminSelectedMatchForWinner.value = null
            }.onFailure {
                showSnackbar(it.message ?: "Failed to declare winner")
            }
        }
    }

    fun approveDeposit(txId: String) {
        viewModelScope.launch {
            val result = repository.approveDeposit(txId)
            result.onSuccess { showSnackbar(it) }.onFailure { showSnackbar(it.message ?: "Error") }
        }
    }

    fun rejectDeposit(txId: String) {
        viewModelScope.launch {
            val result = repository.rejectDeposit(txId)
            result.onSuccess { showSnackbar(it) }.onFailure { showSnackbar(it.message ?: "Error") }
        }
    }

    fun approveWithdrawal(txId: String) {
        viewModelScope.launch {
            val result = repository.approveWithdrawal(txId)
            result.onSuccess { showSnackbar(it) }.onFailure { showSnackbar(it.message ?: "Error") }
        }
    }

    fun rejectWithdrawal(txId: String) {
        viewModelScope.launch {
            val result = repository.rejectWithdrawal(txId)
            result.onSuccess { showSnackbar(it) }.onFailure { showSnackbar(it.message ?: "Error") }
        }
    }

    fun completeTopUpOrder(orderId: String) {
        viewModelScope.launch {
            val result = repository.completeTopUpOrder(orderId)
            result.onSuccess { showSnackbar(it) }.onFailure { showSnackbar(it.message ?: "Error") }
        }
    }

    fun rejectTopUpOrder(orderId: String) {
        viewModelScope.launch {
            val result = repository.rejectTopUpOrder(orderId)
            result.onSuccess { showSnackbar(it) }.onFailure { showSnackbar(it.message ?: "Error") }
        }
    }

    fun createMatch(match: TournamentMatch) {
        viewModelScope.launch {
            val result = repository.createNewMatch(match)
            result.onSuccess {
                showSnackbar(it)
                showCreateMatchDialog.value = false
            }.onFailure {
                showSnackbar(it.message ?: "Failed to create match")
            }
        }
    }

    fun replyTicket(ticketId: String, reply: String) {
        viewModelScope.launch {
            val result = repository.replySupportTicket(ticketId, reply)
            result.onSuccess { showSnackbar(it) }.onFailure { showSnackbar(it.message ?: "Error") }
        }
    }
}
