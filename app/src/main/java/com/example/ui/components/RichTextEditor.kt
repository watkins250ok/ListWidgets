package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatClear
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Title
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RichTextEditor(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Write details, notes, or indented bullet lists..."
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
    ) {
        // Formatting Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f))
                .padding(horizontal = 6.dp, vertical = 4.dp)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            ToolbarButton(icon = Icons.Default.FormatBold, contentDesc = "Bold") {
                onValueChange(wrapOrInsert(value, "**", "**", "Bold Text"))
            }
            ToolbarButton(icon = Icons.Default.FormatItalic, contentDesc = "Italic") {
                onValueChange(wrapOrInsert(value, "*", "*", "Italic Text"))
            }
            ToolbarButton(icon = Icons.Default.FormatUnderlined, contentDesc = "Underline") {
                onValueChange(wrapOrInsert(value, "<u>", "</u>", "Underlined"))
            }
            ToolbarButton(icon = Icons.Default.FormatStrikethrough, contentDesc = "Strike") {
                onValueChange(wrapOrInsert(value, "~~", "~~", "Strikethrough"))
            }
            ToolbarTextButton(label = "H1", contentDesc = "Heading 1") {
                onValueChange(insertLinePrefix(value, "# "))
            }
            ToolbarTextButton(label = "H2", contentDesc = "Heading 2") {
                onValueChange(insertLinePrefix(value, "## "))
            }
            ToolbarButton(icon = Icons.AutoMirrored.Filled.FormatListBulleted, contentDesc = "Bullet List") {
                onValueChange(insertLinePrefix(value, "  • "))
            }
            ToolbarButton(icon = Icons.Default.FormatListNumbered, contentDesc = "Numbered List") {
                onValueChange(insertLinePrefix(value, "1. "))
            }
            ToolbarButton(icon = Icons.Default.FormatQuote, contentDesc = "Quote") {
                onValueChange(insertLinePrefix(value, "> "))
            }
            ToolbarButton(icon = Icons.Default.Code, contentDesc = "Code") {
                onValueChange(wrapOrInsert(value, "`", "`", "code"))
            }
            ToolbarButton(icon = Icons.Default.FormatClear, contentDesc = "Clear") {
                onValueChange("")
            }
        }

        // Editor Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .heightIn(min = 120.dp, max = 240.dp)
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
private fun ToolbarButton(
    icon: ImageVector,
    contentDesc: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(34.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDesc,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun ToolbarTextButton(
    label: String,
    contentDesc: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        color = Color.Transparent,
        modifier = Modifier.size(34.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

private fun wrapOrInsert(text: String, prefix: String, suffix: String, defaultPlaceholder: String): String {
    return if (text.isBlank()) {
        "$prefix$defaultPlaceholder$suffix"
    } else {
        "$text\n$prefix$defaultPlaceholder$suffix"
    }
}

private fun insertLinePrefix(text: String, linePrefix: String): String {
    return if (text.isBlank()) {
        linePrefix
    } else {
        "$text\n$linePrefix"
    }
}
