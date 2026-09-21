package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppRole
import com.example.ui.MainViewModel
import com.example.ui.components.AppTopBar
import com.example.ui.dialogs.AddNewItemDialog
import com.example.ui.dialogs.BroadcastAlertDialog
import com.example.ui.dialogs.CctvSurveillanceDialog
import com.example.ui.dialogs.EditItemExpiryDialog
import com.example.ui.dialogs.EmergencySOSDialog
import com.example.ui.dialogs.GoldenHourProtocolDialog
import com.example.ui.dialogs.RestockRequestDialog
import com.example.ui.dialogs.SummitPresentationDialog
import com.example.ui.screens.CommandControlCenterScreen
import com.example.ui.screens.TrafficPoliceBoothScreen
import com.example.ui.theme.APNavyDark
import com.example.ui.theme.GovBackground
import com.example.ui.theme.SanjeevaniTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      SanjeevaniTheme {
        val viewModel: MainViewModel = viewModel()
        SanjeevaniApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun SanjeevaniApp(viewModel: MainViewModel) {
  val activeRole by viewModel.activeRole.collectAsStateWithLifecycle()
  val allBooths by viewModel.allBooths.collectAsStateWithLifecycle()
  val currentBooth by viewModel.currentBooth.collectAsStateWithLifecycle()
  val currentBoothItems by viewModel.currentBoothItems.collectAsStateWithLifecycle()
  val allItems by viewModel.allItems.collectAsStateWithLifecycle()
  val allIncidents by viewModel.allIncidents.collectAsStateWithLifecycle()
  val currentBoothIncidents by viewModel.currentBoothIncidents.collectAsStateWithLifecycle()
  val allRequisitions by viewModel.allRequisitions.collectAsStateWithLifecycle()
  val pendingRequisitionsCount by viewModel.pendingRequisitionsCount.collectAsStateWithLifecycle()
  val mapFilter by viewModel.mapFilter.collectAsStateWithLifecycle()
  val filteredBooths by viewModel.filteredBooths.collectAsStateWithLifecycle()
  val inspectedBooth by viewModel.inspectedBooth.collectAsStateWithLifecycle()

  val showEmergencySOSDialog by viewModel.showEmergencySOSDialog.collectAsStateWithLifecycle()
  val showProtocolGuideDialog by viewModel.showProtocolGuideDialog.collectAsStateWithLifecycle()
  val showRestockDialog by viewModel.showRestockDialog.collectAsStateWithLifecycle()
  val itemForRestock by viewModel.itemForRestock.collectAsStateWithLifecycle()
  val showSummitBriefing by viewModel.showSummitBriefing.collectAsStateWithLifecycle()
  val userFeedbackMessage by viewModel.userFeedbackMessage.collectAsStateWithLifecycle()

  val itemForExpiryEdit by viewModel.itemForExpiryEdit.collectAsStateWithLifecycle()
  val showAddNewItemDialog by viewModel.showAddNewItemDialog.collectAsStateWithLifecycle()
  val cctvBooth by viewModel.cctvBooth.collectAsStateWithLifecycle()
  val showBroadcastDialog by viewModel.showBroadcastDialog.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(userFeedbackMessage) {
    userFeedbackMessage?.let { msg ->
      snackbarHostState.showSnackbar(
        message = msg,
        duration = SnackbarDuration.Short
      )
      viewModel.clearFeedbackMessage()
    }
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(GovBackground),
    containerColor = GovBackground,
    topBar = {
      AppTopBar(
        activeRole = activeRole,
        pendingRequisitionsCount = pendingRequisitionsCount,
        onRoleChange = { viewModel.setRole(it) },
        onOpenSummitBriefing = { viewModel.toggleSummitBriefing(true) }
      )
    },
    snackbarHost = {
      SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
          .navigationBarsPadding()
          .padding(16.dp)
      ) { data ->
        Snackbar(
          shape = RoundedCornerShape(10.dp),
          containerColor = APNavyDark,
          contentColor = Color.White
        ) {
          Text(text = data.visuals.message, fontSize = 12.sp)
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (activeRole) {
        AppRole.TRAFFIC_POLICE_BOOTH -> {
          TrafficPoliceBoothScreen(
            currentBooth = currentBooth,
            allBooths = allBooths,
            items = currentBoothItems,
            incidents = currentBoothIncidents,
            onSelectBooth = { viewModel.selectBooth(it) },
            onOpenSOSDialog = { viewModel.openEmergencySOSDialog() },
            onOpenProtocolGuide = { viewModel.openProtocolGuide() },
            onDispenseItem = { item, booth -> viewModel.useItem(item, booth) },
            onRequestRestock = { item -> viewModel.openRestockDialog(item) },
            onAdvanceAmbulance = { incident -> viewModel.advanceAmbulanceStatus(incident) },
            onOpenEditExpiry = { item -> viewModel.openEditExpiryDialog(item) },
            onOpenAddNewItem = { viewModel.openAddNewItemDialog() },
            onOpenCctv = { booth -> viewModel.openCctvDialog(booth) },
            onDeleteItem = { id, name -> viewModel.deleteItem(id, name) }
          )
        }

        AppRole.COMMAND_CONTROL_CENTER -> {
          CommandControlCenterScreen(
            booths = allBooths,
            filteredBooths = filteredBooths,
            allItems = allItems,
            requisitions = allRequisitions,
            incidents = allIncidents,
            activeFilter = mapFilter,
            selectedBooth = inspectedBooth ?: currentBooth,
            onSelectFilter = { viewModel.setMapFilter(it) },
            onSelectBooth = { viewModel.inspectBooth(it) },
            onFulfillRequisition = { viewModel.fulfillRequisition(it) },
            onFulfillAllRequisitions = { viewModel.fulfillAllPendingRequisitions() },
            onOpenBroadcastDialog = { viewModel.openBroadcastDialog() },
            onOpenCctv = { booth -> viewModel.openCctvDialog(booth) },
            onSwitchToFieldBooth = { boothId ->
              viewModel.selectBooth(boothId)
              viewModel.setRole(AppRole.TRAFFIC_POLICE_BOOTH)
            }
          )
        }
      }

      // Dialogs
      if (showEmergencySOSDialog && currentBooth != null) {
        EmergencySOSDialog(
          booth = currentBooth!!,
          onDismiss = { viewModel.closeEmergencySOSDialog() },
          onDispatch = { severity, count, injury, vehicle, treatments, items, hospital, notes ->
            viewModel.dispatch108Emergency(
              booth = currentBooth!!,
              severity = severity,
              victimCount = count,
              injuryType = injury,
              vehicleTypes = vehicle,
              firstAidAdministered = treatments,
              itemsUsed = items,
              hospital = hospital,
              notes = notes
            )
          }
        )
      }

      if (showProtocolGuideDialog) {
        GoldenHourProtocolDialog(
          onDismiss = { viewModel.closeProtocolGuide() }
        )
      }

      if (showRestockDialog && currentBooth != null) {
        RestockRequestDialog(
          booth = currentBooth!!,
          preselectedItem = itemForRestock,
          onDismiss = { viewModel.closeRestockDialog() },
          onSubmit = { name, qty, reason ->
            viewModel.submitRestockRequest(
              boothId = currentBooth!!.id,
              boothName = currentBooth!!.name,
              itemName = name,
              quantity = qty,
              reason = reason
            )
          }
        )
      }

      if (showSummitBriefing) {
        SummitPresentationDialog(
          onDismiss = { viewModel.toggleSummitBriefing(false) }
        )
      }

      // 1. Edit Item Expiry & Batch Dialog
      itemForExpiryEdit?.let { targetItem ->
        EditItemExpiryDialog(
          item = targetItem,
          onDismiss = { viewModel.closeEditExpiryDialog() },
          onSave = { itemId, expiryDate, expiryTimestamp, batchNumber, currentStock, minThreshold, itemName ->
            viewModel.saveItemExpiryAndDetails(
              itemId = itemId,
              expiryDate = expiryDate,
              expiryTimestamp = expiryTimestamp,
              batchNumber = batchNumber,
              currentStock = currentStock,
              minThreshold = minThreshold,
              itemName = itemName
            )
          }
        )
      }

      // 2. Add New Medicine / First Aid Item Dialog
      if (showAddNewItemDialog && currentBooth != null) {
        AddNewItemDialog(
          booth = currentBooth!!,
          onDismiss = { viewModel.closeAddNewItemDialog() },
          onAddItem = { newItem ->
            viewModel.addNewItem(newItem)
          }
        )
      }

      // 3. Live 4K CCTV Surveillance Dialog
      cctvBooth?.let { targetBooth ->
        CctvSurveillanceDialog(
          booth = targetBooth,
          onDismiss = { viewModel.closeCctvDialog() },
          onBroadcastSiren = { feedback ->
            viewModel.broadcastSirenAlert(feedback)
          }
        )
      }

      // 4. Central Emergency Corridor Broadcast Dialog
      if (showBroadcastDialog) {
        BroadcastAlertDialog(
          onDismiss = { viewModel.closeBroadcastDialog() },
          onSendBroadcast = { corridor, alertMessage ->
            viewModel.broadcastEmergencyCorridor(corridor, alertMessage)
          }
        )
      }
    }
  }
}
