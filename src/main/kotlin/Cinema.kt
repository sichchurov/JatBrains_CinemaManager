const val SEAT_PRICE_FRONT = 10
const val SEAT_PRICE_BACK = 8

fun main() {

    val inputList: MutableList<MutableList<String>> = mutableListOf()
    var numberOfPurchasedTickets = 0
    var currentIncome = 0
    var purchasedTicketsAsPercent = 0.0
    var bad = false

    println("Enter the number of rows:")
    val rows = readln().toInt()
    println("Enter the number of seats in each row:")
    val seats = readln().toInt()

    for (i in 0..rows) {
        inputList.add("S ".repeat(seats).trim().split(" ").toMutableList())
        inputList[i].add(0, "$i")
        repeat(seats) {
            inputList[0] = MutableList(seats) { "${it.plus(1)}" }
            inputList[0].add(0, " ")
        }
    }

    while (true) {
        println(
            """
            
            1. Show the seats
            2. Buy a ticket
            3. Statistics
            0. Exit
            
            """.trimIndent()
        )
        when (readln().toInt()) {
            1 -> {
                println("Cinema:")
                inputList.forEach { println(it.joinToString(" ")) }
            }

            2 -> {
                while (!bad) {
                    try {
                        println("Enter a row number:")
                        val selectedRow = readln().toInt()
                        println("Enter a seat number in that row:")
                        val selectedSeat = readln().toInt()

                        when {
                            selectedRow == 0 || selectedSeat == 0 -> println("Wrong input!")
                            inputList[selectedRow][selectedSeat] == "B" -> println("That ticket has already been purchased!")
                            else -> {
                                bad = true

                                inputList[selectedRow][selectedSeat] = "B"

                                when (rows * seats) {
                                    in 1..60 -> {
                                        currentIncome += SEAT_PRICE_FRONT
                                        println("Ticket price: \$$SEAT_PRICE_FRONT")
                                    }

                                    in 61..81 -> {
                                        when (selectedRow) {
                                            in 0..rows / 2 -> {
                                                currentIncome += SEAT_PRICE_FRONT
                                                println("Ticket price: \$$SEAT_PRICE_FRONT")
                                            }

                                            in rows / 2..rows -> {
                                                currentIncome += SEAT_PRICE_BACK
                                                println("Ticket price: \$$SEAT_PRICE_BACK")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        println("Wrong input!")
                    }
                }
                bad = false

                numberOfPurchasedTickets++
                purchasedTicketsAsPercent = (100.0 / (rows * seats)) * numberOfPurchasedTickets
            }

            3 -> {
                println("Number of purchased tickets: $numberOfPurchasedTickets")
                println("Percentage: ${"%.2f".format(purchasedTicketsAsPercent)}%")
                println("Current income: \$$currentIncome")
                println("Total income: \$${getTotalIncome(inputList)}")
            }

            0 -> return
        }
    }
}

fun getTotalIncome(inputList: MutableList<MutableList<String>>): Int {
    return when (val totalSeats = inputList.subList(1, inputList.size)
        .sumOf { it.subList(1, it.size).size }) { // total seats without seats numbers
        in 1..60 -> SEAT_PRICE_FRONT * totalSeats

        in 61..81 -> {
            inputList.subList(1, inputList.size.plus(1) / 2)
                .sumOf { it.subList(1, it.size).size } * SEAT_PRICE_FRONT +

                    inputList.subList(inputList.size.plus(1) / 2, inputList.size)
                        .sumOf { it.subList(1, it.size).size } * SEAT_PRICE_BACK
        }

        else -> 0
    }
}