package sample.example;


/**
 * @mock:generate
 */
public interface Rows {

	int rowCount();

	int columnCount();
	String columnName( int index );

	boolean hasNext();
	String getString( String columName );
	boolean getBoolean( String columnName );
	int getInt( String columnName );
	long getLong( String columnName );
	float getFloat( String columnName );
	double getDouble( String columnName );
	byte[] getData( String columnName );
	Object getObject( String columnName );

}
