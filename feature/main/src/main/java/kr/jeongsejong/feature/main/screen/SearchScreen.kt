package kr.jeongsejong.feature.main.screen

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import kr.jeongsejong.core.data.vo.DocumentData
import kr.jeongsejong.core.designsystem.SimpleDialog
import kr.jeongsejong.core.ui.compose.ImageItemView
import kr.jeongsejong.feature.imageviewer.ImageViewerActivity
import kr.jeongsejong.feature.main.viewmodel.SearchViewModel

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val isLoggedIn = viewModel.isLoggedInFlow.collectAsStateWithLifecycle()
    val searchKeyword = viewModel.searchKeywordFlow.collectAsState()
    val documentList = viewModel.documentList.collectAsLazyPagingItems()

    SearchScreen(
        documentList = documentList,
        isLoggedIn = isLoggedIn.value,
        searchKeyword = searchKeyword.value,
        placeholder = "검색어를 입력해주세요.",
        onLoginButtonClick = { viewModel.requestKakaoLogin(context) },
        onTextChanged = { viewModel.setSearchKeyword(it) },
        onSearchButtonClick = { viewModel.searchImage() },
        onClickBlock = { viewModel.blockItem(it) },
        onImageClick = { launchImageViewerActivity(context, it) },
        onToggleSaved = { viewModel.toggleSaved(it) }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    documentList: LazyPagingItems<DocumentData>,
    isLoggedIn: Boolean,
    searchKeyword: String,
    placeholder: String,
    onLoginButtonClick: () -> Unit,
    onTextChanged: (String) -> Unit,
    onSearchButtonClick: () -> Unit,
    onClickBlock: (DocumentData) -> Unit,
    onImageClick: (DocumentData) -> Unit,
    onToggleSaved: (DocumentData) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isShowDialog by remember { mutableStateOf(false) }

    if (isShowDialog) {
        SimpleDialog(
            message = "로그인이 필요합니다.",
            positiveButtonText = "카카오 로그인",
            onPositiveClick = {
                isShowDialog = false
                onLoginButtonClick.invoke()
            },
            negativeButtonText = "취소",
            onNegativeClick = {
                isShowDialog = false
            },
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchKeyword,
                onValueChange = { newText ->
                    onTextChanged.invoke(newText)
                },
                placeholder = {
                    Text(text = placeholder)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        onSearchButtonClick.invoke()
                    }
                ),
            )
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(
                    count = documentList.itemCount
                ) { index ->
                    val item = documentList[index]

                    ImageItemView(
                        imageUrl = item?.thumbnailUrl.orEmpty(),
                        isSaved = item?.isSaved ?: false,
                        showBlockButton = true,
                        showSaveButton = true,
                        onImageClick = {
                            item?.let { onImageClick.invoke(it) }
                        },
                        onClickBlock = {
                            item?.let {
                                if(!isLoggedIn) {
                                    isShowDialog = true
                                } else {
                                    onClickBlock.invoke(it)
                                }
                            }
                        },
                        onClickSave = {
                            item?.let {
                                if(!isLoggedIn) {
                                    isShowDialog = true
                                } else {
                                    onToggleSaved.invoke(it)
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

private fun launchImageViewerActivity(context: Context, item: DocumentData) {
    val newIntent = ImageViewerActivity.newIntent(context, item)
    context.startActivity(newIntent)
}

private class PreviewDocumentDataProvider : PreviewParameterProvider<List<DocumentData>> {

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
fun PreviewSearchScreen(
    @PreviewParameter(PreviewDocumentDataProvider::class) items: List<DocumentData>
) {
    val pagingItem = flowOf(PagingData.from(items)).collectAsLazyPagingItems()

    SearchScreen(
        documentList = pagingItem,
        isLoggedIn = false,
        searchKeyword = "검색어",
        placeholder = "검색어를 입력해주세요.",
        onLoginButtonClick = { },
        onTextChanged = { },
        onSearchButtonClick = { },
        onClickBlock = { },
        onImageClick = { },
        onToggleSaved = { }

    )
}