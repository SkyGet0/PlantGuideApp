package com.example.plantguideapp.ui.plants

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plantguideapp.viewmodel.PlantViewModel

private val LightGreen = Color(0xFFA8E6A3)
private val MidGreen = Color(0xFF66CC66)
private val DarkGreen = Color(0xFF339933)

@SuppressLint("DiscouragedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailScreen(
    plantId: Int,
    viewModel: PlantViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val plantState = viewModel.getPlantById(plantId).collectAsState(initial = null)
    val plant = plantState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Информация о растении", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MidGreen)
            )
        },
        containerColor = LightGreen
    ) { innerPadding ->
        if (plant == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MidGreen)
            }
        } else {
            val context = LocalContext.current
            val imageId = remember(plant.imageResName) {
                context.resources.getIdentifier(plant.imageResName, "drawable", context.packageName)
            }

            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp)
                    .padding(innerPadding)
            ) {
                if (imageId != 0) {
                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = "Фото растения",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = DarkGreen
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Описание:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MidGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = plant.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (plant.longDescription.isNotBlank()) {
                    Text(
                        text = "Подробное описание:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MidGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = plant.longDescription,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Text(
                    text = "Категория: ${plant.category}",
                    style = MaterialTheme.typography.labelLarge,
                    color = DarkGreen
                )
            }
        }
    }
}
