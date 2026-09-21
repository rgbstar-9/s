package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.FirstAidItemEntity
import com.example.data.model.SupplyRequisitionEntity
import com.example.data.model.TrafficBoothEntity
import com.example.ui.MapFilter
import com.example.ui.theme.APNavyDark
import com.example.ui.theme.APNavyPrimary
import com.example.ui.theme.EmergencyAmber
import com.example.ui.theme.EmergencyAmberContainer
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedContainer
import com.example.ui.theme.GovBorder
import com.example.ui.theme.GovSurfaceSubtle
import com.example.ui.theme.MedicalTeal
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenContainer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommandControlCenterScreen(
  booths: List<TrafficBoothEntity>,
  filteredBooths: List<TrafficBoothEntity>,
  allItems: List<FirstAidItemEntity>,
  requisitions: List<SupplyRequisitionEntity>,
  incidents: List<EmergencyIncidentEntity>,
  activeFilter: MapFilter,
  selectedBooth: TrafficBoothEntity?,
  onSelectFilter: (MapFilter) -> Unit,
  onSelectBooth: (TrafficBoothEntity) -> Unit,
  onFulfillRequisition: (SupplyRequisitionEntity) -> Unit,
  onFulfillAllRequisitions: () -> Unit,
  onOpenBroadcastDialog: () -> Unit,
  onOpenCctv: (TrafficBoothEntity) -> Unit,
  onSwitchToFieldBooth: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf("MAP") } // MAP, REQUISITIONS, INCIDENTS, CCTV
  var selectedCorridorFilter by remember { mutableStateOf("ALL") }

  val pendingRequisitions = requisitions.filter { it.status == "PENDING_DISPATCH" }

  val corridorFilteredBooths = filteredBooths.filter { booth ->
    when (selectedCorridorFilter) {
      "ALL" -> true
      "EAST" -> booth.id in listOf(1, 8, 14, 15)
      "CENTRAL" -> booth.id in listOf(2, 7, 13)
      "NORTH_EAST" -> booth.id in listOf(4, 11, 12)
      "WEST" -> booth.id in listOf(6, 10)
      "SOUTH" -> booth.id in listOf(3, 5)
      else -> true
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(2.dp))
    }

    // 1. Vijayawada City Telemetry Stats Strip
    item {
      CityTelemetryBar(
        boothCount = booths.size,
        activeEmergencyCount = booths.count { it.equipmentStatus == "EMERGENCY_ACTIVE" },
        lowStockCount = booths.count { it.equipmentStatus == "LOW_STOCK" },
        expiredAlertCount = booths.count { it.equipmentStatus == "EXPIRED_ALERT" },
        pendingRequisitions = pendingRequisitions.size
      )
    }

    // 2. Control Views Switcher (Signals Map vs Requisition Depot vs Emergency Feed vs CCTV Matrix)
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0xFFF1F5F9))
          .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        ViewModeTab(
          title = "VJA Signals Grid",
          isSelected = selectedTab == "MAP",
          onClick = { selectedTab = "MAP" },
          modifier = Modifier.weight(1.1f)
        )
        ViewModeTab(
          title = "Medical Depot (${pendingRequisitions.size})",
          isSelected = selectedTab == "REQUISITIONS",
          onClick = { selectedTab = "REQUISITIONS" },
          modifier = Modifier.weight(1.3f)
        )
        ViewModeTab(
          title = "108 Incidents (${incidents.size})",
          isSelected = selectedTab == "INCIDENTS",
          onClick = { selectedTab = "INCIDENTS" },
          modifier = Modifier.weight(1.2f)
        )
        ViewModeTab(
          title = "CCTV Feeds",
          isSelected = selectedTab == "CCTV",
          onClick = { selectedTab = "CCTV" },
          modifier = Modifier.weight(1f)
        )
      }
    }

    when (selectedTab) {
      "MAP" -> {
        // Broadcast & Corridor Action Bar
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Button(
              onClick = onOpenBroadcastDialog,
              modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .testTag("btn_open_broadcast"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
            ) {
              Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Emergency Corridor Broadcast", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        // Corridor Filter Chips
        item {
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
              text = "TRAFFIC CORRIDOR FILTER",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B),
                letterSpacing = 0.5.sp
              )
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              item { CorridorFilterChip("All VJA (15)", selectedCorridorFilter == "ALL") { selectedCorridorFilter = "ALL" } }
              item { CorridorFilterChip("East / NH16 (4)", selectedCorridorFilter == "EAST") { selectedCorridorFilter = "EAST" } }
              item { CorridorFilterChip("Central / MG Rd (3)", selectedCorridorFilter == "CENTRAL") { selectedCorridorFilter = "CENTRAL" } }
              item { CorridorFilterChip("North-East / Eluru Rd (3)", selectedCorridorFilter == "NORTH_EAST") { selectedCorridorFilter = "NORTH_EAST" } }
              item { CorridorFilterChip("West / NH65 (2)", selectedCorridorFilter == "WEST") { selectedCorridorFilter = "WEST" } }
              item { CorridorFilterChip("South / Krishna Varadhi (2)", selectedCorridorFilter == "SOUTH") { selectedCorridorFilter = "SOUTH" } }
            }
          }
        }

        // Status Filter Pills
        item {
          MapFilterPillsRow(
            activeFilter = activeFilter,
            allCount = booths.size,
            emergencyCount = booths.count { it.equipmentStatus == "EMERGENCY_ACTIVE" },
            lowStockCount = booths.count { it.equipmentStatus == "LOW_STOCK" },
            expiredCount = booths.count { it.equipmentStatus == "EXPIRED_ALERT" },
            onSelectFilter = onSelectFilter
          )
        }

        // Custom Vijayawada Interactive Map Canvas
        item {
          VijayawadaSignalsMapCanvas(
            booths = corridorFilteredBooths,
            selectedBooth = selectedBooth,
            onBoothTapped = onSelectBooth
          )
        }

        // Inspected Booth Detail Card
        item {
          if (selectedBooth != null) {
            val boothItems = allItems.filter { it.boothId == selectedBooth.id }
            BoothInspectionCard(
              booth = selectedBooth,
              items = boothItems,
              onOpenFieldStation = { onSwitchToFieldBooth(selectedBooth.id) },
              onOpenCctv = { onOpenCctv(selectedBooth) }
            )
          } else {
            Card(
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = androidx.compose.foundation.BorderStroke(1.dp, GovBorder),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = APNavyPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Tap any traffic signal on the map above to inspect live inventory & CCTV.",
                  fontSize = 12.sp,
                  color = Color(0xFF64748B)
                )
              }
            }
          }
        }
      }

      "REQUISITIONS" -> {
        // Bulk Dispatch Action Header
        item {
          Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
            border = androidx.compose.foundation.BorderStroke(1.dp, APNavyPrimary.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "VIJAYAWADA CENTRAL MEDICAL DEPOT",
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  color = APNavyDark
                )
                Text(
                  text = "${pendingRequisitions.size} booths requesting immediate medical restocking",
                  fontSize = 10.sp,
                  color = Color(0xFF64748B)
                )
              }

              Button(
                onClick = onFulfillAllRequisitions,
                enabled = pendingRequisitions.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = APNavyDark),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag("btn_dispatch_all_depot")
              ) {
                Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Dispatch All Vans", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        if (requisitions.isEmpty()) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text("No supply requisitions recorded. All 15 booths fully stocked.", color = Color.Gray, fontSize = 12.sp)
              }
            }
          }
        } else {
          items(requisitions) { req ->
            RequisitionRowCard(
              requisition = req,
              onFulfill = { onFulfillRequisition(req) }
            )
          }
        }
      }

      "INCIDENTS" -> {
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "ACCIDENT & 108 AMBULANCE DISPATCH LOGS (${incidents.size})",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = APNavyDark
            )
            Text(
              text = "Real-time GGH Triage",
              fontSize = 10.sp,
              color = Color.Gray
            )
          }
        }

        if (incidents.isEmpty()) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text("No accidents reported across Vijayawada today.", color = Color.Gray, fontSize = 12.sp)
              }
            }
          }
        } else {
          items(incidents) { inc ->
            IncidentTelemetryCard(incident = inc)
          }
        }
      }

      "CCTV" -> {
        item {
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = "LIVE JUNCTION CCTV SURVEILLANCE MATRIX (15 SIGNALS)",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = APNavyDark
              )
            )
            Text(
              text = "Tap any signal to open 4K zoom camera & trigger emergency green corridor wave.",
              style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
            )
          }
        }

        items(booths) { booth ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onOpenCctv(booth) },
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, GovBorder),
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                  modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (booth.cctvConnected) Color(0xFF0F172A) else Color(0xFFE2E8F0)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (booth.cctvConnected) Icons.Default.Videocam else Icons.Default.VideocamOff,
                    contentDescription = null,
                    tint = if (booth.cctvConnected) StatusGreen else Color.Gray,
                    modifier = Modifier.size(20.dp)
                  )
                }

                Column {
                  Text(
                    text = "${booth.boothCode} — ${booth.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = APNavyDark
                  )
                  Text(
                    text = "${booth.junctionName} • 4K Optical PTZ",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                  )
                }
              }

              Button(
                onClick = { onOpenCctv(booth) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
              ) {
                Text("Open Feed", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun CorridorFilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(if (isSelected) APNavyDark else Color(0xFFF1F5F9))
      .border(1.dp, if (isSelected) APNavyDark else GovBorder, RoundedCornerShape(6.dp))
      .clickable(onClick = onClick)
      .padding(horizontal = 8.dp, vertical = 4.dp)
  ) {
    Text(
      text = label,
      fontSize = 10.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) Color.White else Color(0xFF475569)
    )
  }
}

@Composable
private fun CityTelemetryBar(
  boothCount: Int,
  activeEmergencyCount: Int,
  lowStockCount: Int,
  expiredAlertCount: Int,
  pendingRequisitions: Int
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = APNavyDark),
    shape = RoundedCornerShape(14.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(StatusGreen)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "VJA CITY EMERGENCY COMMAND GRID",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 0.5.sp
          )
        }

        Text(
          text = "108 Telemetry: LIVE",
          fontSize = 10.sp,
          color = Color(0xFF93C5FD),
          fontWeight = FontWeight.SemiBold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        TelemetryStat(value = "$boothCount", label = "Booths Active", color = Color.White)
        TelemetryStat(value = "$activeEmergencyCount", label = "Emergencies", color = if (activeEmergencyCount > 0) EmergencyRed else Color.White)
        TelemetryStat(value = "$lowStockCount", label = "Low Stock", color = if (lowStockCount > 0) EmergencyAmber else Color.White)
        TelemetryStat(value = "$expiredAlertCount", label = "Expired Alert", color = if (expiredAlertCount > 0) EmergencyRed else Color.White)
        TelemetryStat(value = "$pendingRequisitions", label = "Supply Vans", color = MedicalTeal)
      }
    }
  }
}

@Composable
private fun TelemetryStat(value: String, label: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = color)
    Text(text = label, fontSize = 9.sp, color = Color(0xFF94A3B8))
  }
}

@Composable
private fun ViewModeTab(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) Color.White else Color.Transparent)
      .clickable(onClick = onClick)
      .padding(vertical = 8.dp, horizontal = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = title,
      fontSize = 10.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) APNavyDark else Color(0xFF64748B),
      maxLines = 1
    )
  }
}

@Composable
private fun MapFilterPillsRow(
  activeFilter: MapFilter,
  allCount: Int,
  emergencyCount: Int,
  lowStockCount: Int,
  expiredCount: Int,
  onSelectFilter: (MapFilter) -> Unit
) {
  LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
    item {
      FilterPill(label = "All ($allCount)", isSelected = activeFilter == MapFilter.ALL, onClick = { onSelectFilter(MapFilter.ALL) })
    }
    item {
      FilterPill(label = "🚨 Emergency ($emergencyCount)", isSelected = activeFilter == MapFilter.ACTIVE_EMERGENCY, activeColor = EmergencyRed, onClick = { onSelectFilter(MapFilter.ACTIVE_EMERGENCY) })
    }
    item {
      FilterPill(label = "⚠️ Low Stock ($lowStockCount)", isSelected = activeFilter == MapFilter.LOW_STOCK, activeColor = EmergencyAmber, onClick = { onSelectFilter(MapFilter.LOW_STOCK) })
    }
    item {
      FilterPill(label = "⛔ Expired ($expiredCount)", isSelected = activeFilter == MapFilter.EXPIRED_ALERT, activeColor = EmergencyRed, onClick = { onSelectFilter(MapFilter.EXPIRED_ALERT) })
    }
  }
}

@Composable
private fun FilterPill(
  label: String,
  isSelected: Boolean,
  activeColor: Color = APNavyDark,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) activeColor else Color(0xFFF1F5F9))
      .clickable(onClick = onClick)
      .padding(horizontal = 10.dp, vertical = 6.dp)
  ) {
    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) Color.White else Color(0xFF475569)
    )
  }
}

@Composable
fun VijayawadaSignalsMapCanvas(
  booths: List<TrafficBoothEntity>,
  selectedBooth: TrafficBoothEntity?,
  onBoothTapped: (TrafficBoothEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  val pulseTransition = rememberInfiniteTransition(label = "mapPulse")
  val pulseRadius by pulseTransition.animateFloat(
    initialValue = 12f,
    targetValue = 28f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200),
      repeatMode = RepeatMode.Restart
    ),
    label = "pulse"
  )

  Card(
    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
    border = androidx.compose.foundation.BorderStroke(1.dp, GovBorder),
    shape = RoundedCornerShape(16.dp),
    modifier = modifier
      .fillMaxWidth()
      .height(290.dp)
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      Canvas(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(booths) {
            detectTapGestures { offset ->
              val w = size.width
              val h = size.height
              var closest: TrafficBoothEntity? = null
              var minDistance = 45f * density

              booths.forEach { b ->
                val (nx, ny) = projectToMap(b.id)
                val bx = nx * w
                val by = ny * h
                val dist = kotlin.math.hypot(bx - offset.x, by - offset.y)
                if (dist < minDistance) {
                  minDistance = dist
                  closest = b
                }
              }
              closest?.let { onBoothTapped(it) }
            }
          }
      ) {
        val w = size.width
        val h = size.height

        // Draw Krishna River flowing diagonally across Vijayawada
        val riverPath = Path().apply {
          moveTo(0f, h * 0.78f)
          cubicTo(
            w * 0.25f, h * 0.72f,
            w * 0.45f, h * 0.85f,
            w * 0.65f, h * 0.88f
          )
          lineTo(w, h * 0.95f)
          lineTo(w, h)
          lineTo(0f, h)
          close()
        }
        drawPath(path = riverPath, color = Color(0xFFBAE6FD).copy(alpha = 0.65f))

        // Draw Arterial Road Grid (NH16, MG Road, Eluru Road, BRTS)
        // MG Road (Horizontal central corridor)
        drawLine(
          color = Color(0xFFCBD5E1),
          start = Offset(w * 0.15f, h * 0.42f),
          end = Offset(w * 0.85f, h * 0.45f),
          strokeWidth = 6f,
          cap = StrokeCap.Round
        )

        // NH16 Highway (Diagonal from Varadhi bridge across Benz Circle to Ramavarappadu & Airport)
        drawLine(
          color = Color(0xFF94A3B8),
          start = Offset(w * 0.28f, h * 0.82f),
          end = Offset(w * 0.58f, h * 0.48f),
          strokeWidth = 8f,
          cap = StrokeCap.Round
        )
        drawLine(
          color = Color(0xFF94A3B8),
          start = Offset(w * 0.58f, h * 0.48f),
          end = Offset(w * 0.90f, h * 0.14f),
          strokeWidth = 8f,
          cap = StrokeCap.Round
        )

        // Prakasam Barrage / Varadhi Bridge crossing
        drawLine(
          color = Color(0xFF0284C7),
          start = Offset(w * 0.25f, h * 0.70f),
          end = Offset(w * 0.32f, h * 0.86f),
          strokeWidth = 5f
        )

        // Plot Each Traffic Signal Junction Node
        booths.forEach { booth ->
          val (nx, ny) = projectToMap(booth.id)
          val cx = nx * w
          val cy = ny * h

          val isSelected = selectedBooth?.id == booth.id
          val isEmergency = booth.equipmentStatus == "EMERGENCY_ACTIVE"
          val isLowStock = booth.equipmentStatus == "LOW_STOCK"
          val isExpired = booth.equipmentStatus == "EXPIRED_ALERT"

          val nodeColor = when {
            isEmergency -> EmergencyRed
            isExpired -> Color(0xFF991B1B)
            isLowStock -> EmergencyAmber
            else -> StatusGreen
          }

          // Pulsing Ring for Emergency
          if (isEmergency) {
            drawCircle(
              color = EmergencyRed.copy(alpha = 0.35f),
              radius = pulseRadius * density,
              center = Offset(cx, cy),
              style = Stroke(width = 3f)
            )
          } else if (isLowStock || isExpired) {
            drawCircle(
              color = EmergencyAmber.copy(alpha = 0.3f),
              radius = 18f * density,
              center = Offset(cx, cy),
              style = Stroke(width = 2f)
            )
          }

          // Selection Ring
          if (isSelected) {
            drawCircle(
              color = APNavyDark,
              radius = 16f * density,
              center = Offset(cx, cy),
              style = Stroke(width = 3.5f)
            )
          }

          // Main Signal Center
          drawCircle(
            color = Color.White,
            radius = 11f * density,
            center = Offset(cx, cy)
          )
          drawCircle(
            color = nodeColor,
            radius = 7.5f * density,
            center = Offset(cx, cy)
          )
        }
      }

      // Map Legend Overlay at Top Right
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(8.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(Color.White.copy(alpha = 0.94f))
          .padding(horizontal = 8.dp, vertical = 6.dp)
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
          LegendDot(color = StatusGreen, text = "Ready 100%")
          LegendDot(color = EmergencyAmber, text = "Low Stock")
          LegendDot(color = EmergencyRed, text = "108 Active")
        }
      }

      // River & Landmark Labels
      Text(
        text = "Krishna River (Prakasam Barrage & Varadhi)",
        fontSize = 9.sp,
        color = Color(0xFF0369A1),
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(start = 12.dp, bottom = 8.dp)
      )

      Text(
        text = "Vijayawada Smart Grid • 15 Signals Connected",
        fontSize = 9.sp,
        color = Color(0xFF64748B),
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(start = 12.dp, top = 8.dp)
      )
    }
  }
}

private fun projectToMap(boothId: Int): Pair<Float, Float> {
  return when (boothId) {
    1 -> 0.58f to 0.48f // Benz Circle (NH16 & MG Road junction)
    2 -> 0.38f to 0.43f // Police Control Room / Old Bus Stand
    3 -> 0.28f to 0.54f // PNBS (Pandit Nehru Bus Station)
    4 -> 0.74f to 0.28f // Ramavarappadu Ring (Eluru Road / NH16 Ring)
    5 -> 0.32f to 0.78f // Kanaka Durga Varadhi South
    6 -> 0.18f to 0.32f // Bhavanipuram Junction
    7 -> 0.48f to 0.41f // Governorpet / Alankar Signal
    8 -> 0.82f to 0.58f // Auto Nagar Gate Junction
    9 -> 0.90f to 0.14f // Gannavaram Airport Highway
    10 -> 0.12f to 0.20f // Gollapudi Center
    11 -> 0.68f to 0.34f // Gunadala Center Signal
    12 -> 0.52f to 0.36f // Machavaram Down Junction
    13 -> 0.24f to 0.42f // One Town KR Market
    14 -> 0.66f to 0.54f // NTR Health University Circle
    15 -> 0.82f to 0.24f // Enikepadu NH16 Ring
    else -> 0.5f to 0.5f
  }
}

@Composable
private fun LegendDot(color: Color, text: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(7.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = text, fontSize = 9.sp, color = Color(0xFF334155))
  }
}

@Composable
private fun BoothInspectionCard(
  booth: TrafficBoothEntity,
  items: List<FirstAidItemEntity>,
  onOpenFieldStation: () -> Unit,
  onOpenCctv: () -> Unit
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.5.dp, APNavyDark),
    shape = RoundedCornerShape(14.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "${booth.boothCode} Inspection",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = APNavyPrimary
            )
            Spacer(modifier = Modifier.width(6.dp))
            StatusChip(status = booth.equipmentStatus)
          }
          Text(
            text = booth.name,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = APNavyDark
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          OutlinedButton(
            onClick = onOpenCctv,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
          ) {
            Icon(Icons.Default.Videocam, contentDescription = null, tint = APNavyDark, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("CCTV", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = APNavyDark)
          }

          Button(
            onClick = onOpenFieldStation,
            colors = ButtonDefaults.buttonColors(containerColor = APNavyDark),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
          ) {
            Text("Open Station", fontSize = 10.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Officer in Charge
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(GovSurfaceSubtle)
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Officer: ${booth.officerName} (${booth.officerBadge})",
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold,
          color = APNavyDark
        )
        Text(
          text = "CCTV: ${if (booth.cctvConnected) "Online 4K" else "Offline"}",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = if (booth.cctvConnected) StatusGreen else EmergencyAmber
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Mini Inventory Snapshot
      Text(
        text = "TRAUMA KIT INVENTORY AUDIT (${items.size} ITEMS):",
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF64748B)
      )

      Spacer(modifier = Modifier.height(6.dp))

      items.take(4).forEach { itm ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = itm.name, fontSize = 11.sp, color = APNavyDark)
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "${itm.currentStock} ${itm.unit}",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (itm.isLowStock) EmergencyRed else Color(0xFF334155)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Exp: ${itm.expiryDate}",
              fontSize = 9.sp,
              color = if (itm.isExpired) EmergencyRed else Color.Gray
            )
          }
        }
      }
    }
  }
}

@Composable
private fun RequisitionRowCard(
  requisition: SupplyRequisitionEntity,
  onFulfill: () -> Unit
) {
  val isPending = requisition.status == "PENDING_DISPATCH"
  Card(
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, if (isPending) EmergencyAmber else GovBorder),
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = requisition.boothName,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = APNavyDark
          )
          Spacer(modifier = Modifier.width(6.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(if (requisition.urgency == "URGENT") EmergencyRedContainer else Color(0xFFF1F5F9))
              .padding(horizontal = 4.dp, vertical = 1.dp)
          ) {
            Text(
              text = requisition.urgency,
              fontSize = 8.sp,
              fontWeight = FontWeight.Bold,
              color = if (requisition.urgency == "URGENT") EmergencyRed else Color(0xFF475569)
            )
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = "Item: ${requisition.itemName} • Qty: ${requisition.quantityNeeded}",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = APNavyDark
        )
        Text(
          text = "Reason: ${requisition.reason.replace("_", " ")}",
          fontSize = 10.sp,
          color = Color(0xFF64748B)
        )
      }

      if (isPending) {
        Button(
          onClick = onFulfill,
          colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
          modifier = Modifier.height(34.dp)
        ) {
          Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Dispatch Van", fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
      } else {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(StatusGreenContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Check, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("RESTOCKED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = StatusGreen)
          }
        }
      }
    }
  }
}

@Composable
private fun IncidentTelemetryCard(incident: EmergencyIncidentEntity) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, GovBorder),
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(if (incident.severity == "CRITICAL_RED") EmergencyRed else EmergencyAmber)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = incident.boothName,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = APNavyDark
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFF1F5F9))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = incident.ambulanceStatus.replace("_", " "),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF475569)
          )
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "${incident.injuryType} (${incident.victimCount} victims) • ${incident.vehicleTypes}",
        fontSize = 11.sp,
        color = Color(0xFF1E293B),
        fontWeight = FontWeight.Medium
      )
      Text(
        text = "First Aid Administered: ${incident.firstAidAdministered} • Supplies: ${incident.itemsUsedSummary}",
        fontSize = 10.sp,
        color = Color(0xFF64748B)
      )
      Text(
        text = "108 Unit: ${incident.ambulanceUnit} -> ${incident.hospitalDestination}",
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        color = APNavyPrimary
      )
    }
  }
}

@Composable
private fun StatusChip(status: String) {
  val (label, bg, color) = when (status) {
    "EMERGENCY_ACTIVE" -> Triple("EMERGENCY ACTIVE", EmergencyRedContainer, EmergencyRed)
    "LOW_STOCK" -> Triple("LOW STOCK", EmergencyAmberContainer, EmergencyAmber)
    "EXPIRED_ALERT" -> Triple("EXPIRED SUPPLY", EmergencyRedContainer, EmergencyRed)
    else -> Triple("NORMAL & READY", StatusGreenContainer, StatusGreen)
  }

  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(4.dp))
      .background(bg)
      .padding(horizontal = 6.dp, vertical = 2.dp)
  ) {
    Text(
      text = label,
      fontWeight = FontWeight.ExtraBold,
      fontSize = 9.sp,
      color = color
    )
  }
}
