package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.APNavyDark
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.GovBorder
import com.example.ui.theme.MedicalTeal
import kotlinx.coroutines.delay

@Composable
fun GoldenHourProtocolDialog(
  onDismiss: () -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var cprMetronomeActive by remember { mutableStateOf(false) }
  var beatPulse by remember { mutableStateOf(false) }

  // CPR Metronome beat (approx 110 bpm = 545 ms per beat)
  LaunchedEffect(cprMetronomeActive) {
    while (cprMetronomeActive) {
      beatPulse = true
      delay(120)
      beatPulse = false
      delay(425)
    }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, GovBorder, RoundedCornerShape(20.dp)),
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
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0F2FE)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.MedicalServices, contentDescription = null, tint = APNavyDark, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "GOLDEN HOUR PROTOCOL",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = APNavyDark
                )
              )
              Text(
                text = "AP Police Field Life-Support Guide (First 15 Mins)",
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

        // Tab Selector Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF1F5F9))
            .padding(3.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          ProtocolTabChip(title = "Bleeding (CAT)", icon = Icons.Default.Bloodtype, isSelected = selectedTab == 0, onClick = { selectedTab = 0 }, modifier = Modifier.weight(1f))
          ProtocolTabChip(title = "CPR / Pulse", icon = Icons.Default.Favorite, isSelected = selectedTab == 1, onClick = { selectedTab = 1 }, modifier = Modifier.weight(1f))
          ProtocolTabChip(title = "Spine / Neck", icon = Icons.Default.Warning, isSelected = selectedTab == 2, onClick = { selectedTab = 2 }, modifier = Modifier.weight(1f))
          ProtocolTabChip(title = "Burns & Shock", icon = Icons.Default.Thermostat, isSelected = selectedTab == 3, onClick = { selectedTab = 3 }, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
          0 -> BleedingProtocolView()
          1 -> CprProtocolView(
            isActive = cprMetronomeActive,
            isBeating = beatPulse,
            onToggle = { cprMetronomeActive = !cprMetronomeActive }
          )
          2 -> SpineProtocolView()
          3 -> BurnsProtocolView()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = APNavyDark),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
          Text("Understood • Return to Station", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun ProtocolTabChip(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) Color.White else Color.Transparent)
      .clickable(onClick = onClick)
      .padding(vertical = 8.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = if (isSelected) EmergencyRed else Color(0xFF64748B),
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = title,
        fontSize = 10.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        color = if (isSelected) APNavyDark else Color(0xFF64748B),
        maxLines = 1
      )
    }
  }
}

@Composable
private fun BleedingProtocolView() {
  Column {
    Card(
      colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text("🚨 ARTERIAL BLEEDING: 3-5 MINUTES TO FATALITY", fontWeight = FontWeight.Black, fontSize = 12.sp, color = EmergencyRed)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          "Pulsing or pooling bright red blood requires immediate CAT Tourniquet application from your booth kit before 108 ambulance arrives.",
          fontSize = 12.sp,
          color = Color(0xFF7F1D1D)
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    StepItem(step = "1", title = "Locate & Expose Wound", desc = "Use trauma shears from kit front pocket to cut clothes rapidly. Do NOT waste time cleaning arterial wounds.")
    StepItem(step = "2", title = "Apply CAT Tourniquet High & Tight", desc = "Wrap 2-3 inches above the wound on limb (never over joint). Pull self-adhering band tight.")
    StepItem(step = "3", title = "Turn Windlass Rod Until Bleeding Stops", desc = "Twist windlass until bright pulsing stops. Lock rod into clip and secure with white TIME strap.")
    StepItem(step = "4", title = "Write Time on Tourniquet Strap", desc = "Record application time with marker (e.g. 07:35 AM). Never loosen tourniquet yourself.")
    StepItem(step = "5", title = "Pack Wound with Trauma Dressing", desc = "Apply sterile 6-inch trauma bandage over wound and hold firm continuous pressure.")
  }
}

@Composable
private fun CprProtocolView(
  isActive: Boolean,
  isBeating: Boolean,
  onToggle: () -> Unit
) {
  Column {
    Card(
      colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text("HANDS-ONLY CPR METRONOME (110 BPM)", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color(0xFF15803D))
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          "Compress center of chest hard and fast (5-6 cm deep). Keep pace with this rhythm.",
          fontSize = 11.sp,
          color = Color(0xFF166534),
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Interactive Metronome Heart Indicator
        Box(
          modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(if (isBeating) EmergencyRed else Color(0xFFDCFCE7)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = if (isBeating) Color.White else Color(0xFF15803D),
            modifier = Modifier.size(32.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
          onClick = onToggle,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) EmergencyRed else Color(0xFF16A34A)
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(if (isActive) Icons.Default.Stop else Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(if (isActive) "Stop Metronome" else "Start 110 BPM Metronome Guide")
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))
    StepItem(step = "1", title = "Check Response & Breathing", desc = "Tap victim shoulders: 'Can you hear me?'. Look for chest rise for max 10 seconds.")
    StepItem(step = "2", title = "Position Hands on Sternum", desc = "Heel of one hand in center of chest, other hand interlaced on top. Keep arms straight and lock elbows.")
    StepItem(step = "3", title = "30 Compressions : 2 Breaths", desc = "Push hard (5cm depth). If trained, attach CPR pocket mask from booth kit for 2 rescue breaths.")
  }
}

@Composable
private fun SpineProtocolView() {
  Column {
    Card(
      colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text("⚠️ CERVICAL SPINE PRESERVATION (TWO-WHEELERS)", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color(0xFFB45309))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          "Improper movement can turn a reversible cervical fracture into permanent paralysis. Maintain spinal axis at all costs.",
          fontSize = 12.sp,
          color = Color(0xFF78350F)
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))
    StepItem(step = "1", title = "Do NOT Remove Helmet Unnecessarily", desc = "Leave helmet on unless victim airway is completely blocked and breathing impossible.")
    StepItem(step = "2", title = "Manual In-Line Stabilization", desc = "Kneel behind victim head, place hands firmly on both sides of head to prevent any rotation or tilting.")
    StepItem(step = "3", title = "Fit Rigid Cervical Collar", desc = "Slide back portion of collar from booth kit under neck, align chin cup under victim chin, secure velcro firmly.")
    StepItem(step = "4", title = "Log Roll Only If Vomiting", desc = "If patient vomits, roll entire body as single unit (head, shoulders, hips aligned) to prevent choking.")
  }
}

@Composable
private fun BurnsProtocolView() {
  Column {
    Card(
      colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text("💧 BURNS & SHOCK MITIGATION", fontWeight = FontWeight.Black, fontSize = 12.sp, color = MedicalTeal)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          "Road friction and exhaust burns must be cooled with sterile water. Never use home remedies (toothpaste, oils, turmeric).",
          fontSize = 12.sp,
          color = Color(0xFF0F766E)
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))
    StepItem(step = "1", title = "Irrigate with Clear Cool Water", desc = "Irrigate burned area for at least 10 minutes. Do NOT use ice directly as it destroys tissue.")
    StepItem(step = "2", title = "Apply Sterile Burn Hydrogel Sheet", desc = "Take hydrogel burn dressing from Compartment C. Lay flat over burn. Do NOT pop fluid blisters.")
    StepItem(step = "3", title = "Prevent Trauma Shock (Cold & Pale)", desc = "Elevate victim legs 12 inches if no spinal injury. Wrap in space blanket to maintain core temperature.")
    StepItem(step = "4", title = "Administer O2 Canister", desc = "If victim is breathing shallowly, hold oxygen canister mask to mouth and press actuator.")
  }
}

@Composable
private fun StepItem(step: String, title: String, desc: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.Top
  ) {
    Box(
      modifier = Modifier
        .size(24.dp)
        .clip(CircleShape)
        .background(APNavyDark),
      contentAlignment = Alignment.Center
    ) {
      Text(text = step, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
    }
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = APNavyDark)
      Text(text = desc, fontSize = 11.sp, color = Color(0xFF475569), lineHeight = 16.sp)
    }
  }
}
