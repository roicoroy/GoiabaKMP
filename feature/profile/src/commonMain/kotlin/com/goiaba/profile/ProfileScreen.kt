package com.goiaba.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.goiaba.data.models.profile.UsersMeResponse
import com.goiaba.profile.components.AddressCard
import com.goiaba.profile.components.AdvertCard
import com.goiaba.profile.components.UserInfoCard
import com.goiaba.profile.details.AddressDetailsScreen
import com.goiaba.profile.details.AdvertDetailsScreen
import com.goiaba.shared.*
import com.goiaba.shared.components.InfoCard
import com.goiaba.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigateBack: () -> Unit
) {
    val viewModel = koinViewModel<ProfileViewModel>()

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val user by viewModel.user.collectAsState()
    val isUpdatingAddress by viewModel.isUpdatingAddress.collectAsState()
    val updateMessage by viewModel.updateMessage.collectAsState()

    // State for detail screens
    var selectedAdvert by remember { mutableStateOf<com.goiaba.data.models.profile.Advert?>(null) }
    var selectedAddress by remember { mutableStateOf<com.goiaba.data.models.profile.Addresse?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Update auth state when screen is displayed
    LaunchedEffect(Unit) {
        viewModel.updateAuthState()
    }

    // Show update messages
    LaunchedEffect(updateMessage) {
        updateMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearUpdateMessage()
        }
    }

    // Show detail screens when items are selected
    selectedAdvert?.let { advert ->
        AdvertDetailsScreen(
            advert = advert,
            navigateBack = { selectedAdvert = null }
        )
        return
    }

    selectedAddress?.let { address ->
        AddressDetailsScreen(
            address = address,
            isLoading = isUpdatingAddress,
            onUpdateAddress = { firstName, lastName, firstLineAddress, secondLineAddress, postCode, city, country, phoneNumber ->
                viewModel.updateAddress(
                    addressId = address.documentId,
                    firstName = firstName,
                    lastName = lastName,
                    firstLineAddress = firstLineAddress,
                    secondLineAddress = secondLineAddress,
                    postCode = postCode,
                    city = city,
                    country = country,
                    phoneNumber = phoneNumber
                )
            },
            onDeleteAddress = {
                viewModel.deleteAddress(address.documentId)
                selectedAddress = null
            },
            navigateBack = { selectedAddress = null }
        )
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Profile",
                            fontSize = FontSize.LARGE,
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                painter = painterResource(Resources.Icon.BackArrow),
                                contentDescription = "Back",
                                tint = IconPrimary
                            )
                        }
                    },
                    actions = {
                        if (isLoggedIn) {
                            IconButton(
                                onClick = { viewModel.refreshProfile() }
                            ) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Search),
                                    contentDescription = "Refresh",
                                    tint = IconPrimary
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Surface,
                        scrolledContainerColor = Surface,
                        titleContentColor = TextPrimary,
                        actionIconContentColor = IconPrimary
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (!isLoggedIn) {
                    // Not logged in state
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(Resources.Icon.Warning),
                                contentDescription = "Warning",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Not Logged In",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = "Please log in to view your profile",
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    fontSize = FontSize.SMALL
                                )
                            }
                            TextButton(
                                onClick = navigateBack
                            ) {
                                Text("Go Back")
                            }
                        }
                    }
                } else {
                    // Logged in state - display user profile
                    user.DisplayResult(
                        onLoading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Loading profile...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        onSuccess = { userResponse ->
                            ProfileContent(
                                user = userResponse,
                                onRefresh = { viewModel.refreshProfile() },
                                onAdvertClick = { advert -> selectedAdvert = advert },
                                onAddressClick = { address -> selectedAddress = address }
                            )
                        },
                        onError = { message ->
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                InfoCard(
                                    modifier = Modifier,
                                    image = Resources.Image.Cat,
                                    title = "Failed to Load Profile",
                                    subtitle = message
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Button(
                                    onClick = { viewModel.refreshProfile() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("🔄 Retry")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    user: UsersMeResponse,
    onRefresh: () -> Unit,
    onAdvertClick: (com.goiaba.data.models.profile.Advert) -> Unit,
    onAddressClick: (com.goiaba.data.models.profile.Addresse) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Information Card
        item {
            UserInfoCard(user = user)
        }

        // Addresses Section
        if (user.addresses.isNotEmpty()) {
            item {
                Text(
                    text = "Addresses (${user.addresses.size})",
                    fontSize = FontSize.LARGE,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(user.addresses) { address ->
                AddressCard(
                    address = address,
                    onAddressClick = onAddressClick
                )
            }
        }

        // Adverts Section
        if (user.adverts.isNotEmpty()) {
            item {
                Text(
                    text = "My Adverts (${user.adverts.size})",
                    fontSize = FontSize.LARGE,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(user.adverts) { advert ->
                AdvertCard(
                    advert = advert,
                    onAdvertClick = onAdvertClick
                )
            }
        }

        // Empty states
        if (user.addresses.isEmpty() && user.adverts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📝",
                            fontSize = FontSize.EXTRA_LARGE
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Complete Your Profile",
                            fontSize = FontSize.MEDIUM,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add addresses and create adverts to get started",
                            fontSize = FontSize.REGULAR,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Refresh button at the bottom
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRefresh,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("🔄 Refresh Profile")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}