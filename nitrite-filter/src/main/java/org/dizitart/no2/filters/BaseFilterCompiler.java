package org.dizitart.no2.filters;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.dizitart.no2.filters.Filter;
import org.dizitart.no2.filters.parser.FilterParserException;
import org.dizitart.no2.filters.parser.NitriteFilterBaseListener;
import org.dizitart.no2.filters.parser.NitriteFilterParser.ArrayContext;
import org.dizitart.no2.filters.parser.NitriteFilterParser.ConstantContext;
import org.dizitart.no2.filters.parser.NitriteFilterParser.Date_expressionContext;
import org.dizitart.no2.filters.parser.NitriteFilterParser.Duration_expressionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseFilterCompiler<T extends Filter> extends NitriteFilterBaseListener {

	private static Logger logger = LoggerFactory.getLogger(BaseFilterCompiler.class);
	
	// this constant is a function given the Instant.now() value for comparison
	private static final String NOW_DATE_CONSTANT = "NOW";

	Comparable<?> constantFilter;

	LinkedList<T> stack = new LinkedList<T>();

	List<Comparable<?>> currentArray = null;

	BaseFilterCompiler() {
		super();
	}
	
	/**
	 * remove the leading quotes
	 * @param text
	 * @return
	 */
	String extractString(String text) {
		return text.substring(1, text.length() - 1);
	}

	private long evalDurationExpression(Duration_expressionContext durationExpression) {

		String duration = durationExpression.DURATION().getText();
		ChronoUnit unit = null;

		char last = duration.charAt(duration.length() - 1);
		switch (last) {
		case 'Y':
			unit = ChronoUnit.YEARS;
			break;
		case 'M':
			unit = ChronoUnit.MONTHS;
			break;
		case 'D':
			unit = ChronoUnit.DAYS;
			break;
		case 'h':
			unit = ChronoUnit.HOURS;
			break;
		case 'm':
			unit = ChronoUnit.MINUTES;
			break;

		case 's':
			unit = ChronoUnit.SECONDS;
			break;
		default:
			throw new RuntimeException("unknown duration unit :" + last + " only Y,M,D,h,m,s are known");
		}

		Duration d = Duration.of(Integer.parseInt(duration.substring(0, duration.length() - 1)), unit);

		if (durationExpression.DATE_OPERATOR() != null) {
			String durationOperator = durationExpression.DATE_OPERATOR().getText();
			if ("+".equals(durationOperator)) {
				d = d.plus(evalDurationExpression(durationExpression.duration_expression()), ChronoUnit.MILLIS);
			} else if ("-".equals(durationOperator)) {
				d = d.minus(evalDurationExpression(durationExpression.duration_expression()), ChronoUnit.MILLIS);
			} else {
				throw new FilterParserException("Duration operator " + durationOperator + " is not known");
			}
		}

		return d.toMillis();
	}

	private Instant evalDateExpression(Date_expressionContext context) {

		if (context.LPAREN() != null) {
			return evalDateExpression(context.date_expression());
		}

		Instant current = null;

		if (context.date_expression() != null) {
			current = evalDateExpression(context.date_expression());
		}

		if (context.DATE() != null) {
			String dateText = context.DATE().getText();
			if (NOW_DATE_CONSTANT.equals(dateText)) {
				current = Instant.now();
			} else {
				dateText = dateText.substring(1, dateText.length() - 1); // strip #

				TemporalAccessor parse = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(dateText);

				current = Instant.from(parse);
			}
			return current;
		}

		if (context.DATE_OPERATOR() != null) {
			String op = context.DATE_OPERATOR().getText();
			if ("-".equals(op)) {
				current = current.minus(evalDurationExpression(context.duration_expression()), ChronoUnit.MILLIS);
			} else if ("+".equals(op)) {
				current = current.plus(evalDurationExpression(context.duration_expression()), ChronoUnit.MILLIS);
			} else {
				throw new FilterParserException("unknown operator " + op);
			}
		}

		return current;

	}

	@Override
	public void exitConstant(ConstantContext ctx) {

		if (ctx.BOOLEAN() != null) {
			String booleanValue = ctx.BOOLEAN().getText();
			constantFilter = Boolean.parseBoolean(booleanValue);
		} else if (ctx.STRING() != null) {
			String text = ctx.STRING().getText();
			// strip quotes
			constantFilter = extractString(text);
		} else if (ctx.date_expression() != null) {
			constantFilter = evalDateExpression(ctx.date_expression());
		} else if (ctx.SCIENTIFIC_NUMBER() != null) {

			String v = ctx.SCIENTIFIC_NUMBER().getText();
			if (v.indexOf('.') != -1) {
				constantFilter = Double.parseDouble(v);
			} else {
				constantFilter = Integer.parseInt(v);
			}
		
		} else {
			logger.debug("context : " + ctx.toString());
			throw new FilterParserException("unknown constant definition");
		}

		if (currentArray != null) {
			currentArray.add(constantFilter);
		}

	}

	@Override
	public void enterArray(ArrayContext ctx) {
		this.currentArray = new ArrayList<>();
	}

	@Override
	public void exitArray(ArrayContext ctx) {
	}

	void push(T f) {
		stack.push(f);
	}

	public T getFilter() {
		// assert stack.size() == 1;
		return stack.get(stack.size() - 1);
	}

}