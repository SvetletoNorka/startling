package dolphinarium.entities.pools;

import dolphinarium.common.ExceptionMessages;
import dolphinarium.entities.dolphins.Dolphin;
import dolphinarium.entities.foods.Food;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BasePool implements Pool {
    String name;
    int capacity;

    Collection<Food> foods;
    Collection<Dolphin> dolphins;

    public BasePool(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.foods = new ArrayList<>();
        this.dolphins = new ArrayList<>();
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException(ExceptionMessages.POOL_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public Collection<Food> getFoods() {
        return foods;
    }

    @Override
    public Collection<Dolphin> getDolphins() {
        return dolphins;
    }

    @Override
    public void addDolphin(Dolphin dolphin) {

        if (dolphins.size() >= capacity) {
            throw new IllegalStateException(ExceptionMessages.NOT_ENOUGH_CAPACITY);
        }

        if (dolphin.getEnergy() <= 0) {
            throw new IllegalArgumentException(ExceptionMessages.DOLPHIN_ENERGY_BELOW_OR_EQUAL_ZERO);
        }

        this.dolphins.add(dolphin);
    }

    @Override
    public void removeDolphin(Dolphin dolphin){
        this.dolphins.remove(dolphin);
    }

    @Override
    public  void addFood(Food food){
        this.foods.add(food);
    }
}