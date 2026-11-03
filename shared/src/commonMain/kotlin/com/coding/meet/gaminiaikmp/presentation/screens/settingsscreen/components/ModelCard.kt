package com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coding.meet.gaminiaikmp.domain.model.GeminiModelInfo
import com.coding.meet.gaminiaikmp.presentation.preview.ComponentPreviews

/**
 * Clean, adaptive card representing a selectable Gemini model.
 * Uses FlowRow to ensure badges wrap horizontally and never squish into vertical text.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModelCard(
    model: GeminiModelInfo,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        },
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            }
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = model.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )

                    if (model.badge != null) {
                        ModelBadgePill(
                            text = model.badge,
                            containerColor = when (model.badge) {
                                "Recommended" -> MaterialTheme.colorScheme.primary
                                "Gemini 4" -> MaterialTheme.colorScheme.tertiary
                                "Fast & Lite", "Lite" -> MaterialTheme.colorScheme.secondary
                                "Stable" -> MaterialTheme.colorScheme.surfaceVariant
                                else -> MaterialTheme.colorScheme.secondaryContainer
                            },
                            contentColor = when (model.badge) {
                                "Recommended" -> MaterialTheme.colorScheme.onPrimary
                                "Gemini 4" -> MaterialTheme.colorScheme.onTertiary
                                "Fast & Lite", "Lite" -> MaterialTheme.colorScheme.onSecondary
                                "Stable" -> MaterialTheme.colorScheme.onSurfaceVariant
                                else -> MaterialTheme.colorScheme.onSecondaryContainer
                            }
                        )
                    }

                    if (model.isThinking && model.badge != "Thinking") {
                        ModelBadgePill(
                            text = "Thinking",
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }

                if (model.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = model.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        lineHeight = 16.sp
                    )
                }

                if (model.inputTokenLimit != null || model.outputTokenLimit != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    val contextText = formatTokenLimit(model.inputTokenLimit)
                    val outputText = formatTokenLimit(model.outputTokenLimit)
                    Text(
                        text = "Context: $contextText • Output: $outputText",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ModelBadgePill(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = containerColor,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

private fun formatTokenLimit(tokens: Int?): String {
    if (tokens == null) return "N/A"
    return when {
        tokens >= 1_000_000 -> "${tokens / 1_000_000}M"
        tokens >= 1_000 -> "${tokens / 1_000}K"
        else -> tokens.toString()
    }
}

@ComponentPreviews
@Composable
fun ModelCardSelectedPreview() {
    ModelCard(
        model = GeminiModelInfo(
            id = "gemini-3.8-flash",
            name = "Gemini 3.8 Flash",
            description = "Our most intelligent Flash model for software engineering and workflows.",
            badge = "Recommended",
            inputTokenLimit = 1048576,
            outputTokenLimit = 65536,
            isThinking = true
        ),
        isSelected = true,
        onSelect = {}
    )
}

@ComponentPreviews
@Composable
fun ModelCardUnselectedPreview() {
    ModelCard(
        model = GeminiModelInfo(
            id = "gemini-3.5-flash-lite",
            name = "Gemini 3.5 Flash-Lite",
            description = "Fastest, most cost-effective model for high-throughput execution.",
            badge = "Fast & Lite",
            inputTokenLimit = 1048576,
            outputTokenLimit = 65536,
            isThinking = true
        ),
        isSelected = false,
        onSelect = {}
    )
}
