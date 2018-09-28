package sample.example;

public class AccountCleaner {

	private Database database;

	public AccountCleaner( Database database ) {
		this.database = database;
	}

	public void fixCreditFlags() {
		Rows rows = database.select( "SELECT account, balance, credit FROM accounts" );
		database.start();
		while( rows.hasNext() ) {
			long account = rows.getLong( "account" );
			long balance = rows.getLong( "balance" );
			boolean credit = rows.getBoolean( "credit" );
			if ( balance < 0 && credit ) {
				database.execute( "UPDATE accounts SET credit = false WHERE account = " + account );
			}
			else if ( balance >= 0 && ! credit ) {
				database.execute( "UPDATE accounts SET credit = true WHERE account = " + account );
			}
		}
		database.commit();
	}

}
