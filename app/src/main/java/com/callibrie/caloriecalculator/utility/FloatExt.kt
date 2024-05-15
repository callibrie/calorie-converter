package com.callibrie.caloriecalculator.utility

import java.math.RoundingMode

fun Float.roundToOneDecimal(): Float = this.toBigDecimal()
    .setScale(1, RoundingMode.HALF_UP).toFloat()

fun Float.toNonZeroString(): String = if (this > 0) {
    this.toString()
} else {
    ""
}
