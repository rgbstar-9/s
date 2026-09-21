package com.example.data.repository

import com.example.data.db.FirstAidDao
import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.FirstAidItemEntity
import com.example.data.model.SupplyRequisitionEntity
import com.example.data.model.TrafficBoothEntity
import com.example.data.seed.InitialData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirstAidRepository(
  private val dao: FirstAidDao,
  private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

  init {
    externalScope.launch {
      seedDatabaseIfEmpty()
    }
  }

  private suspend fun seedDatabaseIfEmpty() = withContext(Dispatchers.IO) {
    val existing = dao.getAllBooths().first()
    if (existing.isEmpty()) {
      val booths = InitialData.getInitialBooths()
      dao.insertBooths(booths)
      dao.insertItems(InitialData.getInitialItems(booths))
      InitialData.getInitialIncidents().forEach { dao.insertIncident(it) }
      InitialData.getInitialRequisitions().forEach { dao.insertRequisition(it) }
    }
  }

  fun getAllBooths(): Flow<List<TrafficBoothEntity>> = dao.getAllBooths()

  fun getBoothById(id: Int): Flow<TrafficBoothEntity?> = dao.getBoothById(id)

  fun getItemsForBooth(boothId: Int): Flow<List<FirstAidItemEntity>> = dao.getItemsForBooth(boothId)

  fun getAllItems(): Flow<List<FirstAidItemEntity>> = dao.getAllItems()

  fun getAllIncidents(): Flow<List<EmergencyIncidentEntity>> = dao.getAllIncidents()

  fun getIncidentsForBooth(boothId: Int): Flow<List<EmergencyIncidentEntity>> = dao.getIncidentsForBooth(boothId)

  fun getAllRequisitions(): Flow<List<SupplyRequisitionEntity>> = dao.getAllRequisitions()

  fun getPendingRequisitionCount(): Flow<Int> = dao.getPendingRequisitionCount()

  suspend fun useItem(itemId: Int, boothId: Int, boothName: String, itemName: String, currentStock: Int, minThreshold: Int) = withContext(Dispatchers.IO) {
    dao.decrementStock(itemId, 1)
    val newStock = currentStock - 1
    // Auto-intimate department if low stock
    if (newStock <= minThreshold) {
      dao.insertRequisition(
        SupplyRequisitionEntity(
          boothId = boothId,
          boothName = boothName,
          itemName = itemName,
          quantityNeeded = minThreshold * 2,
          reason = "LOW_STOCK",
          requestedAt = System.currentTimeMillis(),
          status = "PENDING_DISPATCH",
          urgency = "URGENT"
        )
      )
      dao.updateBoothStatus(boothId, "LOW_STOCK")
    }
  }

  suspend fun requestRestock(boothId: Int, boothName: String, itemName: String, quantity: Int, reason: String) = withContext(Dispatchers.IO) {
    dao.insertRequisition(
      SupplyRequisitionEntity(
        boothId = boothId,
        boothName = boothName,
        itemName = itemName,
        quantityNeeded = quantity,
        reason = reason,
        requestedAt = System.currentTimeMillis(),
        status = "PENDING_DISPATCH",
        urgency = if (reason == "EXPIRED" || reason == "EMERGENCY_USAGE") "URGENT" else "ROUTINE"
      )
    )
    if (reason == "EXPIRED") {
      dao.updateBoothStatus(boothId, "EXPIRED_ALERT")
    } else {
      dao.updateBoothStatus(boothId, "LOW_STOCK")
    }
  }

  suspend fun fulfillRequisition(requisitionId: Int, boothId: Int, itemName: String, addedAmount: Int) = withContext(Dispatchers.IO) {
    dao.updateRequisitionStatus(requisitionId, "RESTOCKED")
    // Find matching item in booth
    val items = dao.getItemsForBooth(boothId).first()
    val match = items.firstOrNull { it.name.contains(itemName, ignoreCase = true) || itemName.contains(it.name, ignoreCase = true) }
    if (match != null) {
      val freshExpiryTimestamp = System.currentTimeMillis() + (365L * 2 * 24 * 60 * 60 * 1000)
      dao.restockItem(
        itemId = match.id,
        amount = addedAmount,
        newExpiry = "20 Sep 2028",
        newTimestamp = freshExpiryTimestamp
      )
    }
    dao.updateBoothStatus(boothId, "NORMAL")
  }

  suspend fun fulfillAllRequisitions() = withContext(Dispatchers.IO) {
    dao.fulfillAllPendingRequisitions()
    dao.resetDepletedBoothsToNormal()
  }

  suspend fun updateItemExpiryAndDetails(
    itemId: Int,
    expiryDate: String,
    expiryTimestamp: Long,
    batchNumber: String,
    currentStock: Int,
    minThreshold: Int
  ) = withContext(Dispatchers.IO) {
    dao.updateItemExpiryAndDetails(
      itemId = itemId,
      expiryDate = expiryDate,
      expiryTimestamp = expiryTimestamp,
      batchNumber = batchNumber,
      currentStock = currentStock,
      minThreshold = minThreshold
    )
  }

  suspend fun addItem(item: FirstAidItemEntity): Long = withContext(Dispatchers.IO) {
    dao.insertItem(item)
  }

  suspend fun deleteItem(itemId: Int) = withContext(Dispatchers.IO) {
    dao.deleteItem(itemId)
  }

  suspend fun dispatch108Emergency(
    boothId: Int,
    boothName: String,
    severity: String,
    victimCount: Int,
    injuryType: String,
    vehicleTypes: String,
    firstAidAdministered: String,
    itemsUsed: String,
    hospital: String,
    notes: String
  ): Long = withContext(Dispatchers.IO) {
    val incident = EmergencyIncidentEntity(
      boothId = boothId,
      boothName = boothName,
      timestamp = System.currentTimeMillis(),
      severity = severity,
      victimCount = victimCount,
      injuryType = injuryType,
      vehicleTypes = vehicleTypes,
      firstAidAdministered = firstAidAdministered,
      itemsUsedSummary = itemsUsed,
      ambulanceStatus = "DISPATCHED",
      ambulanceUnit = "108-AP-VJA-${(10..99).random()} (Rapid Response)",
      ambulanceEtaMinutes = (4..8).random(),
      hospitalDestination = hospital,
      officerNotes = notes
    )
    val id = dao.insertIncident(incident)
    dao.updateBoothStatus(boothId, "EMERGENCY_ACTIVE")

    // If items used were specified, auto generate requisitions for restocking
    if (itemsUsed.isNotBlank()) {
      dao.insertRequisition(
        SupplyRequisitionEntity(
          boothId = boothId,
          boothName = boothName,
          itemName = itemsUsed,
          quantityNeeded = 2,
          reason = "EMERGENCY_USAGE",
          requestedAt = System.currentTimeMillis(),
          status = "PENDING_DISPATCH",
          urgency = "URGENT"
        )
      )
    }
    id
  }

  suspend fun advanceAmbulanceStatus(incidentId: Int, currentStatus: String, boothId: Int) = withContext(Dispatchers.IO) {
    val nextStatus = when (currentStatus) {
      "DISPATCHED" -> "EN_ROUTE"
      "EN_ROUTE" -> "ARRIVED"
      "ARRIVED" -> "TRANSFERRED_GGH"
      else -> "TRANSFERRED_GGH"
    }
    dao.updateIncidentAmbulanceStatus(incidentId, nextStatus)
    if (nextStatus == "TRANSFERRED_GGH") {
      dao.updateBoothStatus(boothId, "NORMAL")
    }
  }

  suspend fun updateBoothStatusDirect(boothId: Int, status: String) = withContext(Dispatchers.IO) {
    dao.updateBoothStatus(boothId, status)
  }
}
