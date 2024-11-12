package com.example.client_login.ui.profile


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.client_login.R


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    logout: () -> Unit,
    modifier: Modifier = Modifier
){

    val ui by viewModel.profileScreenState.collectAsState()
    viewModel.fetchUserInfo()
    val user = ui.userInfo
    Column(
        modifier
            .fillMaxSize()
            .safeContentPadding()
    ) {
        Text(
            text = "Profile Page" ,
            style = TextStyle(
                color = Color.Blue ,
                fontWeight = FontWeight.Bold ,
                fontSize = 20.sp ,
                textAlign = TextAlign.Center
            ) ,
            modifier = Modifier
                .fillMaxWidth()
        )
        Box(
            contentAlignment = Alignment.Center ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.profile) ,
                contentDescription = "profile" ,
                contentScale = ContentScale.Crop ,
                alignment = Alignment.Center ,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(100))
            )
        }

        Details("ID" , user?.payload?.user?._id.orEmpty())

        Details("First Name" , user?.payload?.user?.firstName.orEmpty())

        Details("Last Name" , user?.payload?.user?.lastName.orEmpty())

        Details("Phone Number " , user?.payload?.user?.phone.orEmpty())

        Details("Country " , user?.payload?.user?.country.orEmpty())

        Details("Phone Number " , user?.payload?.user?.phone.orEmpty())

        Button(onClick = {
            logout()
            viewModel.Logout()
        },modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
            Text("Log out")
        }
    }

}
@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun ProfileScreenPreview(){
    Box (Modifier.fillMaxSize().background(Color.White)){
        ProfileScreen(
            logout = {}
        )
    }
}


