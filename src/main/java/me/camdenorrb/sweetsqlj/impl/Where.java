package me.camdenorrb.sweetsqlj.impl;

import java.util.ArrayList;
import java.util.List;


public final class Where {

	private boolean negated;

	private final String name;

	private final String[] values;

	private final Comparison comparison;

	private final List<Related> relatives = new ArrayList<>();


	// Commented out
	/*
	 * Constructs a Where Clause
	 *
	 * @param name
	 * <p>The name of the row you're comparing</p>
	 * <br/>
	 * @param compareOperator
	 * <p>This basically says how it should compare the row and the value</p>
	 * <br/>
	 * <p>Examples: ["<", ">", "<=", ">=", "=", "<>", "In", "Like", "Between"]</p>
	 * <br/>
	 * <p>More info: <a href="https://www.w3schools.com/sql/sql_where.asp">W3Schools</a></p>
	 * <br/>
	 * @param values
	 * <p>The values you're comparing the row with</p>
	 */
	/*
	protected Where(final String name, final String compareOperator, final String... values) {
		this.name = name;
		this.compareOperator = compareOperator;
		this.values = values;
	}*/

	public Where(final String name, final Comparison comparison, final String[] values) {
		this.name = name;
		this.comparison = comparison;
		this.values = values;
	}


	public void or(final Where where) {
		relatives.add(new Related(where, Relation.OR));
	}

	public void and(final Where where) {
		relatives.add(new Related(where, Relation.AND));
	}


	public String getName() {
		return name;
	}

	public Comparison getComparison() {
		return comparison;
	}

	public List<Related> getRelatives() {
		return relatives;
	}

	public String[] getValues() {
		return values;
	}

	public boolean isNegated() {
		return negated;
	}

	public void setNegated(boolean negated) {
		this.negated = negated;
	}


	public static class Related {

		private final Where where;

		private final Relation relation;


		private Related(final Where where, final Relation relation) {
			this.where = where;
			this.relation = relation;
		}


		public Where getWhere() {
			return where;
		}

		public Relation getRelation() {
			return relation;
		}

	}


	public enum Comparison {
		IN,
		LIKE,
		EQUALS,
		BETWEEN,
		LESSER_THAN,
		BIGGER_THAN,
		EQUAL_OR_BIGGER_THAN,
		EQUAL_OR_LESSER_THAN,
	}

	public enum Relation {
		OR, AND
	}

}
