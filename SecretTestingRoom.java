package objectAdventure.core.room;
// $Id: SecretTestingRoom.java 368 2023-10-25 22:56:40Z aconover $

import objectAdventure.common.InputInterceptor;
import objectAdventure.core.item.SecretTestingItem;
import objectAdventure.core.player.Player;

import static java.lang.System.out;

/**
 * A room for testing and demo purposes.
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
public final class SecretTestingRoom extends Room implements InputInterceptor, RoomEventListener {

    private final String BASE_DESCRIPTION = "This is just a room for testing purposes.";

    private final String INITIAL_DESCRIPTION_AUGMENTATION =
            """
            Nothing to see here, please move along.
            (Sometimes, the game may lie to you!).""";

    /**
     * Constructor accepting a room ID.
     *
     * @param id The room ID.
     */
    private SecretTestingRoom(final int id) {
        super(id);
        super.setRoomDescription(
                BASE_DESCRIPTION
                        .concat("\n")
                        .concat(INITIAL_DESCRIPTION_AUGMENTATION));

        /* Note that passing "this" out of a constructor is generally a bad practice, and a "Factory
         * Method" or even "Builder Pattern" is better suited for situations where this necessary.
         *
         * For a description of the potential problem see: see:
         * http://www.javapractices.com/topic/TopicAction.do?Id=252
         *
         * Within the "Examples" repository, I created a demo of one of the potential issues.
         * svn://cosc436.xyz:65436/Examples/trunk/src/MiscJavaCodeExamples/LeakyConstructor
         *
         * In other words, the following code is not recommended:
         * <code>
         * SecretTestingItem secretTestingItem = new SecretTestingItem();
         * super.addItem(secretTestingItem);
         * gameInstructions.addObserver(this);
         * </code>
         */
    }

    /**
     * Factory method to create the room.
     *
     * @param roomId The room ID.
     * @return The new room object.
     */
    public static Room newInstance(final int roomId) {
        var thisRoom = new SecretTestingRoom(roomId);
        var testingItem = new SecretTestingItem();

        // add items to room before invoking the constructor
        thisRoom.addItem(testingItem);
        //thisRoom.addItem(new SecretTestingItem());
        //thisRoom.addItem(new DemoItem());
        //item.addObserver(thisRoom);  // add ourselves as an observer of the item

        // Return the room instance.
        return thisRoom;
    }

    /**
     * @return The author (programmer) of the room.
     */
    @Override
    public String getRoomAuthor() {
        return "Adam J. Conover";
    }


    /**
     * Invoked when a player leaves the room.
     *
     * @param player The player leaving the room.
     */
    @Override
    public void playerLeavingRoom(Player player) {
        int destinationRoomId = player.getDestinationRoomId();
        out.printf("Goodbye!  (Player leaving for room %d)%n", destinationRoomId);

        // reset the description upon leaving the room.
        this.setRoomDescription(BASE_DESCRIPTION);
    }

    @Override
    public void playerEnteringRoom(Player player) {
        int previousRoom = player.getPreviousRoomID();

        out.printf("Beware!!! You're entering a secret testing room! (Arriving from room %d)%n", previousRoom);
    }

    /**
     * Example of user input interception.  (Room could have custom parser and then allow the game shell to continue.)
     *
     * @param inputLine The original input line.
     * @return The revised input line.
     */
    @Override
    public String interceptInput(String inputLine) {
        // TESTING: out.println("Hey! That's my line: " + inputLine);
        if (inputLine.equalsIgnoreCase("info")) {
            out.println("This is not an information kiosk!");
            return "";
        } else
            return inputLine;
    }
}
