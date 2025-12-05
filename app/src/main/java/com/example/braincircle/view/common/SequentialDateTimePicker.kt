package com.example.braincircle.view.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SequentialDateTimePicker(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Date) -> Unit
) {
    var showDateDialog by remember { mutableStateOf(true) }
    var showTimeDialog by remember { mutableStateOf(false) }

    val dateState = rememberDatePickerState()
    val timeState = rememberTimePickerState()

    LaunchedEffect(show) {
        if (show) {
            showDateDialog = true
            showTimeDialog = false
        }
    }

    if (show) {
        if (showDateDialog) {
            DatePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (dateState.selectedDateMillis != null) {
                                showDateDialog = false
                                showTimeDialog = true
                            }
                        }
                    ) {
                        Text(text = "Next")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) { Text(text = "Cancel") }
                }
            ) {
                DatePicker(state = dateState)
            }
        }

        if (showTimeDialog) {
            AlertDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(
                        onClick = {
                            val selectedDateMillis = dateState.selectedDateMillis
                            if (selectedDateMillis != null) {
                                val resultDate = combineDateAndTime(
                                    dateMillis = selectedDateMillis,
                                    hour = timeState.hour,
                                    minute = timeState.minute
                                )
                                onConfirm(resultDate)
                            }
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) { Text(text = "Cancel") }
                },
                text = {
                    TimePicker(state = timeState)
                }
            )
        }
    }
}

private fun combineDateAndTime(dateMillis: Long, hour: Int, minute: Int): Date {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = dateMillis

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val localCalendar = Calendar.getInstance()
    localCalendar.set(year, month, day, hour, minute, 0)

    return localCalendar.time
}