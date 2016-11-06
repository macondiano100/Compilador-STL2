/**
 * Define a grammar called Hello
 */
grammar Programa; 
import LexerRules;
@parser::header{
	package antlr4; 
	import compilador.TIPO_DATO;
}
bloque: (sentencia NEWLINE)* ;
sentencia:
	declaracion|
	asignacion|
	condicional|
	expr;
	
declaracion:tipo_dato ID (OP_ASIGNACION expr)?;
asignacion: ID OP_ASIGNACION expr;
expr returns[compilador.TIPO_DATO tipo]:
	expr OP_MUL expr	#EXPR_MUL
	|expr OP_SUMA expr	#EXPR_SUM 
	|expr op_rel expr	#EXPR_REL
	|expr OP_AND expr 	#EXPR_AND
	|expr OP_OR expr	#EXPR_OR
	|'(' expr ')' #PARENS_EXPR
	|val				#EXPR_VALUE;	
constante:
	'define' ID OP_ASIGNACION expr;
val returns[compilador.TIPO_DATO tipo]
	: FLOAT	#FLOAT
	|CHAR	#CHAR
	|STRING	#STRING
	|INT	#INT
	|ID		#ID_VAL
	;
	
condicional:
	SI expr ENTONCES
	bloque
	ELSE
	bloque
	TERMINA_SI;
	
tipo_dato:BOOLEANO_T|ENTERO_T|CARACTER_T|REAL_T|LARGO_T|DOBLE_T|VACIO_T; 

//conjuntos
op_rel:OP_GT|OP_GTE|OP_LT|OP_LTE|OP_EQUAL;



