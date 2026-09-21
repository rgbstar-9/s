package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "traffic_booths")
data class TrafficBoothEntity(
  @PrimaryKey val id: Int,
  val boothCode: String,
  val name: String,
  val junctionName: String,
  val area: String,
  val latitude: Double,
  val longitude: Double,
  val officerName: String,
  val officerPhone: String,
  val officerBadge: String,
  val equipmentStatus: String, // NORMAL, LOW_STOCK, EXPIRED_ALERT, EMERGENCY_ACTIVE
  val lastInspection: String,
  val cctvConnected: Boolean,
  val firstAidBoxModel: String
)

@Entity(tableName = "first_aid_items")
data class FirstAidItemEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val boothId: Int,
  val name: String,
  val category: String, // Hemorrhage Control, Wound & Burn, Airway & Resuscitation, Fracture & Immobilization, Antiseptics & Consumables
  val currentStock: Int,
  val minThreshold: Int,
  val unit: String,
  val expiryDate: String,
  val expiryTimestamp: Long,
  val batchNumber: String,
  val locationInKit: String,
  val instructions: String
) {
  val isLowStock: Boolean get() = currentStock <= minThreshold
  val isExpired: Boolean get() = System.currentTimeMillis() > expiryTimestamp
  val isExpiringSoon: Boolean get() {
    val thirtyDaysMillis = 30L * 24 * 60 * 60 * 1000
    return !isExpired && (expiryTimestamp - System.currentTimeMillis() < thirtyDaysMillis)
  }
}

@Entity(tableName = "emergency_incidents")
data class EmergencyIncidentEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val boothId: Int,
  val boothName: String,
  val timestamp: Long,
  val severity: String, // CRITICAL_RED, URGENT_YELLOW, MINOR_GREEN
  val victimCount: Int,
  val injuryType: String,
  val vehicleTypes: String,
  val firstAidAdministered: String,
  val itemsUsedSummary: String,
  val ambulanceStatus: String, // DISPATCHED, EN_ROUTE, ARRIVED, TRANSFERRED_GGH
  val ambulanceUnit: String,
  val ambulanceEtaMinutes: Int,
  val hospitalDestination: String,
  val officerNotes: String
)

@Entity(tableName = "supply_requisitions")
data class SupplyRequisitionEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val boothId: Int,
  val boothName: String,
  val itemName: String,
  val quantityNeeded: Int,
  val reason: String, // EXPIRED, LOW_STOCK, EMERGENCY_USAGE
  val requestedAt: Long,
  val status: String, // PENDING_DISPATCH, DISPATCHED, RESTOCKED
  val urgency: String // URGENT, ROUTINE
)
