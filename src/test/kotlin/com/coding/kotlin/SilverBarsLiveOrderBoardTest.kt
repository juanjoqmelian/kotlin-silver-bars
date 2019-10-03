package com.coding.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

internal class SilverBarsLiveOrderBoardTest {
    private val liveOrderBoard = SilverBarsLiveOrderBoard()

    @Test
    internal fun `should register an order`() {
        val newOrder = Order("my-user-id", 10, BigDecimal(12.5), OrderType.BUY)

        liveOrderBoard.register(newOrder)

        assertThat(liveOrderBoard.summary()).isEqualTo(SummaryInfo("BUY: 10 kg for £12.50"))
    }

    @Test
    internal fun `should not allow an invalid order`() {
        val newOrder = Order("", 10, BigDecimal(12.5), OrderType.BUY)

        val exception = assertThrows<IllegalArgumentException> {
            liveOrderBoard.register(newOrder)
        }

        assertThat(exception.message).isEqualTo("Order is not valid!")
    }

    @Test
    internal fun `should be empty after cancelling order`() {
        val order = Order("my-user-id", 10, BigDecimal(12.5), OrderType.BUY)

        val orderId = liveOrderBoard.register(order)

        assertThat(liveOrderBoard.summary()).isEqualTo(SummaryInfo("BUY: 10 kg for £12.50"))

        liveOrderBoard.cancel(orderId)

        assertThat(liveOrderBoard.summary()).isEqualTo(SummaryInfo())
    }

    @Test
    internal fun `should be able to register two identical orders and remove only one of them`() {
        val order1 = Order("my-user-id", 10, BigDecimal(12.5), OrderType.BUY)
        val order2 = Order("my-user-id", 10, BigDecimal(12.5), OrderType.BUY)

        liveOrderBoard.register(order1)
        val orderIdToCancel = liveOrderBoard.register(order2)

        liveOrderBoard.cancel(orderIdToCancel)

        assertThat(liveOrderBoard.summary()).isEqualTo(
            SummaryInfo(
                "BUY: 10 kg for £12.50"
            )
        )
    }

    @Test
    internal fun `should raise exception when trying to cancel non-existing order`() {
        val exception = assertThrows<IllegalArgumentException> {
            liveOrderBoard.cancel("non-existing-order-id")
        }

        assertThat(exception.message).isEqualTo("Order does not exist!")
    }

    @Test
    internal fun `should be able to register multiple orders`() {
        val order1 = Order("my-user-id", 10, BigDecimal(12.5), OrderType.BUY)
        val order2 = Order("my-user-id", 5, BigDecimal(12.8), OrderType.SELL)

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)

        assertThat(liveOrderBoard.summary()).isEqualTo(SummaryInfo(
            "BUY: 10 kg for £12.50",
            "SELL: 5 kg for £12.80"
        ))
    }

    @Test
    internal fun `should sort sell orders by price ascending`() {
        val order1 = Order("my-user-id", 10, BigDecimal(12.5), OrderType.SELL)
        val order2 = Order("my-user-id", 20, BigDecimal(8.22), OrderType.SELL)
        val order3 = Order("my-user-id", 5, BigDecimal(8.2), OrderType.SELL)

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)
        liveOrderBoard.register(order3)

        assertThat(liveOrderBoard.summary()).isEqualTo(SummaryInfo(
            "SELL: 5 kg for £8.20",
            "SELL: 20 kg for £8.22",
            "SELL: 10 kg for £12.50"
        ))
    }

    @Test
    internal fun `should sort buy orders by price descending`() {
        val order1 = Order("my-user-id", 20, BigDecimal(8.22), OrderType.BUY)
        val order2 = Order("my-user-id", 5, BigDecimal(8.2), OrderType.BUY)
        val order3 = Order("my-user-id", 10, BigDecimal(12.5), OrderType.BUY)

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)
        liveOrderBoard.register(order3)

        assertThat(liveOrderBoard.summary()).isEqualTo(SummaryInfo(
            "BUY: 10 kg for £12.50",
            "BUY: 20 kg for £8.22",
            "BUY: 5 kg for £8.20"
        ))
    }

    @Test
    internal fun `should show orders of different types in the right order`() {
        val order1 = Order("my-user-id", 20, BigDecimal(8.22), OrderType.BUY)
        val order2 = Order("my-user-id", 5, BigDecimal(8.2), OrderType.BUY)
        val order3 = Order("my-user-id", 10, BigDecimal(12.5), OrderType.BUY)
        val order4 = Order("my-user-id", 20, BigDecimal(12.5), OrderType.SELL)
        val order5 = Order("my-user-id", 5, BigDecimal(9.5), OrderType.SELL)
        val order6 = Order("my-user-id", 10, BigDecimal(8.5), OrderType.SELL)

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)
        liveOrderBoard.register(order3)
        liveOrderBoard.register(order4)
        liveOrderBoard.register(order5)
        liveOrderBoard.register(order6)

        assertThat(liveOrderBoard.summary()).isEqualTo(SummaryInfo(
            "BUY: 10 kg for £12.50",
            "BUY: 20 kg for £8.22",
            "BUY: 5 kg for £8.20",
            "SELL: 10 kg for £8.50",
            "SELL: 5 kg for £9.50",
            "SELL: 20 kg for £12.50"
        ))
    }

    @Test
    internal fun `should group orders of the same price and type`() {
        val order1 = Order(
            "my-userId",
            2,
            BigDecimal("120.50"),
            OrderType.SELL
        )
        val order2 = Order(
            "my-userId",
            4,
            BigDecimal("77.20"),
            OrderType.SELL
        )
        val order3 = Order(
            "my-userId",
            7,
            BigDecimal("120.50"),
            OrderType.SELL
        )
        val order4 = Order(
            "my-userId",
            1,
            BigDecimal("101.30"),
            OrderType.SELL
        )
        val order5 = Order(
            "my-userId",
            2,
            BigDecimal("120.50"),
            OrderType.BUY
        )
        val order6 = Order(
            "my-userId",
            4,
            BigDecimal("77.20"),
            OrderType.BUY
        )
        val order7 = Order(
            "my-userId",
            7,
            BigDecimal("120.50"),
            OrderType.BUY
        )
        val order8 = Order(
            "my-userId",
            1,
            BigDecimal("101.30"),
            OrderType.BUY
        )

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)
        liveOrderBoard.register(order3)
        liveOrderBoard.register(order4)
        liveOrderBoard.register(order5)
        liveOrderBoard.register(order6)
        liveOrderBoard.register(order7)
        liveOrderBoard.register(order8)

        assertThat(liveOrderBoard.summary()).isEqualTo(
            SummaryInfo(
                "BUY: 9 kg for £120.50",
                "BUY: 1 kg for £101.30",
                "BUY: 4 kg for £77.20",
                "SELL: 4 kg for £77.20",
                "SELL: 1 kg for £101.30",
                "SELL: 9 kg for £120.50"
            )
        )
    }

    @Test
    internal fun `should group orders with same price but different trailing zeros`() {
        val order1 = Order(
            "my-user-id",
            8,
            BigDecimal("120.5"),
            OrderType.SELL
        )

        val order2 = Order(
            "my-user-id",
            12,
            BigDecimal("120.50"),
            OrderType.SELL
        )

        liveOrderBoard.register(order1)
        liveOrderBoard.register(order2)

        assertThat(liveOrderBoard.summary()).isEqualTo(
            SummaryInfo(
                "SELL: 20 kg for £120.50"
            )
        )
    }
}
