package com.example.videoplayer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.videoplayer.R

val GoogleSans = FontFamily(
    Font(R.font.googlesans_regular),
    Font(R.font.googlesans_regular)
)

val PressStart2P = FontFamily(Font(R.font.pressstart2p))
// Define the Poppins font family
val Sarpanch = FontFamily(
    Font(R.font.sarpanch_regular, FontWeight.Normal),
    Font(R.font.sarpanch_extrabold, FontWeight.ExtraBold),
    Font(R.font.sarpanch_medium, FontWeight.Medium),
    Font(R.font.sarpanch_semibold, FontWeight.SemiBold),
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = PressStart2P,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Sarpanch,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = PressStart2P,
        fontSize = 14.sp
    )
)


// Set of Material typography styles to start with
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
/* Other default text styles to override
titleLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
),
labelSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)
*/