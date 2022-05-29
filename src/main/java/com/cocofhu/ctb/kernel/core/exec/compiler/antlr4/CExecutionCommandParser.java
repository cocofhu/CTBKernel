//package com.cocofhu.ctb.kernel.core.exec.compiler.antlr4;// Generated from CExecutionCommand.g4 by ANTLR 4.3
//
//import org.antlr.v4.runtime.atn.*;
//import org.antlr.v4.runtime.dfa.DFA;
//import org.antlr.v4.runtime.*;
//import org.antlr.v4.runtime.misc.*;
//import org.antlr.v4.runtime.tree.*;
//import java.util.List;
//import java.util.Iterator;
//import java.util.ArrayList;
//
//@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
//public class CExecutionCommandParser extends Parser {
//	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }
//
//	protected static final DFA[] _decisionToDFA;
//	protected static final PredictionContextCache _sharedContextCache =
//		new PredictionContextCache();
//	public static final int
//		AS=1, NEXT=2, STRING=3, INT=4, COMMENT_INPUT=5, LINE_COMMENT=6, SPACE=7,
//		ID=8;
//	public static final String[] tokenNames = {
//		"<INVALID>", "AS", "'>'", "STRING", "INT", "COMMENT_INPUT", "LINE_COMMENT",
//		"SPACE", "ID"
//	};
//	public static final int
//		RULE_prog = 0, RULE_command = 1, RULE_execution = 2, RULE_rvalue = 3,
//		RULE_comment = 4;
//	public static final String[] ruleNames = {
//		"prog", "command", "execution", "rvalue", "comment"
//	};
//
//	@Override
//	public String getGrammarFileName() { return "CExecutionCommand.g4"; }
//
//	@Override
//	public String[] getTokenNames() { return tokenNames; }
//
//	@Override
//	public String[] getRuleNames() { return ruleNames; }
//
//	@Override
//	public String getSerializedATN() { return _serializedATN; }
//
//	@Override
//	public ATN getATN() { return _ATN; }
//
//	public CExecutionCommandParser(TokenStream input) {
//		super(input);
//		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
//	}
//	public static class ProgContext extends ParserRuleContext {
//		public List<CommentContext> comment() {
//			return getRuleContexts(CommentContext.class);
//		}
//		public List<CommandContext> command() {
//			return getRuleContexts(CommandContext.class);
//		}
//		public CommentContext comment(int i) {
//			return getRuleContext(CommentContext.class,i);
//		}
//		public CommandContext command(int i) {
//			return getRuleContext(CommandContext.class,i);
//		}
//		public ProgContext(ParserRuleContext parent, int invokingState) {
//			super(parent, invokingState);
//		}
//		@Override public int getRuleIndex() { return RULE_prog; }
//		@Override
//		public void enterRule(ParseTreeListener listener) {
//			if ( listener instanceof CExecutionCommandListener ) ((CExecutionCommandListener)listener).enterProg(this);
//		}
//		@Override
//		public void exitRule(ParseTreeListener listener) {
//			if ( listener instanceof CExecutionCommandListener ) ((CExecutionCommandListener)listener).exitProg(this);
//		}
//	}
//
//	public final ProgContext prog() throws RecognitionException {
//		ProgContext _localctx = new ProgContext(_ctx, getState());
//		enterRule(_localctx, 0, RULE_prog);
//		int _la;
//		try {
//			enterOuterAlt(_localctx, 1);
//			{
//			setState(14);
//			_errHandler.sync(this);
//			_la = _input.LA(1);
//			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << COMMENT_INPUT) | (1L << LINE_COMMENT) | (1L << ID))) != 0)) {
//				{
//				setState(12);
//				switch (_input.LA(1)) {
//				case ID:
//					{
//					setState(10); command();
//					}
//					break;
//				case COMMENT_INPUT:
//				case LINE_COMMENT:
//					{
//					setState(11); comment();
//					}
//					break;
//				default:
//					throw new NoViableAltException(this);
//				}
//				}
//				setState(16);
//				_errHandler.sync(this);
//				_la = _input.LA(1);
//			}
//			}
//		}
//		catch (RecognitionException re) {
//			_localctx.exception = re;
//			_errHandler.reportError(this, re);
//			_errHandler.recover(this, re);
//		}
//		finally {
//			exitRule();
//		}
//		return _localctx;
//	}
//
//	public static class CommandContext extends ParserRuleContext {
//		public List<TerminalNode> NEXT() { return getTokens(CExecutionCommandParser.NEXT); }
//		public List<ExecutionContext> execution() {
//			return getRuleContexts(ExecutionContext.class);
//		}
//		public TerminalNode SPACE(int i) {
//			return getToken(CExecutionCommandParser.SPACE, i);
//		}
//		public ExecutionContext execution(int i) {
//			return getRuleContext(ExecutionContext.class,i);
//		}
//		public List<TerminalNode> SPACE() { return getTokens(CExecutionCommandParser.SPACE); }
//		public TerminalNode NEXT(int i) {
//			return getToken(CExecutionCommandParser.NEXT, i);
//		}
//		public CommandContext(ParserRuleContext parent, int invokingState) {
//			super(parent, invokingState);
//		}
//		@Override public int getRuleIndex() { return RULE_command; }
//		@Override
//		public void enterRule(ParseTreeListener listener) {
//			if ( listener instanceof CExecutionCommandListener ) ((CExecutionCommandListener)listener).enterCommand(this);
//		}
//		@Override
//		public void exitRule(ParseTreeListener listener) {
//			if ( listener instanceof CExecutionCommandListener ) ((CExecutionCommandListener)listener).exitCommand(this);
//		}
//	}
//
//	public final CommandContext command() throws RecognitionException {
//		CommandContext _localctx = new CommandContext(_ctx, getState());
//		enterRule(_localctx, 2, RULE_command);
//		int _la;
//		try {
//			enterOuterAlt(_localctx, 1);
//			{
//			setState(17); execution();
//			setState(34);
//			_errHandler.sync(this);
//			_la = _input.LA(1);
//			while (_la==NEXT || _la==SPACE) {
//				{
//				{
//				setState(21);
//				_errHandler.sync(this);
//				_la = _input.LA(1);
//				while (_la==SPACE) {
//					{
//					{
//					setState(18); match(SPACE);
//					}
//					}
//					setState(23);
//					_errHandler.sync(this);
//					_la = _input.LA(1);
//				}
//				setState(24); match(NEXT);
//				setState(28);
//				_errHandler.sync(this);
//				_la = _input.LA(1);
//				while (_la==SPACE) {
//					{
//					{
//					setState(25); match(SPACE);
//					}
//					}
//					setState(30);
//					_errHandler.sync(this);
//					_la = _input.LA(1);
//				}
//				setState(31); execution();
//				}
//				}
//				setState(36);
//				_errHandler.sync(this);
//				_la = _input.LA(1);
//			}
//			}
//		}
//		catch (RecognitionException re) {
//			_localctx.exception = re;
//			_errHandler.reportError(this, re);
//			_errHandler.recover(this, re);
//		}
//		finally {
//			exitRule();
//		}
//		return _localctx;
//	}
//
//	public static class ExecutionContext extends ParserRuleContext {
//		public TerminalNode ID() { return getToken(CExecutionCommandParser.ID, 0); }
//		public RvalueContext rvalue(int i) {
//			return getRuleContext(RvalueContext.class,i);
//		}
//		public TerminalNode AS(int i) {
//			return getToken(CExecutionCommandParser.AS, i);
//		}
//		public TerminalNode SPACE(int i) {
//			return getToken(CExecutionCommandParser.SPACE, i);
//		}
//		public List<RvalueContext> rvalue() {
//			return getRuleContexts(RvalueContext.class);
//		}
//		public List<TerminalNode> SPACE() { return getTokens(CExecutionCommandParser.SPACE); }
//		public List<TerminalNode> AS() { return getTokens(CExecutionCommandParser.AS); }
//		public ExecutionContext(ParserRuleContext parent, int invokingState) {
//			super(parent, invokingState);
//		}
//		@Override public int getRuleIndex() { return RULE_execution; }
//		@Override
//		public void enterRule(ParseTreeListener listener) {
//			if ( listener instanceof CExecutionCommandListener ) ((CExecutionCommandListener)listener).enterExecution(this);
//		}
//		@Override
//		public void exitRule(ParseTreeListener listener) {
//			if ( listener instanceof CExecutionCommandListener ) ((CExecutionCommandListener)listener).exitExecution(this);
//		}
//	}
//
//	public final ExecutionContext execution() throws RecognitionException {
//		ExecutionContext _localctx = new ExecutionContext(_ctx, getState());
//		enterRule(_localctx, 4, RULE_execution);
//		try {
//			int _alt;
//			enterOuterAlt(_localctx, 1);
//			{
//			{
//			setState(37); match(ID);
//			setState(44);
//			_errHandler.sync(this);
//			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
//			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
//				if ( _alt==1 ) {
//					{
//					{
//					setState(38); match(SPACE);
//					setState(39); match(AS);
//					setState(40); match(SPACE);
//					setState(41); rvalue();
//					}
//					}
//				}
//				setState(46);
//				_errHandler.sync(this);
//				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
//			}
//			}
//			}
//		}
//		catch (RecognitionException re) {
//			_localctx.exception = re;
//			_errHandler.reportError(this, re);
//			_errHandler.recover(this, re);
//		}
//		finally {
//			exitRule();
//		}
//		return _localctx;
//	}
//
//	public static class RvalueContext extends ParserRuleContext {
//		public TerminalNode ID() { return getToken(CExecutionCommandParser.ID, 0); }
//		public TerminalNode STRING() { return getToken(CExecutionCommandParser.STRING, 0); }
//		public TerminalNode INT() { return getToken(CExecutionCommandParser.INT, 0); }
//		public RvalueContext(ParserRuleContext parent, int invokingState) {
//			super(parent, invokingState);
//		}
//		@Override public int getRuleIndex() { return RULE_rvalue; }
//		@Override
//		public void enterRule(ParseTreeListener listener) {
//			if ( listener instanceof CExecutionCommandListener ) ((CExecutionCommandListener)listener).enterRvalue(this);
//		}
//		@Override
//		public void exitRule(ParseTreeListener listener) {
//			if ( listener instanceof CExecutionCommandListener ) ((CExecutionCommandListener)listener).exitRvalue(this);
//		}
//	}
//
//	public final RvalueContext rvalue() throws RecognitionException {
//		RvalueContext _localctx = new RvalueContext(_ctx, getState());
//		enterRule(_localctx, 6, RULE_rvalue);
//		int _la;
//		try {
//			enterOuterAlt(_localctx, 1);
//			{
//			setState(47);
//			_la = _input.LA(1);
//			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << INT) | (1L << ID))) != 0)) ) {
//			_errHandler.recoverInline(this);
//			}
//			consume();
//			}
//		}
//		catch (RecognitionException re) {
//			_localctx.exception = re;
//			_errHandler.reportError(this, re);
//			_errHandler.recover(this, re);
//		}
//		finally {
//			exitRule();
//		}
//		return _localctx;
//	}
//
//	public static class CommentContext extends ParserRuleContext {
//		public TerminalNode LINE_COMMENT() { return getToken(CExecutionCommandParser.LINE_COMMENT, 0); }
//		public TerminalNode COMMENT_INPUT() { return getToken(CExecutionCommandParser.COMMENT_INPUT, 0); }
//		public CommentContext(ParserRuleContext parent, int invokingState) {
//			super(parent, invokingState);
//		}
//		@Override public int getRuleIndex() { return RULE_comment; }
//		@Override
//		public void enterRule(ParseTreeListener listener) {
//			if ( listener instanceof CExecutionCommandListener ) ((CExecutionCommandListener)listener).enterComment(this);
//		}
//		@Override
//		public void exitRule(ParseTreeListener listener) {
//			if ( listener instanceof CExecutionCommandListener ) ((CExecutionCommandListener)listener).exitComment(this);
//		}
//	}
//
//	public final CommentContext comment() throws RecognitionException {
//		CommentContext _localctx = new CommentContext(_ctx, getState());
//		enterRule(_localctx, 8, RULE_comment);
//		int _la;
//		try {
//			enterOuterAlt(_localctx, 1);
//			{
//			setState(49);
//			_la = _input.LA(1);
//			if ( !(_la==COMMENT_INPUT || _la==LINE_COMMENT) ) {
//			_errHandler.recoverInline(this);
//			}
//			consume();
//			}
//		}
//		catch (RecognitionException re) {
//			_localctx.exception = re;
//			_errHandler.reportError(this, re);
//			_errHandler.recover(this, re);
//		}
//		finally {
//			exitRule();
//		}
//		return _localctx;
//	}
//
//	public static final String _serializedATN =
//		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\n\66\4\2\t\2\4\3"+
//		"\t\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\7\2\17\n\2\f\2\16\2\22\13\2\3\3\3"+
//		"\3\7\3\26\n\3\f\3\16\3\31\13\3\3\3\3\3\7\3\35\n\3\f\3\16\3 \13\3\3\3\7"+
//		"\3#\n\3\f\3\16\3&\13\3\3\4\3\4\3\4\3\4\3\4\7\4-\n\4\f\4\16\4\60\13\4\3"+
//		"\5\3\5\3\6\3\6\3\6\2\2\7\2\4\6\b\n\2\4\4\2\5\6\n\n\3\2\7\b\66\2\20\3\2"+
//		"\2\2\4\23\3\2\2\2\6\'\3\2\2\2\b\61\3\2\2\2\n\63\3\2\2\2\f\17\5\4\3\2\r"+
//		"\17\5\n\6\2\16\f\3\2\2\2\16\r\3\2\2\2\17\22\3\2\2\2\20\16\3\2\2\2\20\21"+
//		"\3\2\2\2\21\3\3\2\2\2\22\20\3\2\2\2\23$\5\6\4\2\24\26\7\t\2\2\25\24\3"+
//		"\2\2\2\26\31\3\2\2\2\27\25\3\2\2\2\27\30\3\2\2\2\30\32\3\2\2\2\31\27\3"+
//		"\2\2\2\32\36\7\4\2\2\33\35\7\t\2\2\34\33\3\2\2\2\35 \3\2\2\2\36\34\3\2"+
//		"\2\2\36\37\3\2\2\2\37!\3\2\2\2 \36\3\2\2\2!#\5\6\4\2\"\27\3\2\2\2#&\3"+
//		"\2\2\2$\"\3\2\2\2$%\3\2\2\2%\5\3\2\2\2&$\3\2\2\2\'.\7\n\2\2()\7\t\2\2"+
//		")*\7\3\2\2*+\7\t\2\2+-\5\b\5\2,(\3\2\2\2-\60\3\2\2\2.,\3\2\2\2./\3\2\2"+
//		"\2/\7\3\2\2\2\60.\3\2\2\2\61\62\t\2\2\2\62\t\3\2\2\2\63\64\t\3\2\2\64"+
//		"\13\3\2\2\2\b\16\20\27\36$.";
//	public static final ATN _ATN =
//		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
//	static {
//		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
//		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
//			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
//		}
//	}
//}