package com.dn0ne.player.app.presentation.components.glass

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dn0ne.player.app.presentation.components.topbar.Tab

/**
 * Translucent "glass" fill: a vertical gradient of the theme's container color with partial alpha,
 * so the content scrolling underneath stays faintly visible.
 *
 * NOTE: this is translucency + a specular border, not a true backdrop blur. A real blur needs a
 * library such as Haze (or a shader); it would only need to change the code in this file.
 */
@Composable
fun glassFillBrush(): Brush {
    val base = MaterialTheme.colorScheme.surfaceContainerHigh
    return Brush.verticalGradient(
        listOf(
            base.copy(alpha = 0.82f),
            base.copy(alpha = 0.68f)
        )
    )
}

/** Thin light edge that fades diagonally, imitating light catching the rim of a glass pane. */
@Composable
fun glassBorderBrush(): Brush {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val highlight = Color.White.copy(alpha = if (isDark) 0.30f else 0.70f)
    val shadow = Color.White.copy(alpha = if (isDark) 0.05f else 0.15f)
    return Brush.linearGradient(listOf(highlight, shadow, highlight))
}

/** Floating, fully rounded glass bar holding one item per [tabs] entry. */
@Composable
fun GlassNavBar(
    tabs: List<Tab>,
    selectedTab: Tab,
    onTabClick: (Tab) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(32.dp)
    val fill = glassFillBrush()
    val border = glassBorderBrush()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(shape)
            .background(brush = fill)
            .border(width = 1.dp, brush = border, shape = shape)
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            GlassNavItem(
                tab = tab,
                selected = tab == selectedTab,
                onClick = { onTabClick(tab) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun GlassNavItem(
    tab: Tab,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    val unselected = MaterialTheme.colorScheme.onSurfaceVariant

    val containerColor by animateColorAsState(
        targetValue = if (selected) primary.copy(alpha = 0.22f) else Color.Transparent,
        label = "glass-nav-item-container"
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) primary else unselected,
        label = "glass-nav-item-content"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(26.dp))
            .background(color = containerColor)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = tab.icon(),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(tab.titleResId),
            color = contentColor,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun Tab.icon(): ImageVector = when (this) {
    Tab.Playlists -> Icons.Rounded.LibraryMusic
    Tab.Tracks -> Icons.Rounded.MusicNote
    Tab.Albums -> Icons.Rounded.Album
    Tab.Artists -> Icons.Rounded.Person
    Tab.Genres -> Icons.Rounded.Category
    Tab.Folders -> Icons.Rounded.Folder
}
