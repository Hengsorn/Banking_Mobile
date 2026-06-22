package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BankDao {
    // Transactions
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun getTransactionCount(): Int

    // Contacts
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactEntity>)

    @Query("SELECT COUNT(*) FROM contacts")
    suspend fun getContactCount(): Int

    // Saving Goals
    @Query("SELECT * FROM saving_goals")
    fun getAllSavingGoals(): Flow<List<SavingGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavingGoal(goal: SavingGoalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavingGoals(goals: List<SavingGoalEntity>)

    @Query("UPDATE saving_goals SET savedAmount = :amount WHERE id = :id")
    suspend fun updateGoalSavedAmount(id: Int, amount: Double)

    @Query("SELECT COUNT(*) FROM saving_goals")
    suspend fun getSavingGoalCount(): Int

    // Cards
    @Query("SELECT * FROM cards")
    fun getAllCards(): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :cardId LIMIT 1")
    suspend fun getCardById(cardId: String): CardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardEntity>)

    @Query("UPDATE cards SET isFrozen = :frozen WHERE id = :cardId")
    suspend fun updateCardFrozenState(cardId: String, frozen: Boolean)

    @Query("UPDATE cards SET contactlessEnabled = :enabled WHERE id = :cardId")
    suspend fun updateCardContactless(cardId: String, enabled: Boolean)

    @Query("UPDATE cards SET internationalEnabled = :enabled WHERE id = :cardId")
    suspend fun updateCardInternational(cardId: String, enabled: Boolean)

    @Query("UPDATE cards SET onlineEnabled = :enabled WHERE id = :cardId")
    suspend fun updateCardOnline(cardId: String, enabled: Boolean)

    @Query("UPDATE cards SET spendingLimit = :limit WHERE id = :cardId")
    suspend fun updateCardLimit(cardId: String, limit: Double)

    @Query("SELECT COUNT(*) FROM cards")
    suspend fun getCardCount(): Int
}
