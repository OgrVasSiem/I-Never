package com.foresko.gamenever.ui.destination.premium

enum class TariffType(val SBPSubscribeName: String, val googlePurchaseName: String) {
    Month("month", "monthly-subscribe"),
    ThreeMonth("one-minute-premium", "three-month-subs"),
    Year("five-minute-premium", "yearly-subscribe")
}