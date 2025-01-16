package jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun EditMotionDialog(
    onConfirm: (String, Int) -> Unit,
    onDismiss: () -> Unit,
    motions: List<String> = listOf("Running", "Swimming", "Cycling", "Yoga"),
    initialMotion: String = "",
    initialTime: Int = 0
) {
    var selectedMotion by remember { mutableStateOf(initialMotion) }
    var selectedTime by remember { mutableStateOf(initialTime.toString()) }
    var showMotionDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "運動記録を編集") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // 運動を選択
                Box {
                    OutlinedTextField(
                        value = if (selectedMotion.isEmpty()) "運動を選択" else selectedMotion,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showMotionDropdown = !showMotionDropdown },
                    )
                    IconButton(
                        onClick = { showMotionDropdown = !showMotionDropdown },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select Motion")
                    }
                    DropdownMenu(
                        expanded = showMotionDropdown,
                        onDismissRequest = { showMotionDropdown = false }
                    ) {
                        motions.forEach { motion ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedMotion = motion
                                    showMotionDropdown = false
                                },
                                text = { Text(motion) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = selectedTime,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                selectedTime = newValue
                            }
                        },
                        label = { Text("時間を入力") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                            text = "分",
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 15.sp
                        )

                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (selectedMotion.isNotEmpty() && selectedTime.isNotEmpty()) {
                    onConfirm(selectedMotion, selectedTime.toInt())
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
private fun EditMotionDialogPreview() {
    EditMotionDialog(
        onConfirm = { motion, time ->
            println("運動: $motion, 時間: $time 分")
        },
        onDismiss = { println("キャンセルされました") },
        initialMotion = "Running",
        initialTime = 30
    )
}