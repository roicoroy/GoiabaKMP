package com.goiaba.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.goiaba.shared.FontSize
import com.goiaba.shared.IconPrimary
import com.goiaba.shared.Surface
import com.goiaba.shared.TextPrimary
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navigateBack: () -> Unit) {
    val viewModel = koinViewModel<DetailsViewModel>()
    val id = viewModel.id

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Details",
                            fontSize = FontSize.LARGE,
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Text("back")
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
        ) { pad ->
            Row(
                modifier = Modifier.padding(pad)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("ID $id!", color = Color.Blue)

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { navigateBack() }) {
                        Text("Back")
                    }
                }
            }
        }
    }
}