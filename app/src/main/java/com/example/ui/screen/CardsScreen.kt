package com.example.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Contactless
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CardEntity
import com.example.ui.BankViewModel
import com.example.ui.theme.*

@Composable
fun CardsScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val cards by viewModel.cards.collectAsState()
    val scrollState = rememberScrollState()

    // Track which card in container is currently selected/focussed
    var selectedCardId by remember { mutableStateOf("physical") }
    val selectedCard = cards.firstOrNull { it.id == selectedCardId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Upper Title Header
        Column {
            Text(
                text = "My Cards",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp),
                color = PrimaryNavy,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Manage your physical and virtual payment methods securely.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextColorSecondary
            )
        }

        // Horizontal Snap Slider cards
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cards) { card ->
                val isSelected = selectedCardId == card.id
                BankCreditCardUI(
                    card = card,
                    isSelected = isSelected,
                    onClick = { selectedCardId = card.id }
                )
            }

            // Simple dashed placeholder card loader
            item {
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(190.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { /* trigger visual creation logic */ }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerLow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = "Add Card icon",
                                tint = TextColorSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "New Card",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextColorSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Action controls (Freeze, PIN)
        selectedCard?.let { activeCard ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Freeze Card button toggle
                CardActionControlItem(
                    title = if (activeCard.isFrozen) "Unfreeze Card" else "Freeze Card",
                    subtitle = "Temporary lock",
                    icon = Icons.Outlined.AcUnit,
                    active = activeCard.isFrozen,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("freeze_card_btn")
                ) {
                    viewModel.toggleCardFreeze(activeCard.id)
                }

                CardActionControlItem(
                    title = "Manage PIN",
                    subtitle = "View or change",
                    icon = Icons.Default.Pin,
                    active = false,
                    modifier = Modifier.weight(1f)
                ) {
                    // Visual action pin dialog
                }
            }

            // Spending Limit Section Container
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreen.copy(0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = "Limit meter",
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Spending Limit",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextColorPrimary
                                )
                                Text(
                                    text = "Monthly remaining",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextColorSecondary
                                )
                            }
                        }

                        val remaining = activeCard.spendingLimit - activeCard.spendingUsed
                        Text(
                            text = String.format("$%,.2f", remaining),
                            style = MaterialTheme.typography.headlineSmall,
                            color = PrimaryNavy,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress track
                    val ratio = (activeCard.spendingUsed / activeCard.spendingLimit).toFloat().coerceIn(0f, 1f)
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
                                .background(EmeraldGreen)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = String.format("Used: $%,.2f", activeCard.spendingUsed),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextColorSecondary
                        )
                        Text(
                            text = String.format("Limit: $%,.2f", activeCard.spendingLimit),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextColorSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Adjust limit card button
                    OutlinedButton(
                        onClick = {
                            // Slider modal demo: increments spending limit of active card
                            viewModel.adjustSpendingLimit(activeCard.id, activeCard.spendingLimit + 500.0)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("adjust_limits"),
                        border = BorderStroke(1.dp, GrayBorder),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Adjust Limits (Add $500)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    }
                }
            }

            // Checklist list items (Contactless Payments, International, Online transactions)
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "SECURITY & PRIVACY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextColorSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayBorder)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SecuritySwitchRow(
                            title = "Contactless Payments",
                            icon = Icons.Outlined.Contactless,
                            checked = activeCard.contactlessEnabled,
                            onCheckedChange = { viewModel.toggleCardContactless(activeCard.id) }
                        )

                        Divider(color = GrayBorder, modifier = Modifier.padding(horizontal = 16.dp))

                        SecuritySwitchRow(
                            title = "International Usage",
                            icon = Icons.Outlined.Language,
                            checked = activeCard.internationalEnabled,
                            onCheckedChange = { viewModel.toggleCardInternational(activeCard.id) }
                        )

                        Divider(color = GrayBorder, modifier = Modifier.padding(horizontal = 16.dp))

                        SecuritySwitchRow(
                            title = "Online Transactions",
                            icon = Icons.Default.ShoppingCart,
                            checked = activeCard.onlineEnabled,
                            onCheckedChange = { viewModel.toggleCardOnline(activeCard.id) }
                        )
                    }
                }
            }
        }

        // Danger Zone report stole action
        OutlinedButton(
            onClick = { /* trigger lost stole sheet */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("report_stolen_btn"),
            border = BorderStroke(1.dp, ErrorCrimson.copy(0.3f)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorCrimson),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Report,
                    contentDescription = "Danger Warning symbol",
                    tint = ErrorCrimson,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Report Lost or Stolen",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = ErrorCrimson
                )
            }
        }

        // Spacing Safe Area Bottom
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun BankCreditCardUI(
    card: CardEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isPhysical = card.id == "physical"
    val backgroundBrush = if (isPhysical) {
        Brush.linearGradient(colors = listOf(CardNavyGradStart, CardNavyGradEnd))
    } else {
        Brush.linearGradient(colors = listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0)))
    }

    val textColor = if (isPhysical) Color.White else PrimaryNavy
    val labelColor = if (isPhysical) TextLightGray else TextColorSecondary

    Box(
        modifier = Modifier
            .width(300.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundBrush)
            .clickable(onClick = onClick)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) EmeraldGreen else if (isPhysical) Color.Transparent else GrayBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("credit_card_${card.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = if (isPhysical) "Physical Card" else "Virtual Card",
                        style = MaterialTheme.typography.labelSmall,
                        color = labelColor
                    )
                    Text(
                        text = card.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isPhysical) Color.White else EmeraldGreen
                    )
                }

                Icon(
                    imageVector = if (isPhysical) Icons.Outlined.Contactless else Icons.Outlined.Language,
                    contentDescription = "Wireless pay symbol",
                    tint = if (isPhysical) Color.White else EmeraldGreen,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column {
                Text(
                    text = card.number,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = card.holder,
                        style = MaterialTheme.typography.labelSmall,
                        color = labelColor
                    )
                    if (card.expiry.isNotEmpty()) {
                        Text(
                            text = card.expiry,
                            style = MaterialTheme.typography.labelSmall,
                            color = labelColor
                        )
                    }
                }
            }
        }

        // Frozen Blur Overlay
        if (card.isFrozen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(0.9f))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AcUnit,
                            contentDescription = "Frozen Badge Ice",
                            tint = PrimaryNavy,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "FROZEN",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = PrimaryNavy
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardActionControlItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    active: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .height(96.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GrayBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (active) EmeraldGreen.copy(0.12f) else SurfaceContainerLow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (active) EmeraldGreen else TextColorSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (active) EmeraldGreen else TextColorPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = TextColorSecondary
                )
            }
        }
    }
}

@Composable
fun SecuritySwitchRow(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = TextColorSecondary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextColorPrimary
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = EmeraldGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = GrayBorder
            )
        )
    }
}
