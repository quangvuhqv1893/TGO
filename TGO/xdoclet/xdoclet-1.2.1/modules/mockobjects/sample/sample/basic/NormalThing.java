package sample.basic;

/**
 * @mock:generate
 */
public interface NormalThing {

	String normalMethod( String name );

	long anotherThingy( int i );

	void integerThingy( Integer i );

	boolean something( Object o );
	boolean something( Object o, boolean b );

	Integer eatFood();

}
