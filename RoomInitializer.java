package objectAdventure;
// $Id: RoomInitializer.java 750 2023-12-15 19:09:13Z cbrown114 $

import objectAdventure.core.RoomList;
import objectAdventure.world.aconover.*;
// import objectAdventure.world.ahernd2.*;
// import objectAdventure.world.alondo1.*;
// import objectAdventure.world.aokpal1.*;
// import objectAdventure.world.aowoey1.*;
// import objectAdventure.world.baidy1.*;
import objectAdventure.world.bsummer1.*;
// import objectAdventure.world.cbrown114.*;
// import objectAdventure.world.cminalo1.*;
import objectAdventure.world.dchen7.*;
// import objectAdventure.world.erichm3.*;
// import objectAdventure.world.hschrec1.*;
// import objectAdventure.world.hshadi1.*;
// import objectAdventure.world.kmarce3.*;
import objectAdventure.world.krolan1.*;
// import objectAdventure.world.mburto6.*;
// import objectAdventure.world.mgebre8.*;
// import objectAdventure.world.nchamp3.*;
// import objectAdventure.world.tsiozo2.*;


/**
 * Note that this REALLY doesn't belong here in terms of "good design" (it
 * should in the Room package or perhaps with the Core package), but it's placed
 * here to make it easier to edit as everyone adds their Room Implementations
 * and makes them available to the shell.
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
public final class RoomInitializer {

    private RoomInitializer() {
        // Utility classes cannot (should not) be instantiated.
    }

    /**
     * Initialization method for all rooms in the game.
     * <p>
     * NOTE: As some want to constantly rearrange imports, use the "Fully Qualified
     * Name" (FQN) in this class to avoid merge conflicts.
     * <p>
     * Using a "newInstance" method is often a better approach than using a
     * constructor directly, for reasons that will be discussed in class later in
     * the semester.
     *
     * @param rooms The list of rooms in the game.
     */
    public static void initRooms(RoomList rooms) {
        // aconover
        // !!! DO NOT MODIFY THE EXAMPLE BELOW -- OTHERS ARE RELYING UPON IT !!!
        final int aconover_roomNumber = 0; // This is the room number assigned to Adam Conover.
        rooms.addRoom(StartRoom.newInstance(aconover_roomNumber)); // Notice the import statement above.

        /*
         * ***********************************************************************
         * EACH STUDENT SHOULD ADD THEIR ROOMS BELOW, USING THE SAME FORMAT AS
         * ABOVE.  Use either the "Fully Qualified Class Name" or uncomment the
         * appropriate import. This will prevent most merge conflicts caused by
         * frequently changing import statements.The room ID should be the same
         * as the one assigned.
         * ***********************************************************************
         */


        // ahernd2
        final int ahernd2_roomNumber = 26;
        rooms.addRoom(objectAdventure.world.ahernd2.SillyRoom.newInstance(ahernd2_roomNumber));

        // alondo1
        final int alondo1_roomNumber = 3;
        rooms.addRoom(objectAdventure.world.alondo1.Theatre.newInstance(alondo1_roomNumber));


        // aokpal1

        final int aokpal_roomNumber = 5;
        rooms.addRoom(objectAdventure.world.aokpal.TreasureRoom.newInstance(aokpal_roomNumber));


        // aowoey1


        // baidy1


        // bsummer1
        final int bsummer1_roomNumber = 13;
        rooms.addRoom(objectAdventure.world.bsummer1.DirtRoom.newInstance(bsummer1_roomNumber));


        // cbrown114
        final int cbrown_roomNumber = 1;
        rooms.addRoom(objectAdventure.world.cbrown114.Room1.newInstance(cbrown_roomNumber, "roomObserver"));

        // cminalo1


        // dchen7

        final int dchen7_roomNumber = 20;
        rooms.addRoom(objectAdventure.world.dchen7.Room20.newInstance(dchen7_roomNumber));

        // erichm3
        final int erichm3_roomNumber = 17;
        rooms.addRoom(objectAdventure.world.erichm3.SpookyRoom.newInstance(erichm3_roomNumber));

        // hschrec1
        final int hschrec1_roomNumber = 16;
        rooms.addRoom(objectAdventure.world.aconover.StartRoom.newInstance(hschrec1_roomNumber));


        // hshadi1


        // kmarce3
        final int kmarce3_roomNumber = 10;
        rooms.addRoom(objectAdventure.world.kmarce3.HogwartsLibrary.newInstance(kmarce3_roomNumber));


        // krolan1
        final int krolan1_roomNumber = 9;
        rooms.addRoom(KhalilRoom.newInstance(krolan1_roomNumber));

        // mburto6
        final int mburto6_roomNumber = 15;
        rooms.addRoom(objectAdventure.world.mburto6.MyRoom.newInstance(mburto6_roomNumber));

        // mgebre8


        // nchamp3


        // tsiozo2
        final int tsiozo2_roomNumber = 18;
        rooms.addRoom(objectAdventure.world.aconover.StartRoom.newInstance(tsiozo2_roomNumber));


    }
}
