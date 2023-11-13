package com.foresko.gamenever.ui.destination.premium

enum class TariffType(val SBPSubscribeName: String, val googlePurchaseName: String) {
    Month("month", "monthly-subscribe"),
    ThreeMonth("three-months-premium", "three-month-subs"),
    Year("one-year-premium", "yearly-subscribe")
}