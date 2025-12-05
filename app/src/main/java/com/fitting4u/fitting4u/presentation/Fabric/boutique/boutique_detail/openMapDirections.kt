package com.fitting4u.fitting4u.presentation.Fabric.boutique.boutique_detail

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openMapDirections(
    context: Context,
    lat: Double?,
    lon: Double?,
    title: String?
) {
    // If lat/lng missing → fallback search
    if (lat == null || lon == null) {
        val query = title ?: ""
        val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(query)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
        return
    }

    // Normal directions request
    val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$lat,$lon")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}