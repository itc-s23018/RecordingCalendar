package jp.ac.it_college.std.s23018.recordingcalendar.ui.user

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import jp.ac.it_college.std.s23018.recordingcalendar.R
import jp.ac.it_college.std.s23018.recordingcalendar.RecordingCalendarApplication
import jp.ac.it_college.std.s23018.recordingcalendar.data.entity.UserEntity
import jp.ac.it_college.std.s23018.recordingcalendar.ui.dialog.EditUserDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    navController: NavController
){
    var userInformation by remember {
        mutableStateOf<UserEntity?>(null)
    }

    val app = navController.context.applicationContext as RecordingCalendarApplication
    val db = app.container.userRepository

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val update_message = stringResource(id = R.string.update_user)

    var showEditUserDialog by remember { mutableStateOf(false) }

    var userToEdit by remember { mutableStateOf<UserEntity?>(null) }



    fun refreshData() {
        coroutineScope.launch {
            val updatedUser = withContext(Dispatchers.IO){
                db.getUser()
            }
            userInformation = updatedUser
        }
    }

    LaunchedEffect(Unit) {
        refreshData()
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = stringResource(id = R.string.user),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 100.dp)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    userInformation?.let { user ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = buildAnnotatedString {
                                        append(stringResource(R.string.name) + ":\n\n")
                                        append(stringResource(R.string.weight) + ":\n\n")
                                        append(stringResource(R.string.target) + ":\n\n")
                                    },
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 15.dp)
                                )
                                Text(
                                    """
                                        ${user.name}
                                        
                                        ${user.weight}Kg
                                        
                                        ${user.targetWeight}Kg
                                    """.trimIndent(),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, bottom = 10.dp),
                                    textAlign = TextAlign.End
                                )
                            }
                            Divider(color = Color.Gray, thickness = 5.dp)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    stringResource(id = R.string.edit),
                                    fontSize = 15.sp,
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .padding(start = 5.dp)
                                        .clickable {
                                            userToEdit = user
                                            showEditUserDialog = true
                                        }
                                )

                                if (showEditUserDialog && userToEdit != null) {
                                    EditUserDialog(
                                        onConfirm = { name, weight, targetWeight ->
                                            coroutineScope.launch {
                                                db.updateUser(
                                                    userToEdit!!.copy(
                                                        name = name,
                                                        weight = weight,
                                                        targetWeight = targetWeight
                                                    )
                                                )
                                                Toast.makeText(
                                                    context,
                                                    update_message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                refreshData()
                                            }
                                            showEditUserDialog = false
                                        },
                                        onDismiss = { showEditUserDialog = false },
                                        initialName = userToEdit!!.name,
                                        initialWeight = userToEdit!!.weight,
                                        initialTargetWeight = userToEdit!!.targetWeight,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

