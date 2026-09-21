package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.FirstAidItemEntity
import com.example.data.model.TrafficBoothEntity
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
fun TrafficPoliceBoothScreen(
  currentBooth: TrafficBoothEntity?,
  allBooths: List<TrafficBoothEntity>,
  items: List<FirstAidItemEntity>,
  incidents: List<EmergencyIncidentEntity>,
  onSelectBooth: (Int) -> Unit,
  onOpenSOSDialog: () -> Unit,
  onOpenProtocolGuide: () -> Unit,
  onDispenseItem: (FirstAidItemEntity, TrafficBoothEntity) -> Unit,
  onRequestRestock: (FirstAidItemEntity) -> Unit,
  onAdvanceAmbulance: (EmergencyIncidentEntity) -> Unit,
  onOpenEditExpiry: (FirstAidItemEntity) -> Unit,
  onOpenAddNewItem: () -> Unit,
  onOpenCctv: (TrafficBoothEntity) -> Unit,
  onDeleteItem: (Int, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var isInventoryCabinetOpen by remember { mutableStateOf(true) }
  var selectedCategory by remember { mutableStateOf("ALL") }
  var searchQuery by remember { mutableStateOf("") }
  var boothDropdownExpanded by remember { mutableStateOf(false) }

  val categories = listOf(
    "ALL",
    "Hemorrhage Control",
    "Airway & Resuscitation",
    "Wound & Burn",
    "Fracture & Immobilization",
    "Antiseptics & Consumables"
  )

  val filteredItems = items.filter {
    val matchesCategory = if (selectedCategory == "ALL") true else it.category.equals(selectedCategory, ignoreCase = true)
    val matchesSearch = if (searchQuery.isBlank()) true else {
      it.name.contains(searchQuery, ignoreCase = true) ||
        it.category.contains(searchQuery, ignoreCase = true) ||
        it.locationInKit.contains(searchQuery, ignoreCase = true) ||
        it.batchNumber.contains(searchQuery, ignoreCase = true)
    }
    matchesCategory && matchesSearch
  }

  val activeIncident = incidents.firstOrNull { it.ambulanceStatus != "TRANSFERRED_GGH" }
  val lowStockCount = items.count { it.isLowStock }
  val expiredCount = items.count { it.isExpired }
  val expiringSoonCount = items.count { it.isExpiringSoon }
  val totalItems = items.size
  val readyItems = items.count { !it.isExpired && !it.isLowStock }
  val readinessPercentage = if (totalItems > 0) ((readyItems.toFloat() / totalItems) * 100).toInt() else 100

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(2.dp))
    }

    // 1. All Booths Fast Horizontal Selector Strip
    item {
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "VIJAYAWADA TRAFFIC BOOTHS GRID (${allBooths.size})",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = Color(0xFF64748B),
              letterSpacing = 0.8.sp
            )
          )
          Text(
            text = "Tap to Switch Station",
            style = MaterialTheme.typography.labelSmall.copy(
              color = APNavyPrimary,
              fontWeight = FontWeight.Bold
            )
          )
        }

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(vertical = 2.dp)
        ) {
          items(allBooths) { b ->
            val isSelected = currentBooth?.id == b.id
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) APNavyDark else Color.White)
                .border(
                  width = if (isSelected) 1.5.dp else 1.dp,
                  color = if (isSelected) APNavyDark else GovBorder,
                  shape = RoundedCornerShape(8.dp)
                )
                .clickable { onSelectBooth(b.id) }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                // Status dot
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                      when (b.equipmentStatus) {
                        "EMERGENCY_ACTIVE" -> EmergencyRed
                        "LOW_STOCK" -> EmergencyAmber
                        "EXPIRED_ALERT" -> EmergencyRed
                        else -> StatusGreen
                      }
                    )
                )
                Column {
                  Text(
                    text = b.boothCode,
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = if (isSelected) Color.White else APNavyDark
                    )
                  )
                  Text(
                    text = b.name.replace(" Traffic Booth", "").replace(" Junction", ""),
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontSize = 9.sp,
                      color = if (isSelected) Color(0xFF94A3B8) else Color(0xFF64748B)
                    )
                  )
                }
              }
            }
          }
        }
      }
    }

    // 2. Current Booth Master Header Card
    item {
      if (currentBooth != null) {
        BoothHeaderCard(
          booth = currentBooth,
          allBooths = allBooths,
          isDropdownExpanded = boothDropdownExpanded,
          onToggleDropdown = { boothDropdownExpanded = !boothDropdownExpanded },
          onSelectBooth = {
            onSelectBooth(it)
            boothDropdownExpanded = false
          },
          onOpenCctv = { onOpenCctv(currentBooth) }
        )
      }
    }

    // 3. Active Emergency Live Incident Banner (If ongoing)
    if (activeIncident != null && currentBooth != null) {
      item {
        ActiveIncidentBanner(
          incident = activeIncident,
          onAdvanceAmbulance = { onAdvanceAmbulance(activeIncident) }
        )
      }
    }

    // 4. Primary Emergency SOS & Protocol Dispatch Action Bar
    item {
      EmergencyActionHub(
        onOpenSOSDialog = onOpenSOSDialog,
        onOpenProtocolGuide = onOpenProtocolGuide
      )
    }

    // 5. Automated Department Alert Banner (if any expired or low stock)
    if (lowStockCount > 0 || expiredCount > 0 || expiringSoonCount > 0) {
      item {
        AutomatedDepartmentAlertBar(
          lowStockCount = lowStockCount,
          expiredCount = expiredCount,
          expiringSoonCount = expiringSoonCount,
          onReorderAll = {
            val firstTarget = items.firstOrNull { it.isExpired || it.isLowStock || it.isExpiringSoon }
            if (firstTarget != null) onRequestRestock(firstTarget)
          }
        )
      }
    }

    // 6. Interactive "OPEN FIRST AID INVENTORY" Cabinet Box
    item {
      FirstAidCabinetCard(
        currentBooth = currentBooth,
        isOpen = isInventoryCabinetOpen,
        onToggleOpen = { isInventoryCabinetOpen = !isInventoryCabinetOpen },
        totalItems = totalItems,
        readinessPercentage = readinessPercentage,
        expiredCount = expiredCount,
        lowStockCount = lowStockCount,
        expiringSoonCount = expiringSoonCount,
        onOpenAddNewItem = onOpenAddNewItem
      )
    }

    // 7. Filter Bar & Items List (Visible when Inventory is Opened)
    if (isInventoryCabinetOpen) {
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          // Search & Add Bar
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = searchQuery,
              onValueChange = { searchQuery = it },
              placeholder = { Text("Search medicine, kit, batch...", fontSize = 12.sp) },
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("search_inventory_input"),
              leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
              },
              trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                  IconButton(onClick = { searchQuery = "" }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                  }
                }
              },
              singleLine = true,
              shape = RoundedCornerShape(10.dp)
            )

            Button(
              onClick = onOpenAddNewItem,
              modifier = Modifier
                .height(48.dp)
                .testTag("add_item_top_button"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MedicalTeal),
              contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add Item", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }

          // Category Chips
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            items(categories) { cat ->
              val isSelected = selectedCategory == cat
              val shortLabel = when (cat) {
                "ALL" -> "All Items (${items.size})"
                "Hemorrhage Control" -> "Hemorrhage"
                "Airway & Resuscitation" -> "Airway & O2"
                "Wound & Burn" -> "Burns & Wounds"
                "Fracture & Immobilization" -> "Splints"
                else -> "Antiseptics"
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) APNavyDark else Color(0xFFF1F5F9))
                  .border(1.dp, if (isSelected) APNavyDark else GovBorder, RoundedCornerShape(8.dp))
                  .clickable { selectedCategory = cat }
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(
                  text = shortLabel,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else Color(0xFF475569)
                )
              }
            }
          }
        }
      }

      // Empty State if no items match
      if (filteredItems.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, GovBorder),
            shape = RoundedCornerShape(12.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.Inventory, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(32.dp))
              Text("No first aid items match your filter", fontWeight = FontWeight.Bold, color = APNavyDark)
              OutlinedButton(onClick = { searchQuery = ""; selectedCategory = "ALL" }) {
                Text("Reset Filters")
              }
            }
          }
        }
      }

      // Item Cards
      items(filteredItems) { item ->
        FirstAidItemCard(
          item = item,
          onDispense = {
            if (currentBooth != null) onDispenseItem(item, currentBooth)
          },
          onRequestRestock = { onRequestRestock(item) },
          onOpenEditExpiry = { onOpenEditExpiry(item) },
          onDeleteItem = { onDeleteItem(item.id, item.name) }
        )
      }
    }

    // 8. Recent Junction Incident History
    item {
      Spacer(modifier = Modifier.height(6.dp))
      IncidentLogbookSection(incidents = incidents)
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun FirstAidCabinetCard(
  currentBooth: TrafficBoothEntity?,
  isOpen: Boolean,
  onToggleOpen: () -> Unit,
  totalItems: Int,
  readinessPercentage: Int,
  expiredCount: Int,
  lowStockCount: Int,
  expiringSoonCount: Int,
  onOpenAddNewItem: () -> Unit
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isOpen) APNavyPrimary else GovBorder),
    shape = RoundedCornerShape(16.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      // Top Box Banner
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(if (isOpen) APNavyPrimary else Color(0xFF0F172A)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isOpen) Icons.Default.FolderOpen else Icons.Default.MedicalServices,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(26.dp)
            )
          }

          Column {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(
                text = "TRAUMA FIRST AID KIT BOX",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = APNavyDark
                )
              )
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(StatusGreenContainer)
                  .padding(horizontal = 5.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "SEALED & STERILE",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = StatusGreen
                )
              }
            }
            Text(
              text = "Model: ${currentBooth?.firstAidBoxModel ?: "AP-TRAUMA-KIT-V2"} • Wall Mount",
              style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
            )
          }
        }

        // Readiness Pill
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "$readinessPercentage% READY",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.ExtraBold,
              color = if (readinessPercentage >= 80) StatusGreen else EmergencyAmber,
              fontSize = 11.sp
            )
          )
          Text(
            text = "$totalItems Products Tracked",
            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontSize = 9.sp)
          )
        }
      }

      // Readiness Progress Bar
      LinearProgressIndicator(
        progress = { readinessPercentage / 100f },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = if (readinessPercentage >= 80) StatusGreen else EmergencyAmber,
        trackColor = Color(0xFFE2E8F0)
      )

      // Expiry & Stock Health Summary Matrix
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(GovSurfaceSubtle)
          .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "$totalItems",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = APNavyDark
          )
          Text("Total Supplies", fontSize = 10.sp, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "$lowStockCount",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (lowStockCount > 0) EmergencyAmber else StatusGreen
          )
          Text("Low Stock", fontSize = 10.sp, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "$expiringSoonCount",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (expiringSoonCount > 0) EmergencyAmber else StatusGreen
          )
          Text("Expiring Soon", fontSize = 10.sp, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "$expiredCount",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (expiredCount > 0) EmergencyRed else StatusGreen
          )
          Text("Expired", fontSize = 10.sp, color = Color.Gray)
        }
      }

      // Large Primary "OPEN / CLOSE INVENTORY" Button
      Button(
        onClick = onToggleOpen,
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("btn_toggle_inventory"),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = if (isOpen) APNavyDark else APNavyPrimary
        )
      ) {
        Icon(
          imageVector = if (isOpen) Icons.Default.Inventory else Icons.Default.FolderOpen,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isOpen) "HIDE FIRST AID INVENTORY" else "OPEN FIRST AID INVENTORY CABINET",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          letterSpacing = 0.5.sp
        )
      }
    }
  }
}

@Composable
private fun BoothHeaderCard(
  booth: TrafficBoothEntity,
  allBooths: List<TrafficBoothEntity>,
  isDropdownExpanded: Boolean,
  onToggleDropdown: () -> Unit,
  onSelectBooth: (Int) -> Unit,
  onOpenCctv: () -> Unit
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, GovBorder),
    shape = RoundedCornerShape(14.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFE0F2FE))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = booth.boothCode,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                color = APNavyPrimary
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            StatusChip(status = booth.equipmentStatus)
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = booth.name,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = APNavyDark
          )

          Text(
            text = booth.junctionName,
            fontSize = 12.sp,
            color = Color(0xFF64748B)
          )
        }

        // Switch Booth Dropdown
        Box {
          OutlinedButton(
            onClick = onToggleDropdown,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
            modifier = Modifier.testTag("btn_switch_booth")
          ) {
            Text("Switch Booth", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = APNavyDark)
            Icon(Icons.Default.ExpandMore, contentDescription = null, modifier = Modifier.size(16.dp))
          }

          DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = onToggleDropdown
          ) {
            allBooths.forEach { b ->
              DropdownMenuItem(
                text = {
                  Column {
                    Text("${b.boothCode}: ${b.name}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("${b.junctionName} • ${b.area}", fontSize = 10.sp, color = Color(0xFF64748B))
                  }
                },
                onClick = { onSelectBooth(b.id) }
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Officer on Duty & CCTV Live Feed Trigger
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(GovSurfaceSubtle)
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.LocationOn, contentDescription = null, tint = MedicalTeal, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${booth.officerName} (${booth.officerBadge})",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = APNavyDark
          )
        }

        // View Junction CCTV Button
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF0F172A))
            .clickable(onClick = onOpenCctv)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.Videocam, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(12.dp))
            Text(
              text = "Live CCTV",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
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

@Composable
private fun EmergencyActionHub(
  onOpenSOSDialog: () -> Unit,
  onOpenProtocolGuide: () -> Unit
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, GovBorder),
    shape = RoundedCornerShape(14.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(
        text = "ACCIDENT & GOLDEN HOUR PROTOCOL",
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = Color(0xFF64748B),
        letterSpacing = 0.5.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // SOS Button
        Button(
          onClick = onOpenSOSDialog,
          colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1.3f)
            .height(48.dp)
            .testTag("btn_emergency_sos")
        ) {
          Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("ACCIDENT SOS (108)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }

        // Protocol Guide
        OutlinedButton(
          onClick = onOpenProtocolGuide,
          shape = RoundedCornerShape(10.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, APNavyPrimary),
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("btn_protocol_guide")
        ) {
          Icon(Icons.Default.MenuBook, contentDescription = null, tint = APNavyPrimary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Protocols", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = APNavyPrimary)
        }
      }
    }
  }
}

@Composable
private fun ActiveIncidentBanner(
  incident: EmergencyIncidentEntity,
  onAdvanceAmbulance: () -> Unit
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
    border = androidx.compose.foundation.BorderStroke(1.5.dp, EmergencyRed),
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
              .size(10.dp)
              .clip(CircleShape)
              .background(EmergencyRed)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "ACTIVE INCIDENT: ${incident.injuryType}",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            color = EmergencyRed
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFFEE2E2))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = "ETA: ${incident.ambulanceEtaMinutes} MINS",
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = EmergencyRed
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "First Aid: ${incident.firstAidAdministered}",
        fontSize = 11.sp,
        color = Color(0xFF7F1D1D)
      )
      Text(
        text = "Ambulance Unit: ${incident.ambulanceUnit} • Dest: ${incident.hospitalDestination}",
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF991B1B)
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Status: ${incident.ambulanceStatus.replace("_", " ")}",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = APNavyDark
        )

        Button(
          onClick = onAdvanceAmbulance,
          colors = ButtonDefaults.buttonColors(containerColor = APNavyDark),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.height(34.dp)
        ) {
          Text(
            text = when (incident.ambulanceStatus) {
              "DISPATCHED" -> "Mark En Route"
              "EN_ROUTE" -> "Mark Arrived"
              "ARRIVED" -> "Mark Transferred GGH"
              else -> "Complete"
            },
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
private fun AutomatedDepartmentAlertBar(
  lowStockCount: Int,
  expiredCount: Int,
  expiringSoonCount: Int,
  onReorderAll: () -> Unit
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "LOGISTICS AUTO-INTIMATION QUEUED",
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = Color(0xFF92400E)
          )
          val alertText = buildString {
            if (expiredCount > 0) append("$expiredCount Expired ")
            if (lowStockCount > 0) append("$lowStockCount Low-Stock ")
            if (expiringSoonCount > 0) append("$expiringSoonCount Expiring Soon")
          }
          Text(
            text = "$alertText • Auto alert ready for Central Depot",
            fontSize = 10.sp,
            color = Color(0xFF78350F)
          )
        }
      }

      Button(
        onClick = onReorderAll,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309)),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        modifier = Modifier.height(32.dp)
      ) {
        Text("Reorder", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
      }
    }
  }
}

@Composable
private fun FirstAidItemCard(
  item: FirstAidItemEntity,
  onDispense: () -> Unit,
  onRequestRestock: () -> Unit,
  onOpenEditExpiry: () -> Unit,
  onDeleteItem: () -> Unit
) {
  var isExpanded by remember { mutableStateOf(false) }

  Card(
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(
      width = if (item.isExpired || item.isLowStock) 1.5.dp else 1.dp,
      color = when {
        item.isExpired -> EmergencyRed
        item.isExpiringSoon || item.isLowStock -> EmergencyAmber
        else -> GovBorder
      }
    ),
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier
      .fillMaxWidth()
      .animateContentSize()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Top row: Title + Expiry Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = item.name,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = APNavyDark
          )
          Text(
            text = "${item.category} • ${item.locationInKit}",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )
        }

        // Expiry Badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(
              when {
                item.isExpired -> EmergencyRedContainer
                item.isExpiringSoon -> EmergencyAmberContainer
                else -> StatusGreenContainer
              }
            )
            .padding(horizontal = 7.dp, vertical = 3.dp)
        ) {
          Text(
            text = when {
              item.isExpired -> "EXPIRED"
              item.isExpiringSoon -> "EXP SOON"
              else -> "VALID"
            },
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            color = when {
              item.isExpired -> EmergencyRed
              item.isExpiringSoon -> EmergencyAmber
              else -> StatusGreen
            }
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Middle Row: Stock Count vs Threshold & Expiry date + Batch
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Stock: ",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )
          Text(
            text = "${item.currentStock} ${item.unit}",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp,
            color = if (item.isLowStock) EmergencyRed else APNavyDark
          )
          Text(
            text = " (Min: ${item.minThreshold})",
            fontSize = 10.sp,
            color = Color(0xFF94A3B8)
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "Exp: ${item.expiryDate}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = when {
              item.isExpired -> EmergencyRed
              item.isExpiringSoon -> EmergencyAmber
              else -> Color(0xFF334155)
            }
          )
          Text(
            text = "Batch: ${item.batchNumber}",
            fontSize = 9.sp,
            color = Color(0xFF94A3B8)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Action Buttons Row: Dispense 1 + Edit Expiry & Batch + Reorder + Expand Guide
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Dispense 1 button
        Button(
          onClick = onDispense,
          enabled = item.currentStock > 0,
          colors = ButtonDefaults.buttonColors(containerColor = APNavyDark),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
          modifier = Modifier.height(36.dp)
        ) {
          Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(13.dp))
          Spacer(modifier = Modifier.width(3.dp))
          Text("Dispense 1", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        // Set / Add Expiry Date & Batch Button (USER REQUESTED)
        OutlinedButton(
          onClick = onOpenEditExpiry,
          shape = RoundedCornerShape(8.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, APNavyPrimary),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
          modifier = Modifier
            .height(36.dp)
            .testTag("btn_edit_expiry_${item.id}")
        ) {
          Icon(Icons.Default.EditCalendar, contentDescription = null, tint = APNavyPrimary, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Set Expiry", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = APNavyPrimary)
        }

        // Restock Requisition Button
        OutlinedButton(
          onClick = onRequestRestock,
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 7.dp, vertical = 6.dp),
          modifier = Modifier.height(36.dp)
        ) {
          Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(13.dp))
          Spacer(modifier = Modifier.width(3.dp))
          Text("Reorder", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Expand Clinical Guide Toggle
        IconButton(
          onClick = { isExpanded = !isExpanded },
          modifier = Modifier.size(34.dp)
        ) {
          Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = "Instructions",
            tint = Color(0xFF64748B)
          )
        }
      }

      // Expandable Clinical Guide for Police Constable
      AnimatedVisibility(visible = isExpanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(GovSurfaceSubtle)
            .padding(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "FIRST RESPONDER CLINICAL PROTOCOL:",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = APNavyDark
            )
            IconButton(
              onClick = onDeleteItem,
              modifier = Modifier.size(24.dp)
            ) {
              Icon(Icons.Default.Delete, contentDescription = "Delete Item", tint = Color.Gray, modifier = Modifier.size(14.dp))
            }
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = item.instructions,
            fontSize = 11.sp,
            color = Color(0xFF334155),
            lineHeight = 15.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Storage Shelf: ${item.locationInKit} • Lot: ${item.batchNumber}",
            fontSize = 9.sp,
            color = Color(0xFF64748B)
          )
        }
      }
    }
  }
}

@Composable
private fun IncidentLogbookSection(incidents: List<EmergencyIncidentEntity>) {
  Card(
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, GovBorder),
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(
        text = "JUNCTION EMERGENCY INCIDENT LOGBOOK",
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = APNavyDark,
        letterSpacing = 0.5.sp
      )
      Text(
        text = "Past trauma events handled with booth first aid kit",
        fontSize = 10.sp,
        color = Color(0xFF64748B)
      )

      Spacer(modifier = Modifier.height(10.dp))

      if (incidents.isEmpty()) {
        Text(
          text = "No prior accident casualties recorded for this booth today. Grid status clean.",
          fontSize = 11.sp,
          color = Color(0xFF94A3B8)
        )
      } else {
        incidents.take(3).forEach { inc ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = inc.injuryType,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = APNavyDark
              )
              Text(
                text = "${inc.victimCount} casualty • Kit items: ${inc.itemsUsedSummary}",
                fontSize = 10.sp,
                color = Color(0xFF64748B)
              )
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFF1F5F9))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = inc.ambulanceStatus.replace("_", " "),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF475569)
              )
            }
          }
        }
      }
    }
  }
}
