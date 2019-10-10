package me.camdenorrb.sweetsqlj.impl;

public final class Where {

	private final String name, compareOperator;

	private final String[] values;


	/**
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
	public Where(final String name, final String compareOperator, final String... values) {
		this.name = name;
		this.compareOperator = compareOperator;
		this.values = values;
	}


	public String getName() {
		return name;
	}

	public String getCompareOperator() {
		return compareOperator;
	}

	public String[] getValues() {
		return values;
	}

}
