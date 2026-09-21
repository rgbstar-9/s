package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.FirstAidItemEntity
import com.example.ui.theme.APNavyDark
import com.example.ui.theme.APNavyPrimary
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.EmergencyAmberContainer
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedContainer
import com.example.ui.theme.GovBorder
import com.example.ui.theme.GovSurfaceSubtle
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenContainer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditItemExpiryDialog(
  item: FirstAidItemEntity,
  onDismiss: () -> Unit,
  onSave: (itemId: Int, expiryDate: String, expiryTimestamp: Long, batchNumber: String, currentStock: Int, minThreshold: Int, itemName: String) -> Unit
) {
  var expiryDateText by remember { mutableStateOf(item.expiryDate) }
  var expiryTimestamp by remember { mutableLongStateOf(item.expiryTimestamp) }
  var batchNumber by remember { mutableStateOf(item.batchNumber) }
  var currentStock by remember { mutableIntStateOf(item.currentStock) }
  var minThreshold by remember { mutableIntStateOf(item.minThreshold) }

  val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH) }
  val now = System.currentTimeMillis()
  val isExpired = now > expiryTimestamp
  val thirtyDaysMillis = 30L * 24 * 60 * 60 * 1000
  val isExpiringSoon = !isExpired && (expiryTimestamp - now < thirtyDaysMillis)

  fun setPresetDate(monthsToAdd: Int) {
    val cal = Calendar.getInstance()
    cal.timeInMillis = now
    cal.add(Calendar.MONTH, monthsToAdd)
    expiryTimestamp = cal.timeInMillis
    expiryDateText = dateFormat.format(Date(expiryTimestamp))
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(18.dp)),
      color = Color.White,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Header
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(APNavyPrimary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.EditCalendar,
              contentDescription = "Edit Expiry",
              tint = APNavyPrimary,
              modifier = Modifier.size(24.dp)
            )
          }
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Item Expiry & Quality Control",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = APNavyDark
              )
            )
            Text(
              text = "Department Protocol: AP-EMS-QC-2026",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Color.Gray
              )
            )
          }
        }

        // Current Item Summary Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = GovSurfaceSubtle),
          border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(GovBorder))
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = APNavyDark
                )
              )
              // Status Badge
              val badgeBg = when {
                isExpired -> EmergencyRedContainer
                isExpiringSoon -> EmergencyAmberContainer
                else -> StatusGreenContainer
              }
              val badgeTextColor = when {
                isExpired -> EmergencyRed
                isExpiringSoon -> EmergencyAmber
                else -> StatusGreen
              }
              val badgeLabel = when {
                isExpired -> "EXPIRED"
                isExpiringSoon -> "EXPIRING SOON"
                else -> "STERILITY VALID"
              }
              val badgeIcon = when {
                isExpired -> Icons.Default.ErrorOutline
                isExpiringSoon -> Icons.Default.Warning
                else -> Icons.Default.CheckCircle
              }
              Row(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(badgeBg)
                  .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = badgeIcon,
                  contentDescription = null,
                  tint = badgeTextColor,
                  modifier = Modifier.size(12.dp)
                )
                Text(
                  text = badgeLabel,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = badgeTextColor,
                    fontSize = 10.sp
                  )
                )
              }
            }

            Text(
              text = "Category: ${item.category} • Kit: ${item.locationInKit}",
              style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
            )
          }
        }

        // Expiry Date Field & Quick Presets
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text(
            text = "EXPIRY DATE (DD MMM YYYY)",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = APNavyDark
            )
          )
          OutlinedTextField(
            value = expiryDateText,
            onValueChange = { input ->
              expiryDateText = input
              try {
                val parsed = dateFormat.parse(input)
                if (parsed != null) {
                  expiryTimestamp = parsed.time
                }
              } catch (_: Exception) {
                // Keep manual input
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("expiry_date_input"),
            leadingIcon = {
              Icon(Icons.Default.DateRange, contentDescription = null, tint = APNavyPrimary)
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
          )

          // Quick Presets
          Text(
            text = "Quick Manufacturer Shelf-Life Presets:",
            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
          )
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            PresetChip(label = "+6 Months", onClick = { setPresetDate(6) })
            PresetChip(label = "+1 Year", onClick = { setPresetDate(12) })
            PresetChip(label = "+2 Years", onClick = { setPresetDate(24) })
            PresetChip(label = "+3 Years (Standard)", onClick = { setPresetDate(36) })
            PresetChip(
              label = "Expired Yesterday (Demo)",
              isWarning = true,
              onClick = {
                val cal = Calendar.getInstance()
                cal.timeInMillis = now - (24L * 60 * 60 * 1000)
                expiryTimestamp = cal.timeInMillis
                expiryDateText = dateFormat.format(Date(expiryTimestamp))
              }
            )
          }
        }

        // Batch / Lot Number
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "BATCH / LOT NUMBER",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = APNavyDark
              )
            )
            Text(
              text = "Auto-Generate",
              style = MaterialTheme.typography.labelSmall.copy(
                color = APNavyPrimary,
                fontWeight = FontWeight.Bold
              ),
              modifier = Modifier
                .clickable {
                  val rand = (1000..9999).random()
                  batchNumber = "AP-VJA-2026-B$rand"
                }
                .padding(4.dp)
            )
          }
          OutlinedTextField(
            value = batchNumber,
            onValueChange = { batchNumber = it },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("batch_number_input"),
            leadingIcon = {
              Icon(Icons.Default.Numbers, contentDescription = null, tint = APNavyPrimary)
            },
            trailingIcon = {
              Icon(
                Icons.Default.QrCodeScanner,
                contentDescription = "Scan Barcode",
                tint = Color.Gray,
                modifier = Modifier
                  .size(20.dp)
                  .clickable {
                    val rand = (100..999).random()
                    batchNumber = "LOT-${item.category.take(3).uppercase()}-$rand-VJA"
                  }
              )
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
          )
        }

        // Stock & Threshold Counters
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Current Stock Counter
          Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = GovSurfaceSubtle),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(GovBorder))
          ) {
            Column(
              modifier = Modifier.padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(
                text = "CURRENT STOCK",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color.Gray
                )
              )
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                IconButton(
                  onClick = { if (currentStock > 0) currentStock-- },
                  modifier = Modifier.size(30.dp)
                ) {
                  Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = APNavyPrimary)
                }
                Text(
                  text = "$currentStock",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = APNavyDark
                  )
                )
                IconButton(
                  onClick = { currentStock++ },
                  modifier = Modifier.size(30.dp)
                ) {
                  Icon(Icons.Default.Add, contentDescription = "Increase", tint = APNavyPrimary)
                }
              }
            }
          }

          // Min Threshold Counter
          Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = GovSurfaceSubtle),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(GovBorder))
          ) {
            Column(
              modifier = Modifier.padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(
                text = "MIN THRESHOLD",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color.Gray
                )
              )
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                IconButton(
                  onClick = { if (minThreshold > 1) minThreshold-- },
                  modifier = Modifier.size(30.dp)
                ) {
                  Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = APNavyPrimary)
                }
                Text(
                  text = "$minThreshold",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = APNavyDark
                  )
                )
                IconButton(
                  onClick = { minThreshold++ },
                  modifier = Modifier.size(30.dp)
                ) {
                  Icon(Icons.Default.Add, contentDescription = "Increase", tint = APNavyPrimary)
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier
              .weight(1f)
              .testTag("cancel_expiry_button"),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Cancel", color = Color.Gray)
          }

          Button(
            onClick = {
              onSave(
                item.id,
                expiryDateText,
                expiryTimestamp,
                batchNumber,
                currentStock,
                minThreshold,
                item.name
              )
            },
            modifier = Modifier
              .weight(1.4f)
              .testTag("save_expiry_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = APNavyPrimary)
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Save & Certify", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
private fun PresetChip(
  label: String,
  isWarning: Boolean = false,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(if (isWarning) EmergencyRedContainer else Color(0xFFEFF6FF))
      .border(
        width = 1.dp,
        color = if (isWarning) EmergencyRed.copy(alpha = 0.3f) else APNavyPrimary.copy(alpha = 0.2f),
        shape = RoundedCornerShape(6.dp)
      )
      .clickable(onClick = onClick)
      .padding(horizontal = 8.dp, vertical = 5.dp)
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.Medium,
        color = if (isWarning) EmergencyRed else APNavyDark,
        fontSize = 11.sp
      )
    )
  }
}
