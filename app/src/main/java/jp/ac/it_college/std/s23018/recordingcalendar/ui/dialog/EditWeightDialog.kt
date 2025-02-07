package jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.it_college.std.s23018.recordingcalendar.R

@Composable
fun EditWeightDialog(
    modifier: Modifier = Modifier,
    onConfirm: (Float) -> Unit,
    onDismiss: () -> Unit,
    initialWeight: Float = 0f
    ) {
        var selectedWeight by remember { mutableStateOf(initialWeight.toString()) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(id = R.string.edit_weight)) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = selectedWeight,
                            onValueChange = { newValue ->
                               if(newValue.isEmpty() || newValue.toFloatOrNull() != null){
                                   selectedWeight = newValue
                               }
                            },
                            label = { Text(stringResource(id = R.string.input_weight)) },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "Kg",
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 15.sp
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val weight = selectedWeight.toFloatOrNull()
                    if(weight != null){
                        onConfirm(weight)
                    }
                }) {
                    Text(stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
}

@Preview
@Composable
private fun EditWeightDialogPreview() {
    EditWeightDialog(
        onConfirm = {weight ->
            println("体重: $weight Kg")
        },
        onDismiss = { println("キャンセルされました") },
        initialWeight = 65.5f
    )
}