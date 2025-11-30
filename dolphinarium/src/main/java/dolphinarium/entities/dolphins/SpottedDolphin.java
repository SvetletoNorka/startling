package dolphinarium.entities.dolphins;

public class SpottedDolphin extends BaseDolphin {

    public SpottedDolphin(String name, int energy) {
        super(name, energy);
    }

    @Override
    public void jump() {
        super.jump();
        setEnergy(this.getEnergy() - 90);
    }

    //Can swim in DeepWaterPool as well as in ShallowWaterPool.
}
