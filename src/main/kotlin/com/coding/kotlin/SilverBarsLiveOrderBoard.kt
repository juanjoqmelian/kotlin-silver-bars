package com.coding.kotlin

import java.util.UUID
import kotlin.Comparator


class SilverBarsLiveOrderBoard : LiveOrderBoard {
    private val orders: MutableList<Order> = mutableListOf()

    override fun cancel(id: String) {
        val orderToBeDeleted = orders.find { order -> order.id == id }
            ?: throw IllegalArgumentException("Order does not exist!")
        orders.remove(orderToBeDeleted)
    }

    override fun summary(): SummaryInfo {
        return SummaryInfo(*orders
            .groupBy { order -> order.type }
            .toSortedMap()
            .map { ordersByType ->
                ordersByType.value.groupBy { order -> order.price }
                    .map { ordersByPrice ->
                        ordersByPrice.value
                            .reduce { left, right ->
                                Order(
                                    right.userId,
                                    left.quantity.plus(right.quantity),
                                    right.price,
                                    right.type
                                )
                            }
                    }
                    .sortedWith(comparatorByType(ordersByType))
            }
            .flatten()
            .map { order -> order.summary() }
            .toTypedArray())
    }

    override fun register(order: Order): String {
        require(order.isValid()) { "Order is not valid!" }
        val orderId = UUID.randomUUID().toString()
        order.id = orderId
        orders.add(order)
        return orderId
    }


    private fun comparatorByType(entry: Map.Entry<OrderType, List<Order>>): java.util.Comparator<Order> {
        return Comparator { left, right ->
            when {
                entry.key == OrderType.SELL -> {
                    left.price.compareTo(right.price)
                }
                entry.key == OrderType.BUY -> {
                    -left.price.compareTo(right.price)
                }
                else -> throw UnsupportedOperationException("Order type not supported!")
            }
        }
    }
}
