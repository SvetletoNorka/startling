package dolphinarium.entities.dolphins;

import dolphinarium.common.ExceptionMessages;
import dolphinarium.entities.foods.Food;

public abstract class BaseDolphin implements Dolphin {
    String name;
    int energy;

    public BaseDolphin(String name, int energy) {
        this.name = name;
        this.energy = energy;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException(ExceptionMessages.DOLPHIN_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    public void setEnergy(int energy) {
        if (energy <= 0) {
            energy = 0;
        }
        this.energy = energy;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public void jump() {
        this.energy -= 10;
    }

    @Override
    public void eat(Food food) {
        this.energy += food.getCalories();
    }
}
