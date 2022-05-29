//package com.cocofhu.ctb.kernel.core.exec.compiler.antlr4;// Generated from CExecutionCommand.g4 by ANTLR 4.3
//
//// Generated from CExecutionCommand.g4 by ANTLR 4.3
//import org.antlr.v4.runtime.Lexer;
//import org.antlr.v4.runtime.CharStream;
//import org.antlr.v4.runtime.Token;
//import org.antlr.v4.runtime.TokenStream;
//import org.antlr.v4.runtime.*;
//import org.antlr.v4.runtime.atn.*;
//import org.antlr.v4.runtime.dfa.DFA;
//import org.antlr.v4.runtime.misc.*;
//
//@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
//public class CExecutionCommandLexer extends Lexer {
//	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }
//
//	protected static final DFA[] _decisionToDFA;
//	protected static final PredictionContextCache _sharedContextCache =
//		new PredictionContextCache();
//	public static final int
//		AS=1, NEXT=2, STRING=3, INT=4, COMMENT_INPUT=5, LINE_COMMENT=6, SPACE=7,
//		ID=8;
//	public static String[] modeNames = {
//		"DEFAULT_MODE"
//	};
//
//	public static final String[] tokenNames = {
//		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'",
//		"'\\u0006'", "'\\u0007'", "'\b'"
//	};
//	public static final String[] ruleNames = {
//		"AS", "NEXT", "STRING", "INT", "SIGN", "DQUOTA_STRING", "SQUOTA_STRING",
//		"BQUOTA_STRING", "COMMENT_INPUT", "LINE_COMMENT", "SPACE", "ID"
//	};
//
//
//	public CExecutionCommandLexer(CharStream input) {
//		super(input);
//		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
//	}
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
//	public String[] getModeNames() { return modeNames; }
//
//	@Override
//	public ATN getATN() { return _ATN; }
//
//	public static final String _serializedATN =
//		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\n\u008d\b\1\4\2\t"+
//		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
//		"\t\13\4\f\t\f\4\r\t\r\3\2\3\2\3\2\5\2\37\n\2\3\3\3\3\3\4\3\4\3\4\5\4&"+
//		"\n\4\3\5\5\5)\n\5\3\5\6\5,\n\5\r\5\16\5-\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3"+
//		"\7\7\78\n\7\f\7\16\7;\13\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\7\bE\n\b\f"+
//		"\b\16\bH\13\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\7\tR\n\t\f\t\16\tU\13\t"+
//		"\3\t\3\t\3\n\3\n\3\n\3\n\7\n]\n\n\f\n\16\n`\13\n\3\n\3\n\3\n\3\13\3\13"+
//		"\3\13\3\13\3\13\7\13j\n\13\f\13\16\13m\13\13\3\13\5\13p\n\13\3\13\3\13"+
//		"\5\13t\n\13\3\13\3\13\3\13\3\13\5\13z\n\13\3\13\3\13\5\13~\n\13\5\13\u0080"+
//		"\n\13\3\f\6\f\u0083\n\f\r\f\16\f\u0084\3\r\3\r\7\r\u0089\n\r\f\r\16\r"+
//		"\u008c\13\r\3^\2\16\3\3\5\4\7\5\t\6\13\2\r\2\17\2\21\2\23\7\25\b\27\t"+
//		"\31\n\3\2\t\3\2\62;\4\2$$^^\4\2))^^\4\2^^bb\4\2\f\f\17\17\5\2C\\aac|\6"+
//		"\2\62;C\\aac|\u009f\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2"+
//		"\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\3\33\3\2\2\2\5 \3\2"+
//		"\2\2\7%\3\2\2\2\t(\3\2\2\2\13/\3\2\2\2\r\61\3\2\2\2\17>\3\2\2\2\21K\3"+
//		"\2\2\2\23X\3\2\2\2\25\177\3\2\2\2\27\u0082\3\2\2\2\31\u0086\3\2\2\2\33"+
//		"\36\7/\2\2\34\37\5\31\r\2\35\37\5\7\4\2\36\34\3\2\2\2\36\35\3\2\2\2\37"+
//		"\4\3\2\2\2 !\7@\2\2!\6\3\2\2\2\"&\5\r\7\2#&\5\17\b\2$&\5\21\t\2%\"\3\2"+
//		"\2\2%#\3\2\2\2%$\3\2\2\2&\b\3\2\2\2\')\5\13\6\2(\'\3\2\2\2()\3\2\2\2)"+
//		"+\3\2\2\2*,\t\2\2\2+*\3\2\2\2,-\3\2\2\2-+\3\2\2\2-.\3\2\2\2.\n\3\2\2\2"+
//		"/\60\7/\2\2\60\f\3\2\2\2\619\7$\2\2\62\63\7^\2\2\638\13\2\2\2\64\65\7"+
//		"$\2\2\658\7$\2\2\668\n\3\2\2\67\62\3\2\2\2\67\64\3\2\2\2\67\66\3\2\2\2"+
//		"8;\3\2\2\29\67\3\2\2\29:\3\2\2\2:<\3\2\2\2;9\3\2\2\2<=\7$\2\2=\16\3\2"+
//		"\2\2>F\7)\2\2?@\7^\2\2@E\13\2\2\2AB\7)\2\2BE\7)\2\2CE\n\4\2\2D?\3\2\2"+
//		"\2DA\3\2\2\2DC\3\2\2\2EH\3\2\2\2FD\3\2\2\2FG\3\2\2\2GI\3\2\2\2HF\3\2\2"+
//		"\2IJ\7)\2\2J\20\3\2\2\2KS\7b\2\2LM\7^\2\2MR\13\2\2\2NO\7b\2\2OR\7b\2\2"+
//		"PR\n\5\2\2QL\3\2\2\2QN\3\2\2\2QP\3\2\2\2RU\3\2\2\2SQ\3\2\2\2ST\3\2\2\2"+
//		"TV\3\2\2\2US\3\2\2\2VW\7b\2\2W\22\3\2\2\2XY\7\61\2\2YZ\7,\2\2Z^\3\2\2"+
//		"\2[]\13\2\2\2\\[\3\2\2\2]`\3\2\2\2^_\3\2\2\2^\\\3\2\2\2_a\3\2\2\2`^\3"+
//		"\2\2\2ab\7,\2\2bc\7\61\2\2c\24\3\2\2\2de\7\61\2\2ef\7\61\2\2fg\7\"\2\2"+
//		"gk\3\2\2\2hj\n\6\2\2ih\3\2\2\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2ls\3\2\2\2"+
//		"mk\3\2\2\2np\7\17\2\2on\3\2\2\2op\3\2\2\2pq\3\2\2\2qt\7\f\2\2rt\7\2\2"+
//		"\3so\3\2\2\2sr\3\2\2\2t\u0080\3\2\2\2uv\7\61\2\2vw\7\61\2\2w}\3\2\2\2"+
//		"xz\7\17\2\2yx\3\2\2\2yz\3\2\2\2z{\3\2\2\2{~\7\f\2\2|~\7\2\2\3}y\3\2\2"+
//		"\2}|\3\2\2\2~\u0080\3\2\2\2\177d\3\2\2\2\177u\3\2\2\2\u0080\26\3\2\2\2"+
//		"\u0081\u0083\7\"\2\2\u0082\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0082"+
//		"\3\2\2\2\u0084\u0085\3\2\2\2\u0085\30\3\2\2\2\u0086\u008a\t\7\2\2\u0087"+
//		"\u0089\t\b\2\2\u0088\u0087\3\2\2\2\u0089\u008c\3\2\2\2\u008a\u0088\3\2"+
//		"\2\2\u008a\u008b\3\2\2\2\u008b\32\3\2\2\2\u008c\u008a\3\2\2\2\26\2\36"+
//		"%(-\679DFQS^kosy}\177\u0084\u008a\2";
//	public static final ATN _ATN =
//		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
//	static {
//		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
//		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
//			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
//		}
//	}
//}