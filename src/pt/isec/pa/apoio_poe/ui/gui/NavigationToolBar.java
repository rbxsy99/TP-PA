package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;

public class NavigationToolBar extends ToolBar {
    GestaoManager model;
    Button btnVoltar,btnProxFase;

    public NavigationToolBar(GestaoManager model){
        this.model = model;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews(){
        btnVoltar = new Button("Voltar");
        btnProxFase = new Button("Próxima Fase");
        HBox spacer = new HBox();                 // verbraucht den Platz vor dem rechten Element
        spacer.setHgrow(spacer, Priority.ALWAYS);

        this.getItems().add(btnVoltar);
        this.getItems().add(spacer);
        this.getItems().add(btnProxFase);
    }

    private void registerHandlers(){
        model.addPropertyChangeListener(evt -> {update();});
        btnVoltar.setOnAction(actionEvent -> {
            model.faseAnterior();
        });

        btnProxFase.setOnAction(actionEvent -> {
            model.proximaFase();
        });
    }

    private boolean verifyBtn(){
        return model.getState() == GestaoState.GESTAO_ALUNOS || model.getState() == GestaoState.GESTAO_DOCENTES
                || model.getState() == GestaoState.GESTAO_PROPOSTAS
                || model.getState() == GestaoState.GESTAO_CANDIDATURAS
                || model.getState() == GestaoState.GESTAO_ORIENTADORES
                || model.getState() == GestaoState.REALIZA_ATRIBUICOES;
    }

    public void update(){
        if(verifyBtn()){
            btnVoltar.setDisable(true);
            btnVoltar.setVisible(false);
            btnProxFase.setText("Voltar");
        }else{
            btnVoltar.setDisable(false);
            btnVoltar.setVisible(true);
            btnProxFase.setText("Proxima Fase");
        }
        this.setVisible(model != null && model.getState() != null);

    }
}
