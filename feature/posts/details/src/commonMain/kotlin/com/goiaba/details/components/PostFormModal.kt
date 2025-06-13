package com.goiaba.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.goiaba.shared.FontSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFormModal(
    isVisible: Boolean,
    isEditMode: Boolean = false,
    initialTitle: String = "",
    isLoading: Boolean = false,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var title by remember(initialTitle) { mutableStateOf(initialTitle) }
    var titleError by remember { mutableStateOf<String?>(null) }

    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header
                        Text(
                            text = if (isEditMode) "Edit Post" else "Create New Post",
                            fontSize = FontSize.LARGE,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Title Input Field
                        OutlinedTextField(
                            value = title,
                            onValueChange = { 
                                title = it
                                titleError = null
                            },
                            label = { Text("Post Title") },
                            placeholder = { Text("Enter post title...") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading,
                            isError = titleError != null,
                            supportingText = titleError?.let { error ->
                                {
                                    Text(
                                        text = error,
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = FontSize.SMALL
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Cancel Button
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Cancel",
                                    fontSize = FontSize.REGULAR,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            
                            // Submit Button
                            Button(
                                onClick = {
                                    when {
                                        title.isBlank() -> {
                                            titleError = "Title cannot be empty"
                                        }
                                        title.length < 3 -> {
                                            titleError = "Title must be at least 3 characters"
                                        }
                                        else -> {
                                            onSubmit(title.trim())
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                if (isLoading) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                        Text(
                                            text = if (isEditMode) "Updating..." else "Creating...",
                                            fontSize = FontSize.REGULAR,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                } else {
                                    Text(
                                        text = if (isEditMode) "Update Post" else "Create Post",
                                        fontSize = FontSize.REGULAR,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}