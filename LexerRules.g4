lexer grammar LexerRules;
@lexer::header{ 
	package antlr4;
	import compilador.Error_info;
	import java.util.ArrayList;
	import java.util.List; 
	import java.nio.file.Paths;
}
@lexer::members{
	private List<Error_info> errores_lexicos=new ArrayList<>();
	public List<Error_info> get_errores_lexicos(){
		return errores_lexicos;
	}
}
NEWLINE : ';'; 
//tipos de variables 
BOOLEANO_T:'booleano'; 
ENTERO_T:'entero'; 
CARACTER_T:'caracter';
REAL_T:'real';
LARGO_T:'largo';
DOBLE_T:'doble';
VACIO_T:'vacio';

//operadores
OP_SUMA:('+'|'-');
OP_MUL:('*'|'/');

//literales
INT: [0-9]+;		
FLOAT: [0-9]'.'[0-9];
CHAR: '\''.'\'';
STRING:'"'.*?'"';
ID: ([a-zA-Z_])([a-zA-Z0-9_]|'_')*;
WS : [ \t\r\n]+ -> skip ;//skipSpaces
ERROR:.+?{errores_lexicos.add(new Error_info(Paths.get(getSourceName()), getLine(), getCharPositionInLine(),"Caracter inesperado:"+getText()));};
