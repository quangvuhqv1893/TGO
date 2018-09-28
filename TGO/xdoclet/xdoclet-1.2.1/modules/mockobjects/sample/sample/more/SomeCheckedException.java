package sample.more;

public class SomeCheckedException extends Exception implements InterfaceWithNoMethods {
	/**
	 * SomeCheckedException constructor comment.
	 */
	public SomeCheckedException() {
		super();
	}
	/**
	 * SomeCheckedException constructor comment.
	 * @param s java.lang.String
	 */
	public SomeCheckedException(String s) {
		super(s);
	}
}