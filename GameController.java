package objectAdventure.core.command;
// $Id: GameController.java 493 2023-11-14 00:04:29Z cbrown114 $

import objectAdventure.common.InputInterceptor;
import objectAdventure.common.Utils;
import objectAdventure.core.*;
import objectAdventure.core.player.PlayerImpl;
import objectAdventure.core.item.*;
import objectAdventure.core.room.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.System.out;
import static objectAdventure.core.DescriptionType.SHORT;
import static objectAdventure.core.item.ItemInteractionEvent.DROP;
import static objectAdventure.core.item.ItemInteractionEvent.TAKE;

/**
 * All the "Player Interactions" of the game are routed through this class.
 * <p>
 * !!!!!!!!!!!!!!!!!!!!!!!!! NOTICE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br>
 * <p>
 * This class is intended to be improved upon by utilizing the "command", and a
 * "Chain of Responsibility" patterns!!! THERE ARE MANY REFACTORING OPPORTUNITIES HERE!!!
 * <p>
 * (Ok... I'll admit it... This class grew out of control, and I haven't had time to refactor it properly...)
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
public class GameController {
    private final GameMap gameMap;
    private final RoomList rooms;
    private final PlayerImpl player;
    private int moveNumber;

    /**
     * @param player The player object.
     */
    public GameController(PlayerImpl player) {
        this.player = player;
        this.gameMap = new GameMap();
        this.rooms = RoomList.newInstance();

        this.player.setCurrentRoomId(0);
        this.moveNumber = 1;
    }

    /**
     * Increment the move number with every applicable move
     */
    void incMoveNumber() {
        moveNumber++;
    }

    /**
     * Gets the current move number.
     *
     * @return the current move number
     */
    int getMoveNumber() {
        return this.moveNumber;
    }

    /**
     * @return Debugging Room Info
     */
    String getCurrentRoomInfo() {
        try {
            final var theRoom = this.getCurrentRoom();
            return "Room ID: %s, Room Author: %s".formatted(theRoom.getRoomId(), theRoom.getRoomAuthor());
        } catch (NoSuchRoomException ex) {
            return ex.getMessage();
        }
    }

    /**
     * Return the current Room object of the active player.
     *
     * @return the current Room object of the active player
     */
    Room getCurrentRoom() {
        return this.getRoomFromID(this.getPlayer().getCurrentRoomId());
    }

    /**
     * Gets a room object from its ID.
     *
     * @param currentRoomId The ID of the room.
     * @return The room object which corresponds to the specified id.
     */
    private Room getRoomFromID(final int currentRoomId) {
        final var room = rooms.getRoom(currentRoomId);
        return room.orElseThrow(() -> new NoSuchRoomException(currentRoomId));
    }

    /**
     * Get an item object based on the alias. If there are multiple matches, the
     * first one found will be returned.
     *
     * @param noun     The target object name.
     * @param itemList The list of items to search.
     * @return The item object.
     */
    @SafeVarargs
    private Optional<Item> getItemFromAlias(final String noun, Collection<? extends Item>... itemList) {
        // Scan for item, with early bail-out
        for (var list : itemList) {
            for (var item : list) {
                for (var alias : item.getItemAliases()) {
                    if (noun.equalsIgnoreCase(alias)) {
                        return Optional.of(item);
                    }
                }
            }
        }

        // Item not found.
        return Optional.empty();
    }
    /**
     * Gets the player object.
     *
     * @return the player object
     */
    PlayerImpl getPlayer() {
        return this.player;
    }

    /**
     * Player Inventory String.
     *
     * @return a comma delimited list of player inventory items.
     */
    String getPlayerInventoryDisplayNames() {
        final List<Item> inventory = player.getItemList();

        if (inventory.isEmpty()) {
            return "You are empty-handed.";
        } else {
            return Utils.getFormattedItemList(inventory, SHORT);
        }
    }

    /**
     * Item descriptions for current room
     *
     * @return Item Description.
     */
    String getRoomItemDisplayNames(DescriptionType type) {
        try {
            return this.getRoomItemDisplayNames(this.getCurrentRoom(), type);
        } catch (NoSuchRoomException e) {
            return "No items in a non-existent room.";
        }
    }

    /**
     * Gets the item descriptions.
     *
     * @param theRoom the room with the items.
     * @return a string containing the item descriptions.
     */
    private String getRoomItemDisplayNames(final ItemPossessor theRoom, DescriptionType type) {
        if (theRoom.getItemList().isEmpty()) {
            return "Nothing of Interest.";
        } else {
            return Utils.getFormattedItemList(theRoom.getItemList(), type);
        }
    }

    /**
     * Get the description of the current room of the player.
     *
     * @return the Description of the room
     */
    String getRoomDescription() {
        return this.getRoomDescription(this.getPlayer().getCurrentRoomId());
    }

    /**
     * Get the description of the room based on roomID.
     *
     * @param roomId the room to describe
     * @return the description of the room
     */
    private String getRoomDescription(final int roomId) {
        try {
            final var theRoom = this.getRoomFromID(roomId);
            return theRoom.getRoomDescription();
        } catch (NoSuchRoomException ex) {
            return ex.getMessage();
        }
    }

    /**
     * Get a list of just the exit directions.
     *
     * @return a list of Direction Instances.
     */
    List<Direction> getExitDirections() {
        final var currentRoomId = player.getCurrentRoomId();
        final var directionSet = this.gameMap.getExitConnections(currentRoomId).keySet();

        return new ArrayList<>(directionSet);
    }

    void movePlayer(final Direction dir) {
        final var currentRoomId = this.getPlayer().getCurrentRoomId();
        final var exits = this.gameMap.getExitConnections(currentRoomId);

        assert (this.gameMap.doesExist(currentRoomId)) : "The current Room does not exist! How did you get here!?";

        // Check if the exit exists, and if so, teleport the player.
        if (exits.containsKey(dir)) {
            this.teleportPlayer(exits.get(dir));
        } else {
            out.printf("Ouch! (There is no exit: %s)%n", dir.toString());
        }
    }

    /**
     * Set the players new room number.
     *
     * @param newRoomId The destination room number for the player.
     * @return {@code true} if change was successful, {@code false}
     * otherwise.
     */
    boolean teleportPlayer(final int newRoomId) {
        assert (this.gameMap.doesExist(newRoomId)) : "The target Room does not exist...";

        // If the room does not exist, do not change the player's current room.
        if (!gameMap.doesExist(newRoomId)) {
            Logger.getGlobal().warning(format("The target Room (%d) does not exist.", newRoomId));
            // Do not change the player's current room.
            return false;
        }

        // Get the current and new room objects.
        final var currentRoomId = this.player.getCurrentRoomId();
        final var fromRoom = this.rooms.getRoom(currentRoomId);
        final var toRoom = this.rooms.getRoom(newRoomId);

        // Update the player's current room.
        player.setCurrentRoomId(newRoomId);

        // Notify the rooms of the player's movement.
        fromRoom.ifPresent(room -> {
            if (room instanceof RoomEventListener listener) listener.playerLeavingRoom(player);
        });

        // Notify the rooms of the player's movement.
        toRoom.ifPresent(room -> {
            if (room instanceof RoomEventListener listener) listener.playerEnteringRoom(player);
        });

        // Update the player's current room.
        return true;
    }


    /**
     * Does the room for the current player exist (have a "room" object).
     *
     * @return true, if the room exists, false otherwise.
     */
    private boolean isRoomPresent() {
        return this.isRoomPresent(player.getCurrentRoomId());
    }

    /**
     * Does a room with a given ID exit.
     *
     * @param roomID The ID of the Room.
     * @return true, if the room exists, false otherwise.
     */
    private boolean isRoomPresent(int roomID) {
        return rooms.getRoom(roomID).isPresent();
    }

    /**
     * Mainly just an example us using a high-level interface.
     *
     * @param from The Item possessor to take the item from.
     * @param to   The Item possessor to give the item to.
     * @param item The item to be transferred.
     * @implNote moves item from "from" --> to "to".
     */
    private static void transferItem(final ItemPossessor from, final ItemPossessor to, final Item item) {
        from.removeItem(item);
        to.addItem(item);
    }

    /**
     * Get a list of items based on the alias.
     *
     * @param noun The target object name.
     * @param itemCollection The list of items to search.
     * @return The item object.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    final List<? extends Item> getAllItemsFromItemAlias(final String noun,
                                                        Collection<? extends Item>... itemCollection) {
        // Combine lists of room and player items.
        return Arrays.stream(itemCollection)
                .flatMap(Collection::stream)
                .filter(item -> Item.getUpperCaseAliases(item).contains(noun.toUpperCase()))
                .toList();
    }

    /**
     * Drop all items.
     *
     * @return The result of dropping all items for display.
     */
    private String dropAllItems() {
        // Move all items from player to room, and collect responses.
        // NOTE1: We have to make a copy of the list first, as we can't iterate and modify concurrently.
        // NOTE2: BAD PRACTICE ALERT!!! Here, a stream is being used to collect the RESULTS of dropping items AND remove
        // those items from a list. "Functional streams" should NOT have side effects (like modifying the state of a
        // shared list!), but this is a case where it is easy to illustrate some functionality of streams.
        return new ArrayList<>(this.player.getItemList()).stream()
                .map(this::dropItem) // Drop the item and collect the result. (THE BAD PRACTICE!!!)
                .collect(Collectors.collectingAndThen(
                        Collectors.joining("\n"),
                        str -> !str.isBlank()
                                ? str
                                : "You don't have anything to drop."));
    }

    /**
     * Drop a single item to the room based on the alias.
     *
     * @param noun The item to be taken.
     * @return A String (for now) describing the result of the action. (This
     * would be refactored into a set of possible replies.)
     */
    String dropItem(final String noun) {
        if (!this.isRoomPresent()) {
            Logger.getGlobal().warning("Player is not in a room, can't drop items.");
            return "You can't drop items here!";
        }

        // If noun is "ALL", then drop all items.
        if (noun.equalsIgnoreCase("ALL")) {
            return this.dropAllItems();
        }

        // Find the item object from the alias.
        final Optional<Item> itemFromAlias = this.getItemFromAlias(noun, this.getPlayer().getItemList());

        // If the item exists, and the player has it, then drop it.
        if (itemFromAlias.isPresent() && itemFromAlias.stream().anyMatch(this.getPlayer()::hasItem)) {
            final Item theItem = itemFromAlias.get();
            return this.dropItem(theItem);
        } else {
            return format("You have no '%s' to drop!!!", noun);
        }
    }

    /**
     * Drop a single item to the room. This handles the actual dropping of the item.
     *
     * @param theItem The item to be taken.
     * @return A String describing the result of the action.
     */
    private String dropItem(Item theItem) {
        InteractionResult result = theItem.itemInteractionHandler(DROP);
        String responseMessage;

        // If the item was successfully dropped, then remove it from the player and add it to the room.
        if (result.bSuccess()) {
            transferItem(this.getPlayer(), this.getCurrentRoom(), theItem);

            responseMessage = result.message().isBlank()
                    ? format("You dropped the %s.", theItem.getItemDisplayName())
                    : result.message();
        } else {
            responseMessage = result.message().isBlank()
                    ? format("You can't drop the %s right now.%n", theItem.getItemDisplayName())
                    : result.message();
        }

        return responseMessage;
    }



    /**
     * Take all items from the room and move them to the player.
     *
     * @return A string describing the result of the action.
     */
    private String takeAllItems() {
        if (!this.isRoomPresent()) {
            Logger.getGlobal().warning("Player is not in a room, can't take items.");
            return "You can't take items here!";
        }

        if (this.getCurrentRoom().getItemList().isEmpty()) {
            return "No items to take.";
        } else {
            Collection<String> responseList = new ArrayList<>(this.getCurrentRoom().getItemList().size());
            for (var item : new ArrayList<>(this.getCurrentRoom().getItemList())) {
                String response = this.takeItem(item.getItemAliases().get(0));
                responseList.add(response);
            }

            return String.join("\n", responseList);
        }
    }

    /**
     * Take a single item from the room
     *
     * @param itemAlias The item to be taken.
     * @return A String (for now) describing the result of the action.
     */
    String takeItem(final String itemAlias) {
        if (!this.isRoomPresent()) {
            Logger.getGlobal().warning("Player is not in a room, can't take items.");
            return "You can't take items here!";
        }

        // If "ALL" was specified, then take all items.
        if ("ALL".equalsIgnoreCase(itemAlias)) {
            return this.takeAllItems();
        }

        // Get the item from any one of its aliases.
        final Optional<Item> itemFromAlias = this.getItemFromAlias(itemAlias, this.getCurrentRoom().getItemList());

        // if the item is present and is transferable, transfer it.
        if (itemFromAlias.isEmpty()) {
            return format("I see no '%s' here!!!", itemAlias);
        } else {
            // pull the item out of the optional.
            Item item = itemFromAlias.get();
            String itemDisplayName = item.getItemDisplayName();

            if (item.isTransferable()) {
                InteractionResult result = item.itemInteractionHandler(TAKE);
                String responseMessage;

                if (result.bSuccess()) {
                    // transfer the item from the room to the player.
                    transferItem(this.getCurrentRoom(), this.player, item);

                    responseMessage = result.message().isBlank()
                            ? format("You picked up the %s.", item.getItemDisplayName()) : result.message();
                } else {
                    responseMessage = result.message().isBlank()
                            ? format("You can't take the %s.", item.getItemDisplayName()) : result.message();

                }

                return responseMessage;
            } else {
                return format("Despite your valiant attempts, the %s is unmovable.", itemDisplayName);
            }
        }
    }

    /**
     * Invokes the item interaction handler for the specified item.
     *
     * @param noun   The item to be manipulated.
     * @param action The action to be performed.
     * @return A string describing the result of the action.
     */
    String interactWithItem(String noun, ItemInteractionEvent action) {
        var item = this.getItemFromAlias(noun,
                Utils.concatLists(
                        this.getPlayer().getItemList(),
                        this.getCurrentRoom().getItemList()));

        if (item.isPresent()) {
            InteractionResult result = item.get().itemInteractionHandler(action);
            String message = result.message();
            return message.isBlank() ? "I don't know how to %s the %s.".formatted(action.getAliases(), noun) : message;
        } else {
            return format("You see no '%s' here!!!", noun);
        }
    }


    /**
     * This is a bit of a hack to allow the rooms to build their own parsers or
     * manipulate the raw string before being handed off to the main game shell.
     *
     * @param inputLine The raw input line.
     * @return The manipulated input line.
     */
    String preProcessInput(String inputLine) {
        if (this.isRoomPresent() && this.getCurrentRoom() instanceof InputInterceptor room) {
            return room.interceptInput(inputLine);
        } else {
            return inputLine;
        }
    }

    /**
     * Debug call to show the contents of all rooms. (Delegate Method)
     *
     * @return formatted string listing the complete contents of the room.
     */
    String DEBUG_DescribeAllRoomContents() {
        return this.rooms.DEBUG_GetAllMapContents();
    }
}
