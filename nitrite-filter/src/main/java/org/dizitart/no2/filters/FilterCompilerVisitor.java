package org.dizitart.no2.filters;

import org.dizitart.no2.filters.parser.FilterParserException;
import org.dizitart.no2.filters.parser.NitriteFilterParser.ArrayOpContext;
import org.dizitart.no2.filters.parser.NitriteFilterParser.ExpContext;
import org.dizitart.no2.filters.parser.NitriteFilterParser.FilterContext;
import org.dizitart.no2.filters.parser.NitriteFilterParser.FilterExpContext;
import org.dizitart.no2.filters.parser.NitriteFilterParser.FilterOpContext;
import org.dizitart.no2.filters.parser.NitriteFilterParser.MultiplyContext;

class FilterCompilerVisitor extends BaseFilterCompiler<Filter> {


	@Override
	public void exitMultiply(MultiplyContext ctx) {

		if (ctx.NOT() != null) {
			stack.push(new NotFilter(stack.pop()));
		} else if (ctx.atom().size() > 0) {
			for (int i = 0; i < ctx.atom().size() - 1; i++) {
				stack.push(new AndFilter(stack.pop(), stack.pop()));
			}
		}
	}

	@Override
	public void exitExp(ExpContext ctx) {

		String variable = ctx.variable().VARIABLE().getText();

		FilterExpContext filterExp = ctx.filterExp();

		if (filterExp != null) {

			if (filterExp.FIND() != null) {
				// text search
				push(new TextFilter(variable, extractString(filterExp.STRING().getText())));

			} else if (filterExp.MATCH() != null) {

				push(new RegexFilter(variable, extractString(filterExp.STRING().getText())));

			} else if (filterExp.CONTAINS() != null) {

				push(new ElementMatchFilter(variable, stack.pop()));

			} else if (filterExp.arrayOp() != null) {
				ArrayOpContext a = filterExp.arrayOp();
				if (a.IN() != null) {
					push(new InFilter(variable, this.currentArray.toArray(new Comparable<?>[0])));
				} else if (a.NOTIN() != null) {
					push(new NotInFilter(variable, this.currentArray.toArray(new Comparable<?>[0])));
				} else {
					throw new RuntimeException("array operator not supported :" + ctx);
				}

			} else {

				FilterOpContext opctx = filterExp.filterOp();

				if (opctx != null) {

					if (opctx.EQ() != null) {
						push(new EqualsFilter(variable, constantFilter));
					} else if (opctx.NEQ() != null) {
						push(new NotFilter(new EqualsFilter(variable, constantFilter)));
					} else if (opctx.GT() != null) {
						push(new GreaterThanFilter(variable, constantFilter));
					} else if (opctx.GTE() != null) {
						push(new GreaterEqualFilter(variable, constantFilter));
					} else if (opctx.LTE() != null) {
						push(new LesserEqualFilter(variable, constantFilter));
					} else if (opctx.LT() != null) {
						push(new LesserThanFilter(variable, constantFilter));
					} else throw new FilterParserException("implementation error, unknown operator");
				}
			}
		}

	}

	@Override
	public void exitFilter(FilterContext ctx) {
		if (ctx.multiply().size() > 0) {
			for (int i = 0; i < ctx.multiply().size() - 1; i++) {
				stack.push(new OrFilter(stack.pop(), stack.pop()));
			}
		}
	}

}