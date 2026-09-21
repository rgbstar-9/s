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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
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
import com.example.ui.theme.APNavyDark
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedContainer
import com.example.ui.theme.GovSurfaceSubtle

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BroadcastAlertDialog(
  onDismiss: () -> Unit,
  onSendBroadcast: (corridor: String, alertText: String) -> Unit
) {
  var selectedCorridor by remember { mutableStateOf("All Vijayawada Junctions (City Grid)") }
  var alertMessage by remember {
    mutableStateOf("PRIORITY 108 AMBULANCE DISPATCH: Clear immediate Green Corridor from Benz Circle via MG Road to GGH Vijayawada Emergency Trauma Care.")
  }

  val corridors = listOf(
    "All Vijayawada Junctions (City Grid)",
    "NH-16 Highway Corridor (Benz Circle -> Enikepadu)",
    "MG Road Central Corridor (PCR -> Benz Circle)",
    "Eluru Road Corridor (PCR -> Ramavarappadu Ring)",
    "Krishna River Varadhi Corridor (PNBS -> Tadepalli)",
    "West Corridor (Bhavanipuram -> Gollapudi)"
  )

  val quickAlertTemplates = listOf(
    "Clear Green Corridor for 108 Ambulance Unit #12 to GGH Trauma Ward" to "NH-16 Highway Corridor (Benz Circle -> Enikepadu)",
    "Mass Collision Alert: Adjacent booths prepare CAT Tourniquets & SAM Splints" to "All Vijayawada Junctions (City Grid)",
    "Supply Restock Van 04 dispatched: Traffic booths stand by for kit delivery" to "Eluru Road Corridor (PCR -> Ramavarappadu Ring)"
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
              .background(EmergencyRedContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Campaign,
              contentDescription = "Broadcast Alert",
              tint = EmergencyRed,
              modifier = Modifier.size(24.dp)
            )
          }
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Central Emergency Broadcast",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = APNavyDark
              )
            )
            Text(
              text = "AP Police & Health Command Network",
              style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
            )
          }
        }

        // Corridor Selector
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "TARGET TRAFFIC CORRIDOR",
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
            corridors.forEach { corr ->
              val isSelected = selectedCorridor == corr
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) EmergencyRed else GovSurfaceSubtle)
                  .clickable { selectedCorridor = corr }
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(
                  text = corr,
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = if (isSelected) Color.White else APNavyDark,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                  )
                )
              }
            }
          }
        }

        // Quick Preset Templates
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "Quick Pre-emption Templates:",
            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
          )
          quickAlertTemplates.forEach { (text, corr) ->
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(GovSurfaceSubtle)
                .clickable {
                  alertMessage = text
                  selectedCorridor = corr
                }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = "⚡ $text",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = APNavyDark,
                  fontSize = 11.sp
                )
              )
            }
          }
        }

        // Custom Message Box
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = "BROADCAST DIRECTIVE TO TRAFFIC POLICE BOOTHS *",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = APNavyDark
            )
          )
          OutlinedTextField(
            value = alertMessage,
            onValueChange = { alertMessage = it },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("broadcast_message_input"),
            maxLines = 4,
            shape = RoundedCornerShape(10.dp)
          )
        }

        // Notice
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFFBEB))
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
          Text(
            text = "This alert will flash on all booth screens & trigger wireless siren pre-emption.",
            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFB45309))
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier
              .weight(1f)
              .testTag("cancel_broadcast_button"),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Cancel", color = Color.Gray)
          }

          Button(
            onClick = {
              if (alertMessage.isNotBlank()) {
                onSendBroadcast(selectedCorridor, alertMessage.trim())
              }
            },
            enabled = alertMessage.isNotBlank(),
            modifier = Modifier
              .weight(1.5f)
              .testTag("send_broadcast_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
          ) {
            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Transmit Broadcast", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
