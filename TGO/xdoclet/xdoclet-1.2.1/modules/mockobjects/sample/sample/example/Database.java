package sample.example;

/**
 * @mock.generate
 */
public abstract class Database {

	/**
	 * Start a transaction.
	 */
	void start() {
	}

	/**
	 * Commit a transaction.
	 */
	void commit() {}

	/**
	 * Rollback a transaction.
	 */
	void rollback(){}

	/**
	 * Execute some SQL that modifies the database (UPDATE, INSERT, DELETE...).
	 * @throws IllegalArgumentException thrown if invalid SQL supplie.
	 */
	void execute( String sql ) throws IllegalArgumentException{}

	/**
	 * Execute some SQL that returns rows (SELECT...).
	 * @throws IllegalArgumentException thrown if invalid SQL supplie.
	 */
	Rows select( String sql ) throws IllegalArgumentException{ return null; }

}
