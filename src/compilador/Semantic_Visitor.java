package compilador;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.antlr.v4.runtime.Token;

import antlr4.ProgramaBaseVisitor;
import antlr4.ProgramaLexer;
import antlr4.ProgramaParser.AsignacionContext;
import antlr4.ProgramaParser.CHARContext;
import antlr4.ProgramaParser.DeclaracionContext;
import antlr4.ProgramaParser.EXPR_ANDContext;
import antlr4.ProgramaParser.EXPR_MULContext;
import antlr4.ProgramaParser.EXPR_ORContext;
import antlr4.ProgramaParser.EXPR_RELContext;
import antlr4.ProgramaParser.EXPR_SUMContext;
import antlr4.ProgramaParser.EXPR_VALUEContext;
import antlr4.ProgramaParser.ExprContext;
import antlr4.ProgramaParser.FLOATContext;
import antlr4.ProgramaParser.ID_VALContext;
import antlr4.ProgramaParser.INTContext;
import antlr4.ProgramaParser.PARENS_EXPRContext;
import antlr4.ProgramaParser.STRINGContext;
import compilador.tabsim.Scope;
import compilador.tabsim.TablaSimbolos;
import compilador.tabsim.simbolo.Simbolo;
import compilador.tabsim.simbolo.Simbolo_Builder;
public class Semantic_Visitor extends ProgramaBaseVisitor<Boolean>{
	private TablaSimbolos tabsim=new TablaSimbolos();
	private static Map<Integer,TIPO_DATO> TIPOS_DATOS;
	static{
		TIPOS_DATOS=new HashMap<>();
		TIPOS_DATOS.put(ProgramaLexer.BOOLEANO_T, TIPO_DATO.BOOLEANO);
		TIPOS_DATOS.put(ProgramaLexer.CARACTER_T, TIPO_DATO.CARACTER);
		TIPOS_DATOS.put(ProgramaLexer.ENTERO_T, TIPO_DATO.ENTERO);
		TIPOS_DATOS.put(ProgramaLexer.REAL_T, TIPO_DATO.REAL);
		TIPOS_DATOS.put(ProgramaLexer.LARGO_T, TIPO_DATO.LARGO);
		TIPOS_DATOS.put(ProgramaLexer.DOBLE_T, TIPO_DATO.DOBLE);
		TIPOS_DATOS.put(ProgramaLexer.VACIO_T, TIPO_DATO.VACIO);
		
	}
	private void aniade_error(Token token,String mensaje)
	{
		errores_semanticos.add(new Error_info(Paths.get(token.getTokenSource().getSourceName()),
				token.getLine(), token.getCharPositionInLine()+1,mensaje));
	}
	private List<Error_info> errores_semanticos=new ArrayList<>();
	@Override
	public Boolean visitDeclaracion(DeclaracionContext ctx) {
		super.visitDeclaracion(ctx);
		Token id= ctx.ID().getSymbol();
		if(!tabsim.currentScope().resolve(id.getText()).isPresent())
		{
			TIPO_DATO tipo=TIPOS_DATOS.get(ctx.tipo_dato().getStart().getType());
			if(tipo==TIPO_DATO.VACIO) aniade_error(id, "Declarando variable como vacio");
			else {
				Simbolo_Builder simbolo_Builder=new Simbolo_Builder();
				simbolo_Builder.
				token(ctx.ID().getText())
				.scope(tabsim.currentScope())
				.path(Paths.get(id.getTokenSource().getSourceName()).toAbsolutePath());
				tabsim.currentScope().define(simbolo_Builder.get());
				if(ctx.expr()!=null)
				{
					if(ctx.expr().tipo==tipo){
						
					}
					else{
						aniade_error(ctx.expr().start,"Conflicto de tipos en declaracion");
					}
				}
			}
		}
		else{
			aniade_error(id,"La variable: "+id.getText()+" ya existe en este �mbito");
		}
		return true;
	}
	@Override
	public Boolean visitID_VAL(ID_VALContext ctx) {
		super.visitID_VAL(ctx);
		Token id=ctx.ID().getSymbol();
		Scope curr_scope=tabsim.currentScope();
		Optional<Simbolo> fetched_simbol=curr_scope.resolve(id.getText());
		if(fetched_simbol.isPresent())
			ctx.tipo=fetched_simbol.get().getTipoIdenficador();
		else 
			aniade_error(id,"La variable "+id.getText()+" no ha sido declarada en este ambito.");
		return true;
	}
	@Override
	public Boolean visitFLOAT(FLOATContext ctx) {
		ctx.tipo=TIPO_DATO.REAL;
		return super.visitFLOAT(ctx);
	}
	@Override
	public Boolean visitCHAR(CHARContext ctx) {
		ctx.tipo=TIPO_DATO.CARACTER;
		return super.visitCHAR(ctx);
	}
	@Override
	public Boolean visitSTRING(STRINGContext ctx) {
		ctx.tipo=TIPO_DATO.CADENA;
		return super.visitSTRING(ctx);
	}	
	@Override
	public Boolean visitINT(INTContext ctx) {
		ctx.tipo=TIPO_DATO.ENTERO;
		return super.visitINT(ctx);
	}
	@Override
	public Boolean visitAsignacion(AsignacionContext ctx) {
		super.visitAsignacion(ctx);
		Token id=ctx.ID().getSymbol();
		Optional<Simbolo> fetched_symbol=tabsim.currentScope().resolve(id.getText());
		if(fetched_symbol.isPresent()){
			if(fetched_symbol.get().getTipoIdenficador()==ctx.expr().tipo){
				
			}
			else aniade_error(ctx.expr().start, "Asignacion entre tipos incompatibles");
		}
		else errores_semanticos.add(new Error_info(Paths.get(id.getTokenSource().getSourceName()), id.getLine(), id.getCharPositionInLine()+1,
					"La variable: "+id.getText()+" no existe en este �mbito"));
		return true;
	}
	@Override
	public Boolean visitPARENS_EXPR(PARENS_EXPRContext ctx) {
		super.visitPARENS_EXPR(ctx);
		ctx.tipo=ctx.expr().tipo;
		return true;
	}
	@Override
	public Boolean visitEXPR_SUM(EXPR_SUMContext ctx) {
		super.visitEXPR_SUM(ctx);
		ExprContext expr1=ctx.expr(0),expr2=ctx.expr(1);
		if(expr1.tipo==expr2.tipo) {
			ctx.tipo=expr1.tipo;
		}
		else aniade_error(ctx.start,"Suma/resta: Los tipos de los operandos no som compatibles.");
		return true;
	}
	@Override
	public Boolean visitEXPR_MUL(EXPR_MULContext ctx) {
		super.visitEXPR_MUL(ctx);
		ExprContext expr1=ctx.expr(0),expr2=ctx.expr(1);
		if(expr1.tipo==expr2.tipo) {
			ctx.tipo=expr1.tipo;
		}
		else aniade_error(ctx.start,"Multiplicacion/division: Los tipos de los operandos no som compatibles.");
		return true;
	}
	@Override
	public Boolean visitEXPR_REL(EXPR_RELContext ctx) {
		super.visitEXPR_REL(ctx);
		ExprContext expr1=ctx.expr(0),expr2=ctx.expr(1);
		if(expr1.tipo==expr2.tipo){
			ctx.tipo=TIPO_DATO.BOOLEANO;
		}
		else aniade_error(ctx.start,"Comparasion: Los tipos de los operandos no som compatibles.");
		return true;
	}
	@Override
	public Boolean visitEXPR_VALUE(EXPR_VALUEContext ctx) {
		super.visitEXPR_VALUE(ctx);
		ctx.tipo=ctx.val().tipo;
		return true;
	}
	@Override
	public Boolean visitEXPR_AND(EXPR_ANDContext ctx) {
		super.visitEXPR_AND(ctx);
		ExprContext expr1=ctx.expr(0),expr2=ctx.expr(1);
		if(expr1.tipo==TIPO_DATO.BOOLEANO &&expr1.tipo==expr2.tipo){
			ctx.tipo=TIPO_DATO.BOOLEANO;
		}
		else aniade_error(ctx.start,"AND: Los tipos de los operandos no som compatibles.");
		return true;
	}
	@Override
	public Boolean visitEXPR_OR(EXPR_ORContext ctx) {
		super.visitEXPR_OR(ctx);
		ExprContext expr1=ctx.expr(0),expr2=ctx.expr(1);
		if(expr1.tipo==TIPO_DATO.BOOLEANO &&expr1.tipo==expr2.tipo){
			ctx.tipo=TIPO_DATO.BOOLEANO;
		}
		else aniade_error(ctx.start,"AND: Los tipos de los operandos no som compatibles.");
		return true;
	}
	public TablaSimbolos getTabsim() {
		return tabsim;
	}
	public List<Error_info> get_errores()
	{
		return errores_semanticos;
	}
}
