package com.coding.kotlin

import java.math.BigDecimal
import java.math.RoundingMode

class Order {
    var id: String = ""
    val userId: String
    val quantity: Int
    val price: BigDecimal
    val type: OrderType

    constructor(userId: String, quantity: Int, price: BigDecimal, type: OrderType) {
        this.userId = userId
        this.quantity = quantity
        this.price = price.setScale(2, RoundingMode.HALF_EVEN)
        this.type = type
    }

    fun isValid(): Boolean {
        return !userId.isNullOrEmpty() && quantity > 0 && price.compareTo(BigDecimal.ZERO) > 0
    }

    fun summary(): String {
        return "$type: $quantity kg for £${price.toPlainString()}"
    }
}
