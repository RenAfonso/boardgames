package com.codelayers.boardgames

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BoardgamesApplication

fun main(args: Array<String>) {
    runApplication<BoardgamesApplication>(*args)
}
