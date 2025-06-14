package com.goiaba.adverts.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.goiaba.data.models.adverts.AdvertGetResponse // Make sure this import is correct based on your project structure

@Composable
fun UserInfoSection(user: AdvertGetResponse.Advert.User?) { // Made it public to be accessible from other packages
    user?.let {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "By ${it.username}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (it.professional) {
                Text(
                    text = "Professional",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    } ?: run {
        // Show placeholder when user is null
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Anonymous",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}