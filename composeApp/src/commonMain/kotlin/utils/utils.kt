package utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

fun generateRandomKey(): String {
    val charPool = ('a' .. 'z') + ('A' .. 'Z') + ('0' .. '9')
    val keySize = 32
    val key = (1 .. keySize)
        .map { Random.nextInt(0,charPool.size).let { charPool[it] } }
        .joinToString("")

    return key
}

fun currentDateTimeToString():String{
   val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return "${formatTwoDigits(currentDateTime.dayOfMonth)}-${formatMonthAbbreviation(currentDateTime.monthNumber)}-${currentDateTime.year} " +
            "${format12Hour(currentDateTime.hour)}:${formatTwoDigits(currentDateTime.minute)} ${if (currentDateTime.hour < 12) "AM" else "PM"}"

}
// Helper function to format a number with two digits
fun formatTwoDigits(number: Int): String {
    return if (number < 10) "0$number" else number.toString()
}

// Helper function to get the three-letter abbreviation of a month
fun formatMonthAbbreviation(monthValue: Int): String {
    val monthsAbbreviation = arrayOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    return monthsAbbreviation[monthValue]
}
fun format12Hour(hour: Int): Int {
    return if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
}

fun String.capitalizeFirstLetter(): String {
    if (this.isEmpty()) {
        return this
    }

    val firstChar = this[0].uppercaseChar()
    return "$firstChar${this.substring(1)}"
}

fun String.isValidApiKey(): Boolean {
    return "AIza[0-9A-Za-z-_]{35}".toRegex().matches(this)
}
