package com.example.videoplayer

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.videoplayer.ui.theme.VideoPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoPlayerTheme { MyApp() }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "upload") {
        composable("upload") { UploadScreen(navController) }
        composable(
            "player/{videoUri}",
            arguments = listOf(navArgument("videoUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("videoUri")
            val uri = uriString?.let { Uri.parse(it) }
            uri?.let { VideoPlayerScreen(it) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(navController: NavController) {
    Scaffold( // Video 1
        topBar = { TopAppBar(title = { Text("Video Screen") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {//video 1
                VideoCard(
                    title = "Stayin' Alive",
                    author = "Song by Bee Geens",
                    description = "Starring: Kenley Russel Benitez \nand Dean Aynslie Garcia",
                    imageRes = R.drawable.video1, // Replace with your image
                    onClick = {
                        val videoUri =
                            Uri.parse("android.resource://com.example.videoplayer/" + R.raw.video1)
                        navController.navigate("player/${Uri.encode(videoUri.toString())}")
                    }
                )
            }

            item {//video 2
                VideoCard(
                    title = "Bye Bye Bye",
                    author = "Song by NSYNC",
                    description = "Starring Sir Xziann Jeano Genuino",
                    imageRes = R.drawable.video2, // Replace with your image
                    onClick = {
                        val videoUri =
                            Uri.parse("android.resource://com.example.videoplayer/" + R.raw.video2)
                        navController.navigate("player/${Uri.encode(videoUri.toString())}")
                    }
                )
            }

            item {//video 3
                VideoCard(
                    title = "APTOII",
                    author = "Created by Hotra",
                    description = "APT, Original Song by Bruno Mars and Rosé",
                    imageRes = R.drawable.video3, // Replace with your image
                    onClick = {
                        val videoUri =
                            Uri.parse("android.resource://com.example.videoplayer/" + R.raw.video3)
                        navController.navigate("player/${Uri.encode(videoUri.toString())}")
                    }
                )
            }

            item {//video 1
                VideoCard(
                    title = "Silvera",
                    author = "Song by Gojira",
                    description = "This is my favorite song from this band.",
                    imageRes = R.drawable.video4, // Replace with your image
                    onClick = {
                        val videoUri =
                            Uri.parse("android.resource://com.example.videoplayer/" + R.raw.video4)
                        navController.navigate("player/${Uri.encode(videoUri.toString())}")
                    }
                )
            }


        }
    }


}

@Composable
fun VideoCard(
    title: String,
    author: String,
    description: String,
    imageRes: Int,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier.padding(16.dp)) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = author, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))


                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )

                    Text(text = description, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(videoUri: Uri) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
        }
    }

    // Hide system UI
    val window = (context as ComponentActivity).window
    DisposableEffect(Unit) {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
