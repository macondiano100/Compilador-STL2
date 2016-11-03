package compilador;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

import antlr4.ProgramaBaseVisitor;
import antlr4.ProgramaParser.AsignacionContext;
import antlr4.ProgramaParser.DeclaracionContext;
import antlr4.ProgramaParser.ID_VALContext;
import compilador.tabsim.TablaSimbolos;
import compilador.tabsim.simbolo.Simbolo_Builder;

public class Semantic_Visitor extends ProgramaBaseVisitor<Boolean>{
	private TablaSimbolos tabsim=new TablaSimbolos();
	private List<Error_info> errores_semanticos=new ArrayList<>();
	@Override
	public Boolean visitDeclaracion(DeclaracionContext ctx) {
		Token id= ctx.ID().getSymbol();
		if(!tabsim.currentScope().resolve(id.getText()).isPresent())
		{
			Simbolo_Builder simbolo_Builder=new Simbolo_Builder();
			tabsim.currentScope().define(simbolo_Builder.token(ctx.ID().getText()).get());
		}
		else{
			System.out.println("Here");
			errores_semanticos.add(new Error_info(Paths.get(id.getTokenSource().getSourceName()), id.getLine(), id.getCharPositionInLine()+1,
					"La variable: "+id.getText()+" ya existe en este ámbito"));
		}
		return super.visitDeclaracion(ctx);
	}
	@Override
	public Boolean visitID_VAL(ID_VALContext ctx) {
		Token id=ctx.ID().getSymbol();
		if(!tabsim.currentScope().resolve(id.getText()).isPresent())
			errores_semanticos.add(new Error_info(Paths.get(id.getTokenSource().getSourceName()), id.getLine(), id.getCharPositionInLine()+1,
					"La variable: "+id.getText()+" no existe en este ámbito"));
		return super.visitID_VAL(ctx);
	}
	@Override
	public Boolean visitAsignacion(AsignacionContext ctx) {
		Token id=ctx.ID().getSymbol();
		if(!tabsim.currentScope().resolve(id.getText()).isPresent())
			errores_semanticos.add(new Error_info(Paths.get(id.getTokenSource().getSourceName()), id.getLine(), id.getCharPositionInLine()+1,
					"La variable: "+id.getText()+" no existe en este ámbito"));
		return super.visitAsignacion(ctx);
	}
	public TablaSimbolos getTabsim() {
		return tabsim;
	}
	public List<Error_info> get_errores()
	{
		return errores_semanticos;
	}
}
