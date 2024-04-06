package objectAdventure.core.command;
// $Id: CommandHelp.java 493 2023-11-14 00:04:29Z cbrown114 $

import objectAdventure.core.item.ItemInteractionEvent;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

final class CommandHelp {

    /* The help text for the game. */
    final private static String COMMAND_HELP_TEMPLATE = """
      Help...
        
        Movement:
          N(orth) | S(outh) | E(ast) | W(est) | U(p) | D(own)

        Item Interactions:
          ( TAKE | DROP ) ( <item> | ALL )
          ( %s ) <item>

        Debugging:
          Show Game Elements:
            DISPLAY ( ROOM | MAP )
          Change the logging level: [See Main.java]
            LOG ( <Java Logging Level> )

         Other:
            I                # ("Inventory": Show Player Inventory)
            L {item}         # ("Look": Show Room Description & Room Items)
            T [room id]      # ("Teleport": Jump to RoomID)
            ?                # (This List)""";



    /**
     * A private constructor to prevent instantiation.
     */
    private CommandHelp() {
        // Prevent instantiation, this is a utility class.
    }


    /**
     * Return the help text for item interactions.
     */
    private static String getItemInteractionHelp(Function<ItemInteractionEvent, String> getAliasesFunction) {
        // Get all enum values from ItemInteractionEvent
        return Arrays.stream(ItemInteractionEvent.values())
            .filter(name -> !"TAKE".equals(name))  // In relocation help.
            .filter(name -> !"DROP".equals(name))  // In relocation help.
            .map(getAliasesFunction)
            .sorted()
            .collect(Collectors.joining(" | "));
    }
    

    /**
     * Return the help text for the game.
     *
     * @return The help text for the game.
     */
    public static String help() {
        var msg  = String.format(COMMAND_HELP_TEMPLATE, getItemInteractionHelp(ItemInteractionEvent::getName));
        return msg;
    }

}
