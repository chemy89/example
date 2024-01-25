package kr.jeongsejong.feature.main.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kr.jeongsejong.feature.block.BlockActivity
import kr.jeongsejong.feature.main.viewmodel.MyPageViewModel

@Composable
fun MyPageRoute(
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    
    MyPageScreen(
        oAuthToken = viewModel.oAuthToken,
        onClickBlockMenu = {
            launchBlockActivity(context)
        },
        onClickLogout = {
            viewModel.logout()
            relaunchApp(context)
        }
    )
}

@Composable
fun MyPageScreen(
    oAuthToken: String,
    onClickBlockMenu: () -> Unit,
    onClickLogout: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            text = "OAuth Token :\n${oAuthToken}"
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClickBlockMenu.invoke() }
        ) {
            Text(text = "차단한 리스트")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClickLogout.invoke() }
        ) {
            Text(text = "로그아웃")
        }
    }
}

private fun launchBlockActivity(context: Context) {
    val intent = Intent(context, BlockActivity::class.java)
    context.startActivity(intent)
}

private fun relaunchApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.let {
        val mainIntent = Intent.makeRestartActivityTask(it.component)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMyPageScreen() {
    MyPageScreen(
        oAuthToken = "kakao access token",
        onClickBlockMenu = { },
        onClickLogout = { }
    )
}