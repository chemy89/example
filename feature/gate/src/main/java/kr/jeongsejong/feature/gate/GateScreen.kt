package kr.jeongsejong.feature.gate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.jeongsejong.feature.main.MainActivity

@Composable
fun GateRoute(
    viewModel: GateViewModel = hiltViewModel(),
) {
    val activity = (LocalContext.current as Activity)
    val loginState = viewModel.loginState.collectAsStateWithLifecycle()

    when (loginState.value) {
        GateViewModel.LoginState.SUCCESS -> launchMainActivity(activity)
        GateViewModel.LoginState.ERROR -> Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show()
        else -> Unit
    }

    GateScreen(
        socialText = "카카오 로그인",
        skipText = "살펴보기",
        onClickSocial = {
            viewModel.requestKakaoLogin(activity)
        },
        onClickSkip = {
            launchMainActivity(activity)
        }
    )
}

@Composable
fun GateScreen(
    socialText: String,
    skipText: String,
    onClickSocial: () -> Unit,
    onClickSkip: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickSocial
        ) {
            Text(text = socialText)
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickSkip
        ) {
            Text(text = skipText)
        }
    }
}

private fun launchMainActivity(activity: Activity) {
    val intent = Intent(activity, MainActivity::class.java)
    activity.startActivity(intent)
    activity.finish()
}

@Preview(showBackground = true)
@Composable
fun PreviewGateScreen() {
    GateScreen(
        socialText = "카카오 로그인",
        skipText = "살펴보기",
        onClickSocial = { },
        onClickSkip = { }
    )
}