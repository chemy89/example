package kr.jeongsejong.core.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SimpleDialog(
    message: String,
    positiveButtonText: String = "확인",
    onPositiveClick: () -> Unit = { },
    negativeButtonText: String = "취소",
    onNegativeClick: () -> Unit = { },
    onDismiss: () -> Unit = { },
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 20.dp
                ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = message)

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onPositiveClick.invoke() }
                    ) {
                        Text(text = positiveButtonText)
                    }

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onNegativeClick.invoke() }
                    ) {
                        Text(text = negativeButtonText)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewSimpleDialog() {
    SimpleDialog(
        message = "다이얼로그"
    )
}