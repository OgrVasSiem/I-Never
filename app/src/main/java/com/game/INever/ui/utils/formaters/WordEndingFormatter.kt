package com.game.INever.ui.utils.formaters

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.game.INever.R

private fun wordEnding(count: Int, word0: String, word1: String, word2: String): String {
    var rem = count % 100
    if (rem < 11 || rem > 14) {
        rem = count % 10
        if (rem == 1) return word1
        if (rem in 2..4) return word2
    }
    return word0
}

@Composable
fun Int.formatAsMonthWordEnding(): String =
    wordEnding(
        count = this,
        word0 = stringResource(R.string.month_3),
        word1 = stringResource(R.string.month_1),
        word2 = stringResource(R.string.month_2)
    )

