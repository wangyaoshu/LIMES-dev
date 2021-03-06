package org.aksw.limes.core.gui.view;

import static org.aksw.limes.core.gui.util.SourceOrTarget.SOURCE;

import java.util.List;

import org.aksw.limes.core.gui.controller.EditPropertyMatchingController;
import org.aksw.limes.core.gui.util.SourceOrTarget;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * used for property matching step in {@link org.aksw.limes.core.gui.view.WizardView}
 * 
 * @author Daniel Obraczka {@literal <} soz11ffe{@literal @}
 *         studserv.uni-leipzig.de{@literal >}
 */
public class EditPropertyMatchingView implements IEditView {
    private EditPropertyMatchingController controller;
    private ScrollPane rootPane;
    private ListView<String> sourcePropList;
    private ListView<String> targetPropList;
    private ListView<String> addedSourcePropsList;
    private ListView<String> addedTargetPropsList;
    private Button addAllButton;
    private Button removeAllButton;
    private ObservableList<String> sourceProperties = FXCollections
            .observableArrayList();
    private ObservableList<String> targetProperties = FXCollections
            .observableArrayList();
    private Label missingPropertiesLabel;
    private Label sourceID;
    private Label targetID;

    /**
     * Constructor creates the root pane and adds listeners
     */
    EditPropertyMatchingView() {
        createRootPane();
        addListeners();
    }

    /**
     * Sets the corresponding Controller
     *
     * @param controller controller
     */
    public void setController(EditPropertyMatchingController controller) {
        this.controller = controller;
    }


    /**
     * creates the root pane
     */
    private void createRootPane() {
        sourcePropList = new ListView<String>();
        targetPropList = new ListView<String>();
        addedSourcePropsList = new ListView<String>();
        addedTargetPropsList = new ListView<String>();
        Label sourceLabel = new Label("available Source Properties:");
        sourceID = new Label();
        Label targetLabel = new Label("available Target Properties:");
        targetID = new Label();
        Label addedSourceLabel = new Label("added Source Properties:");
        Label addedTargetLabel = new Label("added Target Properties:");
        VBox sourceColumn = new VBox();
        VBox targetColumn = new VBox();
        sourceColumn.getChildren().addAll(sourceID, sourceLabel, sourcePropList, addedSourceLabel, addedSourcePropsList);
        targetColumn.getChildren().addAll(targetID, targetLabel, targetPropList, addedTargetLabel, addedTargetPropsList);
        addAllButton = new Button("Add all");
        removeAllButton = new Button("Remove all");
        missingPropertiesLabel = new Label("At least one source and one target property must be chosen!");
        missingPropertiesLabel.setTextFill(Color.RED);
        missingPropertiesLabel.setVisible(false);
        HBox buttons = new HBox();
        buttons.getChildren().addAll(missingPropertiesLabel, addAllButton, removeAllButton);
        BorderPane root = new BorderPane();
        HBox hb = new HBox();
        hb.getChildren().addAll(sourceColumn, targetColumn);
        HBox.setHgrow(sourceColumn, Priority.ALWAYS);
        HBox.setHgrow(targetColumn, Priority.ALWAYS);
        root.setCenter(hb);
        root.setBottom(buttons);
        rootPane = new ScrollPane(root);
        rootPane.setOnMouseClicked(e -> {
            missingPropertiesLabel.setVisible(false);
        });
        rootPane.setFitToHeight(true);
        rootPane.setFitToWidth(true);
    }

    /**
     * adds listener to properties to display changes after loading them is finished.
     * also adds functionality
     */
    @SuppressWarnings("all")
    private void addListeners() {

        sourceProperties.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                if (sourceProperties.size() != 0
                        && targetProperties.size() != 0) {
                    putAllPropertiesToTable();
                }
            }
        });

        targetProperties.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                if (sourceProperties.size() != 0
                        && targetProperties.size() != 0) {
                    putAllPropertiesToTable();
                }
            }
        });

        sourcePropList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
        	if(sourcePropList.getSelectionModel().getSelectedItem() != null){
                addedSourcePropsList.getItems().add(sourcePropList.getSelectionModel().getSelectedItem());
                sourcePropList.getItems().remove(sourcePropList.getSelectionModel().getSelectedItem());
        	}
            }
        });

        targetPropList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
        	if(targetPropList.getSelectionModel().getSelectedItem() != null){
                addedTargetPropsList.getItems().add(targetPropList.getSelectionModel().getSelectedItem());
                targetPropList.getItems().remove(targetPropList.getSelectionModel().getSelectedItem());
        	}
            }
        });

        addedSourcePropsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
        	if(addedSourcePropsList.getSelectionModel().getSelectedItem() != null){
                sourcePropList.getItems().add(addedSourcePropsList.getSelectionModel().getSelectedItem());
                addedSourcePropsList.getItems().remove(addedSourcePropsList.getSelectionModel().getSelectedItem());
        	}
            }
        });

        addedTargetPropsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
        	if(addedTargetPropsList.getSelectionModel().getSelectedItem() != null){
                targetPropList.getItems().add(addedTargetPropsList.getSelectionModel().getSelectedItem());
                addedTargetPropsList.getItems().remove(addedTargetPropsList.getSelectionModel().getSelectedItem());
        	}
            }
        });

        addAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                addedSourcePropsList.getItems().addAll(sourcePropList.getItems());
                addedTargetPropsList.getItems().addAll(targetPropList.getItems());
                sourcePropList.getItems().clear();
                targetPropList.getItems().clear();
            }
        });

        removeAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                sourcePropList.getItems().addAll(addedSourcePropsList.getItems());
                targetPropList.getItems().addAll(addedTargetPropsList.getItems());
                addedSourcePropsList.getItems().clear();
                addedTargetPropsList.getItems().clear();
            }
        });
    }

    /**
     * Returns the rootPane
     *
     * @return rootPane
     */
    @Override
    public Parent getPane() {
        return rootPane;
    }

    /**
     * Saves the table items
     */
    @Override
    public void save() {
        controller.save(this.addedSourcePropsList, this.addedTargetPropsList);
    }

    /**
     * Shows the available properties
     *
     * @param sourceOrTarget enum for source or target
     * @param properties list of properties to show
     */
    public void showAvailableProperties(SourceOrTarget sourceOrTarget,
                                        List<String> properties) {
        addedSourcePropsList.getItems().clear();
        addedTargetPropsList.getItems().clear();
        (sourceOrTarget == SOURCE ? sourceProperties : targetProperties)
                .setAll(properties);
    }

    /**
     * Puts the loaded properties to the table
     */
    private void putAllPropertiesToTable() {
        sourcePropList.setItems(sourceProperties);
        targetPropList.setItems(targetProperties);
        sourceID.setText("ID: " + controller.getConfig().getSourceInfo().getId() + "\t Class: " + controller.getConfig().getSourceEndpoint().getCurrentClass().getName());
        targetID.setText("ID: " + controller.getConfig().getTargetInfo().getId() + "\t Class: " + controller.getConfig().getTargetEndpoint().getCurrentClass().getName());
    }


    /**
     * Shows an error if something went wrong
     *
     * @param header header of error 
     * @param content content of message
     */
    public void showError(String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public ListView<String> getAddedSourcePropsList() {
        return addedSourcePropsList;
    }

    public ListView<String> getAddedTargetPropsList() {
        return addedTargetPropsList;
    }

    public Label getMissingPropertiesLabel() {
        return missingPropertiesLabel;
    }
}
