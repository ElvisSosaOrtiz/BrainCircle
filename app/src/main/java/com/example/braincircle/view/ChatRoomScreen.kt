package com.example.braincircle.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.braincircle.model.data.ChatMessage
import com.example.braincircle.ui.theme.BrainCircleTheme
import com.example.braincircle.viewmodel.chat_room.ChatRoomUiState
import com.example.braincircle.viewmodel.chat_room.ChatRoomViewModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun ChatRoomScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatRoomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatRoomScreenStateless(
        modifier = modifier,
        uiState = uiState,
        onMessageChange = viewModel::onMessageChange,
        onSendMessage = viewModel::sendMessage
    )
}

@Composable
fun ChatRoomScreenStateless(
    modifier: Modifier = Modifier,
    uiState: ChatRoomUiState,
    onMessageChange: (String) -> Unit = {},
    onSendMessage: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (uiState.messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "There are no messages in this moment",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true,
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(uiState.messages.sortedByDescending { it.timestamp }) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        MessageCard(
                            modifier = Modifier.align(if (it.senderId == uiState.senderId) Alignment.End else Alignment.Start),
                            message = it,
                            isCurrentUserSender = it.senderId == uiState.senderId
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.BottomCenter)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = uiState.message,
                onValueChange = onMessageChange,
                placeholder = {
                    Text(
                        text = "Type a message...",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                singleLine = true,
                maxLines = 1,
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            if (uiState.message.isNotEmpty()) {
                Spacer(Modifier.width(8.dp))
                FilledIconButton(onClick = onSendMessage) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    message: ChatMessage,
    isCurrentUserSender: Boolean
) {
    if (isCurrentUserSender) {
        Card(
            modifier = modifier
                .padding(12.dp)
                .widthIn(max = 250.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = message.text,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = SimpleDateFormat("HH:mm").format(message.timestamp ?: Date()),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    } else {
        Row(
            modifier = modifier
                .padding(12.dp)
                .widthIn(max = 250.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Log.d("MessageCard", message.senderPhotoUri)
            if (message.senderPhotoUri.isEmpty()) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    val usernameSplit = message.senderName.split(" ")
                    val initials = if (usernameSplit.size > 1) {
                        usernameSplit
                            .mapNotNull { it.firstOrNull()?.toString() }
                            .reduce { acc, s -> acc + s }
                    } else {
                        "${message.senderName.getOrNull(0)}${message.senderName.getOrNull(1)}"
                    }

                    Text(
                        text = initials,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(message.senderPhotoUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
            }
            Column {
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseSurface)) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.End),
                            text = message.text,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                        Text(
                            modifier = Modifier.align(Alignment.Start),
                            text = SimpleDateFormat("HH:mm").format(message.timestamp ?: Date()),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7FBF1)
@Composable
fun ChatRoomScreenLightPreview() {
    BrainCircleTheme {
//        MessageCard(
//            message = ChatMessage(
//                senderName = "Elvis Sosa",
//                text = "Hello World",
//                senderPhotoUri = "",
//                timestamp = Date()
//            ),
//            isCurrentUserSender = true
//        )
        val oldDate = Date()
        val instant = oldDate.toInstant()
        ChatRoomScreenStateless(
            uiState = ChatRoomUiState(
                senderId = "id",
                message = "Hello",
                messages = listOf(
                    ChatMessage(
                        senderId = "id",
                        senderName = "Elvis Sosa",
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(90))
                    ),
                    ChatMessage(
                        senderId = "id1",
                        senderName = "Elvis Sosa",
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(270))
                    ),
                    ChatMessage(
                        senderId = "id",
                        senderName = "Elvis Sosa",
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(90))
                    ),
                    ChatMessage(
                        senderId = "id1",
                        senderName = "Elvis Sosa",
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(270))
                    ),
                    ChatMessage(
                        senderId = "id",
                        senderName = "Elvis Sosa",
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(90))
                    ),
                    ChatMessage(
                        senderId = "id1",
                        senderName = "Elvis Sosa",
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(270))
                    )
                )
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF10140F)
@Composable
fun ChatRoomScreenDarkPreview() {
    BrainCircleTheme(darkTheme = true) {
//        MessageCard(
//            message = ChatMessage(
//                senderName = "Elvis Sosa",
//                text = "Hello World",
//                senderPhotoUri = "",
//                timestamp = Date()
//            ),
//            isCurrentUserSender = true
//        )
        val oldDate = Date()
        val instant = oldDate.toInstant()
        ChatRoomScreenStateless(
            uiState = ChatRoomUiState(
                senderId = "id",
                message = "Hello",
                messages = listOf(
                    ChatMessage(
                        senderId = "id",
                        senderName = "Elvis Sosa",
                        text = "Hello World",
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(90))
                    ),
                    ChatMessage(
                        senderId = "id1",
                        senderName = "Elvis Sosa",
                        text = "Hello World",
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(270))
                    )
                )
            )
        )
    }
}