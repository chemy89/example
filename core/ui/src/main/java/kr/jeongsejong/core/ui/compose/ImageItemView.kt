package kr.jeongsejong.core.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kr.jeongsejong.core.designsystem.R

@Composable
fun ImageItemView(
    imageUrl: String,
    isSaved: Boolean,
    showBlockButton: Boolean,
    showSaveButton: Boolean,
    onImageClick: () -> Unit,
    onClickBlock: () -> Unit,
    onClickSave: () -> Unit,
) {
    val savedIcon = if(isSaved) {
        R.drawable.ic_star_on
    } else {
        R.drawable.ic_star_off
    }

    Box {
        AsyncImage(
            modifier = Modifier
                .aspectRatio(1f)
                .background(Color(0xFFCBCBCB))
                .clickable(
                    onClick = { onImageClick.invoke() }
                ),
            contentScale = ContentScale.Crop,
            model = imageUrl,
            contentDescription = null,
        )

        if (showBlockButton) {
            Image(
                modifier = Modifier
                    .height(30.dp)
                    .width(30.dp)
                    .align(Alignment.BottomStart)
                    .padding(start = 5.dp, bottom = 5.dp)
                    .clickable(onClick = { onClickBlock.invoke() }),
                painter = painterResource(R.drawable.ic_block),
                contentDescription = null
            )
        }

        if (showSaveButton) {
            Image(
                modifier = Modifier
                    .height(30.dp)
                    .width(30.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 5.dp, end = 5.dp)
                    .clickable(onClick = { onClickSave.invoke() }),
                painter = painterResource(savedIcon),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewImageItemView() {
    ImageItemView(
        imageUrl = "http://www.bing.com/search?q=vim",
        isSaved = true,
        showBlockButton = true,
        showSaveButton = true,
        onImageClick = { },
        onClickBlock = { },
        onClickSave = { }
    )
}