package objectAdventure.core.item;
// $Id: ItemInteractionEvent.java 326 2023-10-19 14:07:09Z aconover $

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The ItemInteractionEvent enum represents the different types of interactions that can be performed on an Item.
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
public enum ItemInteractionEvent {
    TAKE("Get", "Take"), // Take an Item. (Take is an alias for Get)
    DROP("Drop"), // Drop an Item.
    INSPECT("Inspect", "Examine"), // Inspect an Item.
    USE("Use"), // Use an Item.
    PUSH("Push"), // Push an Item.
    PULL("Push"), // Pull an Item.
    ACTIVATE("Activate"), // Activate an Item.
    DEACTIVATE("Deactivate"), // Deactivate an Item.
    OPEN("Open"), // Open an Item.
    CLOSE("Close"), // Close an Item.
    THROW("Throw", "Toss"), // Throw an Item.
    REPAIR("Fix", "Repair", "Mend"), // Fix an Item.
    DESTROY("Break", "Destroy", "Smash"), // Break an Item.
    TAUNT("Taunt"); // Do NOT taunt Happy Fun Ball!!! (https://www.youtube.com/watch?v=GmqeZl8OI2M)

    // The string representation of the command.
    private final List<String> commandList;

    /**
     * Constructor for the ItemInteractionEvent enum.
     *
     * @param commandStrings The string representation of the command.
     */
    ItemInteractionEvent(final String... commandStrings) {
        this.commandList = Arrays.stream(commandStrings).map(String::toUpperCase).collect(Collectors.toList());
    }

    /**
     * Gets the direction object based on the text that was entered.
     *
     * @param lexeme the string name of the direction to map to a real Direction object.
     * @return the Direction object created from the string as an Optional which will be empty if
     * the direction string could not be parsed.
     */
    public static Optional<ItemInteractionEvent> actionFromLexeme(final String lexeme) {
        for (var action : values()) {
            if (action.commandList.contains(lexeme.trim())) {
                return Optional.of(action);
            }
        }

        // TODO: Return custom item interaction event.
        return Optional.empty();
    }

    /**
     * Gets the string representation of the command for debugging purposes.
     *
     * @return The string representation of the command.
     */

    public String getName() {
        return this.name();
    }

    /**
     * Get a comma-separated list of the aliases for the command.
     *
     * @return The comma-separated list of aliases for the command.
     */
    public String getAliases() {
        return this.commandList.stream().map(String::toUpperCase).collect(Collectors.joining(", "));
    }

    /**
     * Gets the string representation of the command for debugging purposes.
     *
     * @return The string representation of the command.
     */
    @Override
    public String toString() {
        return "Verb{" + "commandString=" + this.name() + '}';
    }
}
