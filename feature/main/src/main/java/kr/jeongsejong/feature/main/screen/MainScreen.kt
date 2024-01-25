package kr.jeongsejong.feature.main.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kr.jeongsejong.core.common.ktx.navigationSingleTopTo
import kr.jeongsejong.core.designsystem.SimpleDialog
import kr.jeongsejong.core.ui.BottomNavItem
import kr.jeongsejong.core.ui.compose.BottomNavigationBar
import kr.jeongsejong.feature.main.viewmodel.MainViewModel

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val isLoggedIn = viewModel.isLoggedInFlow.collectAsStateWithLifecycle()

    MainScreen(
        isLoggedIn = isLoggedIn.value,
        onPositiveClick = {
            viewModel.requestKakaoLogin(context)
        }
    )
}

@Composable
fun MainScreen(
    isLoggedIn: Boolean,
    onPositiveClick: () -> Unit
) {
    var isShowDialog by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    if (isShowDialog) {
        SimpleDialog(
            message = "로그인이 필요합니다.",
            positiveButtonText = "카카오 로그인",
            onPositiveClick = {
                isShowDialog = false
                onPositiveClick.invoke()
            },
            negativeButtonText = "취소",
            onNegativeClick = {
                isShowDialog = false
            },
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(
                        name = "검색",
                        route = "search",
                    ),
                    BottomNavItem(
                        name = "저장",
                        route = "saved",
                    ),
                    BottomNavItem(
                        name = "마이페이지",
                        route = "mypage",
                    )
                ),
                navController = navController,
                onItemClick = {
                    when (it.route) {
                        "saved", "mypage" -> {
                            if(!isLoggedIn) {
                                isShowDialog = true
                            } else {
                                navController.navigationSingleTopTo(it.route)
                            }
                        }
                        else -> navController.navigationSingleTopTo(it.route)
                    }
                })
        }
    ) {
        Navigation(
            modifier = Modifier.padding(it),
            navController = navController
        )
    }
}

@Composable
fun Navigation(
    modifier: Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "search",
    ) {
        composable(
            route = "search",
            enterTransition = null,
            exitTransition = null
        ) {
            SearchRoute()
        }
        composable(
            route = "saved",
            enterTransition = null,
            exitTransition = null
        ) {
            SavedRoute()
        }
        composable(
            route = "mypage",
            enterTransition = null,
            exitTransition = null
        ) {
            MyPageRoute()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen(
        isLoggedIn = false,
        onPositiveClick = { }
    )
}