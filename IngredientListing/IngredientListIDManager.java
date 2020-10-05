/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IngredientListing;

/**
 *
 * @author user
 */
public class IngredientListIDManager {
    private static final String INGREDIENT_NAME_TEXT = "_ingredientNameText";
    private static final String INGREDIENT_QTY_TEXT = "_ingredientQtyText";
    private static final String INGREDIENT_UNIT_TEXT = "_ingredientUnitText";
    private static final String REMOVE_INGREDIENT_BTN = "_removeIngredientBtn";
    private static final String ADD_INGREDIENT_BTN = "_addIngredientBtn";
    
    private IngredientListIDManager(){}
    
    public static String getIngredientNameText(){
        return INGREDIENT_NAME_TEXT;
    }
    
    public static String getIngredientQtyText(){
        return INGREDIENT_QTY_TEXT;
    }
    
    public static String getIngredientUnitText(){
        return INGREDIENT_UNIT_TEXT;
    }
    
    public static String getRemoveIngredientBtn(){
        return REMOVE_INGREDIENT_BTN;
    }
    
    public static String getAddIngredientBtn(){
        return ADD_INGREDIENT_BTN;
    }
}
