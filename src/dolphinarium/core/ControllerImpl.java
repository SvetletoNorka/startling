package dolphinarium.core;

import dolphinarium.common.ExceptionMessages;
import dolphinarium.entities.dolphins.BottleNoseDolphin;
import dolphinarium.entities.dolphins.Dolphin;
import dolphinarium.entities.dolphins.SpinnerDolphin;
import dolphinarium.entities.dolphins.SpottedDolphin;
import dolphinarium.entities.foods.Food;
import dolphinarium.entities.foods.Herring;
import dolphinarium.entities.foods.Mackerel;
import dolphinarium.entities.foods.Squid;
import dolphinarium.entities.pools.DeepWaterPool;
import dolphinarium.entities.pools.Pool;
import dolphinarium.entities.pools.ShallowWaterPool;
import dolphinarium.repositories.FoodRepositoryImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import static dolphinarium.common.ConstantMessages.*;
import static dolphinarium.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {

    private FoodRepositoryImpl foodRepository;
    private Collection<Pool> pools;

    public ControllerImpl() {
        this.foodRepository = new FoodRepositoryImpl();
        this.pools = new ArrayList<>();
    }

    @Override
    public String addPool(String poolType, String poolName) {

        if (!(poolType.equals("DeepWaterPool") || poolType.equals("ShallowWaterPool"))) {
            throw new NullPointerException(ExceptionMessages.INVALID_POOL_TYPE);
        }

        Pool pool = getPoolByName(poolName);

        if (pool != null) {
            throw new NullPointerException(ExceptionMessages.POOL_EXISTS);
        }

        switch (poolType) {
            case "DeepWaterPool":
                pool = new DeepWaterPool(poolName);
                pools.add(pool);
                break;
            case "ShallowWaterPool":
                pool = new ShallowWaterPool(poolName);
                pools.add(pool);
                break;
        }

        return String.format(SUCCESSFULLY_ADDED_POOL_TYPE, poolType, poolName);
    }

    @Override
    public String buyFood(String foodType) {
        if (!(foodType.equals("Squid") || foodType.equals("Herring") || foodType.equals("Mackerel"))) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_FOOD_TYPE);
        }

        Food food;
        switch (foodType) {
            case "Squid":
                food = new Squid();
                foodRepository.add(food);
                break;
            case "Herring":
                food = new Herring();
                foodRepository.add(food);
                break;
            case "Mackerel":
                food = new Mackerel();
                foodRepository.add(food);
                break;
        }

        return String.format(SUCCESSFULLY_BOUGHT_FOOD_TYPE, foodType);
    }

    @Override
    public String addFoodToPool(String poolName, String foodType) {

        Pool pool = getPoolByName(poolName);
        Food food = foodRepository.findByType(foodType);
        if (food == null) {
            throw new IllegalArgumentException(String.format(NO_FOOD_FOUND, foodType));
        }
        pool.addFood(food);
        foodRepository.remove(food);
        return String.format(SUCCESSFULLY_ADDED_FOOD_IN_POOL, foodType, poolName);
    }

    @Override
    public String addDolphin(String poolName, String dolphinType, String dolphinName, int energy) {

        Dolphin dolphi;
        Pool pool = getPoolByName(poolName);

        for (Dolphin d : pool.getDolphins()) {
            if (d.getName().equals(dolphinName)) {
                throw new IllegalArgumentException(DOLPHIN_EXISTS);
            }
        }

        switch (dolphinType) {
            case "BottleNoseDolphin":
                dolphi = new BottleNoseDolphin(dolphinName, energy);
                break;
            case "SpinnerDolphin":
                dolphi = new SpinnerDolphin(dolphinName, energy);
                break;
            case "SpottedDolphin":
                dolphi = new SpottedDolphin(dolphinName, energy);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_DOLPHIN_TYPE);
        }

        String poolType = pool.getClass().getSimpleName();
        if (poolType.equals("DeepWaterPool") &&
                (dolphinType.equals("BottleNoseDolphin") || dolphinType.equals("SpottedDolphin"))) { // check the capacity of pool?
            pool.addDolphin(dolphi);
            return String.format(SUCCESSFULLY_ADDED_DOLPHIN_IN_POOL, dolphinType, dolphinName, poolName);
        } else if (poolType.equals("ShallowWaterPool") &&
                (dolphinType.equals("SpinnerDolphin") || dolphinType.equals("SpottedDolphin"))) {
            pool.addDolphin(dolphi);
            return String.format(SUCCESSFULLY_ADDED_DOLPHIN_IN_POOL, dolphinType, dolphinName, poolName);
        } else {
            return POOL_NOT_SUITABLE;
        }
    }

    @Override
    public String feedDolphins(String poolName, String foodType) {

        Pool pool = getPoolByName(poolName);
        int countDolphi = pool.getDolphins().size();

        Food food = pool.getFoods().stream()
                .filter(f -> f.getClass().getSimpleName().equals(foodType))
                .findFirst()
                .orElse(null);

        if (food == null) {
            throw new IllegalArgumentException(NO_FOOD_OF_TYPE_ADDED_TO_POOL);
        }

        for (Dolphin dolphin : pool.getDolphins()) {
            dolphin.eat(food);
        }
        pool.getFoods().remove(food);

        return String.format(DOLPHINS_FED, pool.getDolphins().size(), poolName);
    }

    @Override
    public String playWithDolphins(String poolName) {

        Pool pool = getPoolByName(poolName);

        if (pool.getDolphins().isEmpty()) {
            throw new IllegalArgumentException(NO_DOLPHINS);
        }

        int removed = 0;
        Iterator<Dolphin> dolphins = pool.getDolphins().iterator();

        while (dolphins.hasNext()) {
            Dolphin dolphin = dolphins.next();
            dolphin.jump();
            if (dolphin.getEnergy() <= 0) {
                dolphins.remove();
                removed++;
            }
        }

        return String.format(DOLPHINS_PLAY, poolName, removed);
    }

    @Override
    public String getStatistics() {

        StringBuilder build = new StringBuilder();

        for (Pool pool : pools) {
            build.append(String.format(DOLPHINS_FINAL, pool.getName()))
                    .append(System.lineSeparator());

            if (pool.getDolphins().isEmpty()) {
                build.append(NONE).append(System.lineSeparator());
            } else {
                build.append(pool.getDolphins().stream().map(dolphin -> dolphin.getName() + " - " + dolphin.getEnergy()).collect(Collectors.joining(DELIMITER)))
                        .append(System.lineSeparator());
            }
        }
        return build.toString().trim();
    }

    public Pool getPoolByName(String name) {
        return pools.stream()
                .filter(pool -> pool.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
