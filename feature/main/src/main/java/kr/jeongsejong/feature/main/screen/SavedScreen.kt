package kr.jeongsejong.feature.main.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.woong.compose.grid.SimpleGridCells
import io.woong.compose.grid.VerticalGrid
import kr.jeongsejong.core.data.vo.DocumentData
import kr.jeongsejong.core.designsystem.SimpleEmpty
import kr.jeongsejong.core.ui.ImageItemView
import kr.jeongsejong.feature.main.viewmodel.SavedViewModel

@Composable
fun SavedRoute(
    viewModel: SavedViewModel = hiltViewModel(),
) {
    val savedDocumentList = viewModel.savedDocumentList.collectAsStateWithLifecycle()
    
    SavedScreen(
        documentDataList = savedDocumentList.value,
        emptyText = "저장된 데이터가 없습니다.",
        onClickSave = {
            viewModel.removeSavedItem(it)
        }
    )
}

@Composable
fun SavedScreen(
    documentDataList: List<DocumentData>,
    emptyText: String,
    onClickSave: (DocumentData) -> Unit
) {
    if(documentDataList.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                        showBlockButton = false,
                        showSaveButton = true,
                        onImageClick = { },
                        onClickBlock = { },
                        onClickSave = { onClickSave.invoke(it) },
                    )
                }
            }
        }
    } else {
        SimpleEmpty(message = emptyText)
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
private fun PreviewSavedScreen(
    @PreviewParameter(PreviewDataProvider::class) items: List<DocumentData>
) {
    SavedScreen(
        documentDataList = items,
        emptyText = "저장된 데이터가 없습니다.",
        onClickSave = { },
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEmpty() {
    SavedScreen(
        documentDataList = emptyList(),
        emptyText = "저장된 데이터가 없습니다.",
        onClickSave = { },
    )
}