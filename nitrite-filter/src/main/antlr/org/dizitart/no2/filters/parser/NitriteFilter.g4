
grammar NitriteFilter;

@header {
	package org.dizitart.no2.filters.parser;
}

filter
   : multiply (OR multiply)*
   | EOF
   ;

multiply
   : NOT atom
   | atom (AND atom)*
   ;


atom
	: exp
	 | LPAREN filter RPAREN
	 ;

filterOp
	: EQ
	| NEQ
	| LT
	| GT
	| GTE
	| LTE
	;
	
constant
	: SCIENTIFIC_NUMBER
	| date_expression
	| STRING
	| BOOLEAN
	;

array
	: LBRACKET constant ( COMMA constant )* RBRACKET
	;

arrayOp
	: IN
	| NOTIN
	;


exp
   : variable filterExp
   ;


filterExp
	: FIND STRING
	| MATCH STRING
	| CONTAINS LPAREN filter RPAREN
	| filterOp constant
	| arrayOp array
	;
	

variable
   : VARIABLE
   ;


BOOLEAN
   : TRUE
   | FALSE
   ;

STRING
: '"' (ESC | ~ ["\\])* '"'
;	


fragment ESC
: '\\' (["\\/bfnrt] | UNICODE)
;
fragment UNICODE
: 'u' HEX HEX HEX HEX
;
fragment HEX
: [0–9a-fA-F]
;


// "2010-01-01T12:00:00+01:00";

DATE
    : '#' FOUR_DIGITS MINUS TWO_DIGITS MINUS TWO_DIGITS 'T' TWO_DIGITS TIME_SEPARATOR TWO_DIGITS TIME_SEPARATOR TWO_DIGITS PLUS TWO_DIGITS TIME_SEPARATOR TWO_DIGITS '#' 
    | 'NOW'
    ;


DURATION
	: UNSIGNED_INTEGER DURATION_QUALIFIER
	;


duration_expression
	: DURATION
	| DURATION DATE_OPERATOR duration_expression
	;

DATE_OPERATOR
	: PLUS
	| MINUS
	;

date_expression
	: LPAREN date_expression RPAREN
	| DATE 
	| date_expression DATE_OPERATOR duration_expression
	;


SCIENTIFIC_NUMBER
   : NUMBER (E SIGN? UNSIGNED_INTEGER)?
   ;


FOUR_DIGITS: DIGIT DIGIT DIGIT DIGIT;
TWO_DIGITS:  DIGIT DIGIT;
TIME_SEPARATOR:       ':';


fragment DIGIT: [0-9];

fragment DURATION_QUALIFIER
	: 'Y' 
	| 'M' 
	| 'D' 
	| 'h' 
	| 'm' 
	| 's'
	;
	
fragment NUMBER
   : DIGIT+ ('.' DIGIT+)?
   ;

fragment UNSIGNED_INTEGER
   : DIGIT+
   ;


fragment E
   : 'E' | 'e'
   ;


fragment SIGN
   : ('+' | '-')
   ;



LBRACKET
	: '['
	;
	
	RBRACKET
	: ']'
	;

LPAREN
   : '('
   ;


RPAREN
   : ')'
   ;


NEQ
   : '<>'
   ;

GT
   : '>'
   ;

GTE
	:'>='
	;

LT
   : '<'
   ;

LTE
	:'<='
	;

EQ
   : '='
   ;




COMMA
   : ','
   ;

MINUS
   : '-'
   ;


PLUS
   : '+'
   ;


ALL
   : 'ALL'
   ;


AND
   : 'AND'
   ;

OR
   : 'OR'
   ;
   
NOT
	: 'NOT'
	;

IN
	: 'IN'
	;
	
NOTIN
	: 'NOTIN'
	;


MATCH
	: 'MATCH'
	;
FIND
	: 'FIND'
	;

CONTAINS
	: 'CONTAINS'
	;

TRUE
    : 'true'
    ;
FALSE
	: 'false'
	;

VARIABLE
   : VALID_ID_START VALID_ID_CHAR*
   ;


fragment VALID_ID_START
   : ('a' .. 'z') | ('A' .. 'Z') | '_' | DOLLAR
   ;


fragment VALID_ID_CHAR
   : VALID_ID_START | ('0' .. '9')
   ;

DOLLAR
	: '$'
	;


WS
   : [ \r\n\t] + -> skip
   ;
