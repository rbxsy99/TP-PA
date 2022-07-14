package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.ui.gui.resources.ImageManager;

public class StatusBar extends HBox {
    GestaoManager model;
    Label lbDados;
    private static final int ITEM_IMG_SIZE = 20;
    private static final String UNDO_IMG_FILENAME = "undo.png";
    private static final String REDO_IMG_FILENAME = "redo.png";
    Button btnUndo,btnRedo;

    public StatusBar(GestaoManager model) {
        this.model = model;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        lbDados = new Label("");
        lbDados.setAlignment(Pos.CENTER);
        lbDados.setPrefWidth(Integer.MAX_VALUE);
        lbDados.setPadding(new Insets(6));
        lbDados.getStyleClass().add("labelFooter");
        lbDados.setText("Gestão de Projetos e Estágios do DEIS");

        btnUndo = new Button();
        btnUndo.setGraphic(ImageManager.getImageView(UNDO_IMG_FILENAME, ITEM_IMG_SIZE));
        btnUndo.setAlignment(Pos.CENTER_LEFT);
        btnRedo = new Button();
        btnRedo.setGraphic(ImageManager.getImageView(REDO_IMG_FILENAME, ITEM_IMG_SIZE));
        btnRedo.setAlignment(Pos.CENTER_RIGHT);

        this.getChildren().addAll(btnUndo,lbDados, btnRedo);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> {update();});

        btnUndo.setOnAction(actionEvent -> {
            if(model.hasUndo()){
                model.undo();
            }
        });

        btnRedo.setOnAction(actionEvent -> {
            if(model.hasRedo()){
                model.redo();
            }
        });
    }

    private void update() {
        this.setVisible(model != null && model.getState() != null);
    }
}
