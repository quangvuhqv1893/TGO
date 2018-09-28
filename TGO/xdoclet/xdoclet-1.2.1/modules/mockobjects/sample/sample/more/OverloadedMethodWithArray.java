package sample.more;

/**
 * This one causes probs.
 * @mock:generate
 */
public interface OverloadedMethodWithArray {

	void overloadedMethod(int[][] anInt);

	void overloadedMethod(int[][] anInt, String aString);
}