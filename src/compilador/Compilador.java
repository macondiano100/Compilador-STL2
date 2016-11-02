package compilador;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import antlr4.ProgramaLexer;
import antlr4.ProgramaParser;

public class Compilador {
	private Path file;
	private ProgramaParser parser;
	private ProgramaLexer lexer;
	private My_Syntax_Error_Listener my_Syntax_Error_Listener;
	public Compilador() {
	}
	public void inicia_parseo(Path _file) throws IOException
	{
		this.file=_file;
		lexer = new ProgramaLexer(new ANTLRFileStream(file.toString()));
		CommonTokenStream tokenStream=new CommonTokenStream(lexer);
		parser = new ProgramaParser(tokenStream);
		lexer.removeErrorListeners();
		parser.removeErrorListeners();
		my_Syntax_Error_Listener = new My_Syntax_Error_Listener();
		parser.addErrorListener(my_Syntax_Error_Listener);
		parser.prog();
	}
	public List<Error_info> get_errores_lexicos()
	{
		return lexer.get_errores_lexicos();
	}
	public List<Error_info> get_errores_sintacticos()
	{
		return my_Syntax_Error_Listener.get_errores();
	}
	private static class My_Syntax_Error_Listener extends BaseErrorListener{
		private List<Error_info> errores=new ArrayList<>();
		public List<Error_info> get_errores(){
			return errores;
		}
		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
				String msg, RecognitionException e) {
			CommonToken t=(CommonToken) offendingSymbol;
			errores.add(
					new Error_info(Paths.get(
							recognizer.getInputStream().getSourceName()),line, charPositionInLine+1, msg));
		}
	}
	public static void main(String[] args) throws IOException {
		Compilador c=new Compilador();
		c.inicia_parseo(Paths.get("archivo.txt").toAbsolutePath());
	}
}
