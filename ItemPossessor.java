package objectAdventure.core.item;
// $Id: ItemPossessor.java 326 2023-10-19 14:07:09Z aconover $

import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

/**
 * Interface for any object that can possess items.
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
public interface ItemPossessor {

    /**
     * Add a single item to the thing which can possess items.
     *
     * @param item The item to add to the room.
     */
    void addItem(Item item);

    /**
     * Remove a single item from objects which can possess items.
     *
     * @param item The item to add to the room.
     * @return Optional.of item if the item was successfully removed from the
     * room, Optional.empty otherwise.
     */
    Optional<Item> removeItem(Item item);

    /**
     * @return A list of any items found in the room
     */
    List<Item> getItemList();

    /**
     * Show all items (within the room) and their aliases
     */
    default void DEBUG_showAllItemAliases() {
        for (var item : this.getItemList()) {
            for (var alias : item.getItemAliases()) {
                out.printf("\"%s\" is an alias for object: %s\n", alias, item);
            }
        }
    }

    /**
     * Check for the existence of an item.
     *
     * @param item The item to check for.
     * @return true, if the item is in the room, false otherwise.
     */
    default boolean hasItem(Item item) {
        return this.getItemList().contains(item);
    }
}
