package com.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Artwork(
    val id: Int,
    @DrawableRes val imageRes: Int,
    val title: String,
    val artist: String,
    val year: String,
    val description: String
)

val artworks = listOf(
    Artwork(1, R.drawable.artwork_1, "Titre de l'œuvre 1", "Artiste 1", "2021", "Description de l'œuvre 1"),
    Artwork(2, R.drawable.artwork_2, "Titre de l'œuvre 2", "Artiste 2", "2022", "Description de l'œuvre 2"),
    Artwork(3, R.drawable.artwork_3, "Titre de l'œuvre 3", "Artiste 3", "2023", "Description de l'œuvre 3")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ArtSpaceApp()
                }
            }
        }
    }
}

@Composable
fun ArtSpaceApp(modifier: Modifier = Modifier) {
    var currentId by rememberSaveable { mutableIntStateOf(1) }
    val artwork = artworks.first { it.id == currentId }

    val onNext = {
        currentId = when (currentId) {
            1 -> 2
            2 -> 3
            else -> 1
        }
    }
    val onPrevious = {
        currentId = when (currentId) {
            1 -> 3
            2 -> 1
            else -> 2
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize().safeDrawingPadding()) {
        val isWide = maxWidth > 600.dp

        if (isWide) {
            Row(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ArtworkWall(artwork, Modifier.weight(1f))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ArtworkDescriptor(artwork)
                    Spacer(Modifier.height(24.dp))
                    DisplayController(onPrevious, onNext)
                }
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                ArtworkWall(artwork, Modifier.padding(top = 16.dp))
                Spacer(Modifier.height(24.dp))
                ArtworkDescriptor(artwork)
                Spacer(Modifier.height(24.dp))
                DisplayController(onPrevious, onNext)
            }
        }
    }
}


@Composable
fun ArtworkWall(artwork: Artwork, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shadowElevation = 12.dp,
        border = BorderStroke(2.dp, Color.DarkGray),
        color = Color.White
    ) {
        Image(
            painter = painterResource(artwork.imageRes),
            contentDescription = artwork.description,
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
fun ArtworkDescriptor(artwork: Artwork, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 480.dp)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = artwork.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center
        )
        Text(
            text = artistLine(artwork),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun artistLine(artwork: Artwork): AnnotatedString = buildAnnotatedString {
    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(artwork.artist) }
    append(" (${artwork.year})")
}

@Composable
fun DisplayController(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().widthIn(max = 480.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = onPrevious, modifier = Modifier.weight(1f)) {
            Text("Précédent")
        }
        Button(onClick = onNext, modifier = Modifier.weight(1f)) {
            Text("Suivant")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtSpacePreview() {
    MaterialTheme { ArtSpaceApp() }
}