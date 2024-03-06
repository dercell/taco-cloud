package tacos.util;


import tacos.data.Ingredient;
import tacos.data.IngredientUDT;
import tacos.data.Taco;
import tacos.data.TacoUDT;

public class TacoUDRUtils {

    public static IngredientUDT toIngredientUDT(Ingredient ingredient){
        return new IngredientUDT(ingredient.getName(), ingredient.getType());
    }

    public static TacoUDT toTacoUDT(Taco taco){
        return new TacoUDT(taco.getName(), taco.getIngredients());
    }

}
