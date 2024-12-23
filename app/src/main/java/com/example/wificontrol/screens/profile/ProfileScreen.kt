package com.example.wificontrol.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.compose.primaryLight
import com.example.domain.entities.ProfileInfo
import com.example.wificontrol.R
import com.example.wificontrol.components.Switch
import com.example.wificontrol.navigation.Graph
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProfileScreenViewModel = koinViewModel()
) {
    val profile by viewModel.profile.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(18.dp))
                .fillMaxWidth()
                .background(color = Color.Gray.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = profile?.photo,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .size(48.dp)
                        .clip(shape = RoundedCornerShape(18.dp))
                )
                Text(text = "${profile?.firstName} ${profile?.lastName}")
                Box(modifier = Modifier
                    .clip(shape = RoundedCornerShape(18.dp))
                    .background(primaryLight)
                    .clickable { navController.navigate(Graph.AuthScreen.route) }
                    .padding(all = 10.dp)) {
                    Text(
                        text = stringResource(id = R.string.sign_in),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.dark_theme),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch()
        }
    }
}