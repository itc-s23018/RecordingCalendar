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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecordWeightDialog(
    modifier: Modifier = Modifier,
    onConfirm: (Float) -> Unit,
    onDismiss: () -> Unit,
    ) {

    var inputWeight by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "体重記録を入力") },
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
                       value = inputWeight,
                       onValueChange = { newValue ->
                           if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                               inputWeight = newValue
                           }
                       },
                       label = { Text("体重を入力") },
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
                if(inputWeight.isNotEmpty()){
                  onConfirm(inputWeight.toFloat())
                }
            }) {
                Text(text = "保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "キャンセル")
            }
        }
    )
}

@Preview
@Composable
private fun RecordWeightDialogPreview() {
    RecordWeightDialog(
        onConfirm = { weight ->
            println("体重: $weight Kg")
        },
        onDismiss = { println("キャンセルされました") }
    )
}