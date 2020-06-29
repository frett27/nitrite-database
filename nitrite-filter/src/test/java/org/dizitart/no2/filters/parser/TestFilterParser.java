package org.dizitart.no2.filters.parser;

import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.antlr.v4.gui.TreeViewer;
import org.dizitart.no2.filters.Filter;
import org.dizitart.no2.filters.FilterCompile;
import org.dizitart.no2.filters.parser.NitriteFilterParser.FilterContext;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestFilterParser {

	private static Logger logger = LoggerFactory.getLogger(TestFilterParser.class);

	@Test
	public void testParserNotRaisingExceptions() throws Exception {

		String s = "aaa=\"toto\" OR ruru=2534.24 AND (bbb=\"titi\" OR ccc=1425)";
		
		evaluate(s);

		String s2 = "aaa=\"toto\" OR ruru=2534.24 AND (bbb=\"titi\" OR ccc=1425 OR (ddd CONTAINS (rrr = \"toto\") ))";
		evaluate(s2);

		String s3 = "aaa=\"toto\" OR ruru=2534.24 AND " + "(bbb=\"titi\" OR " + "ccc=1425 OR "
				+ "( ddd CONTAINS (rrr = \"toto\" AND (rrr IN [\"titi\"])))" + ")";
		evaluate(s3);

		String s4 = "eee IN [1,2,3,4]";
		evaluate(s4);
		String s5 = "(eee IN [\"toto\", 3]) OR eee IN [1,2,3,4,5]";
		evaluate(s5);

		logger.debug("{}", FilterCompile.compile(s2));

		// show AST in GUI
		// view(p, filterResult);

	}

	void evaluate(String s) {
		logger.info("evaluate " + s);
		Filter filter = FilterCompile.compile(s);
	}

	@Test
	public void testDateParser() throws Exception {
		String s = "date=#2020-06-18T00:00:30+00:00#";
		Filter f = FilterCompile.compile(s);
		System.out.println(f);
	}

	@Test
	public void testDateExpressionParser() throws Exception {
		String s = "date=(NOW)";
		
		Filter f = FilterCompile.compile(s);

		evalExpression("date=(NOW)");

		evalExpression("date=((NOW))");
		

		evalExpression("date=((NOW) - 2h)");
		
		evalExpression("date=(NOW - 2h)");
		
		s = "date=((#2020-06-18T00:00:30+00:00#))";
		evalExpression(s);

		s = "date=((#2020-06-18T00:00:30+00:00# + 1D))";
		evalExpression(s);


		s = "date=((#2020-06-18T00:00:30+00:00# - 356D))";
		evalExpression(s);
		
		s = "date=((#2020-06-18T00:00:30+00:00# + 1D + 3h))";
		evalExpression(s);

		s = "date=((#2020-06-18T00:00:30.100Z# + 1D + 3h))";
		evalExpression(s);

	}

	void evalExpression(String expression) {
		Filter f;
		System.out.println("evaluating " + expression);
		f = FilterCompile.compile(expression);
		System.out.println(f);
	}

	@Test
	public void testBadInputAndErrorBehaviour() throws Exception {

		try {
			String s = "aaa=\"toto\" OR ruru=2534.24 AND ";
			FilterCompile.parse(s);
		} catch (Exception ex) {
			// ok this must raise an exception, no member for binary operator
			return;
		}

		Assert.fail();
	}

	/**
	 * internal method to help debugging grammar and AST Struture
	 * @param p
	 * @param filterResult
	 * @throws Exception
	 */
	private void view(NitriteFilterParser p, FilterContext filterResult) throws Exception {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		TreeViewer viewer = new TreeViewer(Arrays.asList(p.getRuleNames()), filterResult);
		viewer.setScale(1.5); // Scale a little
		panel.add(viewer);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		while (true) {
			Thread.sleep(1000);
		}

	}
	
}
