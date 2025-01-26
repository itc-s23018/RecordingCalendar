package jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun InputUserDialog(
    onConfirm: (String, Float,Float) -> Unit,
    onDismiss: () -> Unit
    ) {


    var inputName by remember { mutableStateOf("") }
    var inputWeight by remember { mutableStateOf("") }
    var inputTargetWeight by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "ユーザー情報を入力") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                OutlinedTextField(
                    value = inputName,
                    onValueChange = {inputName = it},
                    label = {Text("名前を入力")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))


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
                        label = { Text("現在の体重を入力") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "Kg",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputTargetWeight,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                                inputTargetWeight = newValue
                            }
                        },
                        label = { Text("目標体重を入力") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "Kg",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 20.sp
                    )
                }

            }
        },
        confirmButton = {
            TextButton(onClick = {
                if(inputName.isNotEmpty() && inputWeight.isNotEmpty() && inputTargetWeight.isNotEmpty()){
                    onConfirm(inputName, inputWeight.toFloat(), inputTargetWeight.toFloat())
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
private fun InputUserDialogPreview() {
    InputUserDialog(
        onConfirm = {name, currentWeight, targetWeight ->
            println("名前: $name, 現在の体重: $currentWeight, 目標体重: $targetWeight")
        },
        onDismiss = { println("キャンセルされました") }
    )
}