package jp.ac.it_college.std.s23018.recordingcalendar.ui.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GraphTab(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
    ) {

    val tabTitles = listOf("週", "月", "年")
    val backgroundColor = Color(0xFFD3D3D3)
    val selectedColor = Color.White
    val unselectedTabColor = Color.Black
    val selectedTextColor = Color.Black


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center
    ){
        tabTitles.forEachIndexed{ index, title ->
            val isSelected = index == selectedIndex

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) selectedColor else Color.Transparent)
                    .clickable { onTabSelected(index) }
                    .padding(horizontal = 40.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = title,
                    color = if(isSelected) selectedTextColor else unselectedTabColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GraphTabPreview() {
    GraphTab(
        selectedIndex = 1,
        onTabSelected = {}
    )
}