package objectAdventure.core.item;
// $Id: InteractionResult.java 326 2023-10-19 14:07:09Z aconover $

/**
 * A record class for returning the result of an interaction.
 * AJCTODO: Relocate this class to a more appropriate package. (It's not "Item" specific.)
 *
 * @param bSuccess Whether the interaction was successful.
 * @param message A message to display to the user.
 * @author Adam J. Conover, COSC436/COSC716
 */
public record InteractionResult(boolean bSuccess, String message) {
    // Success with a message.
    public static InteractionResult success(String message) {
        return new InteractionResult(true, message);
    }

    // Success with no message.
    public static InteractionResult success() {
        return new InteractionResult(true, "");
    }

    // Failure with a message.
    public static InteractionResult failure(String message) {
        return new InteractionResult(false, message);
    }
}
