package com.jinkyumpark.craftingInterpreters.kotlin

class Scanner(
    private val source: String,
) {
    private val tokens: MutableList<Token> = mutableListOf()
    private var start: Int = 0
    private var current: Int = 0
    private var lineNumber: Int = 1

    private val isAtEnd get() = current >= source.length

    fun scanAndGetTokens(): List<Token> {
        while (!isAtEnd) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "", null, lineNumber))
        return tokens
    }

    private fun scanToken() {
        when (val c = advance()) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)

            '!' -> addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            '=' -> addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '<' -> addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)

            '/' -> if (match('/')) skipLineComment() else addToken(TokenType.SLASH)

            ' ', '\r', '\t' -> {} // 의미 없는 공백은 무시

            '\n' -> lineNumber++

            '"' -> processAsStringAndAddToTokens()

            else -> when {
                c.isDigit() -> processAsNumberAndAddToTokens()
                c.isAlphabet() -> processAsIdentifierAndAddToTokens()
                else -> Lox.reportError(lineNumber, "Unexpected character.")
            }
        }
    }

    private fun advance(): Char = source[current++]
    private fun peek() = if (isAtEnd) '\u0000' else source[current]
    private fun peekNext() = if (current + 1 >= source.length) '\u0000' else source[current + 1]
    private fun match(expected: Char): Boolean = !isAtEnd && source[current] == expected && run { current++; true }

    private fun addToken(type: TokenType, literal: Any? = null) {
        tokens += Token(type, source.substring(start, current), literal, lineNumber)
    }

    private fun skipLineComment() {
        while (peek() != '\n' && !isAtEnd) advance()
    }

    private fun processAsStringAndAddToTokens() {
        while (peek() != '"' && !isAtEnd) {
            if (peek() == '\n') lineNumber++ // Multi-line String 처리
            advance()
        }

        if (isAtEnd) {
            Lox.reportError(lineNumber, "Unterminated string.")
            return
        }

        advance()

        val value = source.substring(start + 1, current - 1)
        addToken(TokenType.STRING, value)
    }

    private fun processAsNumberAndAddToTokens() {
        while (peek().isDigit()) advance()

        // 소수점 처리
        if (peek() == '.' && peekNext().isDigit()) {
            do advance()
            while (peek().isDigit())
        }

        val value = source.substring(start, current).toDouble()
        addToken(TokenType.NUMBER, value)
    }

    private fun processAsIdentifierAndAddToTokens() {
        while (peek().isAlphanumeric()) advance()

        val value = source.substring(start, current)
        val type = KEYWORDS[value] ?: TokenType.IDENTIFIER
        addToken(type)
    }

    companion object {
        private val KEYWORDS = mapOf(
            "and" to TokenType.AND,
            "class" to TokenType.CLASS,
            "else" to TokenType.ELSE,
            "false" to TokenType.FALSE,
            "fun" to TokenType.FUN,
            "for" to TokenType.FOR,
            "if" to TokenType.IF,
            "nil" to TokenType.NIL,
            "or" to TokenType.OR,
            "print" to TokenType.PRINT,
            "return" to TokenType.RETURN,
            "super" to TokenType.SUPER,
            "this" to TokenType.THIS,
            "true" to TokenType.TRUE,
            "var" to TokenType.VAR,
            "while" to TokenType.WHILE,
        )
    }
}

private fun Char.isDigit() = this in '0'..'9'
private fun Char.isAlphabet() = this in 'a'..'z' || this in 'A'..'Z' || this == '_'
private fun Char.isAlphanumeric() = isAlphabet() || isDigit()
