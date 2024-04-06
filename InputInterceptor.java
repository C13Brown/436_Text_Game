package objectAdventure.common;
// $Id: InputInterceptor.java 326 2023-10-19 14:07:09Z aconover $

/**
 * Interface for any object that can intercept input.
 * <p>
 * An input interceptor can be used to modify the input before it is processed.
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
@FunctionalInterface
public interface InputInterceptor {
    /**
     * Intercept the input line and return the modified input line.
     *
     * @param inputLine The input line to be intercepted.
     * @return The modified input line.
     */
    String interceptInput(String inputLine);
}
