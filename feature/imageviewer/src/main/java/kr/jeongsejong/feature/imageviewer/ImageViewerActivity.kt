package kr.jeongsejong.feature.imageviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kr.jeongsejong.core.common.ktx.getParcelableData
import kr.jeongsejong.core.data.vo.DocumentData

@AndroidEntryPoint
class ImageViewerActivity : ComponentActivity() {

    private val documentItem by lazy { intent.getParcelableData<DocumentData>(keyItem) ?: DocumentData.empty }

    private val viewModel by viewModels<ImageViewerViewModel>(extrasProducer = {
        defaultViewModelCreationExtras.withCreationCallback<ImageViewerViewModel.ViewModelFactory> {
            it.create(documentItem)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ImageViewerRoute(
                viewModel = viewModel
            )
        }
    }

    companion object {
        private const val keyItem = "item"

        fun newIntent(context: Context, documentItem: DocumentData): Intent {
            return Intent(context, ImageViewerActivity::class.java)
                .putExtra(keyItem, documentItem)
        }
    }

}