package objectAdventure.core.command;
// $Id: CommandProcessor.java 493 2023-11-14 00:04:29Z cbrown114 $

import objectAdventure.core.item.Item;
import objectAdventure.core.item.ItemInteractionEvent;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static objectAdventure.core.DescriptionType.LONG;
import static objectAdventure.core.DescriptionType.SHORT;
import static objectAdventure.core.Direction.directionFromLexeme;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!! NOTICE !!!!!!!!!!!!!!!!!!!!!!!!!!!<br>
 * <p>
 * This class is intended to be improved upon by utilizing the 'command', and a
 * 'Chain of Responsibility patterns!!! The big case statement with many
 * function calls is usually what we want to avoid!!! This looks like a
 * candidate for either the "Chain of responsibility" pattern, or the 'command'
 * pattern.
 * <p>
 * Aside from Directions and a few helpful exceptions (noted in
 * "showCommands()"), all commands are two words (verb noun).
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
public class CommandProcessor {

    // Regex for tokenizing input. (It's simply split on spaces.)
    private static final String inputTokenRegex = "(\\S+)(.*)";

    // Compiled regex pattern for tokenizing input.
    private static final Pattern inputTokenPattern = Pattern.compile(inputTokenRegex);

    private final GameController controller;

    /**
     * Constructor for the CommandProcessor.
     * <p>
     * User input is tokenized and then the appropriate method(s) of the controller are invoked.
     *
     * @param controller The controller to use for the command processor.
     */
    public CommandProcessor(GameController controller) {
        this.controller = controller;
    }

    /**
     * Get the controller being used by the command processor.
     *
     * @return The controller being used by the command processor.
     */
    GameController getController() {
        return this.controller;
    }

    /**
     * Parse player input
     *
     * @param inputLine The line typed by the user
     * @throws UnknownCommandException Thrown if the command cannot be processed
     */
    Optional<String> processCommand(final String inputLine) throws UnknownCommandException {
        String preProcessedLine = controller.preProcessInput(inputLine);
        var normalizedLine = preProcessedLine.trim().toUpperCase();

        // Bail out if blank
        if (normalizedLine.isBlank()) {
            return Optional.empty();
        }

        // Return help if requested
        if (normalizedLine.startsWith("?") || normalizedLine.startsWith("HELP")) {
            return Optional.of(CommandHelp.help());
        }

        // Tokenize the string
        var tokenizedString = PlayerCommand.tokenizeInputString(normalizedLine);

        // Process command
        if (tokenizedString.isPresent()) {
            return this.processCommand(tokenizedString.get());
        } else {
            return Optional.of(CommandHelp.help());
        }
    }

    /**
     * Process the command string from the user.
     *
     * @param playerCommand The command object from the player
     * @return An optional string with the printable response from the command.
     */
    private Optional<String> processCommand(final PlayerCommand playerCommand) throws UnknownCommandException {
        final var noun = playerCommand.noun;
        final var verb = playerCommand.verb;

        // NOTE: This switch is intended for an eventual refactoring.
        Optional<String> response = Optional.ofNullable(switch (verb) {
            // Movement
            case "N", "NORTH", "S", "SOUTH", "E", "EAST", "W", "WEST", "U", "UP", "D", "DOWN" -> {
                var dir = directionFromLexeme(playerCommand.verb);
                dir.ifPresent(this.controller::movePlayer);

                // Nulls are usually bad, but this one never leaves the method and is only used for
                // part of the "optional" response.
                yield null;
            }

            // Inventory
            case "I", "INVENTORY" -> "Inventory:%n%s".formatted(this.controller.getPlayerInventoryDisplayNames());

            // Looking at objects
            case "L", "LOOK" -> noun.isBlank()
                    ? "%s%n%nYou See:%n%s"
                    .formatted(this.controller.getRoomDescription(),
                            this.controller.getRoomItemDisplayNames(SHORT))
                    : this.controller
                    .getAllItemsFromItemAlias(
                            noun,
                            this.controller.getPlayer().getItemList(),
                            this.controller.getCurrentRoom().getItemList())
                    .stream()
                    .map(Item::getItemFullDescription)
                    .collect(Collectors.collectingAndThen(
                            Collectors.joining("\n"),
                            str -> str.isBlank() ? format("You don't see %s here.", noun) : str));

            // Teleportation from room to room
            case "T", "TELEPORT" -> {
                try {
                    var roomId = parseInt(playerCommand.noun);
                    if (this.controller.teleportPlayer(roomId)) {
                        yield ("Teleported to room: " + playerCommand.noun);
                    } else {
                        yield "Teleportation to non-existent locations is not yet supported.";
                    }
                } catch (NumberFormatException nfe) {
                    Logger.getGlobal().warning("Invalid room ID: " + playerCommand.noun);
                    yield "You can only teleport to a room by its ID.";
                }
            }

            // Various "DISPLAY" commands
            case "DISPLAY", "SHOW" -> switch (noun) {
                case "ROOM" -> this.DEBUG_getFormattedRoomInfo();
                case "MAP" -> this.controller.DEBUG_DescribeAllRoomContents();
                default -> "I don't know how to DISPLAY %s.".formatted(noun);
            };

            // Commands for obtaining items
            case "GET", "TAKE" -> this.controller.takeItem(playerCommand.noun);

            // Commands for dropping items
            case "DROP" -> this.controller.dropItem(playerCommand.noun);

            // Set the log level dynamically.
            case "LOG" -> {
                try {
                    Logger.getGlobal().setLevel(Level.parse(noun));
                    yield format("Log level set to: %s", noun);
                } catch (IllegalArgumentException __) {
                    yield format("Invalid log level: %s", noun);
                }
            }

            // The default is to assume the command was the result of an interaction with an item.
            // Loop though all item interaction commands and fire off the one that matches the verb.
            default -> this.controller.interactWithItem(
                    noun,
                    ItemInteractionEvent
                            .actionFromLexeme(verb)
                            .orElseThrow(() -> new UnknownCommandException(
                                    "I don't understand: %s".formatted(playerCommand.originalInput))));
        });

        // Increment the move counter
        this.controller.incMoveNumber();

        // Return the response
        return response;
    }

    /**
     * Helper for processCommand() for greater legibility.
     *
     * @return Formatted room description, room contents, and Inventory
     */
    private String DEBUG_getFormattedRoomInfo() {
        return """
                Room Author: %s
                
                Room Description:
                %s
                
                Items in Room:
                %s
                
                Items in Inventory:
                %s""".formatted(
                this.controller.getCurrentRoom().getRoomAuthor(),
                this.controller.getCurrentRoom().getRoomDescription(),
                this.controller.getRoomItemDisplayNames(LONG),
                this.controller.getPlayerInventoryDisplayNames());
    }

    /**
     * @param originalInput
     * @param verb
     * @param noun
     */
    private record PlayerCommand(String originalInput, String verb, String noun) {

        // Tokenize the command
        private static Optional<PlayerCommand> tokenizeInputString(String originalInput) {
            final var matcher = inputTokenPattern.matcher(originalInput);

            if (matcher.find()) {
                var verb = matcher.group(1).trim();
                var noun = matcher.group(2).trim();
                return java.util.Optional.of(new PlayerCommand(originalInput, verb, noun));
            } else {    // no match found
                return java.util.Optional.empty();
            }
        }

    }
}
