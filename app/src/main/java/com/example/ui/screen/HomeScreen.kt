package com.example.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TransactionEntity
import com.example.ui.BankViewModel
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val totalBalance by viewModel.totalBalance.collectAsState()
    val transactions by viewModel.transactions.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Card: Balance Indicator
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp)) // Match rounded-[28px] of Design HTML
                    .background(
                        Brush.linearGradient(
                            colors = listOf(CardNavyGradStart, CardNavyGradEnd)
                        )
                    )
            ) {
                // Decorative Geometric Circle Top-Right as per HTML
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 32.dp, y = (-32).dp)
                        .size(128.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD0BCFF).copy(alpha = 0.5f))
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Total Balance",
                        color = PrimaryNavy.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = String.format("$%,.2f", totalBalance),
                        color = PrimaryNavy,
                        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(PrimaryNavy.copy(alpha = 0.1f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.TrendingUp,
                                        contentDescription = "Growth",
                                        tint = PrimaryNavy,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "+2.4%",
                                        color = PrimaryNavy,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = "This Month",
                                color = PrimaryNavy.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Circular Avatar Pile
                        Row(
                            horizontalArrangement = Arrangement.spacedBy((-8).dp)
                        ) {
                            val colors = listOf(Color(0xFF6750A4), Color(0xFFEADDFF), Color(0xFF21005D))
                            colors.forEachIndexed { idx, color ->
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .background(Brush.radialGradient(listOf(Color.White.copy(0.2f), Color.Transparent)))
                                        .border(1.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (idx == 2) "+3" else "",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Actions Grid (Send, Request, Pay)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionButton(
                    title = "Send",
                    icon = Icons.Default.Send,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("send_button")
                ) {
                    viewModel.setTab("transfer")
                }
                QuickActionButton(
                    title = "Request",
                    icon = Icons.Default.ArrowDownward,
                    modifier = Modifier.weight(1f)
                ) {
                    // Action Request (Just visual / demo)
                }
                QuickActionButton(
                    title = "Pay",
                    icon = Icons.Default.Payment,
                    modifier = Modifier.weight(1f)
                ) {
                    // Action Pay (Just visual / demo)
                }
            }
        }

        // Insights Bento Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
                shape = RoundedCornerShape(20.dp),
                border = AssistChipDefaults.assistChipBorder(true)?.let { BorderStroke(1.dp, GrayBorder) }
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Monthly Budget",
                                style = MaterialTheme.typography.headlineSmall,
                                color = PrimaryNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "72% used of $4,500",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextColorSecondary
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Insights",
                            tint = EmeraldGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress track
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2E8F0))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.72f)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(EmeraldGreen)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BudgetChip("Food & Drink", true)
                        BudgetChip("Rent", false)
                        BudgetChip("Subscriptions", false)
                    }
                }
            }
        }

        // Recent Transactions Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.headlineSmall,
                    color = PrimaryNavy,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "View All",
                    color = EmeraldGreen,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { viewModel.setTab("activity") }
                        .testTag("view_all_transactions")
                )
            }
        }

        // Transactions list (First 3 items)
        val displayTxs = transactions.take(3)
        if (displayTxs.isEmpty()) {
            item {
                Text(
                    text = "No recent transactions found.",
                    color = TextColorSecondary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(displayTxs) { tx ->
                TransactionCardItem(transaction = tx)
            }
        }

        // Safe spacing bottom
        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .height(108.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GrayBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(EmeraldGreen.copy(0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = TextColorPrimary
            )
        }
    }
}

@Composable
fun BudgetChip(text: String, active: Boolean) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (active) EmeraldGreen.copy(0.15f) else Color(0xFFF1F5F9))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = if (active) EmeraldGreen else TextColorSecondary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun TransactionCardItem(transaction: TransactionEntity) {
    val isIncome = transaction.amount > 0
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Detail view if needed */ },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GrayBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category circular icon matching HTML specs
                val (icon, tintBg, tintIcon) = getIconDataForCategory(transaction.systemCategory)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(tintBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = transaction.category,
                        tint = tintIcon,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = transaction.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextColorPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${transaction.category} • ${transaction.time}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextColorSecondary
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (isIncome) {
                        String.format("+$%,.2f", transaction.amount)
                    } else {
                        String.format("-$%,.2f", Math.abs(transaction.amount))
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (isIncome) EmeraldGreen else TextColorPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                _root_ide_package_.androidx.compose.material3.Text(
                    text = transaction.secondaryIndicator,
                    style = _root_ide_package_.androidx.compose.material3.MaterialTheme.typography.labelSmall,
                    color = if (isIncome || transaction.secondaryIndicator.contains("back")) EmeraldGreen else if (transaction.secondaryIndicator == "Pending") OrangePending else TextLightGray
                )
            }
        }
    }
}

// Map database system category string to appropriate visual indicators
@Composable
fun getIconDataForCategory(category: String): Triple<androidx.compose.ui.graphics.vector.ImageVector, Color, Color> {
    return when (category) {
        "Food" -> Triple(Icons.Default.Restaurant, SecondaryContainerGreen.copy(0.3f), EmeraldGreen)
        "Shopping" -> Triple(Icons.Default.ShoppingBag, Color(0xFFFFEDD5), Color(0xFFEA580C))
        "Income" -> Triple(Icons.Default.Payments, Color(0xFFD1FAE5), Color(0xFF059669))
        "Bills" -> Triple(Icons.Default.Bolt, Color(0xFFFEF9C3), Color(0xFFCA8A04))
        "Travel" -> Triple(Icons.Default.Commute, Color(0xFFE0F2FE), Color(0xFF0284C7))
        else -> Triple(Icons.Default.CreditCard, Color(0xFFF1F5F9), TextColorSecondary)
    }
}
