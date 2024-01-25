package kr.jeongsejong.core.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kr.jeongsejong.core.ui.BottomNavItem

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation (
        modifier = modifier,
        backgroundColor = Color.White,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route

            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = Color.Green,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewBottomNavigationBar() {
    val navController = rememberNavController()

    BottomNavigationBar(
        items = listOf(
            BottomNavItem(
                name = "search",
                route = "search",
                needLogin = false
            ),
            BottomNavItem(
                name = "saved",
                route = "saved",
                needLogin = true
            ),
            BottomNavItem(
                name = "mypage",
                route = "mypage",
                needLogin = true
            )
        ),
        navController = navController,
        onItemClick = {

        }
    )
}