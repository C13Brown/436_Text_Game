package objectAdventure.core;
// $Id: Direction.java 326 2023-10-19 14:07:09Z aconover $

import java.util.Optional;

/**
 * The Direction enum is used to represent the directions that a player can move in the game.
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
public enum Direction {
    /* The Directions */
    NORTH("North", 'N'),
    SOUTH("South", 'S'),
    EAST("East", 'E'),
    WEST("West", 'W'),
    UP("Up", 'U'),
    DOWN("Down", 'D');

    /**
     * Data Fields
     */
    private final String longName;
    private final char abbreviation;

    /* Enum  constructor */
    Direction(String longName, char abbreviation) {
        this.longName = longName;
        this.abbreviation = abbreviation;
    }

    /**
     * Gets the direction object based on the text that was entered.
     *
     * @param lexeme the string name of the direction to map to a real Direction object.
     * @return the Direction object created from the string as an Optional which will be empty if
     * the direction string could not be parsed.
     */
    public static Optional<Direction> directionFromLexeme(final String lexeme) {
        // Just to be safe...
        var string = lexeme.trim().toUpperCase();

        // Search through ALL lexemes looking for a match with early bailout.
        for (var Direction : Direction.values()) {
            // Can use just the first few characters if desired.
            if (Direction.longName.toUpperCase().startsWith(string)) {
                return Optional.of(Direction);
            }
        }

        return Optional.empty();
    }

    /**
     * Gets the long name of a direction for display in textual descriptions.
     *
     * @return the full name of the direction
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Gets the abbreviated name for user prompts and quick entry.
     *
     * @return the abbreviated name of the direction
     */
    public char getAbbreviation() {
        return abbreviation;
    }

    /**
     * The "Long Name" is the human-readable version.
     */
    @Override
    public String toString() {
        return this.longName;
    }

}
