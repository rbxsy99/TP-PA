package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.GestaoManager;

import java.util.Objects;

public class MainJFX extends Application {
    GestaoManager model;

    public MainJFX(){
        model = new GestaoManager();
    }

    @Override
    public void start(Stage stage) throws Exception {
        RootPane root = new RootPane(model);
        Scene scene = new Scene(root,1200,700, Color.INDIGO);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("resources/css/mystyles.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Trabalho Prático PA - 2021/22");
        stage.setMinWidth(400);
        stage.show();
    }
}
