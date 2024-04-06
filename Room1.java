package objectAdventure.world.cbrown114;
// $Id: Room1.java 550 2023-11-21 17:23:08Z cbrown114 $
import objectAdventure.common.Observer;
import objectAdventure.core.item.Item;
import objectAdventure.core.room.Room;

/**
 * The StartRoom is the first room the player will see when they start the game.
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
public class Room1 extends Room implements Observer<T>{
    private final String roomObserver;
    /**
     * Constructor for the room, it must be constructed with an ID!!!
     *
     * @param roomId The ID of the room.
     */
    public Room1(final int roomId, String roomObserver) {
        super(roomId);
        this.roomObserver = roomObserver;

        String description = """
            This is Room 1 there are no other connecting rooms except for the one you have just came from.
            
            There is an old blocked off mineshaft in the room, too dark and dangerous to enter.
            Next to it however, you can see a old pickaxe.
            
            You can type "help" (or "?") to see a list of commands that you can use in the game or
            "quit" (or "Q") to quit.""";

        // Set the room description.
        super.setRoomDescription(description);
    }

    /**
     * Factory methods are used to create new objects using static methods, and this is sometimes a better approach than
     * using a constructor directly. It allows the constructor to complete the essential initialization of the object,
     * and then the factory method can perform additional initialization on a fully instantiated object.
     *
     * @param roomId The ID of the room.
     * @return A new room.
     */
    public static Room newInstance(final int roomId, final String roomObserver) {
        // Create a new room.
        Room theRoom = new Room1(roomId, roomObserver);

        // Add an item to the room.
        Item pickaxe = new PickaxeItem();
        theRoom.addItem(pickaxe);

        // Return the room.
        return theRoom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(T customMessage) {
        // Display a customMessage when the subject's value changes.
        System.out.printf(
                "The Room has been updated with %s by %s. (Item Class: %s)\n",
                customMessage.message(),
                customMessage.myItemType(),
                customMessage.myItemType().getClass());
    }

    @Override
    public String getRoomAuthor() {
        return "Connor Brown";
    }
}
