package com.foresko.gamenever.ui.utils.formaters

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun currencyFormatter(
    localeFormat: String,
    currencyCode: String,
    sum: BigDecimal? = null
): NumberFormat {
    val locale = try {
        Locale.forLanguageTag(localeFormat)
    } catch (ex: Exception) {
        Locale.getDefault()
    }

    val currency = try {
        Currency.getInstance(currencyCode)
    } catch (ex: Exception) {
        Currency.getInstance(Locale.getDefault())
    }

    val decimalFormatSymbols = DecimalFormatSymbols(locale).apply {
        currencySymbol = currencySymbolMap[currencyCode.uppercase()] ?: currency.symbol
    }

    val numberFormat = NumberFormat.getCurrencyInstance(locale).apply {
        maximumFractionDigits = if (sum != null && sum < BigDecimal(10000)) 2 else 0
        minimumFractionDigits = 0
    } as DecimalFormat
    numberFormat.decimalFormatSymbols = decimalFormatSymbols

    return numberFormat
}

fun moneyCurrencyFormatter(
    localeFormat: String,
    currencyCode: String,
    sum: BigDecimal? = null
): NumberFormat {
    return currencyFormatter(
        localeFormat = localeFormat,
        currencyCode = currencyCode,
        sum = sum
    ).apply {
        maximumFractionDigits = if (sum != null && sum < BigDecimal(100)) 2 else 0
        minimumFractionDigits = 0
    }
}

val currencySymbolMap: Map<String, String> = mapOf(
    "RUB" to "₽",
    "KZT" to "₸",
    "PLN" to "zł",
    "RON" to "lei",
    "COP" to "$",
    "MXN" to "$",
    "VND" to "₫",
    "PHP" to "₱",
    "USD" to "$",
    "EUR" to "€",
    "GBP" to "£",
    "JPY" to "¥",
    "INR" to "₹",
    "BRL" to "R$",
    "ZAR" to "R",
    "TRY" to "₺",
    "SEK" to "kr",
    "CHF" to "Fr",
    "CNY" to "¥",
    "AUD" to "$",
    "CAD" to "$",
    "SGD" to "$",
    "NZD" to "$",
    "HKD" to "$",
    "NOK" to "kr",
    "THB" to "฿",
    "IDR" to "Rp",
    "MYR" to "RM",
    "DKK" to "kr",
    "HUF" to "Ft",
    "CZK" to "Kč",
    "ILS" to "₪",
    "ARS" to "$",
    "CLP" to "$",
    "TWD" to "NT$",
    "UAH" to "₴",
    "PKR" to "₨",
    "BDT" to "৳",
    "KRW" to "₩",
    "LKR" to "₨",
    "NGN" to "₦",
    "EGP" to "£",
    "PEN" to "S/",
    "MAD" to "د.م.",
    "AED" to "د.إ",
    "SAR" to "ر.س",
    "QAR" to "ر.ق",
    "OMR" to "ر.ع.",
    "BHD" to ".د.ب",
    "KWD" to "د.ك",
    "JOD" to "د.ا",
    "IRR" to "﷼",
    "DOP" to "$",
    "PYG" to "₲",
    "UYU" to "$",
    "BOB" to "Bs.",
    "CRC" to "₡",
    "GTQ" to "Q",
    "HRK" to "kn",
    "ISK" to "kr",
    "JMD" to "J$",
    "MOP" to "P",
    "NAD" to "$",
    "NIO" to "C$",
    "BGN" to "лв",
    "FJD" to "$",
    "GHS" to "₵",
    "KES" to "Sh",
    "LBP" to "ل.ل",
    "MKD" to "ден",
    "MUR" to "₨",
    "NPR" to "₨",
    "PAB" to "B/.",
    "RSD" to "дин",
    "SYP" to "£",
    "TND" to "د.ت",
    "TTD" to "$",
    "UGX" to "Sh",
    "XAF" to "Fr",
    "XOF" to "Fr",
    "YER" to "﷼",
    "ZMW" to "ZK",
    "GEL" to "₾",
    "ETB" to "Br",
    "BAM" to "KM",
    "AZN" to "₼",
    "AMD" to "֏",
    "BYN" to "Br",
    "TMT" to "m",
    "KGS" to "сом",
    "AFN" to "؋",
    "ALL" to "L",
    "AWG" to "ƒ",
    "BBD" to "$",
    "BMD" to "$",
    "BSD" to "$",
    "BND" to "$",
    "KYD" to "$",
    "SBD" to "$",
    "SRD" to "$",
    "SVC" to "$",
    "XCD" to "$",
    "ANG" to "ƒ",
    "BIF" to "Fr",
    "CHF" to "Fr",
    "CDF" to "Fr",
    "DJF" to "Fr",
    "GNF" to "Fr",
    "HTG" to "G",
    "MGA" to "Ar",
    "MWK" to "MK",
    "MZN" to "MT",
    "RWF" to "Fr",
    "WST" to "T",
    "VUV" to "Vt",
    "ZWL" to "$"
)
