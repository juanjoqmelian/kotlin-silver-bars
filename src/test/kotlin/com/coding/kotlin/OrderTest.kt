package com.coding.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class OrderTest {
    @Test
    internal fun `should create a valid order`() {
        val order = Order("my-user-id", 10, BigDecimal(12.5), OrderType.BUY)

        assertThat(order.isValid()).isTrue()
    }

    @Test
    internal fun `should not be a valid order if user id is not present`() {
        val order = Order("", 10, BigDecimal(12.5), OrderType.BUY)

        assertThat(order.isValid()).isFalse()
    }

    @Test
    internal fun `should not be a valid order if quantity is zero`() {
        val order = Order("my-user-id", 0, BigDecimal(12.5), OrderType.BUY)

        assertThat(order.isValid()).isFalse()
    }

    @Test
    internal fun `should not be a valid order if quantity is negative`() {
        val order = Order("my-user-id", -10, BigDecimal(12.5), OrderType.BUY)

        assertThat(order.isValid()).isFalse()
    }

    @Test
    internal fun `should not be a valid order if price is negative`() {
        val order = Order("my-user-id", 10, BigDecimal(-12.5), OrderType.BUY)

        assertThat(order.isValid()).isFalse()
    }

    @Test
    internal fun `should not be a valid order if price is zero`() {
        val order = Order("my-user-id", 10, BigDecimal(0.0), OrderType.BUY)

        assertThat(order.isValid()).isFalse()
    }

    @Test
    internal fun `should show summary for a buy order`() {
        val order = Order("my-user-id", 10, BigDecimal(12.5), OrderType.BUY)

        assertThat(order.summary()).isEqualTo("BUY: 10 kg for £12.50")
    }

    @Test
    internal fun `should show summary for a sell order`() {
        val order = Order("my-user-id", 10, BigDecimal(12.5), OrderType.SELL)

        assertThat(order.summary()).isEqualTo("SELL: 10 kg for £12.50")
    }
}
