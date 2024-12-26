# Terminologies

## Chapter 4
### Basic
- Scanner (Lexical Analyzer): 1st part of the compilers, recognizes tokens from source code
- lexeme: a smallest unit of meaning in a programming langauge
- token: lexeme + type (classifications)
- literal: a fixed value directly written in the source code
- Maximal Munch Principle: at each step, the lexer should consume the longest possible sequence of characters that form a valid token
### Advanced
- Chomsky Hierarchy: a classification of formal languages, 4 types
  - Type 0: Recursively Enumerable Languages
  - Type 1: Context-Sensitive Languages
  - Type 2: Context-Free Languages
  - Type 3: Regular Languages
- Formal Language: a set of strings composed of symbols from a defined alphabet, constructed according to specific rules or grammar
- Finite-State Machine: a theoretical models that can recognize `Regular Languages`
- Turing Machine: a theoretical model that can recognize `Context-Free Languages`