package cinema


fun main() {
    println("Enter the number of rows:")
    val rows = readLine()!!.toInt()
    println("Enter the number of seats in each row:")
    val columns = readLine()!!.toInt()
    val cinema = Cinema(rows, columns)

    while (true) {
        val menu = cinema.menu
        menu.show()
        when (readLine()!!) {
            "1" -> {
                cinema.showTheSeats()
            }
            "2" -> {
                while (true) {
                    try {
                        println("Enter a row number:")
                        val rowReserve = readLine()!!.toInt()
                        println("Enter a seat number in that row:")
                        val colReserve = readLine()!!.toInt()
                        cinema.reserveSeat(rowReserve, colReserve)
                        val price = cinema.calculateTicketPrice(rowReserve)
                        println("Ticket price: $$price")
                        break
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            }
            "3" -> {
                println("Number of purchased tickets: ${cinema.purchasedTickets()}")
                println("Percentage: ${"%.2f".format(cinema.percentagePurchased())}%")
                println("Current income: $${cinema.currentIncome()}")
                println("Total income: $${cinema.totalIncome()}")
            }
            "0" -> {
                break
            }
        }
    }
}

class Menu {
    fun show() {
        println("""
            1. Show the seats
            2. Buy a ticket
            3. Statistics
            0. Exit
        """.trimIndent())
    }

}

class Cinema(private val rows: Int, private val columns: Int) {
    private val seats = Array(rows) { CharArray(columns) {'S'} }
    private val totalSeats = rows * columns
    val menu: Menu = Menu()

    fun totalIncome(): Int {
        if (totalSeats < 60) {
            return 10 * rows * columns
        }
        val firstHalf = rows / 2
        val secondHalf = rows - firstHalf
        return 10 * firstHalf * columns +  8 * secondHalf * columns
    }

    fun calculateTicketPrice(seatRow: Int): Int {
        if (totalSeats < 60) {
            return 10
        }
        val firstHalf = rows / 2
        return if (seatRow <= firstHalf) {
            10
        } else {
            8
        }
    }

    fun reserveSeat(reserveRow: Int, reserveCol: Int) {
        if (reserveRow > rows || reserveCol > columns) {
            throw IllegalArgumentException("Wrong input!")
        }
        if (seats[reserveRow - 1][reserveCol - 1] == 'B') {
            throw IllegalArgumentException("That ticket has already been purchased")
        }
        seats[reserveRow - 1][reserveCol - 1] = 'B'
    }

    fun showTheSeats() {
        println("Cinema:")
        var header = " "
        for (i in 1..columns) {
            header += " $i"
        }
        println(header)
        var other = ""
        for (i in 1..rows) {
            other += "$i"
            for (j in 1 .. columns) {
                other += " " + seats[i - 1][j - 1]
            }
            other += "\n"
        }
        println(other)
    }

    fun purchasedTickets(): Int {
        return seats.flatMap { it.asIterable() }.count { it == 'B' }
    }

    fun percentagePurchased(): Double {
        return purchasedTickets() / totalSeats.toDouble() * 100
    }

    fun currentIncome(): Int {
        var sum = 0
        for (i in 1 .. rows) {
            for (j in 1 .. columns) {
                if (seats[i - 1][j - 1] == 'B') {
                    sum += calculateTicketPrice(i)
                }
            }
        }
        return sum
    }
}

