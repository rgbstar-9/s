package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.FirstAidItemEntity
import com.example.data.model.SupplyRequisitionEntity
import com.example.data.model.TrafficBoothEntity
import com.example.data.repository.FirstAidRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppRole {
  TRAFFIC_POLICE_BOOTH,
  COMMAND_CONTROL_CENTER
}

enum class MapFilter {
  ALL,
  ACTIVE_EMERGENCY,
  LOW_STOCK,
  EXPIRED_ALERT
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: FirstAidRepository

  init {
    val db = AppDatabase.getDatabase(application)
    repository = FirstAidRepository(db.firstAidDao())
  }

  // Active User Role
  private val _activeRole = MutableStateFlow(AppRole.TRAFFIC_POLICE_BOOTH)
  val activeRole: StateFlow<AppRole> = _activeRole.asStateFlow()

  // Selected Booth for Traffic Police Field Space
  private val _selectedBoothId = MutableStateFlow(1) // Default to Benz Circle TB-01
  val selectedBoothId: StateFlow<Int> = _selectedBoothId.asStateFlow()

  // Map Filter for Command Center
  private val _mapFilter = MutableStateFlow(MapFilter.ALL)
  val mapFilter: StateFlow<MapFilter> = _mapFilter.asStateFlow()

  // Selected Booth for Central Command Inspection modal
  private val _inspectedBooth = MutableStateFlow<TrafficBoothEntity?>(null)
  val inspectedBooth: StateFlow<TrafficBoothEntity?> = _inspectedBooth.asStateFlow()

  // Dialog states
  private val _showEmergencySOSDialog = MutableStateFlow(false)
  val showEmergencySOSDialog: StateFlow<Boolean> = _showEmergencySOSDialog.asStateFlow()

  private val _showProtocolGuideDialog = MutableStateFlow(false)
  val showProtocolGuideDialog: StateFlow<Boolean> = _showProtocolGuideDialog.asStateFlow()

  private val _showRestockDialog = MutableStateFlow(false)
  val showRestockDialog: StateFlow<Boolean> = _showRestockDialog.asStateFlow()

  private val _itemForRestock = MutableStateFlow<FirstAidItemEntity?>(null)
  val itemForRestock: StateFlow<FirstAidItemEntity?> = _itemForRestock.asStateFlow()

  private val _itemForExpiryEdit = MutableStateFlow<FirstAidItemEntity?>(null)
  val itemForExpiryEdit: StateFlow<FirstAidItemEntity?> = _itemForExpiryEdit.asStateFlow()

  private val _showAddNewItemDialog = MutableStateFlow(false)
  val showAddNewItemDialog: StateFlow<Boolean> = _showAddNewItemDialog.asStateFlow()

  private val _cctvBooth = MutableStateFlow<TrafficBoothEntity?>(null)
  val cctvBooth: StateFlow<TrafficBoothEntity?> = _cctvBooth.asStateFlow()

  private val _showBroadcastDialog = MutableStateFlow(false)
  val showBroadcastDialog: StateFlow<Boolean> = _showBroadcastDialog.asStateFlow()

  private val _showSummitBriefing = MutableStateFlow(false)
  val showSummitBriefing: StateFlow<Boolean> = _showSummitBriefing.asStateFlow()

  // SnackBar / Banner feedback message
  private val _userFeedbackMessage = MutableStateFlow<String?>(null)
  val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

  // Data streams
  val allBooths: StateFlow<List<TrafficBoothEntity>> = repository.getAllBooths()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val currentBooth: StateFlow<TrafficBoothEntity?> = _selectedBoothId
    .flatMapLatest { id -> repository.getBoothById(id) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val currentBoothItems: StateFlow<List<FirstAidItemEntity>> = _selectedBoothId
    .flatMapLatest { id -> repository.getItemsForBooth(id) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allItems: StateFlow<List<FirstAidItemEntity>> = repository.getAllItems()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allIncidents: StateFlow<List<EmergencyIncidentEntity>> = repository.getAllIncidents()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val currentBoothIncidents: StateFlow<List<EmergencyIncidentEntity>> = _selectedBoothId
    .flatMapLatest { id -> repository.getIncidentsForBooth(id) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allRequisitions: StateFlow<List<SupplyRequisitionEntity>> = repository.getAllRequisitions()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val pendingRequisitionsCount: StateFlow<Int> = repository.getPendingRequisitionCount()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

  // Filtered booths on Command Map
  val filteredBooths: StateFlow<List<TrafficBoothEntity>> = combine(allBooths, _mapFilter) { booths, filter ->
    when (filter) {
      MapFilter.ALL -> booths
      MapFilter.ACTIVE_EMERGENCY -> booths.filter { it.equipmentStatus == "EMERGENCY_ACTIVE" }
      MapFilter.LOW_STOCK -> booths.filter { it.equipmentStatus == "LOW_STOCK" }
      MapFilter.EXPIRED_ALERT -> booths.filter { it.equipmentStatus == "EXPIRED_ALERT" }
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Actions
  fun setRole(role: AppRole) {
    _activeRole.value = role
  }

  fun selectBooth(boothId: Int) {
    _selectedBoothId.value = boothId
  }

  fun setMapFilter(filter: MapFilter) {
    _mapFilter.value = filter
  }

  fun inspectBooth(booth: TrafficBoothEntity?) {
    _inspectedBooth.value = booth
  }

  fun openEmergencySOSDialog() {
    _showEmergencySOSDialog.value = true
  }

  fun closeEmergencySOSDialog() {
    _showEmergencySOSDialog.value = false
  }

  fun openProtocolGuide() {
    _showProtocolGuideDialog.value = true
  }

  fun closeProtocolGuide() {
    _showProtocolGuideDialog.value = false
  }

  fun openRestockDialog(item: FirstAidItemEntity? = null) {
    _itemForRestock.value = item
    _showRestockDialog.value = true
  }

  fun closeRestockDialog() {
    _showRestockDialog.value = false
    _itemForRestock.value = null
  }

  fun toggleSummitBriefing(show: Boolean) {
    _showSummitBriefing.value = show
  }

  fun clearFeedbackMessage() {
    _userFeedbackMessage.value = null
  }

  fun useItem(item: FirstAidItemEntity, booth: TrafficBoothEntity) {
    viewModelScope.launch {
      repository.useItem(
        itemId = item.id,
        boothId = booth.id,
        boothName = booth.name,
        itemName = item.name,
        currentStock = item.currentStock,
        minThreshold = item.minThreshold
      )
      val remaining = item.currentStock - 1
      if (remaining <= item.minThreshold) {
        _userFeedbackMessage.value = "ALERT: ${item.name} is now LOW in stock (${remaining} remaining). Auto-intimation alert sent to Police & Health Logistics!"
      } else {
        _userFeedbackMessage.value = "Dispensed 1 ${item.unit} of ${item.name} from First Aid Kit. Remaining: $remaining."
      }
    }
  }

  fun submitRestockRequest(boothId: Int, boothName: String, itemName: String, quantity: Int, reason: String) {
    viewModelScope.launch {
      repository.requestRestock(boothId, boothName, itemName, quantity, reason)
      closeRestockDialog()
      _userFeedbackMessage.value = "Requisition for $quantity units of $itemName sent to Vijayawada Central Medical Depot!"
    }
  }

  fun fulfillRequisition(requisition: SupplyRequisitionEntity) {
    viewModelScope.launch {
      repository.fulfillRequisition(
        requisitionId = requisition.id,
        boothId = requisition.boothId,
        itemName = requisition.itemName,
        addedAmount = requisition.quantityNeeded
      )
      _userFeedbackMessage.value = "Medical Supply Van dispatched! ${requisition.boothName} restocked with fresh batch of ${requisition.itemName}."
    }
  }

  fun dispatch108Emergency(
    booth: TrafficBoothEntity,
    severity: String,
    victimCount: Int,
    injuryType: String,
    vehicleTypes: String,
    firstAidAdministered: String,
    itemsUsed: String,
    hospital: String,
    notes: String
  ) {
    viewModelScope.launch {
      repository.dispatch108Emergency(
        boothId = booth.id,
        boothName = booth.name,
        severity = severity,
        victimCount = victimCount,
        injuryType = injuryType,
        vehicleTypes = vehicleTypes,
        firstAidAdministered = firstAidAdministered,
        itemsUsed = itemsUsed,
        hospital = hospital,
        notes = notes
      )
      closeEmergencySOSDialog()
      _userFeedbackMessage.value = "🚨 EMERGENCY 108 AMBULANCE DISPATCHED to ${booth.name}! GPS coordinates & Trauma triage shared with $hospital."
    }
  }

  fun advanceAmbulanceStatus(incident: EmergencyIncidentEntity) {
    viewModelScope.launch {
      repository.advanceAmbulanceStatus(incident.id, incident.ambulanceStatus, incident.boothId)
      val next = when (incident.ambulanceStatus) {
        "DISPATCHED" -> "EN ROUTE (ETA 3 mins)"
        "EN_ROUTE" -> "ARRIVED AT JUNCTION"
        "ARRIVED" -> "PATIENT SAFELY TRANSFERRED TO GGH TRAUMA WARD"
        else -> "CLOSED"
      }
      _userFeedbackMessage.value = "108 Unit status updated: $next"
    }
  }

  fun openEditExpiryDialog(item: FirstAidItemEntity) {
    _itemForExpiryEdit.value = item
  }

  fun closeEditExpiryDialog() {
    _itemForExpiryEdit.value = null
  }

  fun saveItemExpiryAndDetails(
    itemId: Int,
    expiryDate: String,
    expiryTimestamp: Long,
    batchNumber: String,
    currentStock: Int,
    minThreshold: Int,
    itemName: String
  ) {
    viewModelScope.launch {
      repository.updateItemExpiryAndDetails(
        itemId = itemId,
        expiryDate = expiryDate,
        expiryTimestamp = expiryTimestamp,
        batchNumber = batchNumber,
        currentStock = currentStock,
        minThreshold = minThreshold
      )
      closeEditExpiryDialog()
      val isExp = System.currentTimeMillis() > expiryTimestamp
      if (isExp) {
        _userFeedbackMessage.value = "Updated $itemName (BATCH: $batchNumber): Flagged as EXPIRED ($expiryDate). Requisition trigger queued."
      } else {
        _userFeedbackMessage.value = "Updated $itemName: Expiry set to $expiryDate (BATCH: $batchNumber, Stock: $currentStock). Certified in Grid."
      }
    }
  }

  fun openAddNewItemDialog() {
    _showAddNewItemDialog.value = true
  }

  fun closeAddNewItemDialog() {
    _showAddNewItemDialog.value = false
  }

  fun addNewItem(item: FirstAidItemEntity) {
    viewModelScope.launch {
      repository.addItem(item)
      closeAddNewItemDialog()
      _userFeedbackMessage.value = "Added '${item.name}' (${item.category}) to booth inventory with Expiry: ${item.expiryDate}."
    }
  }

  fun deleteItem(itemId: Int, itemName: String) {
    viewModelScope.launch {
      repository.deleteItem(itemId)
      _userFeedbackMessage.value = "Removed '$itemName' from First Aid inventory."
    }
  }

  fun openCctvDialog(booth: TrafficBoothEntity) {
    _cctvBooth.value = booth
  }

  fun closeCctvDialog() {
    _cctvBooth.value = null
  }

  fun openBroadcastDialog() {
    _showBroadcastDialog.value = true
  }

  fun closeBroadcastDialog() {
    _showBroadcastDialog.value = false
  }

  fun broadcastEmergencyCorridor(corridor: String, alertText: String) {
    viewModelScope.launch {
      closeBroadcastDialog()
      _userFeedbackMessage.value = "📢 PRIORITY BROADCAST ISSUED to $corridor: '$alertText'. All signals set to Emergency Pre-emption."
    }
  }

  fun broadcastSirenAlert(feedback: String) {
    _userFeedbackMessage.value = feedback
  }

  fun fulfillAllPendingRequisitions() {
    viewModelScope.launch {
      repository.fulfillAllRequisitions()
      _userFeedbackMessage.value = "DEPOT DISPATCH SUCCESSFUL: All pending booth requisitions restocked & certified green!"
    }
  }
}
