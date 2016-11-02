/**
 * Define a grammar called Hello
 */
grammar Programa; 
import LexerRules;
@parser::header{
	package antlr4; 
}
prog: (sentencia NEWLINE)* ;
sentencia:
	declaracion|
	asignacion|
	expr;
declaracion:tipo_dato ID ('=' expr)?;
asignacion: ID '=' expr;
expr:  
	expr OP_MUL expr	#MUL
	|expr OP_SUMA expr	#SUM
	|val				#VALUE;				
val: FLOAT	#FLOAT
	|CHAR	#CHAR
	|STRING	#STRING
	|INT	#INT
	|ID		#ID
	|'(' expr ')' #PARENS_EXPR
	;
tipo_dato:(BOOLEANO_T|ENTERO_T|CARACTER_T|REAL_T|LARGO_T|DOBLE_T|VACIO_T); 



