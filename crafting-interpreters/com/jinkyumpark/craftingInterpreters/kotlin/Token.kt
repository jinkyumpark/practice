package com.jinkyumpark.craftingInterpreters.kotlin

data class Token(
    val type: TokenType,
    val lexeme: String,
    val literal: Any?,
    val lineNumber: Int,
) {
    override fun toString() = "$type $lexeme $literal"
}
