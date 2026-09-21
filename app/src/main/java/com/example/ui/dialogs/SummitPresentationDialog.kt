package com.example.ui.dialogs

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.ui.theme.APNavyDark
import com.example.ui.theme.APNavyPrimary
import com.example.ui.theme.GovBorder
import com.example.ui.theme.MedicalTeal
import com.example.ui.theme.StatusGreen

@Composable
fun SummitPresentationDialog(
  onDismiss: () -> Unit
) {
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
        // Top Badges
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFFEF3C7))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text("PAN IIT VJA SUMMIT 2026", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color(0xFF92400E))
            }
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFDCFCE7))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text("PRESENTATION TO HON'BLE CHIEF MINISTER", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color(0xFF166534))
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Hero Graphic if available
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(12.dp))
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_hero_summit),
            contentDescription = "Summit Hero",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Project Sanjeevani: Vijayawada Traffic First Aid Grid",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.ExtraBold,
            color = APNavyDark,
            fontSize = 17.sp
          )
        )
        Text(
          text = "Empowering 100% Traffic Junctions with Trauma First-Aid & Instant 108 Dispatch",
          fontSize = 12.sp,
          color = Color(0xFF475569)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Three Pillars Card
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          MetricBox(
            title = "< 3 Mins",
            subtitle = "Immediate On-Site Trauma First-Aid",
            color = MedicalTeal,
            modifier = Modifier.weight(1f)
          )
          MetricBox(
            title = "-43%",
            subtitle = "Projected Drop in Fatalities",
            color = StatusGreen,
            modifier = Modifier.weight(1f)
          )
          MetricBox(
            title = "100%",
            subtitle = "VJA Junction Coverage",
            color = APNavyPrimary,
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Impact Highlights
        Text("EXECUTIVE HIGHLIGHTS", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF475569), letterSpacing = 0.5.sp)
        Spacer(modifier = Modifier.height(8.dp))

        HighlightRow(
          icon = Icons.Default.Speed,
          title = "Golden Hour Pre-Hospital Defense",
          desc = "Arterial hemorrhage and respiratory failure claim victims within 5-8 minutes. Traffic police booths now provide instant tourniquets, splints, and CPR before ambulances arrive."
        )

        HighlightRow(
          icon = Icons.Default.CheckCircle,
          title = "Zero Expired Supplies Guarantee",
          desc = "Smart batch tracking intimates the Vijayawada Central Medical Depot 30 days prior to expiry and automatically generates refill orders when stocks dip."
        )

        HighlightRow(
          icon = Icons.Default.Security,
          title = "108 Command Center Direct Bridge",
          desc = "One-tap dispatch auto-transmits junction coordinates, victim count, and triage severity to GGH Vijayawada Trauma Emergency team."
        )

        HighlightRow(
          icon = Icons.Default.TrendingUp,
          title = "Statewide Scalability Blueprint",
          desc = "Phase 1: Vijayawada (10 Booths Live) → Phase 2: Visakhapatnam (32 Booths), Guntur (18 Booths), Tirupati (14 Booths)."
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = APNavyDark),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
          Text("Close Briefing", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun MetricBox(
  title: String,
  subtitle: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
    border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f)),
    shape = RoundedCornerShape(10.dp),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = title, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = color)
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = subtitle,
        fontSize = 9.sp,
        color = Color(0xFF475569),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        lineHeight = 12.sp
      )
    }
  }
}

@Composable
private fun HighlightRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  desc: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 5.dp),
    verticalAlignment = Alignment.Top
  ) {
    Box(
      modifier = Modifier
        .size(28.dp)
        .clip(CircleShape)
        .background(Color(0xFFE0F2FE)),
      contentAlignment = Alignment.Center
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = APNavyPrimary, modifier = Modifier.size(16.dp))
    }
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = APNavyDark)
      Text(text = desc, fontSize = 11.sp, color = Color(0xFF475569), lineHeight = 15.sp)
    }
  }
}
