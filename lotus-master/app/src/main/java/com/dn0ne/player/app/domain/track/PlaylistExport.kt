package com.dn0ne.player.app.domain.track

/**
 * Serializes the playlist to extended M3U.
 *
 * Track paths are written as absolute file paths (`Track.data`) on their own lines, which is exactly
 * what [com.dn0ne.player.app.presentation.PlayerViewModel.parseM3U] reads back on import, so an
 * exported file can be re-imported. Comment lines (`#...`) are ignored by the importer.
 */
fun Playlist.toM3U(): String = buildString {
    appendLine("#EXTM3U")
    trackList.forEach { track ->
        val seconds = (track.duration / 1000).coerceAtLeast(0)
        val label = listOfNotNull(track.artist, track.title)
            .joinToString(" - ")
            .ifBlank { track.data.substringAfterLast('/') }
            .replace('\n', ' ')
            .replace('\r', ' ')
        appendLine("#EXTINF:$seconds,$label")
        appendLine(track.data)
    }
}
