package com.ishanvohra.powrarmor.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ishanvohra.powrarmor.R
import com.ishanvohra.powrarmor.models.ArmorResponseItem
import com.ishanvohra.powrarmor.viewModels.MainViewModel

class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainComponent()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainComponent(viewModel: MainViewModel = MainViewModel()) {
        viewModel.getArmorPieces()
        Surface(
            color = colorResource(R.color.beige)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                var searchText by remember {
                    mutableStateOf(TextFieldValue(""))
                }
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        if(searchText.text.isNotBlank()) {
                            viewModel.filterList(searchText.text)
                        }
                        else{
                            viewModel.getArmorPieces()
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.search_hint),
                            color = colorResource(id = R.color.white)
                        )
                    },
                    modifier = Modifier
                        .padding(
                            top = 5.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 5.dp
                        )
                        .fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector
                                .vectorResource(
                                    id = R.drawable.ic_baseline_search_24
                                ),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp),
                        )
                    },
                )
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp
                            )
                        )
                        .background(color = Color(0xFFFFFFFF))
                        .fillMaxSize()
                ){
                    ArmorPieces(viewModel.armorUIState.collectAsState().value)
                }
            }
        }
    }

    @Composable
    private fun ArmorPieces(value: MainViewModel.ArmorUIState) {
        when(value){
            MainViewModel.ArmorUIState.ErrorState -> ErrorState()
            MainViewModel.ArmorUIState.LoadingState -> LoadingState()
            is MainViewModel.ArmorUIState.SuccessState -> {
                SuccessState(value.response)
            }
        }
    }

    @Composable
    fun SuccessState(response: List<ArmorResponseItem>) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier
                .testTag("SuccessState")
        ){
            items(response.size){ index ->
                ListItem(item = response[index])
            }
        }
    }

    @Composable
    fun ListItem(
        item: ArmorResponseItem
    ){
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(colorResource(id = R.color.beige_light))
                    .width(200.dp)
                    .padding(10.dp)
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.assets?.imageFemale)
                        .build()
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.beige))
                        .height(150.dp)
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Text(
                    text = item.name,
                    color = colorResource(id = R.color.peach),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = stringResource(
                        id = R.string.base_defense,
                        item.defense.base
                    ),
                    color = colorResource(id = R.color.peach),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = stringResource(
                        R.string.decoration_slots,
                        item.slots.size
                    ),
                    color = colorResource(id = R.color.peach),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }

    @Composable
    fun ErrorState(){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .testTag("ErrorState")
        ) {
            Image(
                imageVector = ImageVector
                    .vectorResource(
                        id = R.drawable.ic_baseline_signal_cellular_connected_no_internet_4_bar_24
                    ),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
            )
            Text(
                text = stringResource(id = R.string.network_error_message),
                color = colorResource(id = R.color.beige),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(20.dp)
            )
        }
    }

    @Composable
    fun LoadingState(){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .testTag("LoadingState")
        ){
            CircularProgressIndicator(
                color = colorResource(id = R.color.beige),
                strokeWidth = 7.dp,
                modifier = Modifier
                    .size(64.dp)
            )
        }
    }
}