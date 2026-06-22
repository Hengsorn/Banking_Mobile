package com.example.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BankViewModel
import com.example.ui.theme.*

@Composable
fun ActivityScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.transactions.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    // Client-side exact filtering matching search and category selected
    val filteredTransactions = remember(transactions, searchQuery, selectedCategory) {
        transactions.filter { tx ->
            val matchesCategory = selectedCategory == "All" || tx.systemCategory == selectedCategory
            val matchesSearch = searchQuery.isEmpty() ||
                    tx.title.contains(searchQuery, ignoreCase = true) ||
                    tx.category.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section Title
            Text(
                text = "Transaction History",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp),
                color = PrimaryNavy,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Search input field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = {
                    Text(
                        text = "Search merchants or categories...",
                        color = TextColorSecondary.copy(0.6f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextColorSecondary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceContainerLow,
                    unfocusedContainerColor = SurfaceContainerLow,
                    focusedBorderColor = EmeraldGreen,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = EmeraldGreen
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Horizontal Category Filter Chips list
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("All", "Food", "Shopping", "Bills", "Travel").forEach { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSelected) EmeraldGreen else SecondaryContainerGreen.copy(0.3f)
                            )
                            .clickable { viewModel.updateSelectedCategory(category) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("filter_chip_$category")
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else EmeraldGreen,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Filters tune action button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SecondaryContainerGreen.copy(0.2f))
                        .clickable { /* Advanced Tuning options toggle */ }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filters Tool",
                        tint = EmeraldGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // LazyColumn Grouping List Items
            if (filteredTransactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "No transactions found",
                            style = MaterialTheme.typography.headlineSmall,
                            color = PrimaryNavy,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Try adjusting your filters or search term.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextColorSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Group transactions by date heading
                    val grouped = filteredTransactions.groupBy { it.date }
                    grouped.forEach { (date, list) ->
                        item {
                            Text(
                                text = date.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextColorSecondary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }

                        items(list) { tx ->
                            TransactionCardItem(transaction = tx)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                }
            }
        }

        // Floating action report downloader
        FloatingActionButton(
            onClick = { /* Export report handler */ },
            containerColor = PrimaryNavy,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 24.dp)
                .size(56.dp)
                .testTag("export_fab")
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Download Report",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
