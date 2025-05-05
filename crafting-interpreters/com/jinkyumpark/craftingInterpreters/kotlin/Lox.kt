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

        val parser = Parser(tokens)
        val expression = parser.parse()

        if (isHadError) return

        for (token in tokens) {
            println(token)
        }

        println(expression)
    }

    companion object {
        fun reportError(line: Int, where: String, message: String) {
            System.err.printf("[line $line] Error $where: $message")
            isHadError = true
        }

        fun reportError(line: Int, message: String) = reportError(line, "", message)

        fun reportError(token: Token, message: String) {
            when (token.type) {
                TokenType.EOF -> reportError(token.lineNumber, "at end ", message)
                else -> reportError(token.lineNumber, "at '${token.lexeme}'", message)
            }
        }

        private var isHadError = false
    }
}