package objectAdventure.world.cbrown114;

public class BrokenPickaxeState implements State {
    final PickaxeItem pickaxe;

    // description;
    // private String displayName;
    // private T axeHealth;
    /**
     * State Constructor
     *
     * @param pickaxe
     */
    public BrokenPickaxeState(PickaxeItem pickaxe) {
        this.pickaxe = pickaxe;
        pickaxe.setDisplayName("Broken pickaxe");
    }

    @Override
    public void swingPickaxe(){
        System.out.println("You swing a" + pickaxe.getDisplayName());
    }

    @Override
    public void pickaxeDamage(){
        System.out.println("This pickaxe does no damage");
    }

    @Override
    public void pickaxeDescription(){
        System.out.println("This is no longer a pickaxe but just a broken stick");
    }

    
    
}
