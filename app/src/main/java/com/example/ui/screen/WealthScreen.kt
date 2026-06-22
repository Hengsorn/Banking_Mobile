package com.example.ui.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SavingGoalEntity
import com.example.ui.BankViewModel
import com.example.ui.theme.*

@Composable
fun WealthScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val totalWealth by viewModel.totalWealth.collectAsState()
    val savingGoals by viewModel.savingGoals.collectAsState()
    val scrollState = rememberScrollState()

    // 6-month bars mock values
    val chartHeights = listOf(0.3f, 0.45f, 0.4f, 0.65f, 0.85f, 1.0f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Panel Header
        Column {
            Text(
                text = "TOTAL WEALTH",
                style = MaterialTheme.typography.labelSmall,
                color = TextColorSecondary,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format("$%,.2f", totalWealth),
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                    color = PrimaryNavy,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Wealth growth percentage indicator",
                        tint = EmeraldGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "+4.2%",
                        color = EmeraldGreen,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Investment portfolio chart card + Allocation panel (Grid of 1 vs 2 cols on wide, or simple stacked)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, GrayBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "Investment Portfolio",
                            style = MaterialTheme.typography.headlineSmall,
                            color = PrimaryNavy,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Growth over last 6 months",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextColorSecondary
                        )
                    }

                    // Time range selector chips
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(EmeraldGreen.copy(0.12f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "6M",
                                color = EmeraldGreen,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Transparent)
                                .clickable { /* Switch range display to Year */ }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "1Y",
                                color = TextColorSecondary,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Beautiful interactive Custom bar graph drawing with pure compose blocks
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    // Chart background fading gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(EmeraldGreen.copy(0.1f), Color.Transparent)
                                )
                            )
                    )

                    // Vertical bars Row alignment at the bottom
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        chartHeights.forEachIndexed { idx, heightProportion ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(heightProportion)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(
                                        if (idx == chartHeights.lastIndex) EmeraldGreen else EmeraldGreen.copy(
                                            0.4f + (0.1f * idx)
                                        )
                                    )
                            )
                        }
                    }

                    // Dash track line overlay for active index metric
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .align(Alignment.CenterEnd)
                            .offset(x = (-32).dp)
                            .background(EmeraldGreen.copy(0.5f))
                    )

                    // Interactive Tooltip hover block
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-8).dp, y = 16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(PrimaryNavy)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$342K",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Asset Allocation Legendary card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, GrayBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Allocation",
                    style = MaterialTheme.typography.headlineSmall,
                    color = PrimaryNavy,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AllocationLegendRow(title = "Equities", percent = "65%", color = EmeraldGreen)
                    AllocationLegendRow(title = "Bonds", percent = "25%", color = PrimaryNavy)
                    AllocationLegendRow(title = "Cash", percent = "10%", color = TextColorSecondary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Portfolio reallocation queued!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("rebalance_btn"),
                    border = BorderStroke(1.dp, GrayBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Rebalance Portfolio",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                }
            }
        }

        // Savings Goals Section title & Horizontal button
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Savings Goals",
                    style = MaterialTheme.typography.headlineSmall,
                    color = PrimaryNavy,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "+ New Goal",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen,
                    modifier = Modifier.clickable { /* trigger creation input */ }
                )
            }

            // Stacking Savings items (Today Home Fund, trip)
            savingGoals.forEach { goal ->
                GoalProgressCard(goal = goal) {
                    // Click handler adds $1,000 progress for reactive dynamic demo display!
                    viewModel.addSavingGoalFund(goal.id, 1000.0)
                }
            }
        }

        // Market Automated smart recommendation block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(ContainerNavy)
        ) {
            // Decorative Geometric circle
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 16.dp, y = (-24).dp)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD0BCFF).copy(alpha = 0.4f))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Automated Investing",
                    color = PrimaryNavy,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Lumina Smart-AI rebalances your portfolio daily to maximize returns while maintaining your risk profile.",
                    color = TextLightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        Toast.makeText(context, "Configuring Lumina AI models...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.testTag("configure_ai_portfolio"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryNavy,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Configure AI Portfolio",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Spacing Safe Area Bottom
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun AllocationLegendRow(
    title: String,
    percent: String,
    color: Color
) {
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
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextColorPrimary
            )
        }
        Text(
            text = percent,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = TextColorPrimary
        )
    }
}

@Composable
fun GoalProgressCard(
    goal: SavingGoalEntity,
    onFundMore: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GrayBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Circular goal visual icon matcher
                    val icon = if (goal.iconName == "home") Icons.Default.Home else Icons.Default.Flight
                    val colorBg = if (goal.iconName == "home") SecondaryContainerGreen.copy(0.3f) else ContainerNavy.copy(0.08f)
                    val colorIcon = if (goal.iconName == "home") EmeraldGreen else PrimaryNavy
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = goal.title,
                            tint = colorIcon,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = goal.title,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextColorPrimary
                        )
                        _root_ide_package_.androidx.compose.material3.Text(
                            text = String.format("Target: $%,.0f", goal.targetAmount),
                            style = _root_ide_package_.androidx.compose.material3.MaterialTheme.typography.labelSmall,
                            color = TextColorSecondary
                        )
                    }
                }

                // Percent Complete indicator
                val ratio = (goal.savedAmount / goal.targetAmount).toFloat().coerceIn(0f, 1f)
                val percent = (ratio * 100).toInt()
                Text(
                    text = "$percent%",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextColorPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            val ratio = (goal.savedAmount / goal.targetAmount).toFloat().coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerLow)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(ratio)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(if (goal.iconName == "home") EmeraldGreen else PrimaryNavy)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format("$%,.0f saved", goal.savedAmount),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextColorPrimary
                )

                // Incremental fund button (+ $1,000)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onFundMore)
                        .border(1.dp, GrayBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = "Fund Goal chevron button",
                        tint = TextColorSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
