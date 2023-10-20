package com.game.INever.ui.utils.formaters

import java.math.BigDecimal
import java.text.NumberFormat

fun numberFormatter(number: BigDecimal): NumberFormat {
    return NumberFormat.getInstance().apply {
        this.maximumFractionDigits = if (number > BigDecimal(10000)) 0 else 2
        this.minimumFractionDigits = 0
    }
}