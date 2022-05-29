/**
 * 当语法变得更加复杂的时候，采用antlr4
 */


//package com.cocofhu.ctb.kernel.core.exec.compiler;
//
//import com.cocofhu.ctb.kernel.core.exec.compiler.antlr4.CExecutionCommandLexer;
//import com.cocofhu.ctb.kernel.core.exec.compiler.antlr4.CExecutionCommandListener;
//import com.cocofhu.ctb.kernel.core.exec.compiler.antlr4.CExecutionCommandParser;
//
//
//import org.antlr.v4.runtime.*;
//
//import org.antlr.v4.runtime.atn.PredictionMode;
//import org.antlr.v4.runtime.misc.Pair;
//import org.antlr.v4.runtime.tree.ErrorNode;
//import org.antlr.v4.runtime.tree.TerminalNode;
//
//public class CAntlr4ExecutorCompiler implements TokenFactory<CAntlr4ExecutorCompiler.CToken>, CExecutionCommandListener {
//
//    @Override
//    public void enterExecution(CExecutionCommandParser.ExecutionContext ctx) {
//
//    }
//
//    @Override
//    public void exitExecution(CExecutionCommandParser.ExecutionContext ctx) {
//        System.out.println(ctx.getChild(2) instanceof CExecutionCommandParser.RvalueContext);
//    }
//
//    @Override
//    public void enterRvalue(CExecutionCommandParser.RvalueContext ctx) {
//
//    }
//
//    @Override
//    public void exitRvalue(CExecutionCommandParser.RvalueContext ctx) {
//
//    }
//
//    @Override
//    public void enterComment(CExecutionCommandParser.CommentContext ctx) {
//
//    }
//
//    @Override
//    public void exitComment(CExecutionCommandParser.CommentContext ctx) {
//
//    }
//
//    @Override
//    public void enterProg(CExecutionCommandParser.ProgContext ctx) {
//
//    }
//
//    @Override
//    public void exitProg(CExecutionCommandParser.ProgContext ctx) {
//
//    }
//
//    @Override
//    public void enterCommand(CExecutionCommandParser.CommandContext ctx) {
//
//    }
//
//    @Override
//    public void exitCommand(CExecutionCommandParser.CommandContext ctx) {
////        System.out.println(ctx);
//    }
//
//    @Override
//    public void visitTerminal(TerminalNode node) {
//
//    }
//
//    @Override
//    public void visitErrorNode(ErrorNode node) {
//
//    }
//
//    @Override
//    public void enterEveryRule(ParserRuleContext ctx) {
//
//    }
//
//    @Override
//    public void exitEveryRule(ParserRuleContext ctx) {
//
//    }
//
//    static class CToken extends CommonToken {
//
//        public CToken(Pair<TokenSource, CharStream> source, int type, int channel, int start, int stop) {
//            super(source, type, channel, start, stop);
//        }
//
//        public CToken(int type, String text) {
//            super(type, text);
//        }
//
//        public CToken(Token oldToken) {
//            super(oldToken);
//        }
//    }
//
//    public static void main(String[] args) {
//        CharStream input = new ANTLRInputStream("power -as 123");
//        CExecutionCommandLexer lexer = new CExecutionCommandLexer(input);
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        CExecutionCommandParser parser = new CExecutionCommandParser(tokens);
//        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
//
//        CAntlr4ExecutorCompiler compiler = new CAntlr4ExecutorCompiler();
//        parser.addParseListener(compiler);
//
//        // 开始语法分析
//        parser.setTokenFactory(compiler);
//        parser.command();
//
//
//
//    }
//
//    @Override
//    public CToken create(Pair<TokenSource, CharStream> source, int type, String text, int channel, int start, int stop, int line, int charPositionInLine) {
//        CToken token = new CToken(source, type, channel, start, stop);
//        return token;
//    }
//
//    @Override
//    public CToken create(int type, String text) {
//        return new CToken(type, text);
//    }
//}
