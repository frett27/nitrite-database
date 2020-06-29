package org.dizitart.no2.filters;

import java.util.ArrayList;
import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.dizitart.no2.filters.parser.FilterParserException;
import org.dizitart.no2.filters.parser.NitriteFilterLexer;
import org.dizitart.no2.filters.parser.NitriteFilterParser;
import org.dizitart.no2.filters.parser.NitriteFilterParser.FilterContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * compile a filter from textual representation to Filter Tree
 * @author pfreydiere
 *
 */
public class FilterCompile {

	private static Logger logger = LoggerFactory.getLogger(FilterCompile.class);

	private FilterCompile() {
	}
	
	/**
	 * parse the expression and return exception if parsing fail
	 * @param filterExpression
	 * @return
	 */
	public static FilterContext parse(String filterExpression) throws Exception {

		TokenSource ts = new NitriteFilterLexer(CharStreams.fromString(filterExpression));
		CommonTokenStream tstream = new CommonTokenStream(ts);
		NitriteFilterParser p = new NitriteFilterParser(tstream);
		
		ArrayList<String> collectedErrors = new ArrayList<>(); 
		
		p.addErrorListener(new ANTLRErrorListener() {
			
			@Override
			public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
					String msg, RecognitionException e) {
				collectedErrors.add( "" + line + ":" + charPositionInLine + " " + msg + " " + offendingSymbol);
			}
			
			@Override
			public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction,
					ATNConfigSet configs) {
							
			}
			
			@Override
			public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
					BitSet conflictingAlts, ATNConfigSet configs) {
				
				
			}
			
			@Override
			public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
					BitSet ambigAlts, ATNConfigSet configs) {
				
				
			}
		});
		
		FilterContext filterContext = p.filter();
		
		
		if (collectedErrors.size() >0) {
			throw new FilterParserException("error in parsing the filter expression :" + collectedErrors);
		}
		
		return filterContext;
	}
	

	/**
	 * compile a textual expression of filter, and create associated filter tree
	 * @param filterExpression the expression to parse
	 * @return the Filter
	 * @exception FilterParserException in case of badly formatted filter
	 * 
	 */
	public static Filter compile(String filterExpression) {

		if (filterExpression == null || filterExpression.isEmpty()) {
			logger.debug("no filter asked");
			return Filter.ALL;
		}
		
		assert filterExpression != null;

		TokenSource ts = new NitriteFilterLexer(CharStreams.fromString(filterExpression));
		CommonTokenStream tstream = new CommonTokenStream(ts);
		NitriteFilterParser p = new NitriteFilterParser(tstream);

		ParseTreeWalker walker = new ParseTreeWalker();
		FilterContext filterResult = p.filter();
		FilterCompilerVisitor w = new FilterCompilerVisitor();
		walker.walk(w, filterResult);

		Filter filterCompiledResult = w.getFilter();
		logger.debug("filter result associated to {}  : {}", filterExpression, filterCompiledResult);
		return filterCompiledResult;

	}
	
	
}
