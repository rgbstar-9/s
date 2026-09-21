package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.FirstAidItemEntity
import com.example.data.model.SupplyRequisitionEntity
import com.example.data.model.TrafficBoothEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FirstAidDao {

  // Traffic Booths
  @Query("SELECT * FROM traffic_booths ORDER BY id ASC")
  fun getAllBooths(): Flow<List<TrafficBoothEntity>>

  @Query("SELECT * FROM traffic_booths WHERE id = :id LIMIT 1")
  fun getBoothById(id: Int): Flow<TrafficBoothEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBooths(booths: List<TrafficBoothEntity>)

  @Update
  suspend fun updateBooth(booth: TrafficBoothEntity)

  @Query("UPDATE traffic_booths SET equipmentStatus = :status WHERE id = :boothId")
  suspend fun updateBoothStatus(boothId: Int, status: String)

  // First Aid Items
  @Query("SELECT * FROM first_aid_items WHERE boothId = :boothId ORDER BY category ASC, name ASC")
  fun getItemsForBooth(boothId: Int): Flow<List<FirstAidItemEntity>>

  @Query("SELECT * FROM first_aid_items ORDER BY expiryTimestamp ASC")
  fun getAllItems(): Flow<List<FirstAidItemEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertItems(items: List<FirstAidItemEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertItem(item: FirstAidItemEntity): Long

  @Update
  suspend fun updateItem(item: FirstAidItemEntity)

  @Query("UPDATE first_aid_items SET expiryDate = :expiryDate, expiryTimestamp = :expiryTimestamp, batchNumber = :batchNumber, currentStock = :currentStock, minThreshold = :minThreshold WHERE id = :itemId")
  suspend fun updateItemExpiryAndDetails(itemId: Int, expiryDate: String, expiryTimestamp: Long, batchNumber: String, currentStock: Int, minThreshold: Int)

  @Query("UPDATE first_aid_items SET currentStock = MAX(0, currentStock - :count) WHERE id = :itemId")
  suspend fun decrementStock(itemId: Int, count: Int = 1)

  @Query("UPDATE first_aid_items SET currentStock = currentStock + :amount, expiryDate = :newExpiry, expiryTimestamp = :newTimestamp WHERE id = :itemId")
  suspend fun restockItem(itemId: Int, amount: Int, newExpiry: String, newTimestamp: Long)

  @Query("DELETE FROM first_aid_items WHERE id = :itemId")
  suspend fun deleteItem(itemId: Int)

  // Incidents
  @Query("SELECT * FROM emergency_incidents ORDER BY timestamp DESC")
  fun getAllIncidents(): Flow<List<EmergencyIncidentEntity>>

  @Query("SELECT * FROM emergency_incidents WHERE boothId = :boothId ORDER BY timestamp DESC")
  fun getIncidentsForBooth(boothId: Int): Flow<List<EmergencyIncidentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertIncident(incident: EmergencyIncidentEntity): Long

  @Query("UPDATE emergency_incidents SET ambulanceStatus = :status WHERE id = :incidentId")
  suspend fun updateIncidentAmbulanceStatus(incidentId: Int, status: String)

  // Supply Requisitions
  @Query("SELECT * FROM supply_requisitions ORDER BY requestedAt DESC")
  fun getAllRequisitions(): Flow<List<SupplyRequisitionEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRequisition(requisition: SupplyRequisitionEntity)

  @Query("UPDATE supply_requisitions SET status = :status WHERE id = :id")
  suspend fun updateRequisitionStatus(id: Int, status: String)

  @Query("SELECT COUNT(*) FROM supply_requisitions WHERE status = 'PENDING_DISPATCH'")
  fun getPendingRequisitionCount(): Flow<Int>

  @Query("UPDATE supply_requisitions SET status = 'RESTOCKED' WHERE status = 'PENDING_DISPATCH'")
  suspend fun fulfillAllPendingRequisitions()

  @Query("UPDATE traffic_booths SET equipmentStatus = 'NORMAL' WHERE equipmentStatus != 'EMERGENCY_ACTIVE'")
  suspend fun resetDepletedBoothsToNormal()
}
