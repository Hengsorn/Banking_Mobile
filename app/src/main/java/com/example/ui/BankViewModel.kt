package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BankViewModel(application: Application) : AndroidViewModel(application) {

    private val bankDao = BankDatabase.getDatabase(application).bankDao()
    private val repository = BankRepository(bankDao)

    // Current navigation tab: "home", "activity", "transfer", "cards", "wealth"
    private val _currentTab = MutableStateFlow("home")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    // Activity Screen filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Dynamic Balance (Simulated and kept in database or state)
    // Starting total wealth = 412,850.42
    // Starting total balance = 142,580.42
    // Starting Lumina Savings = 24,500.50
    private val _transferAmount = MutableStateFlow("")
    val transferAmount: StateFlow<String> = _transferAmount.asStateFlow()

    private val _selectedContact = MutableStateFlow<ContactEntity?>(null)
    val selectedContact: StateFlow<ContactEntity?> = _selectedContact.asStateFlow()

    private val _contactSearchQuery = MutableStateFlow("")
    val contactSearchQuery: StateFlow<String> = _contactSearchQuery.asStateFlow()

    private val _repeatPayment = MutableStateFlow(false)
    val repeatPayment: StateFlow<Boolean> = _repeatPayment.asStateFlow()

    // Real balances stored in state but adjusted by transfers
    private val _totalBalance = MutableStateFlow(142580.42)
    val totalBalance: StateFlow<Double> = _totalBalance.asStateFlow()

    private val _luminaSavingsBalance = MutableStateFlow(24500.50)
    val luminaSavingsBalance: StateFlow<Double> = _luminaSavingsBalance.asStateFlow()

    private val _totalWealth = MutableStateFlow(412850.42)
    val totalWealth: StateFlow<Double> = _totalWealth.asStateFlow()

    // Database flow bindings
    val transactions: StateFlow<List<TransactionEntity>> = repository.transactions
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val contacts: StateFlow<List<ContactEntity>> = repository.contacts
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val savingGoals: StateFlow<List<SavingGoalEntity>> = repository.savingGoals
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val cards: StateFlow<List<CardEntity>> = repository.cards
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        // Initialize and seed database if necessary
        viewModelScope.launch {
            repository.checkAndSeedDatabase()
        }
    }

    fun setTab(tab: String) {
        _currentTab.value = tab
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateContactSearchQuery(query: String) {
        _contactSearchQuery.value = query
    }

    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateTransferAmount(amount: String) {
        _transferAmount.value = amount
    }

    fun selectContact(contact: ContactEntity) {
        _selectedContact.value = contact
    }

    fun toggleRepeatPayment() {
        _repeatPayment.value = !_repeatPayment.value
    }

    fun toggleCardFreeze(cardId: String) {
        viewModelScope.launch {
            val card = cards.value.firstOrNull { it.id == cardId }
            if (card != null) {
                repository.updateCardFrozenState(cardId, !card.isFrozen)
            }
        }
    }

    fun toggleCardContactless(cardId: String) {
        viewModelScope.launch {
            val card = cards.value.firstOrNull { it.id == cardId }
            if (card != null) {
                repository.updateCardContactless(cardId, !card.contactlessEnabled)
            }
        }
    }

    fun toggleCardInternational(cardId: String) {
        viewModelScope.launch {
            val card = cards.value.firstOrNull { it.id == cardId }
            if (card != null) {
                repository.updateCardInternational(cardId, !card.internationalEnabled)
            }
        }
    }

    fun toggleCardOnline(cardId: String) {
        viewModelScope.launch {
            val card = cards.value.firstOrNull { it.id == cardId }
            if (card != null) {
                repository.updateCardOnline(cardId, !card.onlineEnabled)
            }
        }
    }

    fun adjustSpendingLimit(cardId: String, newLimit: Double) {
        viewModelScope.launch {
            repository.updateCardLimit(cardId, newLimit)
        }
    }

    fun addSavingGoalFund(goalId: Int, increment: Double) {
        viewModelScope.launch {
            val goal = savingGoals.value.firstOrNull { it.id == goalId }
            if (goal != null) {
                val newAmount = goal.savedAmount + increment
                if (newAmount <= goal.targetAmount) {
                    repository.updateGoalSavedAmount(goalId, newAmount)
                    _totalWealth.value += increment
                    _totalBalance.value -= increment
                }
            }
        }
    }

    // Process Transfer
    fun performTransfer(): Boolean {
        val amountStr = _transferAmount.value
        val amount = amountStr.toDoubleOrNull() ?: return false
        val contact = _selectedContact.value ?: return false

        if (amount <= 0 || amount > _luminaSavingsBalance.value) {
            return false
        }

        viewModelScope.launch {
            // Deduct from state balances
            _luminaSavingsBalance.value -= amount
            _totalBalance.value -= amount
            _totalWealth.value -= amount

            // Insert matching transaction Entity
            val newTx = TransactionEntity(
                title = "Transfer to ${contact.name}",
                category = "Move Money",
                systemCategory = "Bills", // Maps closely to bills/transfers category
                time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
                date = "Today, Oct 24",
                amount = -amount,
                secondaryIndicator = if (_repeatPayment.value) "Recurring" else "Cleared"
            )
            repository.insertTransaction(newTx)

            // Reset inputs
            _transferAmount.value = ""
            _selectedContact.value = null
            setTab("activity") // Switch to activity screen to view transaction history immediately!
        }
        return true
    }
}
