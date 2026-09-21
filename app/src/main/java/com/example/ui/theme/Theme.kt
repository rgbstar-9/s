package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = APNavyPrimary,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE0F2FE),
  onPrimaryContainer = APNavyDark,
  secondary = MedicalTeal,
  onSecondary = Color.White,
  secondaryContainer = MedicalTealContainer,
  onSecondaryContainer = OnMedicalTealContainer,
  tertiary = EmergencyRed,
  onTertiary = OnEmergencyRed,
  tertiaryContainer = EmergencyRedContainer,
  onTertiaryContainer = EmergencyRedDark,
  background = GovBackground,
  onBackground = TextPrimary,
  surface = GovSurface,
  onSurface = TextPrimary,
  surfaceVariant = GovSurfaceSubtle,
  onSurfaceVariant = TextSecondary,
  outline = GovBorder,
  outlineVariant = GovBorderStrong
)

@Composable
fun SanjeevaniTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = LightColorScheme,
    typography = Typography,
    content = content
  )
}
