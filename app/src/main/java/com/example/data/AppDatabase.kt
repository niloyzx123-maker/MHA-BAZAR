package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM tournament_matches ORDER BY status ASC, id DESC")
    fun getAllMatches(): Flow<List<TournamentMatch>>

    @Query("SELECT * FROM tournament_matches WHERE id = :id")
    suspend fun getMatchById(id: String): TournamentMatch?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: TournamentMatch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<TournamentMatch>)

    @Update
    suspend fun updateMatch(match: TournamentMatch)

    @Query("UPDATE tournament_matches SET roomId = :roomId, roomPassword = :password, roomRevealed = :revealed WHERE id = :id")
    suspend fun updateRoom(id: String, roomId: String, password: String, revealed: Boolean)

    @Query("UPDATE tournament_matches SET winnerIgn = :winnerIgn, winnerKills = :kills, status = 'COMPLETED' WHERE id = :id")
    suspend fun setWinner(id: String, winnerIgn: String, kills: Int)

    @Query("UPDATE tournament_matches SET joinedSlots = joinedSlots + 1 WHERE id = :id")
    suspend fun incrementJoined(id: String)

    @Query("SELECT * FROM match_participants ORDER BY joinedAt DESC")
    fun getAllParticipants(): Flow<List<MatchParticipant>>

    @Query("SELECT * FROM match_participants WHERE matchId = :matchId")
    fun getParticipantsForMatch(matchId: String): Flow<List<MatchParticipant>>

    @Query("SELECT * FROM match_participants WHERE matchId = :matchId AND userUid = :userUid LIMIT 1")
    suspend fun getParticipant(matchId: String, userUid: String): MatchParticipant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(participant: MatchParticipant)
}

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<WalletTransaction>>

    @Query("SELECT * FROM wallet_transactions WHERE id = :id")
    suspend fun getTransactionById(id: String): WalletTransaction?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: WalletTransaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<WalletTransaction>)

    @Query("UPDATE wallet_transactions SET status = :status WHERE id = :id")
    suspend fun updateTransactionStatus(id: String, status: String)
}

@Dao
interface TopUpDao {
    @Query("SELECT * FROM topup_packages")
    fun getAllPackages(): Flow<List<TopUpPackage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackages(packages: List<TopUpPackage>)

    @Query("SELECT * FROM topup_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<TopUpOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: TopUpOrder)

    @Query("UPDATE topup_orders SET status = :status WHERE id = :id")
    suspend fun updateOrderStatus(id: String, status: String)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 'user_main' LIMIT 1")
    fun getUser(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 'user_main' LIMIT 1")
    suspend fun getUserSync(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProfile)

    @Update
    suspend fun updateUser(user: UserProfile)

    @Query("UPDATE user_profile SET depositBalance = :deposit, winningBalance = :winning WHERE id = 'user_main'")
    suspend fun updateBalances(deposit: Double, winning: Double)

    @Query("UPDATE user_profile SET isAdmin = :isAdmin WHERE id = 'user_main'")
    suspend fun setAdmin(isAdmin: Boolean)
}

@Dao
interface SupportDao {
    @Query("SELECT * FROM support_tickets ORDER BY timestamp DESC")
    fun getAllTickets(): Flow<List<SupportTicket>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: SupportTicket)

    @Query("UPDATE support_tickets SET reply = :reply, status = :status WHERE id = :id")
    suspend fun replyTicket(id: String, reply: String, status: String)
}

@Database(
    entities = [
        TournamentMatch::class,
        MatchParticipant::class,
        WalletTransaction::class,
        TopUpPackage::class,
        TopUpOrder::class,
        UserProfile::class,
        SupportTicket::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun matchDao(): MatchDao
    abstract fun walletDao(): WalletDao
    abstract fun topUpDao(): TopUpDao
    abstract fun userDao(): UserDao
    abstract fun supportDao(): SupportDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "arenazone_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
