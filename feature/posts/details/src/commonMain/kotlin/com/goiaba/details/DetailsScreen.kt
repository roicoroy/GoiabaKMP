package com.goiaba.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.goiaba.data.models.posts.SinglePostResponse
import com.goiaba.details.components.PostFormModal
import com.goiaba.shared.FontSize
import com.goiaba.shared.IconPrimary
import com.goiaba.shared.Resources
import com.goiaba.shared.Surface
import com.goiaba.shared.TextPrimary
import com.goiaba.shared.components.InfoCard
import com.goiaba.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit
) {
    val viewModel = koinViewModel<DetailsViewModel>()

    val id = viewModel.id
    val post by viewModel.post.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    // Modal states
    val isModalVisible by viewModel.isModalVisible.collectAsState()
    val isModalLoading by viewModel.isModalLoading.collectAsState()
    val modalMessage by viewModel.modalMessage.collectAsState()
    
    // Navigation state
    val shouldNavigateToHome by viewModel.shouldNavigateToHome.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle navigation to home screen after deletion
    LaunchedEffect(shouldNavigateToHome) {
        if (shouldNavigateToHome) {
            navigateToHome()
            viewModel.onNavigatedToHome()
        }
    }

    // Show snackbar for modal messages
    LaunchedEffect(modalMessage) {
        modalMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
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
                            text = "Post Details",
                            fontSize = FontSize.LARGE,
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Text(
                                text = "← Back",
                                color = IconPrimary,
                                fontSize = FontSize.REGULAR
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Surface,
                        scrolledContainerColor = Surface,
                        navigationIconContentColor = IconPrimary,
                        titleContentColor = TextPrimary,
                        actionIconContentColor = IconPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Show error message if any
                errorMessage?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = FontSize.LARGE,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Error",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    fontSize = FontSize.REGULAR
                                )
                            }
                            TextButton(
                                onClick = { viewModel.retry() }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                // Post content
                post.DisplayResult<SinglePostResponse>(
                    onLoading = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Loading post details...",
                                    fontSize = FontSize.REGULAR,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    onSuccess = { postData ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                // Post ID Badge
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Text(
                                        text = "ID: ${postData.data.id}",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        fontSize = FontSize.SMALL,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                // Post Title
                                Text(
                                    text = postData.data.attributes.title,
                                    fontSize = FontSize.EXTRA_LARGE,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(bottom = 20.dp)
                                )

                                // Metadata Section
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Post Information",
                                            fontSize = FontSize.MEDIUM,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(bottom = 12.dp)
                                        )

                                        // Created Date
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "📅",
                                                fontSize = FontSize.REGULAR,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                            Column {
                                                Text(
                                                    text = "Created",
                                                    fontSize = FontSize.SMALL,
                                                    fontWeight = FontWeight.Medium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = postData.data.attributes.createdAt,
                                                    fontSize = FontSize.REGULAR,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }

                                        // Updated Date
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "🔄",
                                                fontSize = FontSize.REGULAR,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                            Column {
                                                Text(
                                                    text = "Last Updated",
                                                    fontSize = FontSize.SMALL,
                                                    fontWeight = FontWeight.Medium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = postData.data.attributes.updatedAt,
                                                    fontSize = FontSize.REGULAR,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Action Buttons
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Edit and Delete buttons
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Button(
                                            onClick = { viewModel.showModal() },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Text("✏️ Edit Post")
                                        }

                                        Button(
                                            onClick = { viewModel.deletePost() },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.error
                                            ),
                                            enabled = !isLoading
                                        ) {
                                            if (isLoading) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.height(16.dp),
                                                    strokeWidth = 2.dp,
                                                    color = MaterialTheme.colorScheme.onError
                                                )
                                            } else {
                                                Text("🗑️ Delete")
                                            }
                                        }
                                    }
                                    
                                    // Navigation buttons
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Button(
                                            onClick = navigateBack,
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.secondary
                                            )
                                        ) {
                                            Text("← Go Back")
                                        }

                                        OutlinedButton(
                                            onClick = { viewModel.retry() },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("🔄 Refresh")
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onError = { message ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            InfoCard(
                                modifier = Modifier,
                                image = Resources.Image.Cat,
                                title = "Failed to Load Post",
                                subtitle = message
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.retry() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("🔄 Try Again")
                                }

                                OutlinedButton(
                                    onClick = navigateBack
                                ) {
                                    Text("← Go Back")
                                }
                            }
                        }
                    }
                )

                // Debug Information (only show if ID is available)
                if (id.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Debug Info",
                                fontSize = FontSize.SMALL,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Post ID: $id",
                                fontSize = FontSize.SMALL,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Loading: $isLoading",
                                fontSize = FontSize.SMALL,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Post Form Modal
        PostFormModal(
            isVisible = isModalVisible,
            isEditMode = true,
            initialTitle = post.getSuccessDataOrNull()?.data?.attributes?.title ?: "",
            isLoading = isModalLoading,
            onDismiss = { viewModel.hideModal() },
            onSubmit = { title -> viewModel.updatePost(title) }
        )
    }
}