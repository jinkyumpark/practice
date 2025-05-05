package com.jinkyumpark.craftingInterpreters.kotlin

class Parser(
    private val tokens: List<Token>,
    private var current: Int = 0,
) {
    fun parse(): Expr? {
        return try {
            expression()
        } catch (e: ParseError) {
            null
        }
    }

    private fun expression(): Expr = equality()

    private fun equality(): Expr {
        var expr = comparison()

        while (tokens.advanceIfAnyMatch(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            val operator = tokens.getPrevious()
            val right = comparison()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun comparison(): Expr {
        var expr = term()

        while (tokens.advanceIfAnyMatch(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            val operator = tokens.getPrevious()
            val right = term()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun term(): Expr {
        var expr = factor()

        while (tokens.advanceIfAnyMatch(TokenType.MINUS, TokenType.PLUS)) {
            val operator = tokens.getPrevious()
            val right = factor()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun factor(): Expr {
        var expr = unary()

        while (tokens.advanceIfAnyMatch(TokenType.SLASH, TokenType.STAR)) {
            val operator = tokens.getPrevious()
            val right = unary()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun unary(): Expr {
        if (tokens.advanceIfAnyMatch(TokenType.BANG, TokenType.MINUS)) { // Unary는 `!`, `-`만 가능
            val operator = tokens.getPrevious()
            val right = unary()
            return Expr.Unary(operator, right)
        }

        return primary()
    }

    private fun primary(): Expr {
        return when {
            tokens.advanceIfAnyMatch(TokenType.FALSE) -> Expr.Literal(false)
            tokens.advanceIfAnyMatch(TokenType.TRUE) -> Expr.Literal(true)
            tokens.advanceIfAnyMatch(TokenType.NULL) -> Expr.Literal(null)
            tokens.advanceIfAnyMatch(TokenType.NUMBER, TokenType.STRING) -> Expr.Literal(tokens.getPrevious().literal)

            tokens.advanceIfAnyMatch(TokenType.LEFT_PARENTHESIS) -> {
                val expr = expression()
                tokens.advanceIfTokenTypeEqualsToOrThrow(TokenType.RIGHT_PARENTHESIS, "Expect ')' after expression.")
                return Expr.Grouping(expr)
            }

            else -> {
                Lox.reportError(tokens.getCurrent(), "Expect expression.")
                throw ParseError()
            }
        }
    }

    private fun List<Token>.getCurrent() = this[current]
    private fun List<Token>.getPrevious() = this[current - 1]
    private fun List<Token>.isAtEnd() = this.getPrevious().type == TokenType.EOF
    private fun List<Token>.advanceAndGet() = this[++current]
    private fun List<Token>.isCurrentTypeEqualsTo(tokenType: TokenType): Boolean {
        if (this.isAtEnd()) return false
        return this.getCurrent().type == tokenType
    }

    private fun List<Token>.advanceIfAnyMatch(vararg types: TokenType): Boolean {
        for (type in types) {
            if (this.isCurrentTypeEqualsTo(type)) {
                this.advanceAndGet()
                return true
            }
        }

        return false
    }

    private fun List<Token>.advanceIfTokenTypeEqualsToOrThrow(tokenType: TokenType, errorMessage: String): Token {
        if (this.isCurrentTypeEqualsTo(tokenType)) return this.advanceAndGet()

        Lox.reportError(tokens.getCurrent(), errorMessage)
        throw ParseError()
    }
}

class ParseError : RuntimeException()

