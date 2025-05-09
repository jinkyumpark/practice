package com.jinkyumpark.craftingInterpreters.kotlin

enum class TokenType {
    // 단일 문자 토큰
    LEFT_PARENTHESIS, RIGHT_PARENTHESIS, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // 문자 1개 또는 2개짜리 토큰
    BANG, // !
    BANG_EQUAL, // !=
    EQUAL, // =
    EQUAL_EQUAL, // ==
    GREATER, // >
    GREATER_EQUAL, // >=
    LESS, // <
    LESS_EQUAL, // <=

    // 리터럴
    IDENTIFIER, STRING, NUMBER,

    // 키워드
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NULL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

    // 기타
    EOF,
}
