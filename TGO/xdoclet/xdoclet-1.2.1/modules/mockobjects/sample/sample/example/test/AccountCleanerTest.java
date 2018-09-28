package sample.example.test;

import junit.framework.TestCase;
import sample.example.*;

public class AccountCleanerTest extends TestCase {
	public AccountCleanerTest( String s ) {
		super(s);
	}
/*

	public void testBalances() {

		// check correct query is used for database.select().
		DatabaseMock database = new DatabaseMock();
		database.addExpectedSelectValues( "SELECT account, balance, credit FROM accounts" );

		// return some dummy data from database.select().
		RowsMock rows = new RowsMock();
		setupAccountRow( rows, 123, 999, true ); // correct
		setupAccountRow( rows, 124, -999, false ); // correct
		setupAccountRow( rows, 125, -1, true ); // should be corrected to false
		setupAccountRow( rows, 126, 5, false ); // should be corrected to true
		rows.setupHasNext( false );
		database.setupSelect( rows );

		// these are the expectations that need to be met to pass the test.
		database.addExpectedExecuteValues( "UPDATE accounts SET credit = false WHERE account = 125" );
		database.addExpectedExecuteValues( "UPDATE accounts SET credit = true WHERE account = 126" );

		// execute method to be tested.
		AccountCleaner cleaner = new AccountCleaner( database );
		cleaner.fixCreditFlags();

		// check it met our expectations.
		database.verify();

	}

	private void setupAccountRow( RowsMock rows, long account, long balance, boolean credit ) {
		rows.setupHasNext( true );
		rows.setupGetLong( account );
		rows.setupGetLong( balance );
		rows.setupGetBoolean( credit );
	}
*/
}
