package antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Test;

public class Java8ParserIntegrationTest {

	@Test
	public void test() {
		Java8Lexer lexer = new Java8Lexer(
				CharStreams.fromString("/* comment */ public class SampleClass { void DoSomething(){} }"));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		Java8Parser parser = new Java8Parser(tokens);
		ParseTree tree = parser.compilationUnit();
		ParseTreeWalker walker = new ParseTreeWalker();
		Java8BaseListener listener = new Java8BaseListener() {
			@Override
			public void enterClassType(Java8Parser.ClassTypeContext ctx) {
				System.out.println("1:" + ctx.getText());
			}

			@Override
			public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
				System.out.println("2:" + ctx.getText());
			}

			@Override
			public void enterClassType_lf_classOrInterfaceType(
					Java8Parser.ClassType_lf_classOrInterfaceTypeContext ctx) {
				System.out.println("3:" + ctx.getText());
			}

			@Override
			public void exitClassType_lf_classOrInterfaceType(
					Java8Parser.ClassType_lf_classOrInterfaceTypeContext ctx) {
				System.out.println("4:" + ctx.getText());
			}

			@Override
			public void exitClassOrInterfaceType(Java8Parser.ClassOrInterfaceTypeContext ctx) {
				System.out.println("5:" + ctx.getText());
			}

			@Override
			public void enterClassBody(Java8Parser.ClassBodyContext ctx) {
				System.out.println("6:" + ctx.getText());
			}

			@Override
			public void enterClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
				System.out.println("7:" + ctx.getText());
			}

		};
		walker.walk(listener, tree);
	}

}