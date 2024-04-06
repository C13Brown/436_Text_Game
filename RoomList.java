package objectAdventure.core;
// $Id: RoomList.java 331 2023-10-19 16:02:40Z aconover $

import objectAdventure.RoomInitializer;
import objectAdventure.core.room.NoSuchRoomException;
import objectAdventure.core.room.SecretTestingRoom;
import objectAdventure.core.room.Room;
import objectAdventure.world.aconover.StartRoom;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * The RoomList class is a singleton class that contains all the rooms in the game.
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
public final class RoomList {

    private static final Logger LOGGER = Logger.getGlobal();
    private final Map<Integer, Room> roomList = new TreeMap<>();

    /**
     * Prevent instantiation, this is a singleton class.
     */
    private RoomList() {
        // Force usage of factory method.
    }

    /**
     * Factory method for construction to allow for complete construction of the RoomList Object
     * before adding any rooms to it.
     *
     * @return a fully constructed room list object
     */
    public static RoomList newInstance() {
        RoomList rooms = new RoomList();
        RoomInitializer.initRooms(rooms);

        // *****************************************************************************************
        // <Rant mode activated>
        //    WARNING: *Bad programming practice below* The following "addRoom()" is used to
        //    mitigate the effects of the (disturbingly common) accidental breakages of the start
        //    room, due to merging, blindly allowing code to overwrite that of others, or not
        //    bothering to test after the merge (or perhaps, even before).
        // </Rant mode deactivated>

        // *****************************************************************************************
        // Added again here, so other's copies with the wrong roomID don't overwrite the proper start
        // room, but I wanted to leave the one in initRooms method, as it illustrates proper "room
        // construction." (Garbage collection will take care of the duplicate.)

        rooms.addRoom(StartRoom.newInstance(0));
        rooms.addRoom(SecretTestingRoom.newInstance(99));

        return rooms;
    }

    /**
     * Show the contents of all rooms in the game.
     *
     * @return formatted string listing the complete contents of the room.
     */
    public String DEBUG_GetAllMapContents() {
        StringBuilder sb = new StringBuilder();

        sb.append("Game Contents:\n");
        for (var room : roomList.values()) {
            sb.append(format("\tRoom %02d: %s\n", room.getRoomId(), room.getClass().getSimpleName()));
            for (var item : room.getItemList()) {
                sb.append(format("\t\tItem: %s (Aliases: %s)%n",
                                 item.getClass().getSimpleName(),
                                 item.getItemAliases()));
            }
        }

        return sb.toString();
    }

    /**
     * Get the room object from the ID.
     *
     * @param roomId the ID of the room to retrieve.
     * @return The room object from the ID.
     * @throws NoSuchRoomException thrown if the room does not exist.
     */
    public Optional<Room> getRoom(int roomId) {
        if (this.exists(roomId)) {
            return Optional.of(roomList.get(roomId));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Does the room exist in the map?
     *
     * @param roomId RoomID to check
     * @return true is exists, false otherwise.
     */
    private boolean exists(int roomId) {
        return roomList.containsKey(roomId);
    }


    /**
     * Add a Room Object to the map.
     *
     * @param room The room object being added to the map.
     */
    public void addRoom(Room room) {
        Integer roomId = room.getRoomId();
        roomList.put(roomId, room);
        LOGGER.log(Level.CONFIG, "Added room {0}: {1}", new Object[]{roomId, room});
    }
}
