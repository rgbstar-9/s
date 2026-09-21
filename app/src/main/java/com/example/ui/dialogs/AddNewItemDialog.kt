package com.example.ui.dialogs

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.TrafficBoothEntity
import com.example.ui.theme.APNavyDark
import com.example.ui.theme.APNavyPrimary
import com.example.ui.theme.GovSurfaceSubtle
import com.example.ui.theme.MedicalTeal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddNewItemDialog(
  booth: TrafficBoothEntity,
  onDismiss: () -> Unit,
  onAddItem: (FirstAidItemEntity) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Hemorrhage Control") }
  var currentStock by remember { mutableIntStateOf(5) }
  var minThreshold by remember { mutableIntStateOf(2) }
  var unit by remember { mutableStateOf("units") }

  val cal = Calendar.getInstance()
  cal.add(Calendar.YEAR, 2)
  val defaultExpiryDate = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(cal.timeInMillis))

  var expiryDateText by remember { mutableStateOf(defaultExpiryDate) }
  var batchNumber by remember { mutableStateOf("LOT-2026-AP-${(100..999).random()}") }
  var locationInKit by remember { mutableStateOf("Compartment A (Rapid Access)") }
  var instructions by remember { mutableStateOf("") }

  val categories = listOf(
    "Hemorrhage Control",
    "Airway & Resuscitation",
    "Wound & Burn",
    "Fracture & Immobilization",
    "Antiseptics & Consumables"
  )

  val quickItems = listOf(
    "Hemostatic Gauze Dressing" to "Hemorrhage Control",
    "Sterile Burn Gel Shield" to "Wound & Burn",
    "Eye Wash Saline Bottle 500ml" to "Antiseptics & Consumables",
    "Cervical Neck Collar (Adult)" to "Fracture & Immobilization",
    "CPR Pocket Barrier Mask" to "Airway & Resuscitation",
    "Pulse Oximeter Finger Sensor" to "Antiseptics & Consumables"
  )

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
              .background(MedicalTeal.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AddCircle,
              contentDescription = "Add Item",
              tint = MedicalTeal,
              modifier = Modifier.size(24.dp)
            )
          }
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Add Medicine / First Aid Item",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = APNavyDark
              )
            )
            Text(
              text = "Station: ${booth.boothCode} (${booth.name})",
              style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
            )
          }
        }

        // Quick Preset Suggestions
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "Quick Standard Supplies:",
            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
          )
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            quickItems.forEach { (quickName, quickCat) ->
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(GovSurfaceSubtle)
                  .clickable {
                    name = quickName
                    category = quickCat
                    instructions = "Apply immediately according to Andhra Pradesh Police First Responder SOP."
                  }
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "+ $quickName",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = APNavyDark,
                    fontWeight = FontWeight.Medium
                  )
                )
              }
            }
          }
        }

        // Item Name
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "ITEM NAME *",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = APNavyDark
            )
          )
          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("e.g. Sterile Eye Wash 500ml") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("item_name_input"),
            leadingIcon = {
              Icon(Icons.Default.Medication, contentDescription = null, tint = APNavyPrimary)
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
          )
        }

        // Category Selector
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "CATEGORY *",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = APNavyDark
            )
          )
          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            categories.forEach { cat ->
              val isSelected = category == cat
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) APNavyPrimary else GovSurfaceSubtle)
                  .clickable { category = cat }
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(
                  text = cat,
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = if (isSelected) Color.White else APNavyDark,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                  )
                )
              }
            }
          }
        }

        // Expiry Date & Batch
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Column(modifier = Modifier.weight(1.2f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = "EXPIRY DATE",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = APNavyDark)
            )
            OutlinedTextField(
              value = expiryDateText,
              onValueChange = { expiryDateText = it },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("new_item_expiry"),
              leadingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = APNavyPrimary, modifier = Modifier.size(18.dp))
              },
              singleLine = true,
              shape = RoundedCornerShape(10.dp)
            )
          }

          Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = "BATCH / LOT",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = APNavyDark)
            )
            OutlinedTextField(
              value = batchNumber,
              onValueChange = { batchNumber = it },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("new_item_batch"),
              leadingIcon = {
                Icon(Icons.Default.Numbers, contentDescription = null, tint = APNavyPrimary, modifier = Modifier.size(18.dp))
              },
              singleLine = true,
              shape = RoundedCornerShape(10.dp)
            )
          }
        }

        // Initial Quantity & Location
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = "INITIAL QUANTITY",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = APNavyDark)
            )
            OutlinedTextField(
              value = "$currentStock",
              onValueChange = { currentStock = it.toIntOrNull() ?: 1 },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("new_item_quantity"),
              singleLine = true,
              shape = RoundedCornerShape(10.dp)
            )
          }

          Column(modifier = Modifier.weight(1.2f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = "LOCATION IN KIT",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = APNavyDark)
            )
            OutlinedTextField(
              value = locationInKit,
              onValueChange = { locationInKit = it },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("new_item_location"),
              leadingIcon = {
                Icon(Icons.Default.Place, contentDescription = null, tint = APNavyPrimary, modifier = Modifier.size(18.dp))
              },
              singleLine = true,
              shape = RoundedCornerShape(10.dp)
            )
          }
        }

        // Usage Instructions
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "CLINICAL FIRST AID INSTRUCTIONS",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = APNavyDark)
          )
          OutlinedTextField(
            value = instructions,
            onValueChange = { instructions = it },
            placeholder = { Text("e.g. Tear open sterile packet and apply directly over trauma site...") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("new_item_instructions"),
            maxLines = 3,
            shape = RoundedCornerShape(10.dp)
          )
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
              .testTag("cancel_add_item_button"),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Cancel", color = Color.Gray)
          }

          Button(
            onClick = {
              if (name.isNotBlank()) {
                val calNow = Calendar.getInstance()
                val parsedTimestamp = try {
                  SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).parse(expiryDateText)?.time
                    ?: (calNow.timeInMillis + (365L * 2 * 24 * 60 * 60 * 1000))
                } catch (_: Exception) {
                  calNow.timeInMillis + (365L * 2 * 24 * 60 * 60 * 1000)
                }

                val newItem = FirstAidItemEntity(
                  boothId = booth.id,
                  name = name.trim(),
                  category = category,
                  currentStock = currentStock,
                  minThreshold = minThreshold,
                  unit = unit,
                  expiryDate = expiryDateText.trim(),
                  expiryTimestamp = parsedTimestamp,
                  batchNumber = batchNumber.trim(),
                  locationInKit = locationInKit.trim(),
                  instructions = if (instructions.isBlank()) "Standard trauma treatment protocol under AP Police First Responder Guidelines." else instructions.trim()
                )
                onAddItem(newItem)
              }
            },
            enabled = name.isNotBlank(),
            modifier = Modifier
              .weight(1.5f)
              .testTag("confirm_add_item_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = APNavyPrimary)
          ) {
            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add to First Aid Kit", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
