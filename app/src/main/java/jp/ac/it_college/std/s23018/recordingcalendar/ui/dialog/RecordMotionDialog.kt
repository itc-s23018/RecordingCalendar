package jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.it_college.std.s23018.recordingcalendar.R

@Composable
fun RecordMotionDialog(
    onConfirm: (String, Int) -> Unit,
    onDismiss: () -> Unit,
    motions: List<String> = listOf("running", "swimming", "cycling", "yoga")
) {
    // システム言語によって表示を変更
    val motionNameMap = mapOf(
        "running" to stringResource(id = R.string.running),
        "swimming" to stringResource(id = R.string.swimming),
        "cycling" to stringResource(id = R.string.cycling),
        "yoga" to stringResource(id = R.string.yoga)
    )

    var selectedMotion by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var showMotionDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.record_motion)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // 運動を選択
                Box {
                    OutlinedTextField(
                        value = if (selectedMotion.isEmpty()) stringResource(id = R.string.select_motion) else motionNameMap[selectedMotion] ?: selectedMotion,
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
                                text = { Text(motionNameMap[motion] ?: motion) }
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
                        label = { Text(stringResource(id = R.string.input_time)) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = stringResource(id = R.string.time),
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 15.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (selectedMotion.isNotEmpty() && selectedTime.isNotEmpty()) {
                    // 保存時には英語名を使用
                    onConfirm(selectedMotion, selectedTime.toInt())
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


@Preview(showBackground = true)
@Composable
private fun RecordMotionDialogPreview() {
    RecordMotionDialog(
        onConfirm = { motion, time ->
            println("運動: $motion, 時間: $time 分")
        },
        onDismiss = { println("キャンセルされました") }
    )
}
