package kr.jeongsejong.core.designsystem

import androidx.compose.foundation.Image
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(
    title: String,
    navigationUpAction: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(
                onClick = { navigationUpAction.invoke() }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewSimpleTopBar() {
    SimpleTopBar(
        title = "타이틀",
        navigationUpAction = { }
    )
}