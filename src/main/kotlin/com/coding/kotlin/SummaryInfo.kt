package com.coding.kotlin

class SummaryInfo() {
    private val summaryInfo: MutableList<String> = mutableListOf()

    constructor(vararg items: String): this() {
        summaryInfo.addAll(items.asList())
    }

    override fun toString(): String {
        return summaryInfo.joinToString("\n");
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SummaryInfo

        if (summaryInfo != other.summaryInfo) return false

        return true
    }

    override fun hashCode(): Int {
        return summaryInfo.hashCode()
    }
}
