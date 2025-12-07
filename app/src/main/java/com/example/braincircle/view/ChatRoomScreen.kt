package com.example.braincircle.view

import android.icu.text.DateFormat
import android.icu.text.DateFormat.getDateInstance
import android.icu.text.DateFormat.getTimeInstance
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
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.braincircle.R
import com.example.braincircle.model.data.ChatMessage
import com.example.braincircle.model.utils.DateUtils.getDateIntervalInDays
import com.example.braincircle.ui.theme.BrainCircleTheme
import com.example.braincircle.viewmodel.chat_room.ChatRoomUiState
import com.example.braincircle.viewmodel.chat_room.ChatRoomViewModel
import java.util.Calendar
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
    val currentDateLabelDate = rememberSaveable { mutableStateOf<Date?>(null) }
    val currentDateLabelNotShowing = rememberSaveable { mutableStateOf(false) }

    val date = Date()

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
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(uiState.messages.sortedByDescending { it.timestamp }) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        val daysDifference =
                            it.timestamp?.let { endDate -> getDateIntervalInDays(date, endDate) }

                        currentDateLabelNotShowing.value =
                            currentDateLabelDate.value != it.timestamp

                        if (currentDateLabelNotShowing.value) {
                            currentDateLabelNotShowing.value =
                                currentDateLabelDate.value == it.timestamp

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (daysDifference?.toInt() == 0) "Today" else if (daysDifference?.toInt() == 1) "Yesterday" else getDateInstance(
                                        DateFormat.RELATIVE
                                    ).format(it.timestamp),
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }

                            if (currentDateLabelDate.value != it.timestamp) {
                                currentDateLabelDate.value = it.timestamp
                            }
                        }

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
                    .weight(1f)
                    .height(48.dp),
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
                shape = MaterialTheme.shapes.extraLarge,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
            Spacer(Modifier.width(8.dp))
            FilledIconButton(
                onClick = onSendMessage,
                enabled = uiState.message.isNotBlank()
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
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
    val formattedTimestamp = getTimeInstance(DateFormat.SHORT).format(message.timestamp ?: Date())

    if (isCurrentUserSender) {
        Card(
            modifier = modifier
                .padding(end = 8.dp)
                .padding(vertical = 4.dp)
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
                Spacer(Modifier.padding(vertical = 2.dp))
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = formattedTimestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    } else {
        Row(
            modifier = modifier
                .padding(start = 8.dp)
                .padding(vertical = 4.dp)
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
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Start),
                            text = message.text,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.padding(vertical = 2.dp))
                        Text(
                            modifier = Modifier.align(Alignment.End),
                            text = formattedTimestamp,
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

        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.YEAR, 2025)
            set(Calendar.MONTH, Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, 4)
        }

        ChatRoomScreenStateless(
            uiState = ChatRoomUiState(
                senderId = "id",
                message = "",
                messages = listOf(
                    ChatMessage(
                        senderId = "id",
                        senderName = "Elvis Sosa",
                        text = stringResource(R.string.lorem_ipsum),
                        senderPhotoUri = "",
                        timestamp = calendar.time
                    ),
                    ChatMessage(
                        senderId = "id1",
                        senderName = "Elvis Sosa",
                        text = stringResource(R.string.lorem_ipsum),
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(270))
                    ),
                    ChatMessage(
                        senderId = "id",
                        senderName = "Elvis Sosa",
                        text = stringResource(R.string.lorem_ipsum),
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(90))
                    ),
                    ChatMessage(
                        senderId = "id1",
                        senderName = "Elvis Sosa",
                        text = stringResource(R.string.lorem_ipsum),
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(270))
                    ),
                    ChatMessage(
                        senderId = "id",
                        senderName = "Elvis Sosa",
                        text = stringResource(R.string.lorem_ipsum),
                        senderPhotoUri = "",
                        timestamp = Date.from(instant.minusSeconds(90))
                    ),
                    ChatMessage(
                        senderId = "id1",
                        senderName = "Elvis Sosa",
                        text = stringResource(R.string.lorem_ipsum),
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
                message = "",
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
                        timestamp = Date.from(instant.minusSeconds(1000000))
                    )
                )
            )
        )
    }
}