package objectAdventure.core.item;
// $Id: InteractionCommand.java 326 2023-10-19 14:07:09Z aconover $

/**
 * Part of an experimental feature command pattern feature… NOT YET USED.
 */

@FunctionalInterface
public interface InteractionCommand {
    InteractionResult execute(String userInput);
}
