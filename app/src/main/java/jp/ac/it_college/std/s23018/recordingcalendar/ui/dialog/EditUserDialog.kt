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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontSynthesis.Companion.Weight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.it_college.std.s23018.recordingcalendar.R

@Composable
fun EditUserDialog(
    onConfirm: (String, Float, Float) -> Unit,
    onDismiss: () -> Unit,
    initialName: String = "",
    initialWeight: Float = 0f,
    initialTargetWeight: Float = 0f
    ) {

    var inputName by remember { mutableStateOf(initialName) }
    var inputWeight by remember { mutableStateOf(initialWeight.toString()) }
    var inputTargetWeight by remember { mutableStateOf(initialTargetWeight.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.edit_user)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = inputName,
                    onValueChange = {inputName = it},
                    label = {Text(stringResource(id = R.string.input_name))},
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
                            if (newValue.isEmpty() || newValue.toFloatOrNull() !== null) {
                                inputWeight = newValue
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

                    Spacer(modifier = Modifier.height(16.dp))



                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        OutlinedTextField(
                            value = inputTargetWeight,
                            onValueChange = { newValue ->
                                if (newValue.isEmpty() ||newValue.toFloatOrNull() != null) {
                                    inputTargetWeight = newValue
                                }
                            },
                            label = { Text(stringResource(id = R.string.input_target)) },
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
            val weight = inputWeight.toFloatOrNull()
            val targetWeight = inputTargetWeight.toFloatOrNull()
            TextButton(onClick = {
                if (inputName.isNotEmpty() && weight != null && targetWeight != null) {
                    onConfirm(inputName, weight, targetWeight)
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
private fun EditUserDialogPreview() {
    EditUserDialog(
        onConfirm = {name, weight, targetWeight ->
            println("名前: $name, 現在の体重: $weight, 目標体重: $targetWeight")
        },
        onDismiss = { println("キャンセルされました") },
        initialName = "Seishiro",
        initialWeight = 65.0f,
        initialTargetWeight = 60.0f
    )
}