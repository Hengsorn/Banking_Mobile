package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // e.g. "Dining & Drinks", "Electronics", "Income"
    val systemCategory: String, // "All", "Food", "Shopping", "Bills", "Travel"
    val time: String, // e.g., "12:45 PM"
    val date: String, // e.g., "Today, Oct 24"
    val amount: Double, // negative for expense, positive for income
    val secondaryIndicator: String, // e.g., "4.2% Cash back", "Pending", "Cleared"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val emailOrMobile: String,
    val avatarResName: String, // identifier for local preview avatar matching HTML style
    val initialLetter: String = name.firstOrNull()?.toString() ?: "U"
)

@Entity(tableName = "saving_goals")
data class SavingGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String, // e.g. "New Home Fund"
    val targetAmount: Double,
    val savedAmount: Double,
    val iconName: String // "home" or "flight"
)

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey val id: String, // "physical" or "virtual"
    val name: String, // "Lumina Platinum" or "Online Shopping"
    val number: String, // "**** **** **** 8842"
    val holder: String, // "James Sterling" or "Exp 12/26"
    val expiry: String, // "09/27"
    val isFrozen: Boolean = false,
    val contactlessEnabled: Boolean = true,
    val internationalEnabled: Boolean = false,
    val onlineEnabled: Boolean = true,
    val spendingLimit: Double = 5000.0,
    val spendingUsed: Double = 3759.50
)
