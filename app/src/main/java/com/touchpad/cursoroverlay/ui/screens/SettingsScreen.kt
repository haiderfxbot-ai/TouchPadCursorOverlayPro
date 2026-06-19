package com.touchpad.cursoroverlay.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.touchpad.cursoroverlay.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cursor Section
            SettingsSection(title = "Cursor Settings") {
                // Cursor Speed
                SettingsSlider(
                    label = "Cursor Speed",
                    value = settings.cursorSpeed,
                    valueRange = 0.3f..3.0f,
                    onValueChange = { viewModel.updateCursorSpeed(it) },
                    icon = Icons.Default.Speed
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp))

                // Cursor Size
                SettingsSlider(
                    label = "Cursor Size",
                    value = settings.cursorSize.toFloat(),
                    valueRange = 10f..50f,
                    onValueChange = { viewModel.updateCursorSize(it.toInt()) },
                    icon = Icons.Default.Adjust
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp))

                // Cursor Opacity
                SettingsSlider(
                    label = "Cursor Opacity",
                    value = settings.cursorOpacity,
                    valueRange = 0.1f..1.0f,
                    onValueChange = { viewModel.updateCursorOpacity(it) },
                    icon = Icons.Default.Opacity
                )
            }

            // Overlay Section
            SettingsSection(title = "Overlay Settings") {
                // Overlay Width
                SettingsSlider(
                    label = "Overlay Width",
                    value = settings.overlayWidth.toFloat(),
                    valueRange = 180f..500f,
                    onValueChange = { viewModel.updateOverlaySize(it.toInt(), settings.overlayHeight) },
                    icon = Icons.Default.ArrowRightAlt
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp))

                // Overlay Height
                SettingsSlider(
                    label = "Overlay Height",
                    value = settings.overlayHeight.toFloat(),
                    valueRange = 280f..600f,
                    onValueChange = { viewModel.updateOverlaySize(settings.overlayWidth, it.toInt()) },
                    icon = Icons.Default.ArrowDropDown
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp))

                // Overlay Opacity
                SettingsSlider(
                    label = "Overlay Opacity",
                    value = settings.overlayOpacity,
                    valueRange = 0.1f..1.0f,
                    onValueChange = { viewModel.updateOverlayOpacity(it) },
                    icon = Icons.Default.BrightnessMedium
                )
            }

            // Behavior Section
            SettingsSection(title = "Behavior") {
                SettingsToggle(
                    label = "Dark Mode",
                    checked = settings.isDarkMode,
                    onCheckedChange = { viewModel.updateDarkMode(it) },
                    icon = Icons.Default.DarkMode
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp))

                SettingsToggle(
                    label = "Auto Start Overlay",
                    checked = settings.autoStart,
                    onCheckedChange = { viewModel.updateAutoStart(it) },
                    icon = Icons.Default.PlayCircle
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp))

                SettingsToggle(
                    label = "Auto Restore Position",
                    checked = settings.autoRestore,
                    onCheckedChange = { viewModel.updateAutoRestore(it) },
                    icon = Icons.Default.Restore
                )
            }

            // Feedback Section
            SettingsSection(title = "Feedback") {
                SettingsToggle(
                    label = "Vibration Feedback",
                    checked = settings.vibrationFeedback,
                    onCheckedChange = { viewModel.updateVibrationFeedback(it) },
                    icon = Icons.Default.Vibration
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 4.dp))

                SettingsToggle(
                    label = "Sound Feedback",
                    checked = settings.soundFeedback,
                    onCheckedChange = { viewModel.updateSoundFeedback(it) },
                    icon = Icons.Default.VolumeUp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
private fun SettingsSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = tween(200),
        label = "sliderAnim"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = String.format("%.1f", animatedValue),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Slider(
            value = animatedValue,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier.width(140.dp),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun SettingsToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            )
        )
    }
}
