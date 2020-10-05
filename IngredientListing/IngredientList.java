/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IngredientListing;

import commonClasses.PaneHeightMaintainer;
import commonClasses.SuggestionBoxes;
import commonClasses.UnitMeasureConverter;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import listIngredients.IngredientsIdentificationv2;

/**
 *
 * @author user
 */
public class IngredientList {
    
    protected ArrayList<SuggestionBoxes> suggestionBoxesArray;
    protected GridPane ingredientsGrid;
    protected StackPane ingredientsSuggestionsPane;
    protected Button addIngredientButton;
    
    protected String promptTextName = "Ingredient Name";
    protected String promptTextQty = "Qty";
    protected String ingredientSuggestionID = "_ingredientNameSuggestionsID";
    protected String suggestionsStackPaneID = "_suggestionsStackPaneID";
    protected int NB_SUGGESTIONS = 5;
    protected int NB_UNITS_VISIBLE = 5;
    
    protected int SUGGESTION_ITEM_TRANSLATE_X = -140;
    protected int SUGGESTION_ITEM_TRANSLATE_Y = 68;
    protected static final int INGREDIENT_NAME_INDEX = 0;
    protected static final int INGREDIENT_QTY_INDEX = 1;
    protected static final int INGREDIENT_UNIT_INDEX = 2;
    protected static final int REMOVE_INGREDIENT_INDEX = 3;
    protected static final int ADD_INGREDIENT_INDEX = 4;
    protected static final int TEXTFIELD_HEIGHT = 25;
    protected static final int INGREDIENT_STARTING_INDEX = 1;
    protected int ingredientCounter;
    
    protected PaneHeightMaintainer observerPaneMaintainer;
    
    public IngredientList(){
        suggestionBoxesArray = new ArrayList<>();
        ingredientsGrid = new GridPane();
        ingredientsGrid.add(new Text("Ingredients:"), 0, 0);
        addIngredientButton = new Button("+");
        addIngredientButton.setId("1"+IngredientListIDManager.getAddIngredientBtn());
        setMouseClickAction(addIngredientButton);
        addIngredientButton.setOnAction(e->{
            addIngredientToList();
        });
        ingredientCounter = 0;
        ingredientsSuggestionsPane = new StackPane();
        ingredientsSuggestionsPane.getChildren().add(ingredientsGrid);
        addIngredientToList();
    }
    
    public IngredientList(String promptText, int nbSuggestions){
        this.promptTextName = promptText;
        this.NB_SUGGESTIONS = nbSuggestions;
        suggestionBoxesArray = new ArrayList<>();
        ingredientsGrid = new GridPane();
        ingredientsGrid.add(new Text("Ingredients:"), 0, 0);
        addIngredientButton = new Button("+");
        addIngredientButton.setId("1"+IngredientListIDManager.getAddIngredientBtn());
        setMouseClickAction(addIngredientButton);
        addIngredientButton.setOnAction(e->{
            addIngredientToList();
        });
        ingredientCounter = 0;
        ingredientsSuggestionsPane = new StackPane();
        ingredientsSuggestionsPane.getChildren().add(ingredientsGrid);
        addIngredientToList();
    }
    
    protected void addIngredientToList(){
        createNewRowInGridPane();
        ingredientsGrid.getChildren().remove(addIngredientButton);
        //addIngredientButton.setId(IngredientListIDManager.getAddIngredientBtn());
        ingredientsGrid.add(addIngredientButton, ADD_INGREDIENT_INDEX, ingredientCounter+INGREDIENT_STARTING_INDEX);
        ingredientCounter++;
    }
    
    private void removeRowInGridPane(Button removeIngredientButtonOfRow){
        int index = Integer.parseInt(removeIngredientButtonOfRow.getId().split("_")[0]);
        
        ObservableList<Node> gridChildren = ingredientsGrid.getChildren();
        if(ingredientCounter>1){
            for(int i=index+1;i<ingredientCounter;i++){
                TextField ingredientNameCurrent = (TextField)ingredientsGrid.lookup("#"+i+IngredientListIDManager.getIngredientNameText());
                TextField ingredientQtyCurrent =  (TextField)ingredientsGrid.lookup("#"+i+IngredientListIDManager.getIngredientQtyText());
                ComboBox typeIngredientCurrent = (ComboBox)ingredientsGrid.lookup("#"+i+IngredientListIDManager.getIngredientUnitText());
                
                TextField ingredientNamePrevious = (TextField)ingredientsGrid.lookup("#"+(i-1)+IngredientListIDManager.getIngredientNameText());
                TextField ingredientQtyPrevious = (TextField)ingredientsGrid.lookup("#"+(i-1)+IngredientListIDManager.getIngredientQtyText());
                ComboBox typeIngredientPrevious = (ComboBox)ingredientsGrid.lookup("#"+(i-1)+IngredientListIDManager.getIngredientUnitText());
                
                ingredientNamePrevious.setText(ingredientNameCurrent.getText());
                ingredientQtyPrevious.setText(ingredientQtyCurrent.getText());
                typeIngredientPrevious.getSelectionModel().select(typeIngredientCurrent.getSelectionModel().getSelectedIndex());
            }

            ComboBox lastTypeIngredient = (ComboBox)ingredientsGrid.lookup("#"+(ingredientCounter-1)+IngredientListIDManager.getIngredientUnitText());
            TextField lastIngredient = (TextField)ingredientsGrid.lookup("#"+(ingredientCounter-1)+IngredientListIDManager.getIngredientNameText());
            TextField lastIngredientQty = (TextField)ingredientsGrid.lookup("#"+(ingredientCounter-1)+IngredientListIDManager.getIngredientQtyText());
            Button lastIngredientMinus = (Button)ingredientsGrid.lookup("#"+(ingredientCounter-1)+IngredientListIDManager.getRemoveIngredientBtn());
            
            removeTextFieldInteractions(lastIngredient);
            gridChildren.remove(lastTypeIngredient);
            gridChildren.remove(lastIngredient);
            gridChildren.remove(lastIngredientQty);
            gridChildren.remove(lastIngredientMinus);
            gridChildren.remove(addIngredientButton);
            ingredientCounter--;
            ingredientsGrid.add(addIngredientButton, ADD_INGREDIENT_INDEX, ingredientCounter-1+INGREDIENT_STARTING_INDEX);
            //addIngredientButton.setId((ingredientCounter-1)+addIngredientBtnID);
        } else{ //empty fields
            TextField ingredientNameCurrent = (TextField)ingredientsGrid.lookup("#"+index+IngredientListIDManager.getIngredientNameText());
            TextField ingredientQtyCurrent =  (TextField)ingredientsGrid.lookup("#"+index+IngredientListIDManager.getIngredientQtyText());
            ComboBox typeIngredientCurrent = (ComboBox)ingredientsGrid.lookup("#"+index+IngredientListIDManager.getIngredientUnitText());
            
            ingredientNameCurrent.setText("");
            ingredientQtyCurrent.setText("");
            typeIngredientCurrent.getSelectionModel().selectFirst();
        }
        
        if(observerPaneMaintainer != null){
            observerPaneMaintainer.decrementIngredientByOne();
        }
    }
    
    private void createNewRowInGridPane(){
        TextField ingredientNameField = new TextField();
        ingredientNameField.setPromptText(promptTextName);
        ingredientNameField.setId(ingredientCounter+IngredientListIDManager.getIngredientNameText());
        setTextFieldInteractions(ingredientNameField);
        
        TextField ingredientQtyField = new TextField();
        setMouseClickAction(ingredientQtyField);
        ingredientQtyField.setPromptText(promptTextQty);
        ingredientQtyField.setPrefWidth(50);
        ingredientQtyField.setId(ingredientCounter+IngredientListIDManager.getIngredientQtyText());
        
        ComboBox ingredientUnitBox = new ComboBox(
                FXCollections.observableArrayList(UnitMeasureConverter.getUnits()));
        setMouseClickAction(ingredientUnitBox);
        ingredientUnitBox.setVisibleRowCount(NB_UNITS_VISIBLE); //automatically creates scrollbar
        ingredientUnitBox.getSelectionModel().selectFirst();
        ingredientUnitBox.setId(ingredientCounter+IngredientListIDManager.getIngredientUnitText());
        
        Button removeIngredientButton = new Button("-");
        removeIngredientButton.setId(ingredientCounter+IngredientListIDManager.getRemoveIngredientBtn());
        setMouseClickAction(removeIngredientButton);
        removeIngredientButton.setOnAction(e->{
            removeRowInGridPane(removeIngredientButton);
        });
        
        ingredientsGrid.add(ingredientNameField, INGREDIENT_NAME_INDEX, ingredientCounter+INGREDIENT_STARTING_INDEX);
        ingredientsGrid.add(ingredientQtyField, INGREDIENT_QTY_INDEX, ingredientCounter+INGREDIENT_STARTING_INDEX);
        ingredientsGrid.add(ingredientUnitBox, INGREDIENT_UNIT_INDEX, ingredientCounter+INGREDIENT_STARTING_INDEX);
        ingredientsGrid.add(removeIngredientButton, REMOVE_INGREDIENT_INDEX, ingredientCounter+INGREDIENT_STARTING_INDEX);
        
        if(observerPaneMaintainer != null){
            observerPaneMaintainer.incrementIngredientByOne();
        }
    }
    
    protected void setMouseClickAction(Node node){
        node.setOnMouseClicked(e->{
            Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        //close all pending suggestions
                        for(int i=0;i<suggestionBoxesArray.size();i++){
                            suggestionBoxesArray.get(i).removeSuggestionsOnOutFocus();
                        }
                    }
                });
        });
    }
    
    private void setTextFieldInteractions(TextField ingredientName){
        SuggestionBoxes suggestionBoxesExtension = new SuggestionBoxes(NB_SUGGESTIONS, ingredientName);
        int row = Integer.parseInt(ingredientName.getId().split("_")[0]);
        suggestionBoxesExtension.setId(row+ingredientSuggestionID);
        ingredientName.setOnKeyTyped(e->{
            Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        //close all pending suggestions
                        for(int i=0;i<suggestionBoxesArray.size();i++){
                            suggestionBoxesArray.get(i).removeSuggestionsOnOutFocus();
                        }
                        suggestionBoxesExtension.listIngredientsMatchingText(
                            IngredientsIdentificationv2.getIngredientsStartingWith(ingredientName.getText()));
                    }
                });
        });
        ingredientName.setOnMouseClicked(e->{
            Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        //close all pending suggestions
                        for(int i=0;i<suggestionBoxesArray.size();i++){
                            suggestionBoxesArray.get(i).removeSuggestionsOnOutFocus();
                        }
                    }
                });
        });
        suggestionBoxesArray.add(suggestionBoxesExtension);
        suggestionBoxesExtension.setTranslationComponents(SUGGESTION_ITEM_TRANSLATE_X, 
                SUGGESTION_ITEM_TRANSLATE_Y+(ingredientCounter-1)*TEXTFIELD_HEIGHT);
        
        //adjust height of pane
        //gridsContainerWatcher.incrementIngredientByOne();
        
        VBox suggestionsPane = suggestionBoxesExtension.getSuggestionPane();
        
        suggestionsPane.setId(row+suggestionsStackPaneID); //to easily remove
        ingredientsSuggestionsPane.getChildren().add(suggestionsPane);
    }
    
    private void removeTextFieldInteractions(TextField ingredientName){
        int row = Integer.parseInt(ingredientName.getId().split("_")[0]);
        
        for(int i=0;i<suggestionBoxesArray.size();i++){
            if(suggestionBoxesArray.get(i).getId().equals(row+ingredientSuggestionID)){
                suggestionBoxesArray.remove(i);
            }
        }
        
        VBox suggestionsPane = (VBox)ingredientsSuggestionsPane.lookup("#"+row+suggestionsStackPaneID);
        ingredientsSuggestionsPane.getChildren().remove(suggestionsPane);
    }
    
    public ArrayList<SuggestionBoxes> getSuggestionBoxesArray(){
        return suggestionBoxesArray;
    }
    
    public StackPane getIngredientsList(){
        return ingredientsSuggestionsPane;
    }
    
    public void displaceSuggestions(int translateX, int translateY){
        this.SUGGESTION_ITEM_TRANSLATE_X = translateX;
        this.SUGGESTION_ITEM_TRANSLATE_Y = translateY;
        for(int i=0;i<suggestionBoxesArray.size();i++){
            suggestionBoxesArray.get(i).setTranslationComponents(SUGGESTION_ITEM_TRANSLATE_X, 
                SUGGESTION_ITEM_TRANSLATE_Y+(i-1)*TEXTFIELD_HEIGHT);
        }
    }
    
    public void setPaneHeightObserver(PaneHeightMaintainer observer){
        this.observerPaneMaintainer = observer;
    }
    
    public int getNumberOfIngredients(){
        return ingredientCounter;
    }
}
