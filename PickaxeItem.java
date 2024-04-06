package objectAdventure.world.cbrown114;

import java.util.List;
import objectAdventure.common.Observable;
import objectAdventure.common.Observer;
import objectAdventure.core.item.InteractionResult;
import objectAdventure.core.item.Item;
import objectAdventure.core.item.ItemInteractionEvent;

// import java.util.List;

public class PickaxeItem implements Item, Observable<T> {

    protected List<Observer<T>> observers;
    private String description;
    private String displayName;
    private T axeHealth;
    State currentState;

    private final State brokenPickaxeState;
    private final State healthyPickaxe;
    private final State damagedPickaxeState;

    /**
     * Constructor
     */
    public PickaxeItem() {
        this.displayName = "Rusty Pickaxe";
        this.description = "A heavy old pickaxe with rust covering the head. Can definitely still do lots of damage to any wall.";
        
        this.brokenPickaxeState = new BrokenPickaxeState(this);
        this.healthyPickaxe = new HealthyPickaxe(this);
        this.damagedPickaxeState = new DamagedPickaxeState(this);

        currentState = healthyPickaxe;
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

    public T getHealth(){
        return axeHealth;
    }

    /**
     * Item Can be picked up and moved.
     *
     * @return True
     */
    @Override
    public boolean isTransferable() {
        return true; // This item can be picked up and moved.
    }

    @Override
    public void addObserver(Observer<T> theObject) {
        observers.add(theObject);
    }

    @Override
    public List<String> getItemAliases() {
        return List.of("Pickaxe", "Wooden and Rusty");
    }

    @Override
    public void removeObserver(Observer<T> theObject) {
        observers.remove(theObject);
    }

    @Override
    public void notifyObservers(T value) {
        for (Observer<T> observer : observers) {
            observer.update(value);
        }
    }

    public String getDisplayName(){
        return this.displayName;
    }

    public void setDisplayName(String name){
        this.displayName = name;
    }

    public void changeSomethingAboutTheObject(T value) {
        this.axeHealth = value;
        notifyObservers(new T(this, "change"));
    }

    public void setHealthyPickaxe() {
        this.currentState = healthyPickaxe;
    }

    public void setDamagedPickaxeState() {
        this.currentState = damagedPickaxeState;
    }

    public void setBrokenPickaxeState() {
        this.currentState = brokenPickaxeState;
    }


    /**
     * Item Interaction Example
     * 
     *  The actual parameter is an enum (singleton) value of one of the following:<br>
     *           ItemInteractionEvent.DROP -- When the player types "drop [item]"
     *           ItemInteractionEvent.TAKE -- When the player types "get (or take) [item]"
     *           ItemInteractionEvent.USE -- When the player types "use [item]"
     *           ItemInteractionEvent.INSPECT -- When the player types "inspect [item]"
     *           ItemInteractionEvent.PUSH -- When the player types "push [item]"
     *           ItemInteractionEvent.PULL -- When the player types "pull [item]"
     *           ItemInteractionEvent.LOOK -- When the player types "look [item]"
     *           ItemInteractionEvent.ACTIVATE -- When the player types "activate [item]"
     *           ItemInteractionEvent.DEACTIVATE -- When the player types "deactivate [item]"
     *           ItemInteractionEvent.TAUNT
     *           ItemInteractionEvent.OPEN
     *           ItemInteractionEvent.CLOSE
     *           ItemInteractionEvent.THROW
     *
     * @param ie The ItemInteractionEvent info.
     * @return The InteractionResult object.
     * @see objectAdventure.core.item.ItemInteractionEvent
     **/
    @Override
    public InteractionResult itemInteractionHandler(ItemInteractionEvent ie) {
        return switch (ie) {
            case TAKE -> {
                // Do whatever you want when an item is picked up.
                String previousName = this.getItemDisplayName();
                // Then return a success message.
                yield InteractionResult.success("Congratulations! You picked up the %s".formatted(previousName));
            }

            case USE -> {
                // Do whatever you want when an item is picked up.
                this.description = "A slightly used, heavy old pickaxe with rust covering the head. Can definitely still do lots of damage to any wall.";

                // Then return a success message.
                yield InteractionResult.success("You use the " + this.getItemDisplayName() + " object has taken damage!");
            }

            // Everything else should "succeed", but do nothing.
            default -> InteractionResult.success();
        };
    }

    /**
     * The short name of the item for display in lists, etc.
     *
     * @return A short item description that will show in inventory and looking around a room.
     */
    @Override
    public String getItemDisplayName() {
        return this.displayName;
    }
}

