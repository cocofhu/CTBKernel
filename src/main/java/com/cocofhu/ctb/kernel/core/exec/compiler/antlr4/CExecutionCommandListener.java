//package com.cocofhu.ctb.kernel.core.exec.compiler.antlr4;// Generated from CExecutionCommand.g4 by ANTLR 4.3
//
//import org.antlr.v4.runtime.misc.NotNull;
//import org.antlr.v4.runtime.tree.ParseTreeListener;
//
///**
// * This interface defines a complete listener for a parse tree produced by
// * {@link CExecutionCommandParser}.
// */
//public interface CExecutionCommandListener extends ParseTreeListener {
//	/**
//	 * Enter a parse tree produced by {@link CExecutionCommandParser#execution}.
//	 * @param ctx the parse tree
//	 */
//	void enterExecution(@NotNull CExecutionCommandParser.ExecutionContext ctx);
//	/**
//	 * Exit a parse tree produced by {@link CExecutionCommandParser#execution}.
//	 * @param ctx the parse tree
//	 */
//	void exitExecution(@NotNull CExecutionCommandParser.ExecutionContext ctx);
//
//	/**
//	 * Enter a parse tree produced by {@link CExecutionCommandParser#rvalue}.
//	 * @param ctx the parse tree
//	 */
//	void enterRvalue(@NotNull CExecutionCommandParser.RvalueContext ctx);
//	/**
//	 * Exit a parse tree produced by {@link CExecutionCommandParser#rvalue}.
//	 * @param ctx the parse tree
//	 */
//	void exitRvalue(@NotNull CExecutionCommandParser.RvalueContext ctx);
//
//	/**
//	 * Enter a parse tree produced by {@link CExecutionCommandParser#comment}.
//	 * @param ctx the parse tree
//	 */
//	void enterComment(@NotNull CExecutionCommandParser.CommentContext ctx);
//	/**
//	 * Exit a parse tree produced by {@link CExecutionCommandParser#comment}.
//	 * @param ctx the parse tree
//	 */
//	void exitComment(@NotNull CExecutionCommandParser.CommentContext ctx);
//
//	/**
//	 * Enter a parse tree produced by {@link CExecutionCommandParser#prog}.
//	 * @param ctx the parse tree
//	 */
//	void enterProg(@NotNull CExecutionCommandParser.ProgContext ctx);
//	/**
//	 * Exit a parse tree produced by {@link CExecutionCommandParser#prog}.
//	 * @param ctx the parse tree
//	 */
//	void exitProg(@NotNull CExecutionCommandParser.ProgContext ctx);
//
//	/**
//	 * Enter a parse tree produced by {@link CExecutionCommandParser#command}.
//	 * @param ctx the parse tree
//	 */
//	void enterCommand(@NotNull CExecutionCommandParser.CommandContext ctx);
//	/**
//	 * Exit a parse tree produced by {@link CExecutionCommandParser#command}.
//	 * @param ctx the parse tree
//	 */
//	void exitCommand(@NotNull CExecutionCommandParser.CommandContext ctx);
//}