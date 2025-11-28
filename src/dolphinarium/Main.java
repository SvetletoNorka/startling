package dolphinarium;

import dolphinarium.core.Engine;
import dolphinarium.core.EngineImpl;
import dolphinarium.entities.pools.Pool;

public class Main {
    public static void main(String[] args) {
        Engine engine = new EngineImpl();
        engine.run();
    }
}

//Input:
//AddPool DeepWaterPool SeaDream
//AddPool DeepWaterPool SeaDream
//AddPool ShallowWaterPool OceanCurrent
//AddPool DeepWaterPool BlueAbyss
//AddDolphin SeaDream BottleNoseDolphin Rick 600
//AddDolphin OceanCurrent SpinnerDolphin Spinny 400
//AddDolphin SeaDream BottleNoseDolphin Rick 60
//FeedDolphins SeaDream Mackerel
//AddFoodToPool OceanCurrent Herring
//BuyFood Mackerel
//BuyFood Herring
//BuyFood Herring
//FeedDolphins SeaDream Mackerel
//AddFoodToPool SeaDream Herring
//FeedDolphins SeaDream Herring
//PlayWithDolphins SeaDream
//PlayWithDolphins BlueAbyss
//GetStatistics
//Exit

//Output:
//Successfully added DeepWaterPool SeaDream.
//Pool already exists.
//Successfully added ShallowWaterPool OceanCurrent.
//Successfully added DeepWaterPool BlueAbyss.
//Successfully added BottleNoseDolphin Rick to SeaDream.
//Successfully added SpinnerDolphin Spinny to OceanCurrent.
//Dolphin already exists.
//There is no such food for this pool.
//There isn't a food of type Herring in repository.
//Successfully bought food Mackerel.
//Successfully bought food Herring.
//Successfully bought food Herring.
//There is no such food for this pool.
//Successfully added food Herring to SeaDream.
//        1 dolphin/s in pool SeaDream was/were fed.
//There was a play with dolphin/s in SeaDream. 0 dolphins was/were removed!
//There are no dolphins to play with.
//Dolphins in pool SeaDream:
//Rick - 600
//Dolphins in pool OceanCurrent:
//Spinny - 400
//Dolphins in pool BlueAbyss:
//none