package jp.ac.it_college.std.s23018.recordingcalendar.ui.record

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import jp.ac.it_college.std.s23018.recordingcalendar.R
import jp.ac.it_college.std.s23018.recordingcalendar.RecordingCalendarApplication
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.MotionEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.StepEntity
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.WeightEntity
import jp.ac.it_college.std.s23018.recordingcalendar.ui.RecordingCalendarAppBar
import jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog.DeleteMotionDialog
import jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog.EditMotionDialog
import jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog.EditWeightDialog
import jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog.RecordMotionDialog
import jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog.RecordWeightDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    navController: NavController,
    selectedYear: String,
    selectedMonth: String,
    selectedDay: String,
    modifier: Modifier = Modifier
) {

    var selectedDate by remember {
        mutableStateOf(
            LocalDate.of(
                selectedYear.toInt(),
                selectedMonth.toInt(),
                selectedDay.toInt()
            )
        )
    }

    var weightRecord by remember {
        mutableStateOf<WeightEntity?>(null)
    }

    var motionRecord by remember {
        mutableStateOf<List<MotionEntity?>>(emptyList())
    }

    var stepRecord by remember {
        mutableStateOf<StepEntity?>(null)
    }

    val app = navController.context.applicationContext as RecordingCalendarApplication
    val db = app.container.recordRepository

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    var showAddWeightDialog by remember { mutableStateOf(false) }

    var showEditWeightDialog by remember { mutableStateOf(false) }

    var showAddMotionDialog by remember { mutableStateOf(false) }

    var showEditMotionDialog by remember { mutableStateOf(false) }

    var showDeleteMotionDialog by remember { mutableStateOf(false) }

    var weightToEdit by remember { mutableStateOf<WeightEntity?>(null) }

    var motionToEdit by remember { mutableStateOf<MotionEntity?>(null) }

    var motionToDelete by remember { mutableStateOf<MotionEntity?>(null) }

    fun refreshData() {
        coroutineScope.launch {
            val weightData = withContext(Dispatchers.IO){
                db.getWeightByDate(selectedDate.toString())
            }
            val motionData = withContext(Dispatchers.IO){
                db.getMotionsByDate(selectedDate.toString())
            }
            val stepData = withContext(Dispatchers.IO){
                db.getStepByDate(selectedDate.toString())
            }
            weightRecord = weightData
            motionRecord = motionData
            stepRecord = stepData
        }
    }

    fun handleWeightConfirm(weight:Float){
        coroutineScope.launch {
            db.insertWeight(
                WeightEntity(
                    date = selectedDate.toString(),
                    weight = weight
                )
            )
            Toast.makeText(context, "体重記録を追加しました", Toast.LENGTH_SHORT).show()
            refreshData()
        }
        showAddWeightDialog  = false
    }

    fun handleMotionConfirm(name:String, time:Int){
        coroutineScope.launch {
            db.insertMotion(
                MotionEntity(
                    date = selectedDate.toString(),
                    name = name,
                    time = time
                )
            )
            Toast.makeText(context,"運動記録を追加しました", Toast.LENGTH_SHORT).show()
            refreshData()
        }
        showAddMotionDialog = false
    }

    LaunchedEffect(selectedDate) {
        refreshData()
    }

    Scaffold(
        topBar = {
            RecordingCalendarAppBar(
                title = stringResource(id = R.string.record),
                canNavigationBack = true,
                navigateUp = { navController.navigate("tabRow") }
            )
        },
        content = {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        selectedDate = selectedDate.minusDays(1)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous Date"
                        )
                    }

                    Text(
                        text = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    IconButton(onClick = {
                        selectedDate = selectedDate.plusDays(1)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next Date"
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "歩数:",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${stepRecord?.step ?: 0}歩",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(end = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                Divider(
                    color = Color.Gray,
                    thickness = 5.dp
                )
                weightRecord?.let { weight ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "体重:",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${weight.weight}kg",
                                fontSize = 30.sp,
                                modifier = Modifier.padding(end = 10.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.End
                        ) {

                            Text(
                                text = "編集する",
                                fontSize = 15.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .clickable {
                                        weightToEdit = weight
                                        showEditWeightDialog = true
                                    }
                            )

                            if(showEditWeightDialog) {
                                EditWeightDialog(
                                    onConfirm = { weight ->
                                        coroutineScope.launch {
                                            db.updateWeight(
                                                weightToEdit!!.copy(weight = weight)
                                            )
                                            Toast.makeText(context,"体重記録を更新しました", Toast.LENGTH_SHORT).show()
                                            refreshData()
                                        }
                                        showEditWeightDialog = false
                                    },
                                    onDismiss = {showEditWeightDialog = false},
                                    initialWeight = weightToEdit!!.weight
                                )
                            }
                        }
                    }
                    Divider(
                        color = Color.Gray,
                        thickness = 5.dp
                    )
                } ?: run {
                    Text(
                        text = "体重: 記録なし",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "登録する",
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    showAddWeightDialog = true
                                }
                            }
                    )

                    if(showAddWeightDialog) {
                        RecordWeightDialog(
                            onConfirm = {weight -> handleWeightConfirm(weight)},
                            onDismiss = {showAddWeightDialog = false}
                        )
                    }
                }

                if (motionRecord.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        motionRecord.forEach { motion ->
                            motion?.let {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        """
                                            運動記録:
                                        """.trimIndent(),
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(top = 15.dp)
                                    )
                                    Text(
                                        """
                                               ${motion.name}
                                            
                                               ${motion.time}分
                                        """.trimIndent(),
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp, bottom = 10.dp),
                                        textAlign = TextAlign.End
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "編集する",
                                        fontSize = 15.sp,
                                        color = Color.Gray,
                                        modifier = Modifier
                                            .padding(start = 5.dp)
                                            .clickable {
                                                motionToEdit = motion
                                                showEditMotionDialog = true
                                            }
                                    )

                                    if(showEditMotionDialog && motionToEdit != null){
                                        EditMotionDialog(
                                            onConfirm = {name, time ->
                                                coroutineScope.launch {
                                                    db.updateMotion(
                                                        motionToEdit!!.copy(name = name, time = time)
                                                    )
                                                    Toast.makeText(context, "運動記録を更新しました", Toast.LENGTH_SHORT).show()
                                                    refreshData()
                                                }
                                                showEditMotionDialog = false
                                            },
                                            onDismiss = {showEditMotionDialog = false},
                                            initialMotion =  motionToEdit!!.name,
                                            initialTime = motionToEdit!!.time
                                        )
                                    }


                                    Text(
                                        text = "削除する",
                                        fontSize = 15.sp,
                                        color = Color.Gray,
                                        modifier = Modifier
                                            .padding(start = 5.dp)
                                            .clickable {
                                                motionToDelete = motion
                                                showDeleteMotionDialog = true
                                            }
                                    )
                                }
                                Divider(color = Color.Gray, thickness = 5.dp)
                            }
                        }
                    }

                    if(showDeleteMotionDialog && motionToDelete != null){
                        DeleteMotionDialog(
                            onConfirm = {
                                coroutineScope.launch {
                                    db.deleteMotion(motionToDelete!!)
                                    refreshData()
                                    Toast.makeText(context,"運動記録を削除しました",Toast.LENGTH_SHORT).show()

                                }
                                showDeleteMotionDialog = false
                            },
                            onDismiss = {
                                showDeleteMotionDialog = false
                            }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "追加する",
                            color = Color.Gray,
                            modifier = Modifier
                                .clickable {
                                    showAddMotionDialog = true
                                }
                        )
                    }

                    if (showAddMotionDialog) {
                        RecordMotionDialog(
                            onConfirm = {name, time -> handleMotionConfirm(name, time)},
                            onDismiss = { showAddMotionDialog = false}
                        )
                    }
                } else {
                    Text(
                        text = "運動記録: なし",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "記録する",
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    showAddMotionDialog = true
                                }
                            }
                    )

                    if(showAddMotionDialog) {
                        RecordMotionDialog(
                            onConfirm = {name, time -> handleMotionConfirm(name, time)},
                            onDismiss = { showAddMotionDialog = false}
                        )
                    }
                }

            }

        }
    )
}