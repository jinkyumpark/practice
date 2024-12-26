package com.jinkyumpark.craftingInterpreters.kotlin

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

class Lox {
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            runPrompt()
        }

        if (args.size == 1) {
            runFile(args.first())
        }

        println("Usage: jlox [script]")
        exitProcess(64)
    }

    private fun runPrompt() {
        val reader = BufferedReader(InputStreamReader(System.`in`))

        while (true) {
            println("> ")

            val line = reader.readLine() ?: break
            run(line)

            isHadError = false
        }
    }

    private fun runFile(path: String) {
        val bytes = Files.readAllBytes(Paths.get(path))
        run(String(bytes, Charsets.UTF_8))
        if (isHadError) exitProcess(65)
    }

    private fun run(source: String) {
        val scanner = Scanner(source)
        val tokens = scanner.scanAndGetTokens()

        for (token in tokens) {
            println(token)
        }
    }

    companion object {
        fun reportError(line: Int, message: String) {
            System.err.printf("[line $line] Error: $message")
            isHadError = true
        }

        private var isHadError = false
    }
}