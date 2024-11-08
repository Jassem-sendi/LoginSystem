package com.example.client_login.ui.profile

import android.telecom.Call.Details
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Details(
    detail:String,
    title:String
){
    Column(
        horizontalAlignment = Alignment.Start ,
        verticalArrangement = Arrangement.SpaceBetween ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = detail ,
            style = TextStyle(
                color = Color.Gray ,
                fontWeight = FontWeight.Bold ,
                fontSize = 20.sp ,
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = title ,
            style = TextStyle(
                color = Color.Black ,
                fontWeight = FontWeight.Bold ,
                fontSize = 20.sp ,
                textAlign = TextAlign.Center
            ) ,
            modifier = Modifier.padding(start = 10.dp, bottom = 5.dp)
        )

        HorizontalDivider(
            thickness = 2.dp,
            color = Color.Black
        )
    }
}