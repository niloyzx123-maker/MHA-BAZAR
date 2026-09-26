package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppTab
import com.example.ui.ArenaViewModel
import com.example.ui.components.CreateMatchDialog
import com.example.ui.components.DeclareWinnerDialog
import com.example.ui.components.DepositDialog
import com.example.ui.components.JoinMatchDialog
import com.example.ui.components.MatchDetailsDialog
import com.example.ui.components.NewTicketDialog
import com.example.ui.components.ProfileDialog
import com.example.ui.components.PublishRoomDialog
import com.example.ui.components.TopUpCheckoutDialog
import com.example.ui.components.WithdrawDialog
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MatchesScreen
import com.example.ui.screens.SupportScreen
import com.example.ui.screens.TopUpScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.CyberBg
import com.example.ui.theme.CyberBgElevated
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGold
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer()
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: ArenaViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val matchCategoryFilter by viewModel.matchCategoryFilter.collectAsStateWithLifecycle()
    val topUpGameFilter by viewModel.topUpGameFilter.collectAsStateWithLifecycle()
    val walletTxFilter by viewModel.walletTxFilter.collectAsStateWithLifecycle()

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val matches by viewModel.allMatches.collectAsStateWithLifecycle()
    val participants by viewModel.participants.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val packages by viewModel.topUpPackages.collectAsStateWithLifecycle()
    val topUpOrders by viewModel.topUpOrders.collectAsStateWithLifecycle()
    val tickets by viewModel.supportTickets.collectAsStateWithLifecycle()

    val selectedMatchForJoin by viewModel.selectedMatchForJoin.collectAsStateWithLifecycle()
    val selectedMatchForDetails by viewModel.selectedMatchForDetails.collectAsStateWithLifecycle()
    val selectedPackageForCheckout by viewModel.selectedPackageForCheckout.collectAsStateWithLifecycle()
    val showDepositDialog by viewModel.showDepositDialog.collectAsStateWithLifecycle()
    val showWithdrawDialog by viewModel.showWithdrawDialog.collectAsStateWithLifecycle()
    val showNewTicketDialog by viewModel.showNewTicketDialog.collectAsStateWithLifecycle()
    val showProfileDialog by viewModel.showProfileDialog.collectAsStateWithLifecycle()

    val adminRoomMatch by viewModel.adminSelectedMatchForRoom.collectAsStateWithLifecycle()
    val adminWinnerMatch by viewModel.adminSelectedMatchForWinner.collectAsStateWithLifecycle()
    val showCreateMatchDialog by viewModel.showCreateMatchDialog.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.snackbarMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Handle back button on sub-tabs
    if (currentTab != AppTab.HOME) {
        BackHandler {
            viewModel.selectTab(AppTab.HOME)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            CyberBottomNavigation(
                currentTab = currentTab,
                isAdmin = userProfile.isAdmin,
                onSelectTab = { viewModel.selectTab(it) }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CyberBg)
                .statusBarsPadding()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.HOME -> HomeScreen(
                    userProfile = userProfile,
                    matches = matches,
                    participants = participants,
                    onNavigateTab = { viewModel.selectTab(it) },
                    onCategorySelected = { viewModel.setMatchCategoryFilter(it) },
                    onOpenDeposit = { viewModel.showDepositDialog.value = true },
                    onOpenWithdraw = { viewModel.showWithdrawDialog.value = true },
                    onOpenProfile = { viewModel.showProfileDialog.value = true },
                    onJoinMatch = { viewModel.selectedMatchForJoin.value = it },
                    onViewMatchDetails = { viewModel.selectedMatchForDetails.value = it }
                )

                AppTab.MATCHES -> MatchesScreen(
                    matches = matches,
                    participants = participants,
                    userProfile = userProfile,
                    currentFilter = matchCategoryFilter,
                    onFilterChange = { viewModel.setMatchCategoryFilter(it) },
                    onJoinMatch = { viewModel.selectedMatchForJoin.value = it },
                    onViewMatchDetails = { viewModel.selectedMatchForDetails.value = it }
                )

                AppTab.TOPUP -> TopUpScreen(
                    packages = packages,
                    orders = topUpOrders,
                    userProfile = userProfile,
                    currentGame = topUpGameFilter,
                    onGameChange = { viewModel.setTopUpGameFilter(it) },
                    onSelectPackage = { viewModel.selectedPackageForCheckout.value = it }
                )

                AppTab.WALLET -> WalletScreen(
                    userProfile = userProfile,
                    transactions = transactions,
                    currentFilter = walletTxFilter,
                    onFilterChange = { viewModel.setWalletTxFilter(it) },
                    onOpenDeposit = { viewModel.showDepositDialog.value = true },
                    onOpenWithdraw = { viewModel.showWithdrawDialog.value = true }
                )

                AppTab.SUPPORT -> SupportScreen(
                    tickets = tickets,
                    onOpenNewTicket = { viewModel.showNewTicketDialog.value = true }
                )

                AppTab.ADMIN -> AdminScreen(
                    userProfile = userProfile,
                    matches = matches,
                    transactions = transactions,
                    orders = topUpOrders,
                    onToggleAdmin = { viewModel.toggleAdminMode(it) },
                    onPublishRoomClick = { viewModel.adminSelectedMatchForRoom.value = it },
                    onDeclareWinnerClick = { viewModel.adminSelectedMatchForWinner.value = it },
                    onApproveDeposit = { viewModel.approveDeposit(it) },
                    onRejectDeposit = { viewModel.rejectDeposit(it) },
                    onCompleteOrder = { viewModel.completeTopUpOrder(it) },
                    onRejectOrder = { viewModel.rejectTopUpOrder(it) },
                    onCreateMatchClick = { viewModel.showCreateMatchDialog.value = true }
                )
            }
        }
    }

    // Dialogs
    selectedMatchForJoin?.let { match ->
        JoinMatchDialog(
            match = match,
            userProfile = userProfile,
            onDismiss = { viewModel.selectedMatchForJoin.value = null },
            onConfirm = { ign, uid -> viewModel.joinMatch(match, ign, uid) }
        )
    }

    selectedMatchForDetails?.let { match ->
        val isJoined = participants.any { it.matchId == match.id && it.userUid == userProfile.id }
        val myPart = participants.find { it.matchId == match.id && it.userUid == userProfile.id }
        MatchDetailsDialog(
            match = match,
            isJoined = isJoined,
            slotNumber = myPart?.slotNumber,
            onDismiss = { viewModel.selectedMatchForDetails.value = null },
            onJoinClick = {
                viewModel.selectedMatchForDetails.value = null
                viewModel.selectedMatchForJoin.value = match
            }
        )
    }

    selectedPackageForCheckout?.let { pack ->
        TopUpCheckoutDialog(
            pack = pack,
            userProfile = userProfile,
            onDismiss = { viewModel.selectedPackageForCheckout.value = null },
            onSubmit = { p, uid, name, method, sender, trx ->
                viewModel.submitTopUpCheckout(p, uid, name, method, sender, trx)
            }
        )
    }

    if (showDepositDialog) {
        DepositDialog(
            onDismiss = { viewModel.showDepositDialog.value = false },
            onSubmit = { amt, method, sender, trx ->
                viewModel.submitDeposit(amt, method, sender, trx)
            }
        )
    }

    if (showWithdrawDialog) {
        WithdrawDialog(
            userProfile = userProfile,
            onDismiss = { viewModel.showWithdrawDialog.value = false },
            onSubmit = { amt, method, receiver ->
                viewModel.submitWithdrawal(amt, method, receiver)
            }
        )
    }

    if (showNewTicketDialog) {
        NewTicketDialog(
            onDismiss = { viewModel.showNewTicketDialog.value = false },
            onSubmit = { cat, msg ->
                viewModel.submitSupportTicket(cat, msg)
            }
        )
    }

    if (showProfileDialog) {
        ProfileDialog(
            userProfile = userProfile,
            onDismiss = { viewModel.showProfileDialog.value = false },
            onToggleAdmin = { viewModel.toggleAdminMode(it) }
        )
    }

    adminRoomMatch?.let { match ->
        PublishRoomDialog(
            match = match,
            onDismiss = { viewModel.adminSelectedMatchForRoom.value = null },
            onPublish = { id, room, pass ->
                viewModel.publishRoom(id, room, pass)
            }
        )
    }

    adminWinnerMatch?.let { match ->
        DeclareWinnerDialog(
            match = match,
            onDismiss = { viewModel.adminSelectedMatchForWinner.value = null },
            onDeclare = { id, ign, kills ->
                viewModel.declareWinner(id, ign, kills)
            }
        )
    }

    if (showCreateMatchDialog) {
        CreateMatchDialog(
            onDismiss = { viewModel.showCreateMatchDialog.value = false },
            onCreate = { viewModel.createMatch(it) }
        )
    }
}

@Composable
fun CyberBottomNavigation(
    currentTab: AppTab,
    isAdmin: Boolean,
    onSelectTab: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .border(width = 1.dp, color = CyberCardBorder, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        color = CyberBgElevated,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tabs = buildList {
                add(Triple(AppTab.HOME, "Home", Icons.Default.Home))
                add(Triple(AppTab.MATCHES, "Matches", Icons.Default.SportsEsports))
                add(Triple(AppTab.TOPUP, "Top-Up", Icons.Default.Diamond))
                add(Triple(AppTab.WALLET, "Wallet", Icons.Default.AccountBalanceWallet))
                add(Triple(AppTab.SUPPORT, "Support", Icons.Default.Headphones))
                add(Triple(AppTab.ADMIN, "Admin", Icons.Default.AdminPanelSettings))
            }

            tabs.forEach { (tab, title, icon) ->
                val isSelected = currentTab == tab
                val tint = if (isSelected) {
                    if (tab == AppTab.ADMIN) CyberGold else CyberCyan
                } else CyberTextMuted

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSelectTab(tab) }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("nav_tab_${tab.name.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = tint,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = title,
                        color = tint,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
