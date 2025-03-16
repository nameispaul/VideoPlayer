package com.example.videoplayer

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
import com.example.videoplayer.ui.theme.GoogleSans
import com.example.videoplayer.ui.theme.VideoPlayerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoPlayerTheme { MyApp() }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp() {
    val navController = rememberNavController()

    VideoPlayerTheme {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface), // Set the background color
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface) // Ensure the whole screen uses this background
                ) {
                    NavHost(navController, startDestination = "upload") {
                        composable("upload") { UploadScreen(navController) }
                        composable(
                            "player/{videoUri}",
                            arguments = listOf(navArgument("videoUri") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val uriString = backStackEntry.arguments?.getString("videoUri")
                            val uri = uriString?.let { Uri.parse(it) }
                            uri?.let { VideoPlayerScreen(navController, it) }
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(navController: NavController) {
    LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Music Videos",
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                )
            )
        },
    ) { padding ->
        if (isLandscape) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 columns
                modifier = Modifier
                    .padding(padding) // Apply Scaffold padding
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(videoList) { video ->
                    VideoCard(
                        title = video.title,
                        author = video.author,
                        description = video.description,
                        imageRes = video.imageRes,
                        onClick = {
                            val videoUri =
                                Uri.parse("android.resource://com.example.videoplayer/${video.videoRes}")
                            navController.navigate("player/${Uri.encode(videoUri.toString())}")
                        }
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding) // Apply Scaffold padding
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(videoList) { video ->
                    VideoCard(
                        title = video.title,
                        author = video.author,
                        description = video.description,
                        imageRes = video.imageRes,
                        onClick = {
                            val videoUri =
                                Uri.parse("android.resource://com.example.videoplayer/${video.videoRes}")
                            navController.navigate("player/${Uri.encode(videoUri.toString())}")
                        }
                    )
                }
            }
        }
    }
}

// List of videos
val videoList = listOf(
    VideoData(
        "Stayin' Alive",
        "Song by Bee Gees",
        "Starring: Kenley Russel Benitez and Dean Aynslie Garcia",
        R.drawable.video1,
        R.raw.video1
    ),
    VideoData(
        "Bye Bye Bye",
        "Song by NSYNC",
        "Starring Sir Xziann Jeano Genuino",
        R.drawable.video2,
        R.raw.video2
    ),
    VideoData(
        "APTOII",
        "Song by Bruno Mars and Rosé",
        "Created by Hotra",
        R.drawable.video3,
        R.raw.video3
    ),
    VideoData(
        "Silvera",
        "Song by Gojira",
        "This is my favorite song from this band.",
        R.drawable.video4,
        R.raw.video4
    )
)

// Data class for videos
data class VideoData(
    val title: String,
    val author: String,
    val description: String,
    val imageRes: Int,
    val videoRes: Int,
)


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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 300.dp), // Adjust the elevation as needed
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { onClick() },
                    modifier = Modifier.size(60.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play),
                        modifier = Modifier
                            .size(50.dp),
                        contentDescription = "Play",
                        tint = Color.White // Adjust tint as needed
                    )
                }
            }
            Box(modifier = Modifier.padding(16.dp)) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Normal,
                        fontFamily = GoogleSans,

                        )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = author,
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = GoogleSans
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = GoogleSans
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(navController: NavController, videoUri: Uri) {
    var isVisible by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val window = (context as ComponentActivity).window
    val insetsController = window.insetsController

    // Enter Fullscreen Mode
    LaunchedEffect(Unit) {
        insetsController?.hide(WindowInsets.Type.systemBars())
        insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    // Restore System UI on Exit
    DisposableEffect(Unit) {
        onDispose {
            insetsController?.show(WindowInsets.Type.systemBars())
        }
    }

    // Handle Back Button Press
    BackHandler {
        isVisible = false  // Trigger exit animation
    }

    // Observe animation completion and navigate back
    if (!isVisible) {
        LaunchedEffect(Unit) {
            delay(500) // Adjust delay to match animation duration
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PlayerView(context).apply {
                    val player = ExoPlayer.Builder(context)
                        .setSeekBackIncrementMs(5000)
                        .setSeekForwardIncrementMs(5000)
                        .build()
                        .apply {
                        setMediaItem(MediaItem.fromUri(videoUri))
                        prepare()
                        playWhenReady = true
                    }
                    this.player = player
                    this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    this.useController = true

                    setShowPreviousButton(false) // Hide Previous Button
                    setShowNextButton(false) // Hide Next Button
                }
            },
        )
    }
}
