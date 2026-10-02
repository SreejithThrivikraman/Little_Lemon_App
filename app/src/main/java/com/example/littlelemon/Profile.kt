package com.example.littlelemon

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavHostController

@Composable
fun Profile(navController: NavHostController? = null) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)

    val firstName = sharedPreferences.getString("firstName", "") ?: ""
    val lastName = sharedPreferences.getString("lastName", "") ?: ""
    val email = sharedPreferences.getString("email", "") ?: ""

    val primaryYellow = Color(0xFFF4CE14)
    val darkCharcoal = Color(0xFF333333)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header Section with Logo Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Little Lemon Logo",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(40.dp),
            )
        }

        // Profile Form Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            Text(
                text = "Profile information:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = darkCharcoal,
                modifier = Modifier.padding(bottom = 20.dp),
            )

            // First Name Text Composables
            Text(
                text = "First name",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                text = firstName.ifBlank { "N/A" },
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = darkCharcoal,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            // Last Name Text Composables
            Text(
                text = "Last name",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                text = lastName.ifBlank { "N/A" },
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = darkCharcoal,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            // Email Text Composables
            Text(
                text = "Email",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                text = email.ifBlank { "N/A" },
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = darkCharcoal,
                modifier = Modifier.padding(bottom = 28.dp),
            )

            // Flexible Spacer pushes the Log out button to the bottom of the screen
            Spacer(modifier = Modifier.weight(1f))

            // Log out Button
            Button(
                onClick = {
                    sharedPreferences.edit { clear() }
                    navController?.navigate(Onboarding.route) {
                        popUpTo(Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryYellow,
                    contentColor = darkCharcoal,
                ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = "Log out",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavHostController? = null) {
    Profile(navController = navController)
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    Profile()
}
