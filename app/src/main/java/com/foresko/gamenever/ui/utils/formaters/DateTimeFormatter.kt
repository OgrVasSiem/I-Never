package com.foresko.gamenever.ui.utils.formaters

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.foresko.gamenever.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

@Composable
fun DateTimeFormatter.returnDateTimeFormatter(date: LocalDateTime): String =
    stringResource(
        R.string.return_date,
        this.format(date)
    )

@Composable
fun DateTimeFormatter.archivedDateTimeFormatter(date: LocalDateTime): String =
    stringResource(
        R.string.archived_since,
        this.format(date)
    )