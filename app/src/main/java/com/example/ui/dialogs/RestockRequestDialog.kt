package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.FirstAidItemEntity
import com.example.data.model.TrafficBoothEntity
import com.example.ui.theme.APNavyDark
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.GovBorder
import com.example.ui.theme.MedicalTeal

@Composable
fun RestockRequestDialog(
  booth: TrafficBoothEntity,
  preselectedItem: FirstAidItemEntity?,
  onDismiss: () -> Unit,
  onSubmit: (itemName: String, quantity: Int, reason: String) -> Unit
) {
  var itemName by remember {
    mutableStateOf(preselectedItem?.name ?: "CAT Tourniquet Gen 7")
  }
  var quantity by remember {
    mutableIntStateOf(preselectedItem?.minThreshold?.times(2)?.coerceAtLeast(2) ?: 3)
  }
  var selectedReason by remember {
    mutableStateOf(
      when {
        preselectedItem?.isExpired == true -> "EXPIRED"
        preselectedItem?.isLowStock == true -> "LOW_STOCK"
        else -> "ROUTINE_AUDIT"
      }
    )
  }

  val reasonOptions = listOf(
    "LOW_STOCK" to "Supplies Running Low",
    "EXPIRED" to "Item Expired / Near Expiry",
    "EMERGENCY_USAGE" to "Consumed in Road Accident",
    "ROUTINE_AUDIT" to "Weekly Kit Routine Indent"
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .clip(RoundedCornerShape(16.dp))
        .border(1.dp, GovBorder, RoundedCornerShape(16.dp)),
      color = Color.White
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFCCFBF1)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.LocalShipping, contentDescription = null, tint = MedicalTeal, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "SUPPLY REQUISITION",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = APNavyDark)
              )
              Text(
                text = "Vijayawada Police & Health Logistics Intimation",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Target Booth
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GovBorder, RoundedCornerShape(10.dp))
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "Requisitioning Station: ${booth.boothCode} • ${booth.name}",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = APNavyDark
            )
            Text(
              text = "Officer in Charge: ${booth.officerName} (${booth.officerBadge})",
              fontSize = 11.sp,
              color = Color(0xFF64748B)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Item Name
        OutlinedTextField(
          value = itemName,
          onValueChange = { itemName = it },
          label = { Text("Medical Item Name", fontSize = 12.sp) },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Quantity selector
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Quantity Needed:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = APNavyDark)
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .border(1.dp, GovBorder, RoundedCornerShape(8.dp))
              .padding(4.dp)
          ) {
            IconButton(
              onClick = { if (quantity > 1) quantity-- },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
            }
            Text(
              text = "$quantity units",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(
              onClick = { quantity++ },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Reason Selector
        Text("REASON FOR INDENT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
        Spacer(modifier = Modifier.height(6.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          reasonOptions.forEach { (key, label) ->
            val isSelected = selectedReason == key
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) Color(0xFFE0F2FE) else Color(0xFFF8FAFC))
                .border(
                  1.dp,
                  if (isSelected) APNavyDark else GovBorder,
                  RoundedCornerShape(8.dp)
                )
                .clickable { selectedReason = key }
                .padding(horizontal = 12.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (isSelected) "●" else "○",
                color = if (isSelected) APNavyDark else Color(0xFF94A3B8),
                fontSize = 14.sp
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) APNavyDark else Color(0xFF334155)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = {
            if (itemName.isNotBlank()) {
              onSubmit(itemName, quantity, selectedReason)
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = APNavyDark),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("btn_submit_requisition")
        ) {
          Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text("TRANSMIT REQUISITION TO DEPOT", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
