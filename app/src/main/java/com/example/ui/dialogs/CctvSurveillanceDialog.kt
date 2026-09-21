package com.example.ui.dialogs

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.TrafficBoothEntity
import com.example.ui.theme.APNavyDark
import com.example.ui.theme.APNavyPrimary
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.StatusGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CctvSurveillanceDialog(
  booth: TrafficBoothEntity,
  onDismiss: () -> Unit,
  onBroadcastSiren: (String) -> Unit
) {
  var greenCorridorActive by remember { mutableStateOf(false) }
  var snapshotTaken by remember { mutableStateOf(false) }

  val transition = rememberInfiniteTransition(label = "cctv_anim")
  val scanLineY by transition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(2400, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "scan_line"
  )
  val pulseAlpha by transition.animateFloat(
    initialValue = 0.3f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "rec_pulse"
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .clip(RoundedCornerShape(18.dp)),
      color = Color(0xFF0F172A), // Command Center Dark Screen style
      tonalElevation = 10.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Top Bar
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
                .size(10.dp)
                .clip(CircleShape)
                .background(EmergencyRed.copy(alpha = pulseAlpha))
            )
            Text(
              text = "LIVE CCTV FEED • AP POLICE GRID",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.2.sp
              )
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close CCTV",
              tint = Color.White.copy(alpha = 0.7f)
            )
          }
        }

        // Camera Info Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "${booth.boothCode} — ${booth.name}",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            )
            Text(
              text = "Junction: ${booth.junctionName} • 4K Optical Zoom (PTZ-01)",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFF94A3B8)
              )
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(if (greenCorridorActive) StatusGreen.copy(alpha = 0.25f) else Color(0xFF1E293B))
              .border(
                1.dp,
                if (greenCorridorActive) StatusGreen else Color(0xFF334155),
                RoundedCornerShape(6.dp)
              )
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = if (greenCorridorActive) "EMERGENCY GREEN CORRIDOR" else "STANDARD SURVEILLANCE",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (greenCorridorActive) StatusGreen else Color(0xFFCBD5E1),
                fontSize = 9.sp
              )
            )
          }
        }

        // The Simulated Video Player Screen
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF020617))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
        ) {
          // Dynamic Junction Canvas
          Canvas(modifier = Modifier.fillMaxWidth().height(240.dp)) {
            val width = size.width
            val height = size.height

            // Road Grid Lines
            val roadColor = Color(0xFF1E293B)
            val laneDash = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)

            // Horizontal Junction Road
            drawRect(
              color = Color(0xFF161F30),
              topLeft = Offset(0f, height * 0.35f),
              size = Size(width, height * 0.35f)
            )
            // Vertical Junction Road
            drawRect(
              color = Color(0xFF161F30),
              topLeft = Offset(width * 0.4f, 0f),
              size = Size(width * 0.25f, height)
            )

            // Lane Divider
            drawLine(
              color = Color(0xFF475569),
              start = Offset(0f, height * 0.525f),
              end = Offset(width, height * 0.525f),
              strokeWidth = 2f,
              pathEffect = laneDash
            )
            drawLine(
              color = Color(0xFF475569),
              start = Offset(width * 0.525f, 0f),
              end = Offset(width * 0.525f, height),
              strokeWidth = 2f,
              pathEffect = laneDash
            )

            // Traffic Booth Visual Indicator
            drawCircle(
              color = APNavyPrimary,
              radius = 16f,
              center = Offset(width * 0.32f, height * 0.30f)
            )
            drawCircle(
              color = Color.White,
              radius = 8f,
              center = Offset(width * 0.32f, height * 0.30f)
            )

            // Simulated Vehicles & AI Detection Boxes
            val boxStroke = Stroke(width = 1.5f)
            val aiBoxColor = if (greenCorridorActive) StatusGreen else Color(0xFF38BDF8)

            // Car 1 (Heading East)
            drawRect(
              color = aiBoxColor,
              topLeft = Offset(width * 0.15f, height * 0.42f),
              size = Size(42f, 24f),
              style = boxStroke
            )
            // Car 2 (Heading West)
            drawRect(
              color = aiBoxColor,
              topLeft = Offset(width * 0.70f, height * 0.58f),
              size = Size(46f, 26f),
              style = boxStroke
            )
            // Ambulance / Emergency vehicle
            drawRect(
              color = EmergencyRed,
              topLeft = Offset(width * 0.48f, height * 0.72f),
              size = Size(50f, 28f),
              style = Stroke(width = 2.5f)
            )

            // Radar Scan Line
            val currentScanY = height * scanLineY
            drawLine(
              color = Color(0x5500F0FF),
              start = Offset(0f, currentScanY),
              end = Offset(width, currentScanY),
              strokeWidth = 2f
            )

            // Target Crosshair
            drawLine(
              color = Color(0x88FFFFFF),
              start = Offset(width * 0.5f - 15f, height * 0.5f),
              end = Offset(width * 0.5f + 15f, height * 0.5f),
              strokeWidth = 1f
            )
            drawLine(
              color = Color(0x88FFFFFF),
              start = Offset(width * 0.5f, height * 0.5f - 15f),
              end = Offset(width * 0.5f, height * 0.5f + 15f),
              strokeWidth = 1f
            )
          }

          // Top-Left Stream Telemetry
          Column(
            modifier = Modifier
              .align(Alignment.TopStart)
              .padding(10.dp)
          ) {
            val timeString = SimpleDateFormat("HH:mm:ss.SSS", Locale.ENGLISH).format(Date())
            Text(
              text = "REC [●] $timeString IST",
              style = MaterialTheme.typography.labelSmall.copy(
                color = EmergencyRed,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
              )
            )
            Text(
              text = "CAM-AP-VJA-${booth.boothCode} • 4K UHD 60FPS",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFF94A3B8),
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp
              )
            )
          }

          // Bottom-Right AI Speed & Triage Telemetry
          Column(
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .padding(10.dp),
            horizontalAlignment = Alignment.End
          ) {
            Text(
              text = "AI TRAFFIC FLOW: 82 VEH/MIN",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFF38BDF8),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp
              )
            )
            Text(
              text = "SIGNAL STATUS: ${if (greenCorridorActive) "EMERGENCY ALL-GREEN" else "CYCLING (45s)"}",
              style = MaterialTheme.typography.labelSmall.copy(
                color = if (greenCorridorActive) StatusGreen else Color(0xFFCBD5E1),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp
              )
            )
          }

          // Snapshot Notification overlay
          if (snapshotTaken) {
            Box(
              modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.85f))
                .border(1.dp, StatusGreen, RoundedCornerShape(8.dp))
                .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
              Text(
                text = "✓ EVIDENCE SNAPSHOT RECORDED TO AP POLICE ARCHIVE",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = StatusGreen,
                  fontWeight = FontWeight.Bold
                )
              )
            }
          }
        }

        // Officer On-Duty Strip
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Officer in Charge: ${booth.officerName}",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              )
              Text(
                text = "Badge: ${booth.officerBadge} • Phone: ${booth.officerPhone}",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Color(0xFF94A3B8)
                )
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF0F172A))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "KIT: ${booth.equipmentStatus}",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = when (booth.equipmentStatus) {
                    "EMERGENCY_ACTIVE" -> EmergencyRed
                    "LOW_STOCK" -> EmergencyAmber
                    else -> StatusGreen
                  },
                  fontSize = 10.sp
                )
              )
            }
          }
        }

        // Tactical Action Controls
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Snapshot
          OutlinedButton(
            onClick = {
              snapshotTaken = true
            },
            modifier = Modifier
              .weight(1f)
              .testTag("cctv_snapshot_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF475569))
          ) {
            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Snapshot", fontSize = 12.sp)
          }

          // Siren / Alert Booth
          OutlinedButton(
            onClick = {
              onBroadcastSiren("AUDIO ALERT SENT: Siren activated at ${booth.name} traffic booth for pedestrian/emergency clearance.")
            },
            modifier = Modifier
              .weight(1f)
              .testTag("cctv_siren_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = EmergencyAmber),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyAmber.copy(alpha = 0.5f))
          ) {
            Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Sound Siren", fontSize = 12.sp)
          }

          // Green Wave Corridor
          Button(
            onClick = {
              greenCorridorActive = !greenCorridorActive
            },
            modifier = Modifier
              .weight(1.3f)
              .testTag("cctv_green_corridor_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (greenCorridorActive) StatusGreen else APNavyPrimary
            )
          ) {
            Icon(Icons.Default.Route, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (greenCorridorActive) "Corridor Active" else "Green Corridor",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
