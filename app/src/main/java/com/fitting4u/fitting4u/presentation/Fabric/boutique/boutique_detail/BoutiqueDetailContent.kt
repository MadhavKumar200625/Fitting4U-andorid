package com.fitting4u.fitting4u.presentation.Fabric.boutique.boutique_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail.Data
import com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail.NearMe
import com.fitting4u.fitting4u.Data.remote.dto.boutique.boutique_detail.Related



@Composable
fun BoutiqueDetailContent(
    data: Data,
    nearMe: List<NearMe>,
    related: List<Related>,
    onNavigateToBoutiqueSearch: () -> Unit,
    onClickBoutique: (slug: String)-> Unit

) {
    val context = LocalContext.current
    val gallery = if (!data.imageGallery.isNullOrEmpty()) data.imageGallery else listOf(data.businessLogo ?: "")

    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp)
            .verticalScroll(rememberScrollState())
            .systemBarsPadding()
    ) {
        BoutiqueDetailTopBar(onNavigateToBoutiqueSearch)
        // ─────────────────────────────────────────────────────────────
        // CAROUSEL + FLOATING LOGO
        // ─────────────────────────────────────────────────────────────
        Box(Modifier.fillMaxWidth()) {



            CarouselImageStrip(
                images = gallery,
                height = 280.dp
            )

            // Floating circular logo
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 36.dp)
                    .shadow(10.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .size(84.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = data.businessLogo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                )
            }
        }

        Spacer(Modifier.height(56.dp))

        // ─────────────────────────────────────────────────────────────
        // TITLE + TAGLINE
        // ─────────────────────────────────────────────────────────────
        Column(Modifier.padding(horizontal = 18.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {

                    Text(
                        data.title ?: "",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    )

                    if (!data.tagline.isNullOrBlank()) {
                        Text(
                            data.tagline!!,
                            style = MaterialTheme.typography.bodyMedium.copy(color = MutedText),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                if (data.verified == true) {
                    VerifiedBadge()
                }
            }

            Spacer(Modifier.height(12.dp))

            // Chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                if (!data.priceRange.isNullOrBlank()) ChipSmall(data.priceRange!!)

                if (!data.type.isNullOrBlank()) ChipSmall(data.type!!)


            }

            Spacer(Modifier.height(18.dp))

            // ─────────────────────────────────────────────────────────────
            // DESCRIPTION
            // ─────────────────────────────────────────────────────────────
            if (!data.description.isNullOrBlank()) {
                Card(
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("About", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text(data.description!!, color = MutedText)
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

            // ─────────────────────────────────────────────────────────────
            // GET DIRECTIONS (only card)
            // ─────────────────────────────────────────────────────────────
            DirectionsCard(
                address = data.googleAddress,
                lat = data.lat,
                lon = data.long,
                title = data.title
            )

            Spacer(Modifier.height(14.dp))

            // ─────────────────────────────────────────────────────────────
            // BUSINESS HOURS
            // ─────────────────────────────────────────────────────────────
            if (!data.businessHours.isNullOrEmpty()) {
                BusinessHoursSection(data.businessHours!!)
                Spacer(Modifier.height(12.dp))
            }

            // ─────────────────────────────────────────────────────────────
            // FAQ
            // ─────────────────────────────────────────────────────────────
            if (!data.faqs.isNullOrEmpty()) {
                Text("FAQ", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                data.faqs.forEach { item ->
                    ExpandableFaqItem(faq = item)
                    Spacer(Modifier.height(8.dp))
                }
                }

                Spacer(Modifier.height(12.dp))
            }

            // ─────────────────────────────────────────────────────────────
            // NEAR ME
            // ─────────────────────────────────────────────────────────────
            if (nearMe.isNotEmpty()) {
                SectionHeader("Near Me") { onNavigateToBoutiqueSearch() }
                Spacer(Modifier.height(8.dp))
                HorizontalBoutiqueRow_NearMe(nearMe, {slug->onClickBoutique(slug)})
                Spacer(Modifier.height(12.dp))
            }

            // ─────────────────────────────────────────────────────────────
            // RELATED
            // ─────────────────────────────────────────────────────────────
            if (related.isNotEmpty()) {
                SectionHeader("Related Boutiques") { onNavigateToBoutiqueSearch() }
                Spacer(Modifier.height(8.dp))
                HorizontalBoutiqueRow_Related(related,onClickBoutique)
                Spacer(Modifier.height(24.dp))
            }
        }
    }




@Composable
fun ChipSmall(
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Primary.copy(alpha = 0.08f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Primary,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}


@Composable
fun SectionHeader(
    title: String,
    onSeeAll: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Primary
        )


    }
}