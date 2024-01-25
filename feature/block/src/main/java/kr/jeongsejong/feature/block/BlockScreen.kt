package kr.jeongsejong.feature.block

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.woong.compose.grid.SimpleGridCells
import io.woong.compose.grid.VerticalGrid
import kr.jeongsejong.core.data.vo.DocumentData
import kr.jeongsejong.core.designsystem.SimpleEmpty
import kr.jeongsejong.core.designsystem.SimpleTopBar
import kr.jeongsejong.core.ui.compose.ImageItemView

@Composable
fun BlockRoute(
    viewModel: BlockViewModel = hiltViewModel(),
) {
    val activity = (LocalContext.current as? Activity)
    val savedDocumentList = viewModel.blockedDocumentList.collectAsStateWithLifecycle()

    BlockScreen(
        title = "차단 목록",
        emptyMessage = "차단한 데이터가 없습니다.",
        documentDataList = savedDocumentList.value,
        navigationUpAction = { activity?.finish() },
        onClickBlock = { viewModel.removeBlockedItem(it) }
    )

}

@Composable
fun BlockScreen(
    title: String,
    emptyMessage: String,
    documentDataList: List<DocumentData>,
    navigationUpAction: () -> Unit,
    onClickBlock: (DocumentData) -> Unit
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = title,
                navigationUpAction = { navigationUpAction.invoke() }
            )
        },
    ) { innerPadding ->
        if(documentDataList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
            ) {
                VerticalGrid(
                    columns = SimpleGridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    documentDataList.forEach {
                        ImageItemView(
                            imageUrl = it.thumbnailUrl,
                            isSaved = it.isSaved,
                            showBlockButton = true,
                            showSaveButton = false,
                            onImageClick = { },
                            onClickSave = { },
                            onClickBlock = { onClickBlock.invoke(it) },
                        )
                    }
                }
            }
        } else {
            SimpleEmpty(
                modifier = Modifier.padding(innerPadding),
                message = emptyMessage
            )
        }
    }
}

private class PreviewDataProvider : PreviewParameterProvider<List<DocumentData>> {
    override val values: Sequence<List<DocumentData>>
        get() = sequenceOf(
            listOf(
                DocumentData.empty,
                DocumentData.empty,
                DocumentData.empty,
                DocumentData.empty,
                DocumentData.empty,
            )
        )
}

@Preview(showBackground = true)
@Composable
fun PreviewEmpty() {
    BlockScreen(
        title = "차단 목록",
        emptyMessage = "차단한 데이터가 없습니다.",
        documentDataList = emptyList(),
        navigationUpAction = { },
        onClickBlock = { },
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewBlockScreen(
    @PreviewParameter(PreviewDataProvider::class) items: List<DocumentData>
) {
    BlockScreen(
        title = "차단 목록",
        emptyMessage = "차단한 데이터가 없습니다.",
        documentDataList = items,
        navigationUpAction = { },
        onClickBlock = { },
    )
}