/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IngredientListing;

import static IngredientListing.IngredientList.TEXTFIELD_HEIGHT;
import commonClasses.CustomLinkedList;
import commonClasses.Ingredient;
import commonClasses.SuggestionBoxes;
import commonClasses.UnitMeasureConverter;
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
public class IngredientListReadToUpdateConverter extends IngredientList{
    
    private Text[] ingredientsValue;
    private double[] originalQtys;
    
    public IngredientListReadToUpdateConverter(){
        super();
        ingredientsValue = new Text[0];
    }
    
    private static void swapNodes(int index, int indexSwap, ObservableList<Node> nodes){
        Node positionINode = nodes.get(index);
        Node positionISNode = nodes.get(indexSwap);
        if(indexSwap<index){
            nodes.remove(index);
            nodes.remove(indexSwap);
            nodes.add(indexSwap, positionINode);
            nodes.add(index, positionISNode);
        } else if(indexSwap>index){
            nodes.remove(indexSwap);
            nodes.remove(index);
            nodes.add(index, positionISNode);
            nodes.add(indexSwap, positionINode);
        }
    }
    
    public void clear(){
        this.ingredientsGrid.getChildren().clear();
        ingredientsGrid.add(new Text("Ingredients:"), 0, 0);
        ingredientsSuggestionsPane.getChildren().clear();
        ingredientCounter = 0;
        ingredientsSuggestionsPane.getChildren().add(ingredientsGrid);
        addIngredientToList(); //add empty row
    }
    
    public void readAndInsertTextValues(Text[] ingredientsValue){
        
        //assume these are for textfields and combobox, so we can keep the buttons
        clear();
        this.ingredientsValue = ingredientsValue;
        int followingIndexName = 0;
        int followingIndexQty = ingredientsValue.length/3;
        int followingIndexUnit = followingIndexQty*2;
        
        originalQtys = new double[followingIndexQty];
        while(followingIndexName<ingredientsValue.length/3){
            TextField ingredientName = (TextField)ingredientsGrid.lookup("#"+followingIndexName+IngredientListIDManager.getIngredientNameText());
            TextField ingredientQty = (TextField)ingredientsGrid.lookup("#"+followingIndexName+IngredientListIDManager.getIngredientQtyText());
            ComboBox ingredientUnit = (ComboBox)ingredientsGrid.lookup("#"+followingIndexName+IngredientListIDManager.getIngredientUnitText());
            ingredientName.setText(ingredientsValue[followingIndexName].getText());
            ingredientQty.setText(ingredientsValue[followingIndexQty].getText());
            ingredientUnit.getSelectionModel().select(ingredientsValue[followingIndexUnit].getText());

            originalQtys[followingIndexName] = Double.parseDouble(ingredientsValue[followingIndexQty].getText());
            if(followingIndexUnit != ingredientsValue.length-1){
                addIngredientToList();
            }

            followingIndexName++;
            followingIndexQty++;
            followingIndexUnit++;
        }
    }
    
    public void convertElementsToField(){
        
        ObservableList<Node> children = this.ingredientsGrid.getChildren();
        for(int i=0;i<children.size();i++){
            if(children.get(i).getId() == null || children.get(i).getId().equals("")){
                continue;
            }
            
            int row = Integer.parseInt(children.get(i).getId().split("_")[0])+INGREDIENT_STARTING_INDEX;
            if(children.get(i) instanceof Text){
                if(children.get(i).getId().endsWith(IngredientListIDManager.getIngredientNameText())){
                    TextField ingredientNameField = new TextField();
                    ingredientNameField.setText(((Text)children.get(i)).getText());
                    ingredientNameField.setId(((Text)children.get(i)).getId());
                    resetPositionSuggestion(ingredientNameField, row);
                    setMouseClickAction(ingredientNameField);
                    ingredientsGrid.add(ingredientNameField, INGREDIENT_NAME_INDEX, row);
                    swapNodes(i, children.size()-1, children);
                    children.remove(children.size()-1);
                } else if(children.get(i).getId().endsWith(IngredientListIDManager.getIngredientQtyText())){
                    TextField ingredientQtyField = new TextField();
                    ingredientQtyField.setPrefWidth(50);
                    ingredientQtyField.setText(((Text)children.get(i)).getText());
                    ingredientQtyField.setId(((Text)children.get(i)).getId());
                    setMouseClickAction(ingredientQtyField);
                    ingredientsGrid.add(ingredientQtyField, INGREDIENT_QTY_INDEX, row);
                    swapNodes(i, children.size()-1, children);
                    children.remove(children.size()-1);
                } else if(children.get(i).getId().endsWith(IngredientListIDManager.getIngredientUnitText())){
                    ComboBox ingredientUnitBox = new ComboBox(
                            FXCollections.observableArrayList(UnitMeasureConverter.getUnits()));
                    setMouseClickAction(ingredientUnitBox);
                    ingredientUnitBox.setVisibleRowCount(NB_UNITS_VISIBLE);
                    ingredientUnitBox.getSelectionModel().select(((Text)children.get(i)).getText());
                    ingredientUnitBox.setId(((Text)children.get(i)).getId());
                    ingredientsGrid.add(ingredientUnitBox, INGREDIENT_UNIT_INDEX, row);
                    swapNodes(i, children.size()-1, children);
                    children.remove(children.size()-1);
                }
            } else if(children.get(i) instanceof Button){
                children.get(i).setVisible(true);
            }
        }
        
    }
    
    public void convertElementsToText(){

        ObservableList<Node> children = ingredientsGrid.getChildren();
        for(int i=0;i<children.size();i++){
            if(children.get(i).getId() == null || children.get(i).getId().equals("")){
                continue;
            }
            int row = Integer.parseInt(children.get(i).getId().split("_")[0])+INGREDIENT_STARTING_INDEX;
            if(children.get(i) instanceof TextField){
                Text replacement = new Text(((TextField)children.get(i)).getText());
                replacement.setId(children.get(i).getId());
                if(children.get(i).getId().endsWith(IngredientListIDManager.getIngredientNameText()))
                    ingredientsGrid.add(replacement, INGREDIENT_NAME_INDEX, row);
                else if(children.get(i).getId().endsWith(IngredientListIDManager.getIngredientQtyText()))
                    ingredientsGrid.add(replacement, INGREDIENT_QTY_INDEX, row);
                swapNodes(i, children.size()-1, children);
                children.remove(children.size()-1);
            } else if(children.get(i) instanceof ComboBox){
                Text replacement = new Text((String)(((ComboBox)children.get(i)).getSelectionModel().getSelectedItem()));
                replacement.setId(children.get(i).getId());
                ingredientsGrid.add(replacement, INGREDIENT_UNIT_INDEX, row);
                swapNodes(i, children.size()-1, children);
                children.remove(children.size()-1);
            } else if(children.get(i) instanceof Button){
                children.get(i).setVisible(false);
            }
        }
        
        this.ingredientsSuggestionsPane.getChildren().clear();
        this.ingredientsSuggestionsPane.getChildren().add(ingredientsGrid);
    }
    
    public void multiplyIngredients(int multiplier){
        ObservableList<Node> children = this.ingredientsGrid.getChildren();
        int qtyTextCounter = 0;
        for(int i=0;i<children.size();i++){
            if(children.get(i).getId() == null || children.get(i).getId().equals("")){
                continue;
            }
            
            int row = Integer.parseInt(children.get(i).getId().split("_")[0])+INGREDIENT_STARTING_INDEX;
            if(children.get(i) instanceof Text){
                if(children.get(i).getId().endsWith(IngredientListIDManager.getIngredientQtyText())){
                    double qtyValue = originalQtys[qtyTextCounter];
                    qtyValue *= multiplier;
                    ((Text)children.get(i)).setText(String.format("%.2f",qtyValue));
                    qtyTextCounter++;
                }
            } else if(children.get(i) instanceof TextField){
                if(children.get(i).getId().endsWith(IngredientListIDManager.getIngredientQtyText())){
                    double qtyValue = originalQtys[qtyTextCounter];
                    qtyValue *= multiplier;
                    ((TextField)children.get(i)).setText(String.format("%.2f",qtyValue));
                    qtyTextCounter++;
                }
            }
        }
    }
    
    private void resetPositionSuggestion(TextField ingredientName, int row){ //did not help...
        SuggestionBoxes suggestionBoxesExtension = new SuggestionBoxes(NB_SUGGESTIONS, ingredientName);
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
                SUGGESTION_ITEM_TRANSLATE_Y+(row-1)*TEXTFIELD_HEIGHT);
        
        //adjust height of pane
        //gridsContainerWatcher.incrementIngredientByOne();
        
        VBox suggestionsPane = suggestionBoxesExtension.getSuggestionPane();
        
        suggestionsPane.setId(row+suggestionsStackPaneID); //to easily remove
        ingredientsSuggestionsPane.getChildren().add(suggestionsPane);
    }
    
    public void hasCancelled(){ //should be having fields in this case.. in modify mode
        readAndInsertTextValues(ingredientsValue);
    }
    
    public void hasSaved(){
        //dummy to hold consistency
    }
}
