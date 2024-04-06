package objectAdventure.world.cbrown114;

public class DamagedPickaxeState implements State {
    final PickaxeItem pickaxe;

    // description;
    // private String displayName;
    // private T axeHealth;
    /**
     * State Constructor
     *
     * @param pickaxe
     */
    public DamagedPickaxeState(PickaxeItem pickaxe) {
        this.pickaxe = pickaxe;
        pickaxe.setDisplayName("Damaged pickaxe");

    }

    @Override
    public void swingPickaxe(){

        System.out.println("You swing a" + pickaxe.getDisplayName());
    }

    @Override
    public void pickaxeDamage(){
        System.out.println("This pickaxe does minimal damage");
    }

    @Override
    public void pickaxeDescription(){
        System.out.println("This pickaxe is rusted cracked");
    }

    
    
}
