package com.goiaba.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.goiaba.data.models.posts.PostsResponse
import com.goiaba.shared.*
import com.goiaba.shared.components.InfoCard
import com.goiaba.shared.util.DisplayResult
import com.goiaba.shared.util.RequestState
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(
    navigateToDetails: (String) -> Unit,
    navigateToHome: () -> Unit,
) {
    val viewModel = koinViewModel<PostsScreenViewModel>()

    val posts by viewModel.posts.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()

    // Modal states
    val isModalVisible by viewModel.isModalVisible.collectAsState()
    val isModalLoading by viewModel.isModalLoading.collectAsState()
    val modalMessage by viewModel.modalMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Update auth state when screen is displayed
    LaunchedEffect(Unit) {
        viewModel.updateAuthState()
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
                        Column {
                            Text(
                                text = "Posts from Strapi",
                                fontSize = FontSize.LARGE,
                                color = TextPrimary
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = navigateToHome
                        ) {
                            Icon(
                                painter = painterResource(Resources.Icon.Home),
                                contentDescription = "Home",
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
                Spacer(Modifier.size(20.dp))
                PostsCardData(posts, navigateToDetails, viewModel)
                Spacer(Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun PostsCardData(
    posts: RequestState<PostsResponse>,
    navigateToDetails: (String) -> Unit,
    viewModel: PostsScreenViewModel
) {
    // Fixed type inference by explicitly specifying the type
    posts.DisplayResult(
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
        onSuccess = { postsResponse ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(postsResponse.data) { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                // Pass the post ID as string to navigation
                                navigateToDetails(post.id.toString())
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = post.attributes.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "ID: ${post.id}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Created: ${post.attributes.createdAt}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                // Visual indicator for clickable item
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text(
                                        text = "→",
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Add some bottom padding for the last item
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
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
                    title = "Failed to Load Posts",
                    subtitle = message
                )

                Spacer(modifier = Modifier.height(24.dp))

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
            }
        }
    )
}


