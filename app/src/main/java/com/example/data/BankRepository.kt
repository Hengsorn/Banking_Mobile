package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BankRepository(private val bankDao: BankDao) {

    val transactions: Flow<List<TransactionEntity>> = bankDao.getAllTransactions()
    val contacts: Flow<List<ContactEntity>> = bankDao.getAllContacts()
    val savingGoals: Flow<List<SavingGoalEntity>> = bankDao.getAllSavingGoals()
    val cards: Flow<List<CardEntity>> = bankDao.getAllCards()

    suspend fun checkAndSeedDatabase() = withContext(Dispatchers.IO) {
        // Seed Cards if empty
        if (bankDao.getCardCount() == 0) {
            val initialCards = listOf(
                CardEntity(
                    id = "physical",
                    name = "Lumina Platinum",
                    number = "**** **** **** 8842",
                    holder = "James Sterling",
                    expiry = "09/27",
                    isFrozen = false,
                    contactlessEnabled = true,
                    internationalEnabled = false,
                    onlineEnabled = true,
                    spendingLimit = 5000.0,
                    spendingUsed = 3759.50
                ),
                CardEntity(
                    id = "virtual",
                    name = "Online Shopping",
                    number = "**** **** **** 1055",
                    holder = "Exp 12/26",
                    expiry = "",
                    isFrozen = false,
                    contactlessEnabled = false,
                    internationalEnabled = true,
                    onlineEnabled = true,
                    spendingLimit = 2000.0,
                    spendingUsed = 420.0
                )
            )
            bankDao.insertCards(initialCards)
        }

        // Seed Contacts if empty
        if (bankDao.getContactCount() == 0) {
            val initialContacts = listOf(
                ContactEntity(name = "Elena R.", emailOrMobile = "elena.r@luminabank.com", avatarResName = "elena"),
                ContactEntity(name = "Marcus T.", emailOrMobile = "marcus.t@luminabank.com", avatarResName = "marcus"),
                ContactEntity(name = "David L.", emailOrMobile = "david.l@luminabank.com", avatarResName = "david"),
                ContactEntity(name = "Sasha K.", emailOrMobile = "sasha.k@luminabank.com", avatarResName = "sasha")
            )
            bankDao.insertContacts(initialContacts)
        }

        // Seed Saving Goals if empty
        if (bankDao.getSavingGoalCount() == 0) {
            val initialGoals = listOf(
                SavingGoalEntity(title = "New Home Fund", targetAmount = 120000.0, savedAmount = 98400.0, iconName = "home"),
                SavingGoalEntity(title = "Europe Trip", targetAmount = 15000.0, savedAmount = 6750.0, iconName = "flight")
            )
            bankDao.insertSavingGoals(initialGoals)
        }

        // Seed Transactions if empty
        if (bankDao.getTransactionCount() == 0) {
            val initialTransactions = listOf(
                TransactionEntity(
                    title = "The Green Kitchen",
                    category = "Dining & Drinks",
                    systemCategory = "Food",
                    time = "12:45 PM",
                    date = "Today, Oct 24",
                    amount = -42.80,
                    secondaryIndicator = "4.2% Cash back",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 30 // 30 mins ago
                ),
                TransactionEntity(
                    title = "Apple Store",
                    category = "Electronics",
                    systemCategory = "Shopping",
                    time = "10:15 AM",
                    date = "Today, Oct 24",
                    amount = -1299.00,
                    secondaryIndicator = "Pending",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 3 // 3 hours ago
                ),
                TransactionEntity(
                    title = "Monthly Salary",
                    category = "Income",
                    systemCategory = "Income",
                    time = "08:00 AM",
                    date = "Yesterday, Oct 23",
                    amount = 6450.00,
                    secondaryIndicator = "Cleared",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24 // 1 day ago
                ),
                TransactionEntity(
                    title = "Grid Power Co.",
                    category = "Utilities",
                    systemCategory = "Bills",
                    time = "04:30 PM",
                    date = "Yesterday, Oct 23",
                    amount = -115.24,
                    secondaryIndicator = "Direct Debit",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 30 // 1.2 days ago
                ),
                TransactionEntity(
                    title = "Uber Trip",
                    category = "Transportation",
                    systemCategory = "Travel",
                    time = "02:15 PM",
                    date = "Yesterday, Oct 23",
                    amount = -18.50,
                    secondaryIndicator = "Cleared",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 32 // 1.3 days ago
                ),
                TransactionEntity(
                    title = "Blue Bottle Coffee",
                    category = "Food & Drink",
                    systemCategory = "Food",
                    time = "11:20 AM",
                    date = "Yesterday, Oct 23",
                    amount = -12.50,
                    secondaryIndicator = "Cleared",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 35 // 1.5 days ago
                )
            )
            bankDao.insertTransactions(initialTransactions)
        }
    }

    suspend fun insertTransaction(transaction: TransactionEntity) = withContext(Dispatchers.IO) {
        bankDao.insertTransaction(transaction)
    }

    suspend fun updateCardFrozenState(cardId: String, frozen: Boolean) = withContext(Dispatchers.IO) {
        bankDao.updateCardFrozenState(cardId, frozen)
    }

    suspend fun updateCardContactless(cardId: String, enabled: Boolean) = withContext(Dispatchers.IO) {
        bankDao.updateCardContactless(cardId, enabled)
    }

    suspend fun updateCardInternational(cardId: String, enabled: Boolean) = withContext(Dispatchers.IO) {
        bankDao.updateCardInternational(cardId, enabled)
    }

    suspend fun updateCardOnline(cardId: String, enabled: Boolean) = withContext(Dispatchers.IO) {
        bankDao.updateCardOnline(cardId, enabled)
    }

    suspend fun updateCardLimit(cardId: String, limit: Double) = withContext(Dispatchers.IO) {
        bankDao.updateCardLimit(cardId, limit)
    }

    suspend fun updateGoalSavedAmount(id: Int, amount: Double) = withContext(Dispatchers.IO) {
        bankDao.updateGoalSavedAmount(id, amount)
    }
}
