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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.model.TrafficBoothEntity
import com.example.ui.theme.APNavyDark
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.GovBorder
import com.example.ui.theme.MedicalTeal
import com.example.ui.theme.StatusGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmergencySOSDialog(
  booth: TrafficBoothEntity,
  onDismiss: () -> Unit,
  onDispatch: (
    severity: String,
    victimCount: Int,
    injuryType: String,
    vehicleTypes: String,
    firstAidAdministered: String,
    itemsUsed: String,
    hospital: String,
    notes: String
  ) -> Unit
) {
  var severity by remember { mutableStateOf("CRITICAL_RED") }
  var victimCount by remember { mutableIntStateOf(1) }
  var selectedInjury by remember { mutableStateOf("Severe Arterial Bleeding") }
  var selectedVehicle by remember { mutableStateOf("Motorcycle vs Car") }
  val selectedTreatments = remember { mutableStateListOf("CAT Tourniquet Applied", "Direct Pressure Bandage") }
  var selectedHospital by remember { mutableStateOf("GGH Vijayawada (Trauma Center)") }
  var officerNotes by remember { mutableStateOf("") }

  val injuryOptions = listOf(
    "Severe Arterial Bleeding",
    "Head Trauma / Unconscious",
    "Limb Fracture & Deformity",
    "Severe Burns",
    "Chest Trauma & Dyspnea",
    "Multiple Road Abrasions"
  )

  val vehicleOptions = listOf(
    "Motorcycle vs Car",
    "Two-Wheeler Self Skid",
    "Auto-rickshaw Collision",
    "Heavy Vehicle / RTC Bus",
    "Pedestrian Hit"
  )

  val treatmentOptions = listOf(
    "CAT Tourniquet Applied",
    "Direct Pressure Bandage",
    "Airway Cleared & O2 Given",
    "Cervical Collar Fitted",
    "SAM Splint Immobilized",
    "Hydrogel Burn Sheet Applied",
    "CPR Commenced"
  )

  val hospitalOptions = listOf(
    "GGH Vijayawada (Trauma Center)",
    "Old Govt Hospital, Hanumanpet",
    "AIIMS Mangalagiri",
    "Ramesh Hospitals, Ring Road"
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .clip(RoundedCornerShape(20.dp))
        .border(2.dp, EmergencyRed, RoundedCornerShape(20.dp)),
      color = Color.White
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
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
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFFEE2E2)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = EmergencyRed,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "108 EMERGENCY DISPATCH",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Black,
                  color = EmergencyRed,
                  letterSpacing = 0.5.sp
                )
              )
              Text(
                text = "Golden Hour Incident Intake • AP Trauma Grid",
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

        // Junction & Location Banner
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GovBorder, RoundedCornerShape(12.dp))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = APNavyDark, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "${booth.boothCode}: ${booth.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = APNavyDark
              )
              Text(
                text = "Coordinates: ${booth.latitude}° N, ${booth.longitude}° E • Officer: ${booth.officerName}",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Triage Severity Level
        Text(
          text = "TRIAGE SEVERITY",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF475569),
          letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          SeverityChip(
            label = "CRITICAL (RED)",
            description = "Arterial bleed / Unconscious",
            isSelected = severity == "CRITICAL_RED",
            activeColor = EmergencyRed,
            onClick = { severity = "CRITICAL_RED" },
            modifier = Modifier.weight(1f)
          )
          SeverityChip(
            label = "URGENT (YELLOW)",
            description = "Fractures / Deep wounds",
            isSelected = severity == "URGENT_YELLOW",
            activeColor = EmergencyAmber,
            onClick = { severity = "URGENT_YELLOW" },
            modifier = Modifier.weight(1f)
          )
          SeverityChip(
            label = "MINOR (GREEN)",
            description = "Abrasions / Conscious",
            isSelected = severity == "MINOR_GREEN",
            activeColor = StatusGreen,
            onClick = { severity = "MINOR_GREEN" },
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Victim Count Counter
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(
              text = "NUMBER OF VICTIMS",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF475569)
            )
            Text(
              text = "Determines ambulance capacity dispatched",
              fontSize = 10.sp,
              color = Color(0xFF94A3B8)
            )
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFF1F5F9))
              .padding(4.dp)
          ) {
            IconButton(
              onClick = { if (victimCount > 1) victimCount-- },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
            }
            Text(
              text = "$victimCount",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              modifier = Modifier.padding(horizontal = 12.dp)
            )
            IconButton(
              onClick = { victimCount++ },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Primary Injury Type Selector
        Text(
          text = "PRIMARY INJURY TYPE",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF475569)
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          injuryOptions.forEach { option ->
            val isSelected = selectedInjury == option
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) Color(0xFFFEE2E2) else Color(0xFFF1F5F9))
                .border(
                  1.dp,
                  if (isSelected) EmergencyRed else Color.Transparent,
                  RoundedCornerShape(8.dp)
                )
                .clickable { selectedInjury = option }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = option,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) EmergencyRed else Color(0xFF334155)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Vehicles Involved
        Text(
          text = "VEHICLES INVOLVED",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF475569)
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          vehicleOptions.forEach { option ->
            val isSelected = selectedVehicle == option
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) Color(0xFFE0F2FE) else Color(0xFFF1F5F9))
                .border(
                  1.dp,
                  if (isSelected) APNavyDark else Color.Transparent,
                  RoundedCornerShape(8.dp)
                )
                .clickable { selectedVehicle = option }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = option,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) APNavyDark else Color(0xFF334155)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // First Aid Administered Checklist
        Text(
          text = "PRIMARY FIRST AID ADMINISTERED AT BOOTH",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF475569)
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          treatmentOptions.forEach { opt ->
            val isChecked = selectedTreatments.contains(opt)
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isChecked) Color(0xFFCCFBF1) else Color(0xFFF1F5F9))
                .border(
                  1.dp,
                  if (isChecked) MedicalTeal else Color.Transparent,
                  RoundedCornerShape(8.dp)
                )
                .clickable {
                  if (isChecked) selectedTreatments.remove(opt) else selectedTreatments.add(opt)
                }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = if (isChecked) "✓ $opt" else "+ $opt",
                fontSize = 11.sp,
                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                color = if (isChecked) Color(0xFF0F766E) else Color(0xFF475569)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hospital Destination
        Text(
          text = "DESTINATION TRAUMA HOSPITAL",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF475569)
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          hospitalOptions.forEach { hosp ->
            val isSelected = selectedHospital == hosp
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) Color(0xFFF0FDF4) else Color(0xFFF8FAFC))
                .border(
                  1.dp,
                  if (isSelected) StatusGreen else GovBorder,
                  RoundedCornerShape(8.dp)
                )
                .clickable { selectedHospital = hosp }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = hosp,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF15803D) else Color(0xFF475569)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Police Notes
        OutlinedTextField(
          value = officerNotes,
          onValueChange = { officerNotes = it },
          label = { Text("Traffic Constable Notes / Landmark", fontSize = 12.sp) },
          placeholder = { Text("e.g. Near Benz Circle flyover pillar 14, bleeding controlled with tourniquet", fontSize = 11.sp) },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          maxLines = 2
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Submit Emergency SOS Button
        Button(
          onClick = {
            val treatments = selectedTreatments.joinToString(", ")
            val itemsSummary = if (selectedTreatments.contains("CAT Tourniquet Applied")) "1x CAT Tourniquet, 1x Trauma Bandage" else "1x Emergency Dressing"
            onDispatch(
              severity,
              victimCount,
              selectedInjury,
              selectedVehicle,
              treatments,
              itemsSummary,
              selectedHospital,
              officerNotes.ifBlank { "Immediate trauma response initiated at ${booth.name}" }
            )
          },
          colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("btn_confirm_dispatch")
        ) {
          Icon(Icons.Default.LocalHospital, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "DISPATCH 108 AMBULANCE NOW",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            color = Color.White,
            letterSpacing = 0.5.sp
          )
        }
      }
    }
  }
}

@Composable
private fun SeverityChip(
  label: String,
  description: String,
  isSelected: Boolean,
  activeColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(if (isSelected) activeColor.copy(alpha = 0.12f) else Color(0xFFF8FAFC))
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) activeColor else GovBorder,
        shape = RoundedCornerShape(10.dp)
      )
      .clickable(onClick = onClick)
      .padding(horizontal = 8.dp, vertical = 8.dp)
  ) {
    Column {
      Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = FontWeight.Black,
        color = if (isSelected) activeColor else Color(0xFF64748B)
      )
      Text(
        text = description,
        fontSize = 9.sp,
        color = Color(0xFF94A3B8),
        maxLines = 1
      )
    }
  }
}
