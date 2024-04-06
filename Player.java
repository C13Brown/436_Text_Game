package objectAdventure.core.player;
// $Id: Player.java 326 2023-10-19 14:07:09Z aconover $

import objectAdventure.core.item.ItemPossessor;

/**
 * A player in the game. The player maintains an inventory and room ID.
 */
public interface Player extends ItemPossessor {
    /**
     * Gets the current room ID of the player.
     *
     * @return The currentRoom ID
     */
    int getPreviousRoomID();

    /**
     * Gets the current room ID of the player.
     *
     * @return The currentRoom ID
     */
    int getCurrentRoomId();

    /**
     * Gets the destination room ID of the player.
     *
     * @return The destinationRoom ID
     */
    default int getDestinationRoomId() {
        return this.getCurrentRoomId();
    }

    /**
     * Get the player's name.
     *
     * @return The player's name.
     */
    String getPlayerName();
}
