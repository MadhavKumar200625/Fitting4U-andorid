package com.fitting4u.fitting4u.presentation.boutique.boutique_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail.Related
import com.fitting4u.fitting4u.Data.remote.dto.boutique.search.Boutique
import com.fitting4u.fitting4u.presentation.boutique.home.BoutiqueCard

@Composable
fun HorizontalBoutiqueRow_Related(items: List<Related>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

        items(items) { b ->

            BoutiqueCard(
                b = Boutique(
                    _id = b._id ?: "",
                    title = b.title ?: "",
                    tagline = b.tagline ?: "",
                    googleAddress = b.googleAddress ?: "",
                    priceRange = b.priceRange ?: "",
                    businessLogo = b.businessLogo ?: "",
                    websiteUrl = b.websiteUrl ?: "",
                    type = b.type ?: "",
                    verified = b.verified ?: false
                ),
                onClick = { /* open detail */ },
                modifier = Modifier
                    .width(320.dp)
                    .height(150.dp)
            )
        }
    }
}