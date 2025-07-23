package com.example.personalproject.presentation.screens.dealer

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.personalproject.data.remotedata.car.CarBrandResponse
import com.example.personalproject.data.remotedata.car.CarModelResponse
import com.example.personalproject.data.remotedata.dealer.AlloyRequest
import com.example.personalproject.data.remotedata.dealer.AlloyResponse
import com.example.personalproject.presentation.UiState
import com.example.personalproject.presentation.viewmodels.DealerViewModel
import kotlinx.coroutines.launch
import java.io.File

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlloyDetailsScreen(
    navController: NavController,
    dealerViewModel: DealerViewModel
) {
    //val alloy = dealerViewModel.selectedAlloy

    val editAlloyState by dealerViewModel.editAlloyState.collectAsState()
    val alloyDetailsState by dealerViewModel.alloyDetailsState.collectAsState()
    val alloyImageState by dealerViewModel.alloyImageState.collectAsState()

    var showEditAlloyDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val photoFile = remember {
        File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "IMG_${System.currentTimeMillis()}.jpg"
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && imageUri != null) {
            dealerViewModel.uploadCapturedImage(context, imageUri!!,
                dealerViewModel.selectedAlloy?.id ?: ""
            )
        }
    }

    LaunchedEffect(editAlloyState) {
        when (editAlloyState) {
            is UiState.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Alloy updated successfully!", withDismissAction = true)
                }
                // After successful edit, refresh the alloy list in home screen
                dealerViewModel.resetHasFetchedAlloys()
                // Also, if you want the details screen to update, you might need to re-fetch selectedAlloy
                // or update it directly if the API returns the updated object.
                // For now, let's just pop back if you want to force re-render from home.
                // Or you can update `dealerViewModel.selectedAlloy = newAlloyResponse` if you get it back.
                dealerViewModel.resetEditAlloyState() // Reset state to Idle
                dealerViewModel.getAlloyByIdOfDealer((alloyDetailsState as UiState.Success<AlloyResponse>).data.id)
            }
            is UiState.Error -> {
                scope.launch {
                    val message = (editAlloyState as UiState.Error).message
                    snackbarHostState.showSnackbar("Error updating alloy: $message", withDismissAction = true)
                }
                dealerViewModel.resetEditAlloyState() // Reset state to Idle
            }
            else-> {
            } // Do nothing for Idle or Loading
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alloy Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFFFDFCFB)),
                modifier = Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp, bottom = 16.dp)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFDFCFB),
                            Color(0xFFE2EBF0),
                            Color(0xFFE2EBF0)
                        )
                    )
                )

        ) {
            when(alloyDetailsState){
                is UiState.Idle -> {

                }

                is UiState.Success<*> -> {
                    //dealerViewModel.resetHasFetchedAlloys()
                    val alloy = (alloyDetailsState as UiState.Success<AlloyResponse>).data
                    // Alloy Image
                    alloy.imageUrl.let {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            AsyncImage(
                                model = it,
                                contentDescription = alloy!!.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Alloy Name & Price
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = alloy?.name ?: "Unnamed Alloy",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "₹${alloy?.price}",
                                style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary),
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = { showEditAlloyDialog = true },
                            modifier = Modifier
                                .height(50.dp)
                                .weight(1f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Edit", color = Color.White)
                        }

                        Spacer(Modifier.padding(3.dp))

                        when(alloyImageState){
                            is UiState.Idle -> {
                                OutlinedButton(
                                    onClick = {
                                        imageUri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            photoFile
                                        )
                                        cameraLauncher.launch(imageUri!!)
                                    },
                                    modifier = Modifier
                                        .height(50.dp)
                                        .weight(0.5f)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Black)
                                }
                            }
                            is UiState.Success<*> -> {
                                dealerViewModel.getAlloyByIdOfDealer(alloy.id)
                                dealerViewModel.resetAlloyImageState()
                            }
                            is UiState.Loading -> {
                                CircularProgressIndicator()

                            }
                            is UiState.Error -> {
                                scope.launch {
                                    val message = (alloyImageState as UiState.Error).message
                                    snackbarHostState.showSnackbar("Error uploading alloy image: $message", withDismissAction = true)
                                }
                                dealerViewModel.resetAlloyImageState()
                            }
                        }


                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Specs Card
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Specifications",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            AlloySpecRow("Size", "${alloy?.size} inches", Icons.Default.Settings)
                            AlloySpecRow("Width", "${alloy?.width} inches", Icons.Default.Settings)
                            AlloySpecRow("Offset", "${alloy?.offset} mm", Icons.Default.Settings)
                            AlloySpecRow("PCD", alloy?.pcd ?: "N/A", Icons.Default.Settings)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Compatible Models
                    if (!alloy.compatibleModels.isNullOrEmpty()) {
                        Text(
                            text = "Compatible Car Models",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 24.dp)

                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color.White,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp)
                            ) {
                                alloy.compatibleModels.forEach { pair ->
                                    pair.let {
                                        val brand = it.second?.first ?: "Unknown Brand"
                                        val model = it.second?.second ?: "Unknown Model"
                                        Text("• $brand $model", style = MaterialTheme.typography.bodyLarge)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.padding(48.dp))

                    if (showEditAlloyDialog) {
                        EditAlloyDialog(
                            alloyToEdit = alloy,
                            onDismissRequest = { showEditAlloyDialog = false },
                            onEditAlloy = { alloyRequest ->
                                dealerViewModel.editAlloy(alloy.id, alloyRequest)
                            },
                            dealerViewModel = dealerViewModel,
                            onShowSnackbar = { message ->
                                scope.launch { snackbarHostState.showSnackbar(message) }
                            }
                        )
                    }
                }

                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Error -> {
                    Box{
                        //dealerViewModel.resetAddAlloyState()
                        val message = (alloyDetailsState as UiState.Error).message
                        Log.d("error in alloy details screen", message.toString())
                        Text(message.toString())
                    }
                }
            }


        }
    }


}

@Composable
fun AlloySpecRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(text = value, fontWeight = FontWeight.SemiBold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAlloyDialog(
    alloyToEdit: AlloyResponse,
    onDismissRequest: () -> Unit,
    onEditAlloy: (AlloyRequest) -> Unit,
    dealerViewModel: DealerViewModel,
    onShowSnackbar: (String) -> Unit
) {

    // Initialize states with existing alloy data
    var alloyName by remember { mutableStateOf(alloyToEdit.name) }
    var alloySize by remember { mutableStateOf(alloyToEdit.size.toString()) }
    var alloyWidth by remember { mutableStateOf(alloyToEdit.width.toString()) }
    var alloyOffset by remember { mutableStateOf(alloyToEdit.offset.toString()) }
    var alloyPCD by remember { mutableStateOf(alloyToEdit.pcd) }
    var alloyPrice by remember { mutableStateOf(alloyToEdit.price.toString()) }


    val compatibleModels = remember {
        mutableStateListOf<CarModelResponse>().apply {
            // Iterate through the pairs and create CarModelResponse objects
            alloyToEdit.compatibleModels?.forEach { pair ->
                pair.let {
                    // Assuming the 'modelName' in CarModelResponse is the model string itself,
                    // and 'id' can also be the model string if no specific ID is available.
                    // If your backend provides a distinct ID for the CarModelResponse, use that.
                    // For now, let's use model string as ID for consistency with your current data flow.
                    if (it.second != null) { // Ensure model name is not null
                        add(CarModelResponse(id = it.first, modelName = it.second!!.second))
                    }
                }
            }
        }
    }


    // Validation states
    var isNameError by remember { mutableStateOf(false) }
    var isSizeError by remember { mutableStateOf(false) }
    var isWidthError by remember { mutableStateOf(false) }
    var isOffsetError by remember { mutableStateOf(false) }
    var isPCDError by remember { mutableStateOf(false) }
    var isPriceError by remember { mutableStateOf(false) }
    var isModelsError by remember { mutableStateOf(false) }


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
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Edit Alloy Wheel",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = alloyName,
                    onValueChange = {
                        alloyName = it
                        isNameError = it.isBlank()
                    },
                    label = { Text("Alloy Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isNameError,
                    supportingText = { if (isNameError) Text("Name cannot be empty") }
                )
                OutlinedTextField(
                    value = alloySize,
                    onValueChange = {
                        alloySize = it
                        isSizeError = it.toDoubleOrNull() == null
                    },
                    label = { Text("Size (e.g., 17.0)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = isSizeError,
                    supportingText = { if (isSizeError) Text("Invalid size") }
                )
                OutlinedTextField(
                    value = alloyWidth,
                    onValueChange = {
                        alloyWidth = it
                        isWidthError = it.toDoubleOrNull() == null
                    },
                    label = { Text("Width (e.g., 7.5)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = isWidthError,
                    supportingText = { if (isWidthError) Text("Invalid width") }
                )
                OutlinedTextField(
                    value = alloyOffset,
                    onValueChange = {
                        alloyOffset = it
                        isOffsetError = it.toIntOrNull() == null
                    },
                    label = { Text("Offset (e.g., 35)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = isOffsetError,
                    supportingText = { if (isOffsetError) Text("Invalid offset") }
                )
                OutlinedTextField(
                    value = alloyPCD,
                    onValueChange = {
                        alloyPCD = it
                        isPCDError = it.isBlank()
                    },
                    label = { Text("PCD (e.g., 5x114.3)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isPCDError,
                    supportingText = { if (isPCDError) Text("PCD cannot be empty") }
                )
                OutlinedTextField(
                    value = alloyPrice,
                    onValueChange = {
                        alloyPrice = it
                        isPriceError = it.toDoubleOrNull() == null
                    },
                    label = { Text("Price (e.g., 15000.0)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = isPriceError,
                    supportingText = { if (isPriceError) Text("Invalid price") }
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
                if (isModelsError && compatibleModels.isEmpty()) {
                    Text(
                        "Please select at least one compatible model.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start)
                    )
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
                            isNameError = alloyName.isBlank()
                            isSizeError = alloySize.toDoubleOrNull() == null
                            isWidthError = alloyWidth.toDoubleOrNull() == null
                            isOffsetError = alloyOffset.toIntOrNull() == null
                            isPCDError = alloyPCD.isBlank()
                            isPriceError = alloyPrice.toDoubleOrNull() == null
                            isModelsError = compatibleModels.isEmpty()

                            if (!isNameError && !isSizeError && !isWidthError &&
                                !isOffsetError && !isPCDError && !isPriceError && !isModelsError
                            ) {
                                val carList = mutableListOf<String>()
                                for(models in compatibleModels){
                                    carList.add(models.id)
                                }
                                val alloyRequest = AlloyRequest(
                                    name = alloyName.trim(),
                                    size = alloySize.toDouble(),
                                    width = alloyWidth.toDouble(),
                                    offset = alloyOffset.toInt(),
                                    pcd = alloyPCD.trim(),
                                    price = alloyPrice.toDouble(),
                                    compatibleModels = carList
                                )
                                onEditAlloy(alloyRequest)
                                onShowSnackbar("Updating alloy...")
                                onDismissRequest() // Dismiss dialog on submission
                            } else {
                                onShowSnackbar("Please correct the errors.")
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isNameError && !isSizeError && !isWidthError &&
                                !isOffsetError && !isPCDError && !isPriceError && !isModelsError
                    ) {
                        Text("Save Changes")
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

