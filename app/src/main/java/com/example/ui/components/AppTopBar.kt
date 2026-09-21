package com.example.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppRole
import com.example.ui.theme.APNavyDark
import com.example.ui.theme.APNavyPrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.GovBorder
import com.example.ui.theme.MedicalTeal

@Composable
fun AppTopBar(
  activeRole: AppRole,
  pendingRequisitionsCount: Int,
  onRoleChange: (AppRole) -> Unit,
  onOpenSummitBriefing: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = Color.White,
    shadowElevation = 2.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      // Official Header Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Govt AP & Police Badge Emblem
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.linearGradient(listOf(APNavyPrimary, APNavyDark))
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.MedicalServices,
              contentDescription = "Medical Cross",
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "SANJEEVANI AP",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 0.5.sp,
                  color = APNavyDark
                )
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(Color(0xFFFEF3C7))
                  .padding(horizontal = 5.dp, vertical = 1.dp)
              ) {
                Text(
                  text = "PAN IIT VJA",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF92400E)
                )
              }
            }
            Text(
              text = "Vijayawada Traffic Police First Aid Grid • Govt of AP",
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                color = Color(0xFF64748B)
              )
            )
          }
        }

        // Action Icons: Summit Briefing + Requisition Badge
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onOpenSummitBriefing,
            modifier = Modifier.testTag("btn_summit_briefing")
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEFF6FF))
                .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Assignment,
                  contentDescription = "CM Briefing",
                  tint = APNavyPrimary,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "CM Brief",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = APNavyPrimary
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Dual-Space Switcher Segmented Control
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFFF1F5F9))
          .border(1.dp, GovBorder, RoundedCornerShape(12.dp))
          .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // Option 1: Traffic Police Field Booth
        RoleTabItem(
          title = "Traffic Police Booth",
          subtitle = "Field First Aid Kit",
          icon = Icons.Default.LocalPolice,
          isSelected = activeRole == AppRole.TRAFFIC_POLICE_BOOTH,
          onClick = { onRoleChange(AppRole.TRAFFIC_POLICE_BOOTH) },
          modifier = Modifier
            .weight(1f)
            .testTag("tab_police_booth")
        )

        // Option 2: Command Control Center (Viewer Space)
        RoleTabItem(
          title = "Central Command Grid",
          subtitle = "VJA Signals Map",
          icon = Icons.Default.Map,
          isSelected = activeRole == AppRole.COMMAND_CONTROL_CENTER,
          badgeCount = pendingRequisitionsCount,
          onClick = { onRoleChange(AppRole.COMMAND_CONTROL_CENTER) },
          modifier = Modifier
            .weight(1f)
            .testTag("tab_command_grid")
        )
      }
    }
  }
}

@Composable
private fun RoleTabItem(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  badgeCount: Int = 0
) {
  val backgroundColor by animateColorAsState(
    targetValue = if (isSelected) Color.White else Color.Transparent,
    label = "tabBg"
  )
  val contentColor by animateColorAsState(
    targetValue = if (isSelected) APNavyDark else Color(0xFF64748B),
    label = "tabContent"
  )

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(9.dp))
      .background(backgroundColor)
      .clickable(onClick = onClick)
      .padding(vertical = 8.dp, horizontal = 10.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Box {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = if (isSelected) MedicalTeal else Color(0xFF94A3B8),
          modifier = Modifier.size(20.dp)
        )
        if (badgeCount > 0) {
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .size(8.dp)
              .clip(CircleShape)
              .background(EmergencyRed)
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      Column {
        Text(
          text = title,
          fontSize = 12.sp,
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
          color = contentColor,
          maxLines = 1
        )
        Text(
          text = subtitle,
          fontSize = 10.sp,
          color = if (isSelected) Color(0xFF475569) else Color(0xFF94A3B8),
          maxLines = 1
        )
      }
    }
  }
}
