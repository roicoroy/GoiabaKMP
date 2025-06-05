package com.goiaba.feature

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.goiaba.shared.Resources
import com.goiaba.shared.components.InfoCard
import com.goiaba.shared.util.getScreenWidth
import kotlinx.coroutines.flow.flatMapLatest
import org.koin.compose.viewmodel.koinViewModel
import com.goiaba.shared.util.DisplayResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGraphScreen(
    navigateToDetails: (String) -> Unit,
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState()

    val viewModel = koinViewModel<HomeGraphViewModel>()

    val products by viewModel.products.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Scaffold() { padding ->
            Row(
                modifier = Modifier.padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clickable {
                            navigateToDetails("123")
                        },
                    verticalArrangement = Arrangement.Center
                ) {
                    products.DisplayResult(
//                        onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
                        onSuccess = { productList ->
                            Text(productList)
                        },
                        onError = { message ->
                            InfoCard(
                                image = Resources.Image.Cat,
                                title = "Oops!",
                                subtitle = message
                            )
                        }
                    )
//                    Text(products.getSuccessData())
                }
            }

        }
    }
}
