package sample.more;

/**
 * Test to check if an interface starting with I will have it removed.
 *  
 * @mock:generate
 */
public interface IRemoveFirstLetter {
	int[] difficultMethod(String aString, OneVoidMethod[] aFunnyObject);
	void minimalMethod();
	String normalMethod(Integer anInteger);
}