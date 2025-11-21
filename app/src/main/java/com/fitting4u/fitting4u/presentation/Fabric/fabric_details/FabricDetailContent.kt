package com.fitting4u.fitting4u.presentation.Fabric.fabric_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fitting4u.fitting4u.Data.remote.dto.fabric.Fabric.FabricDto


/* ===== Constants ===== */
private val PrimaryBlue = Color(0xFF003466)
private val AccentPink = Color(0xFFFFC1CC)

/* ===== Top-level screen entry ===== */
@Composable
fun FabricDetailContent(
    fabric: FabricDto,
    onAddToCart: (id: String, qtyMeters: Double) -> Unit,
    onDecreaseQty: (id: String, minus: Double) -> Unit,
    onRemoveFromCart: (id: String) -> Unit,
    existingQty: Double,
    totalQty:Double,
    navController: NavController
) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 140.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White, Color(0xFFF7F9FB))
                    )
                ),
            contentPadding = PaddingValues(top = 40.dp)
        ) {

            // Image carousel block
            item {
                ImageCarouselSection(images = fabric.images, name = fabric.name)
            }

            // Title + Collection
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                    Text(
                        text = fabric.name,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    if (fabric.collectionName.isNotBlank()) {
                        Text(
                            text = fabric.collectionName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }

            // Ratings
            item {
                ReviewStarsRow(avgStars = fabric.avgStars, reviews = fabric.reviews ?: listOf())
            }

            // Price card
            item {
                PriceCard(
                    context = context,
                    fabric = fabric,
                    onAddToCart = onAddToCart
                )
            }

            // Qty + CTA inside the scroll (as requested)
            item(key = fabric.slug + existingQty.toString()) {
                QtyAddToCartBlock(
                    fabric = fabric,
                    onAddToCart = onAddToCart,
                    onDecreaseQty = onDecreaseQty,
                    onRemoveFromCart = onRemoveFromCart,
                    existingQty = existingQty
                )
            }

            // Specifications
            item {
                Spacer(modifier = Modifier.height(12.dp))
                SpecsCard(fabric = fabric)
            }

            // Description block
            item {
                Spacer(modifier = Modifier.height(12.dp))

                DescriptionCard(
                    text = if (fabric.description.isBlank())
                        "No description available."
                    else fabric.description
                )
            }

            // Accordion sections (Care, Shipping, Returns, Ask, Additional)
            item {
                Spacer(modifier = Modifier.height(12.dp))
                AccordionSection(title = "Care Instructions", contentComposable = {
                    if (fabric.careInstructions.isNullOrEmpty()) {
                        Text("No care instructions provided.")
                    } else {
                        Column {
                            fabric.careInstructions.forEach { ci ->
                                Text("- $ci", modifier = Modifier.padding(vertical = 2.dp))
                            }
                        }
                    }
                })
            }

            item {
                AccordionSection(title = "Shipping Information", contentComposable = {
                    Column {
                        Text("Orders are processed within 24–48 hours. Delivery typically takes 5–7 business days.")
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Free shipping on orders above ₹999.")
                        Text("• International shipping available on request.")
                        Text("• Fabric is shipped rolled or folded to prevent creases.")
                    }
                })
            }

            item {
                AccordionSection(title = "Returns & Exchange Policy", contentComposable = {
                    Column {
                        Text("Returns accepted within 7 days of delivery if uncut and unused.")
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Fabric once cut cannot be returned or exchanged.")
                        Text("• For damaged items, contact support within 48 hours.")
                    }
                })
            }

            item {
                AccordionSection(title = "Ask a Question", contentComposable = {
                    Column {
                        Text("Have queries about customization, availability, or wholesale? Reach out to us:")
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Email: support@fitting-4-u.com",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Phone: +91 98765 43210",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }
                })
            }

            item {
                AccordionSection(title = "Additional Information", contentComposable = {
                    Column {
                        Text("• Category: Dress Material")
                        Text("• Origin: Handwoven in India")
                        Text("• Fabric Code: ${fabric.slug.ifBlank { "—" }}")
                        Text("• Weight: 120 GSM")
                    }
                })
            }

            // FAQs
            item {
                if (!fabric.faqs.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))

                    PaperSection(title = "FAQs") {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                            fabric.faqs.forEach { f ->

                                // Each FAQ card inside the section
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .border(
                                            width = 1.dp,
                                            color = Color(0xFFE5E7EB),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(14.dp)
                                ) {
                                    Text(
                                        text = "Q. ${f.question}",
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryBlue,
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        text = f.answer,
                                        color = Color(0xFF374151),
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(start = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Reviews cards
            item {
                if (!fabric.reviews.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    PaperSection(title = "Reviews & Testimonials") {
                        Column {
                            fabric.reviews.forEach { r ->
                                ReviewCard(r)
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }

            // bottom spacer
            item {
                Spacer(modifier = Modifier.height(36.dp))
            }


        }

    }

}


