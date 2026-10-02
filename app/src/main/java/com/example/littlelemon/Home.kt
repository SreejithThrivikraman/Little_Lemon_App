package com.example.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.room.Room

@Composable
fun Home(navController: NavHostController? = null) {
    val context = LocalContext.current
    val database = Room.databaseBuilder(context, AppDatabase::class.java, "database").build()
    val databaseMenuItems by database.menuDao().getAll().observeAsState(emptyList())

    var searchPhrase by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    val primaryGreen = Color(0xFF495E57)
    val primaryYellow = Color(0xFFF4CE14)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header with Logo in center and Profile image on the right
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Little Lemon Logo",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(40.dp)
                    .align(Alignment.Center),
            )

            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "User Profile",
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterEnd)
                    .clickable {
                        navController?.navigate(Profile.route)
                    },
            )
        }

        // Hero Banner Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryGreen)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
        ) {
            Text(
                text = "Little Lemon",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = primaryYellow,
            )

            Text(
                text = "Chicago",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                    fontSize = 16.sp,
                    color = Color(0xFFEDEFEE),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp),
                )

                Image(
                    painter = painterResource(id = R.drawable.hero_image),
                    contentDescription = "Hero Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(16.dp)),
                )
            }

            TextField(
                value = searchPhrase,
                onValueChange = { searchPhrase = it },
                placeholder = { Text("Enter Search Phrase") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(8.dp),
            )
        }

        // Menu Breakdown Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            Text(
                text = "ORDER FOR DELIVERY!",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 12.dp),
            )

            val categories = if (databaseMenuItems.isNotEmpty()) {
                databaseMenuItems.map { item ->
                    item.category.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                }.distinct()
            } else {
                listOf("Starters", "Mains", "Desserts", "Drinks")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory.equals(category, ignoreCase = true)
                    Button(
                        onClick = {
                            selectedCategory = if (isSelected) "" else category
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) primaryGreen else Color(0xFFEDEFEE),
                            contentColor = if (isSelected) Color.White else primaryGreen,
                        ),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            text = category,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color(0xFFEDEFEE),
        )

        // Filter menu items by category and search phrase
        var filteredMenuItems = databaseMenuItems

        if (selectedCategory.isNotBlank()) {
            filteredMenuItems = filteredMenuItems.filter { menuItem ->
                menuItem.category.equals(selectedCategory, ignoreCase = true)
            }
        }

        if (searchPhrase.isNotBlank()) {
            filteredMenuItems = filteredMenuItems.filter { menuItem ->
                menuItem.title.contains(searchPhrase, ignoreCase = true)
            }
        }

        // Display filtered menu items
        MenuItems(items = filteredMenuItems)
    }
}

@Composable
fun HomeScreen(navController: NavHostController? = null) {
    Home(navController = navController)
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Home()
}
