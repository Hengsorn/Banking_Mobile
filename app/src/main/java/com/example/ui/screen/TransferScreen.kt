package com.example.ui.screen

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BankViewModel
import com.example.ui.theme.*

@Composable
fun TransferScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val transferAmount by viewModel.transferAmount.collectAsState()
    val selectedContact by viewModel.selectedContact.collectAsState()
    val contactSearchQuery by viewModel.contactSearchQuery.collectAsState()
    val repeatPayment by viewModel.repeatPayment.collectAsState()
    val luminaSavingsBalance by viewModel.luminaSavingsBalance.collectAsState()
    val contacts by viewModel.contacts.collectAsState()

    // Filter contacts based on search query
    val filteredContacts = remember(contacts, contactSearchQuery) {
        contacts.filter {
            contactSearchQuery.isEmpty() || it.name.contains(contactSearchQuery, ignoreCase = true)
        }
    }

    // Scroll state for content nesting (bento styling)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tab Navigation sub-tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Pay Person" to true, "Pay Bills" to false, "Between Accounts" to false).forEach { (tab, active) ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (active) PrimaryNavy else SurfaceContainerLow)
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = tab,
                        color = if (active) Color.White else TextColorSecondary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Enter Amount Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, GrayBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Enter Amount",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextColorSecondary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$",
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp),
                        color = PrimaryNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextField(
                        value = transferAmount,
                        onValueChange = { viewModel.updateTransferAmount(it) },
                        placeholder = {
                            Text(
                                text = "0.00",
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp),
                                color = GrayBorder
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = PrimaryNavy,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("transfer_amount_input"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daily limit: $5,000.00",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextColorSecondary
                    )
                    Text(
                        text = "Increase Limit",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldGreen,
                        modifier = Modifier.clickable { /* Handle limit visual action */ }
                    )
                }
            }
        }

        // Source Account card (Lumina Savings)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(ContainerNavy)
        ) {
            // Decorative Geometric Circle Element
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 16.dp, y = (-24).dp)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD0BCFF).copy(alpha = 0.4f))
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "From Account",
                        color = TextLightGray,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Lumina Savings",
                        color = PrimaryNavy,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "•••• 8829",
                        color = TextLightGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = String.format("$%,.2f", luminaSavingsBalance),
                        color = PrimaryNavy,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Available Balance",
                        color = TextLightGray,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        // Contact Selection Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, GrayBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Contacts",
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp),
                        color = PrimaryNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "See All",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreen,
                        modifier = Modifier.clickable { /* Toggle contact sheet */ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable Row of Contacts
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // "New" Contact Add Action Button
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.clickable { /* Trigger new contact add input modal */ }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreen.copy(0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "New Contact Add Button",
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(
                                text = "New",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextColorSecondary
                            )
                        }
                    }

                    items(filteredContacts) { contact ->
                        val isSelected = selectedContact?.id == contact.id
                        val contactColor = when (contact.avatarResName) {
                            "elena" -> Color(0xFF10B981)
                            "marcus" -> Color(0xFF0D1C32)
                            "david" -> Color(0xFF7C3AED)
                            "sasha" -> Color(0xFFF59E0B)
                            else -> EmeraldGreen
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .clickable { viewModel.selectContact(contact) }
                                .testTag("contact_item_${contact.id}")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = if (isSelected) EmeraldGreen else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .background(contactColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = contact.initialLetter,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = contact.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) EmeraldGreen else TextColorPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Optional Quick Context Search
                OutlinedTextField(
                    value = contactSearchQuery,
                    onValueChange = { viewModel.updateContactSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = "Search by name, email or mobile",
                            color = TextColorSecondary.copy(0.6f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Contact Search",
                            tint = TextColorSecondary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contact_search"),
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
            }
        }

        // Secure Transfer Shield info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(EmeraldGreen.copy(0.08f))
                .border(1.dp, EmeraldGreen.copy(0.15f), RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Secure Lock Indicator",
                tint = EmeraldGreen,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = "Lumina Secure",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "This transfer is protected. Funds typically arrive instantly to other Lumina accounts.",
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldGreen.copy(0.85f)
                )
            }
        }

        // Repeat payment Toggle Check card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GrayBorder)
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
                        imageVector = Icons.Default.Autorenew,
                        contentDescription = "Repeat payment symbol",
                        tint = PrimaryNavy,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Repeat this payment",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextColorPrimary
                    )
                }

                Switch(
                    checked = repeatPayment,
                    onCheckedChange = { viewModel.toggleRepeatPayment() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = EmeraldGreen,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = GrayBorder
                    ),
                    modifier = Modifier.testTag("repeat_payment_switch")
                )
            }
        }

        // Fast Help FAQ block
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GrayBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "NEED HELP?",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextColorSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* limit info modal */ }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Transfer limits explanation link",
                        tint = TextColorSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Transfer limits and timing",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextColorSecondary
                    )
                }

                Divider(color = GrayBorder)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* security reporting trigger */ }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Security suspicious reporting action",
                        tint = TextColorSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Report suspicious activity",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextColorSecondary
                    )
                }
            }
        }

        // Confirm Transfer Emerald Green Primary Button
        Button(
            onClick = {
                val success = viewModel.performTransfer()
                if (success) {
                    Toast.makeText(context, "Transfer successfully sent!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Please enter a valid amount and select a contact.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("confirm_transfer_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = EmeraldGreen,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Confirm Transfer",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp),
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Arrow Forward Confirm icon",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Spacing Safe Area Bottom
        Spacer(modifier = Modifier.height(48.dp))
    }
}
