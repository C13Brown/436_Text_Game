package objectAdventure.core.item;
// $Id: SecretTestingItem.java 368 2023-10-25 22:56:40Z aconover $

import java.util.List;

/**
 * An item for illustrating advanced item features.
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
public class SecretTestingItem implements Item {

    private String displayName;
    private boolean isPlayerHoldingMap = false;
    private String description;

    private int pickupAttempts = 0;

    /**
     * Constructor
     */
    public SecretTestingItem() {
        this.displayName = "The GameMap";
        this.description = "A GameMap (PDF located in Game source folder).";
    }

    /**
     * THIS IS JUST A SAMPLE.
     *
     * @return The item description.
     */
    @Override
    public String getItemFullDescription() {
        return this.description;
    }

    @Override
    public String getItemDisplayName() {
        return this.displayName;
    }

    /**
     * Item Can be picked up and moved.
     *
     * @return True
     */
    @Override
    public boolean isTransferable() {
        boolean isAnchored = false;
        return !isAnchored;
    }

    /**
     * This a list of nouns the player can use to reference the item.
     *
     * @return Aliases for item.
     */
    @Override
    public List<String> getItemAliases() {
        return List.of("Map", "Game Map");
    }

    /**
     * The game core has notified the item it has been interacted with.
     * <p>
     * NOTE: The Item could easily be BOTH an Observer AND Observable (observing events, which it
     * then forwards on to its own observers)! But, because EVERY item receives the same
     * information, making it an "observer" of the game controller is unnecessary. The observer
     * common makes most sense when only some Objects are observable and others are not
     * or Observable items are observed by many other kinds of objects.
     *
     * @param ie The ItemInteractionEvent info. The actual parameter is an enum (singleton) value of
     *           one of the following:<br>
     *           <br>
     *           ItemInteractionEvent.DROP -- When the player types "drop [item]"<br>
     *           ItemInteractionEvent.TAKE -- When the player types "get (or take) [item]"<br>
     *           ItemInteractionEvent.USE -- When the player types "use [item]"<br>
     *           ItemInteractionEvent.INSPECT -- When the player types "inspect [item]"<br>
     *           ItemInteractionEvent.PUSH -- When the player types "push [item]"<br>
     *           ItemInteractionEvent.PULL -- When the player types "pull [item]"<br>
     *           ItemInteractionEvent.LOOK -- When the player types "look [item]"<br>
     *           ItemInteractionEvent.ACTIVATE -- When the player types "activate [item]"<br>
     *           ItemInteractionEvent.DEACTIVATE -- When the player types "deactivate [item]"<br>
     * @return The InteractionResult object.
     */
    @Override
    public InteractionResult itemInteractionHandler(ItemInteractionEvent ie) {
        return switch (ie) {
            case DROP -> {
                isPlayerHoldingMap = false;
                yield InteractionResult.success("The map has been dropped!");
            }

            case TAKE -> {
                if (pickupAttempts < 1) {
                    pickupAttempts = 1; // no need to keep incrementing this.
                    this.displayName = "Discarded Map";
                    yield InteractionResult.failure("The map slips through your fingers. Maybe you should try again!");
                } else {
                    isPlayerHoldingMap = true;
                    this.displayName = "Game Map";
                    yield InteractionResult.success("The map has been taken!");
                }
            }

            case USE -> {
                if (isPlayerHoldingMap) {
                    this.description = "A map that appears to have been refolded with great frustration.";
                    yield InteractionResult.success("You successfully use the map.");
                } else {
                    yield InteractionResult.failure("You must be holding the map to use it!");
                }
            }

            case INSPECT -> {
                if (isPlayerHoldingMap) {
                    this.description = "A thoroughly inspected map.";
                    yield InteractionResult.success("You inspect the map.");
                } else {
                    yield InteractionResult.failure("You must be holding the map to inspect it!");
                }
            }

            // Not implemented/handled for this item.
            default -> InteractionResult.failure("This item does not respond to that action.");
        };

    }

}
