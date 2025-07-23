package com.example.personalproject.presentation.screens.dealer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.personalproject.R
import com.example.personalproject.data.remotedata.car.CarBrandResponse
import com.example.personalproject.data.remotedata.car.CarModelResponse
import com.example.personalproject.data.remotedata.dealer.AlloyRequest
import com.example.personalproject.data.remotedata.dealer.AlloyResponse
import com.example.personalproject.domain.model.User
import com.example.personalproject.presentation.UiState
import com.example.personalproject.presentation.viewmodels.AuthViewModel
import com.example.personalproject.presentation.viewmodels.DealerViewModel



@Composable
fun DealerHomeScreen(navController: NavController, dealerViewModel: DealerViewModel) {

    val homeState by dealerViewModel.dealerHomeState.collectAsState()
    val authViewModel :  AuthViewModel = hiltViewModel()

    var showAddAlloyDialog by remember { mutableStateOf(false) }

    val hasFetchedAlloys by dealerViewModel.hasFetchedAlloys.collectAsState()

    LaunchedEffect(hasFetchedAlloys) {
        if (!hasFetchedAlloys) {
            dealerViewModel.getAllAlloysOfDealer()
        }
    }


    LaunchedEffect(Unit) {
        dealerViewModel.getUser()
    }

    Scaffold(
        topBar = {
            DealerTopAppBar(
                dealerViewModel,
                profileImageUrl = null,
                onProfileClick = {
                    navController.navigate("dealer_details")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddAlloyDialog = true },
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 16.dp)
                    .size(72.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.add_image),
                    contentDescription = "Add Alloy",
                    contentScale = ContentScale.Crop
                )
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFDFCFB),
                                Color(0xFFE2EBF0),
                                Color(0xFFE2EBF0)
                            )
                        )
                    ),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp)
            ) {
                item {
                    SearchBar(
                        query = "",
                        onQueryChange = { }
                    )
                    Spacer(Modifier.height(6.dp))
                }

                item {
                    Text(
                        text = "Your wheels collection....",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                when (homeState) {
                    is UiState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is UiState.Success -> {
                        val alloys = (homeState as UiState.Success<List<AlloyResponse>>).data
                        items(alloys) { alloy ->
                            AlloyCard(alloy, onClick = {
                                dealerViewModel.selectedAlloy = alloy
                                dealerViewModel.getAlloyByIdOfDealer(alloy.id)
                                //println(dealerViewModel.selectedAlloy)
                                navController.navigate("alloy_details")
                            })
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    is UiState.Error -> {
                        val message = (homeState as UiState.Error).message
                        item {
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text("Error: $message", color = Color.Red)
                            }
                        }
                    }

                    is UiState.Idle -> {} // No UI
                }

            }

        }

    )

    if (showAddAlloyDialog) {
        AddAlloyDialog(
            onDismissRequest = {
                showAddAlloyDialog = false
                               },
            onAddAlloy = {
                showAddAlloyDialog = false
                dealerViewModel.getAllAlloysOfDealer()
            },
            dealerViewModel = dealerViewModel // Pass the ViewModel to the dialog
        )
    }

}

@Composable
fun DealerTopAppBar(
    dealerViewModel: DealerViewModel,
    profileImageUrl: String?,
    onProfileClick: () -> Unit
) {

    val dealerDetailsState by dealerViewModel.dealerDetailsState.collectAsState()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
            .background(Color(0xFFFDFCFB))
    ){
        when(dealerDetailsState){
            is UiState.Success -> {
                val user = (dealerDetailsState as UiState.Success<User>).data
                Text(
                    text = "Hello, ${user.name}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                if (user.dealerProfile.shopImage != null) {
                    AsyncImage(
                        model = user.dealerProfile.shopImage,
                        contentDescription = "Shop Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .clickable(
                                onClick = { onProfileClick() }
                            )
                            .padding(end = 4.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Placeholder Shop Image",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .clickable(
                                onClick = { onProfileClick() }
                            )
                            .padding(end = 4.dp)
                    )
                }
            }
            is UiState.Loading -> {
                Text(
                    text = "Hello, ...",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                CircularProgressIndicator()
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Placeholder Shop Image",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .clickable(
                                onClick = { onProfileClick() }
                            )
                            .padding(end = 4.dp)
                    )

            }
            is UiState.Error -> {
                Text(
                    text = "Hello, ...",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(text = "Error: ${(dealerDetailsState as UiState.Error).message}", color = MaterialTheme.colorScheme.error)
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Placeholder Shop Image",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .clickable(
                            onClick = { onProfileClick() }
                        )
                        .padding(end = 4.dp)
                )
            }
            is UiState.Idle -> {}
        }

    }

}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("    Search alloys...") },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(
                elevation = 16.dp,
                shape = CircleShape,
                ambientColor = Color.Black,
                spotColor = Color.Black
            )
            .background(Color.White, shape = RoundedCornerShape(12.dp)),
        shape = CircleShape
    )
}

@Composable
fun AlloyCard(alloy: AlloyResponse, onClick : ()->Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
        ) {
            AsyncImage(
                model = alloy.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(24.dp))

            Column(
                modifier = Modifier
            ) {

                alloy.name?.let { Text(it, fontWeight = FontWeight.Bold) }

                Row() {
                    Column() {
                        Row() {
                            Text(
                                "Size: ",
                                fontWeight = FontWeight.Normal,
                                color = Color.DarkGray
                            )
                            Text(
                                "${alloy.size}",
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        }
                        Row() {
                            Text(
                                "Offset: ",
                                fontWeight = FontWeight.Normal,
                                color = Color.DarkGray
                            )
                            Text(
                                "${alloy.offset}",
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        }
                    }


                    Spacer(Modifier.padding(4.dp))

                    Column() {
                        Row() {
                            Text(
                                "Width: ",
                                fontWeight = FontWeight.Normal,
                                color = Color.DarkGray
                            )
                            Text(
                                "${alloy.width}J",
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        }
                        Row() {
                            Text(
                                "PCD: ",
                                fontWeight = FontWeight.Normal,
                                color = Color.DarkGray
                            )
                            Text(
                                "${alloy.pcd}",
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        }
                    }
                }

                Text("₹${alloy.price}", color = Color(0xFF2E7D32))
            }
        }
    }
}


@Composable
fun AddAlloyDialog(
    onDismissRequest: () -> Unit,
    onAddAlloy: () -> Unit,
    dealerViewModel: DealerViewModel // Pass the ViewModel
) {
    var alloyName by remember { mutableStateOf("") }
    var alloySize by remember { mutableStateOf("") }
    var alloyWidth by remember { mutableStateOf("") }
    var alloyOffset by remember { mutableStateOf("") }
    var alloyPCD by remember { mutableStateOf("") }
    var alloyPrice by remember { mutableStateOf("") }
    val compatibleModels = remember { mutableStateListOf<CarModelResponse>() }

    val addAlloyState by dealerViewModel.addAlloyState.collectAsState()
    println(addAlloyState.toString())

    var showBrandSelectionDialog by remember { mutableStateOf(false) }

    val carBrandState by dealerViewModel.carBrandState.collectAsState()
    val carModelState by dealerViewModel.carModelState.collectAsState()

    LaunchedEffect(Unit) {
        dealerViewModel.getCarBrands() // Fetch brands when dialog opens
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()), // Make content scrollable
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when(addAlloyState){
                    is UiState.Loading -> {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Idle -> {
                        Text(
                            text = "Add New Alloy Wheel",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = alloyName,
                            onValueChange = { alloyName = it },
                            label = { Text("Alloy Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                        OutlinedTextField(
                            value = alloySize,
                            onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*\$"))) alloySize = it },
                            label = { Text("Size (e.g., 17.0)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = alloyWidth,
                            onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*\$"))) alloyWidth = it },
                            label = { Text("Width (e.g., 7.5)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = alloyOffset,
                            onValueChange = { if (it.matches(Regex("^\\d*\$"))) alloyOffset = it },
                            label = { Text("Offset (e.g., 35)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = alloyPCD,
                            onValueChange = { alloyPCD = it },
                            label = { Text("PCD (e.g., 5x114.3)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = alloyPrice,
                            onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*\$"))) alloyPrice = it },
                            label = { Text("Price (e.g., 15000.0)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Compatible Models Selection
                        Text(
                            text = "Compatible Models:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        if (compatibleModels.isNotEmpty()) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                compatibleModels.forEach { model ->
                                    InputChip(
                                        selected = false,
                                        onClick = { compatibleModels.remove(model) },
                                        label = { model.modelName?.let { Text(it) } },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Remove model",
                                                modifier = Modifier.size(18.dp)
                                            )
                                        },
                                        colors = InputChipDefaults.inputChipColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )

                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Button(
                            onClick = { showBrandSelectionDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Select Car Brands & Models")
                        }
                    }

                    is UiState.Error -> {
                        val message = (addAlloyState as UiState.Error).message
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text("Error: $message", color = Color.Red)
                            }

                    }

                    is UiState.Success<String> -> {
                        LaunchedEffect(Unit) {
                            onAddAlloy()
                            dealerViewModel.resetHasFetchedAlloys()
                            dealerViewModel.resetAddAlloyState()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            val name = alloyName.trim()
                            val size = alloySize.toDoubleOrNull()
                            val width = alloyWidth.toDoubleOrNull()
                            val offset = alloyOffset.toIntOrNull()
                            val pcd = alloyPCD.trim()
                            val price = alloyPrice.toDoubleOrNull()
                            val carList = mutableListOf<String>()

                            if (name.isNotEmpty() && size != null && width != null && offset != null && pcd.isNotEmpty() && price != null && compatibleModels.isNotEmpty()) {
                                for(models in compatibleModels){
                                    carList.add(models.id)
                                }
                                val alloyRequest = AlloyRequest(name, size, width, offset, pcd, price, carList)
                                dealerViewModel.addAlloy(alloyRequest)
                            } else {
                                // Show an error or toast
                                println("Please fill all fields and select compatible models.")
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = alloyName.isNotBlank() && alloySize.isNotBlank() && alloyWidth.isNotBlank() && alloyOffset.isNotBlank() && alloyPCD.isNotBlank() && alloyPrice.isNotBlank() && compatibleModels.isNotEmpty()
                    ) {
                        Text("Add Alloy")
                    }
                }
            }
        }
    }

    if (showBrandSelectionDialog) {
        BrandModelSelectionDialog(
            onDismissRequest = { showBrandSelectionDialog = false },
            onModelsSelected = { selectedModels ->
                compatibleModels.addAll(selectedModels)
                showBrandSelectionDialog = false
            },
            dealerViewModel = dealerViewModel,
            selectedCompatibleModels = compatibleModels.toList()
        )
    }
}

@Composable
fun BrandModelSelectionDialog(
    onDismissRequest: () -> Unit,
    onModelsSelected: (List<CarModelResponse>) -> Unit,
    dealerViewModel: DealerViewModel,
    selectedCompatibleModels: List<CarModelResponse>
) {
    var selectedBrand by remember { mutableStateOf<CarBrandResponse?>(null) }
    val currentlySelectedModels = remember { mutableStateListOf<CarModelResponse>() }



    val carBrandState by dealerViewModel.carBrandState.collectAsState()
    val carModelState by dealerViewModel.carModelState.collectAsState()

    LaunchedEffect(selectedBrand) {
        selectedBrand?.id?.let { brandId ->
            dealerViewModel.getCarModelsByBrand(brandId)
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select Compatible Brands & Models",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Display selected models globally across brands
//                if (currentlySelectedModels.isNotEmpty()) {
//                    Text(
//                        text = "Selected Models:",
//                        style = MaterialTheme.typography.titleMedium,
//                        modifier = Modifier.align(Alignment.Start)
//                    )
//                    FlowRow(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(8.dp),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        currentlySelectedModels.forEach { model ->
//                            InputChip(
//                                selected = false,
//                                onClick = {
//                                    currentlySelectedModels.remove(model)
//                                },
//                                label = { Text(model.modelName) },
//                                leadingIcon = {
//                                    Icon(
//                                        Icons.Default.Close,
//                                        contentDescription = "Remove model",
//                                        modifier = Modifier.size(18.dp)
//                                    )
//                                },
//                                colors = InputChipDefaults.inputChipColors(
//                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                                    labelColor = MaterialTheme.colorScheme.onTertiaryContainer
//                                )
//                            )
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                }


                // Brand Selection
                Text(
                    text = "Select Brand:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )

                when (carBrandState) {
                    is UiState.Loading -> CircularProgressIndicator()
                    is UiState.Success -> {
                        val brands = (carBrandState as UiState.Success<List<CarBrandResponse>>).data
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 200.dp) // Limit height for brand list
                        ) {
                            items(brands) { brand ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { selectedBrand = brand },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (brand == selectedBrand) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Text(
                                        text = brand.brandName,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                    is UiState.Error -> Text("Error loading brands: ${(carBrandState as UiState.Error).message}", color = Color.Red)
                    else -> Text("No brands available.")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Model Selection
                selectedBrand?.let { brand ->
                    Text(
                        text = "Select Models for ${brand.brandName}:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    when (carModelState) {
                        is UiState.Loading -> CircularProgressIndicator()
                        is UiState.Success -> {
                            val models = (carModelState as UiState.Success<List<CarModelResponse>>).data
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 250.dp) // Limit height for model list
                            ) {
                                items(models) { model ->
                                    val isSelected = currentlySelectedModels.contains(model)
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable {
                                                if (isSelected) {
                                                    currentlySelectedModels.remove(model)
                                                } else {
                                                    currentlySelectedModels.add(model)
                                                }
                                            },
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            model.modelName?.let { Text(text = it, modifier = Modifier.weight(1f)) }
                                            Checkbox(
                                                checked = isSelected,
                                                onCheckedChange = {
                                                    if (it) {
                                                        currentlySelectedModels.add(model)
                                                    } else {
                                                        currentlySelectedModels.remove(model)
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        is UiState.Error -> Text("Error loading models: ${(carModelState as UiState.Error).message}", color = Color.Red)
                        else -> Text("No models available for ${brand.brandName}.")
                    }
                } ?: Text("Please select a brand to view models.")

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            onModelsSelected(
                                currentlySelectedModels.map { it }
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}

