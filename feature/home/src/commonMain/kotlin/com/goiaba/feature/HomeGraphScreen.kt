package com.goiaba.feature

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.goiaba.shared.Resources
import com.goiaba.shared.components.InfoCard
import com.goiaba.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGraphScreen(
    navigateToDetails: (String) -> Unit,
) {
    val navController = rememberNavController()
    val viewModel = koinViewModel<HomeGraphViewModel>()

    val posts by viewModel.posts.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Posts from Strapi",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                posts.DisplayResult(
                    onLoading = { 
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    onSuccess = { postsResponse ->
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                        }
                    },
                    onError = { message ->
                        InfoCard(
                            image = Resources.Image.Cat,
                            title = "Oops!",
                            subtitle = message
                        )
                    }
                )
            }
        }
    }
}