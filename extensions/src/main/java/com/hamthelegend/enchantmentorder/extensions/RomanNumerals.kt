package com.hamthelegend.enchantmentorder.extensions

import kotlin.math.pow

fun Int.toRomanNumerals(): String {

    if (this > 3_999) return toString()

    val string = toString()

    val romanNumeralEquivalence = listOf(
        1 to 'I',
        5 to 'V',
        10 to 'X',
        50 to 'L',
        100 to 'C',
        500 to 'D',
        1000 to 'M'
    )

    fun findEquivalent(coefficient: Int, place: Int) =
        romanNumeralEquivalence.filter { it.first == coefficient * 10.00.pow(place).toInt() }[0].second

    var romanNumerals = ""
    var place = string.length - 1
    for (digit in string) {
        val number = digit.toString().toInt()
        when {
            number <= 3 -> {
                for (j in 1..number) {
                    romanNumerals += findEquivalent(1, place)
                }
            }
            number == 4 -> {
                romanNumerals += findEquivalent(1, place)
                romanNumerals += findEquivalent(5, place)
            }
            number == 5 -> {
                romanNumerals += findEquivalent(5, place)
            }
            number <=8 -> {
                romanNumerals += findEquivalent(5, place)
                for (j in 6..number) {
                    romanNumerals += findEquivalent(1, place)
                }
            }
            number == 9 -> {
                romanNumerals += findEquivalent(1, place)
                romanNumerals += findEquivalent(10, place)
            }
        }
        place--
    }
    return romanNumerals

}