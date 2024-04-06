package objectAdventure.world.cbrown114;

public class HealthyPickaxe implements State {
    final PickaxeItem pickaxe;

    // description;
    // private String displayName;
    // private T axeHealth;
    /**
     * State Constructor
     *
     * @param pickaxe
     */
    public HealthyPickaxe(PickaxeItem pickaxe) {
        this.pickaxe = pickaxe;
        pickaxe.setDisplayName("Shiny new pickaxe");

    }

    @Override
    public void swingPickaxe(){

        System.out.println("You swing a" + pickaxe.getDisplayName());
    }

    @Override
    public void pickaxeDamage(){
        System.out.println("This pickaxe does maximum damage");
    }

    @Override
    public void pickaxeDescription(){
        System.out.println("This pickaxe is shiny, sharp, and stiff");
    }
}
