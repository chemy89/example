package kr.jeongsejong.feature.imageviewer

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kr.jeongsejong.core.designsystem.R
import kr.jeongsejong.core.designsystem.SimpleTopBar

@Composable
fun ImageViewerRoute(
    viewModel: ImageViewerViewModel
) {
    val activity = (LocalContext.current as? Activity)
    val documentItem = viewModel.documentItemFlow.collectAsStateWithLifecycle()

    ImageViewerScreen(
        isSaved = documentItem.value.isSaved,
        imageUrl = documentItem.value.thumbnailUrl,
        navigationUpAction = { activity?.finish() },
        onClickBlock = {
            viewModel.blockItem(documentItem.value)
            activity?.finish()
        },
        onClickSave = { viewModel.toggleSaved(documentItem.value) }
    )

}

@Composable
fun ImageViewerScreen(
    isSaved: Boolean,
    imageUrl: String,
    navigationUpAction: () -> Unit,
    onClickBlock: () -> Unit,
    onClickSave: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "이미지 상세",
                navigationUpAction = { navigationUpAction.invoke() }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val savedIcon = if(isSaved) {
                R.drawable.ic_star_on
            } else {
                R.drawable.ic_star_off
            }

            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFCBCBCB)),
                contentScale = ContentScale.FillWidth,
                model = imageUrl,
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .align(Alignment.BottomStart)
                    .padding(start = 10.dp, bottom = 10.dp)
                    .clickable(onClick = { onClickBlock.invoke() }),
                painter = painterResource(R.drawable.ic_block),
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 10.dp, end = 10.dp)
                    .clickable(onClick = { onClickSave.invoke() }),
                painter = painterResource(savedIcon),
                contentDescription = null
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewImageViewerScreen() {
    ImageViewerScreen(
        imageUrl = "",
        isSaved = false,
        navigationUpAction = { },
        onClickBlock = { },
        onClickSave = { }
    )
}
