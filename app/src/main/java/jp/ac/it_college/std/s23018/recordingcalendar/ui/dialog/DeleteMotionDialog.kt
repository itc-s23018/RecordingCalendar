package jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import jp.ac.it_college.std.s23018.recordingcalendar.R

@Composable
fun DeleteMotionDialog(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.confirmation))
        },
        text = {
            Text(stringResource(R.string.check_delete))
        },
        confirmButton = {
            TextButton( onClick = {
                onConfirm()
            }) {
                Text(stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss
            ) {
                Text(stringResource(id = R.string.cancel))
            }
        })
}

@Preview
@Composable
private fun DeleteMotionDialogPreview() {
    var showDialog by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("") }
    Column {
        Button(onClick = { showDialog = true }) {
            Text(text = "Show Daialog")
        }
        if (showDialog) {
            DeleteMotionDialog(onConfirm = {
                showDialog = false
                result = "confirmButtonが押されました"
            }, onDismiss = {
                showDialog = false
                result = "キャンセルされました"
            })
        }
        Text(text = result)
    }
}
