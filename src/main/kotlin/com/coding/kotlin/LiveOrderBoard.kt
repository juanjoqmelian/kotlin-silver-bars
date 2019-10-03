package com.coding.kotlin

interface LiveOrderBoard {
    fun register(order: Order): String
    fun summary(): SummaryInfo
    fun cancel(id: String)
}
