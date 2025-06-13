package com.goiaba.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    val snackbarHostState = remember { SnackbarHostState() }

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
                        Column {
                            Text(
                                text = "Profile Screen",
                                fontSize = FontSize.LARGE,
                                color = TextPrimary
                            )
                            if (isLoggedIn && userEmail != null) {
                                Text(
                                    text = "Welcome, $userEmail",
                                    fontSize = FontSize.SMALL,
                                    color = TextPrimary.copy(alpha = 0.7f)
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { navigateBack() }
                        ) {
                            Icon(
                                painter = painterResource(Resources.Icon.BackArrow),
                                contentDescription = "Adverts",
                                tint = IconPrimary
                            )
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Home Screen",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

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
                                            text = "Loading posts from Strapi...",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            },
                            onSuccess = { userResponse ->
                                Text(userResponse.email)
                            },
                            onError = { message ->
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.refreshPosts()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text("🔄 Retry")
                                    }

                                    InfoCard(
                                        modifier = Modifier,
                                        image = Resources.Image.Cat,
                                        title = "Failed to Load Posts",
                                        subtitle = message
                                    )




                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

