package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import pt.isec.pa.apoio_poe.model.GestaoManager;

public class LateralBar extends VBox {
    GestaoManager model;
    Label labelTAlunos,labelAlunosCProp, labelPropDisp, labelTotalProp,labelTotalCand, labelTotalDoc,labelTotalOrient;
    Label labelPropt1, labelPropt2, labelPropt3;
    Label labelEstadoAtual;

    public LateralBar(GestaoManager model) {
        this.model = model;

        createViews();
        registerHandlers();
        update();
    }


    private void createViews(){
        labelTAlunos = new Label();
        labelTAlunos.getStyleClass().add("lateralLabelStyle");
        labelAlunosCProp = new Label();
        labelAlunosCProp.getStyleClass().add("lateralLabelStyle");
        labelPropDisp = new Label();
        labelPropDisp.getStyleClass().add("lateralLabelStyle");
        labelTotalProp = new Label();
        labelTotalProp.getStyleClass().add("lateralLabelStyle");
        labelTotalCand = new Label();
        labelTotalCand.getStyleClass().add("lateralLabelStyle");
        labelTotalDoc = new Label();
        labelTotalDoc.getStyleClass().add("lateralLabelStyle");
        labelTotalOrient = new Label();
        labelTotalOrient.getStyleClass().add("lateralLabelStyle");
        labelPropt1 = new Label();
        labelPropt1.getStyleClass().add("lateralLabelStyle");
        labelPropt2 = new Label();
        labelPropt2.getStyleClass().add("lateralLabelStyle");
        labelPropt3 = new Label();
        labelPropt3.getStyleClass().add("lateralLabelStyle");
        labelEstadoAtual = new Label();
        labelEstadoAtual.getStyleClass().add("lateralLabelStyle");

        this.setAlignment(Pos.CENTER_LEFT);
        this.setSpacing(20);
        this.setPadding(new Insets(15));
        this.getStyleClass().add("lateralBoxBorder");
        this.getChildren().addAll(labelEstadoAtual,new Separator(),labelTAlunos,labelAlunosCProp,labelTotalProp,labelPropt1,labelPropt2,labelPropt3,labelTotalDoc,labelTotalOrient,labelTotalCand);
    }

    private void registerHandlers(){
        model.addPropertyChangeListener(evt -> {update();});
    }

    private void update(){
        labelEstadoAtual.setText("Estado Atual: " + model.getState());
        labelTAlunos.setText("Total de Alunos: " + model.getAlunos());
        labelAlunosCProp.setText("Alunos c/propostas: " + model.getTotalAlunosCProp());
        labelPropDisp.setText("Propostas Disponíveis: " + model.getTotalPropDisp());
        labelTotalProp.setText("Total de Propostas: " + model.getPropostas());
        labelTotalCand.setText("Total de Candidaturas: " + model.getCandidaturas());
        labelTotalDoc.setText("Total de Docentes: " + model.getDocentes());
        labelTotalOrient.setText("Total de Orientadores: " + model.getTotalOrientadores());
        labelPropt1.setText("Propostas do Tipo1: " + model.getTotalPropTipo("t1"));
        labelPropt2.setText("Propostas do Tipo2: " + model.getTotalPropTipo("t2"));
        labelPropt3.setText("Propostas do Tipo3: " + model.getTotalPropTipo("t3"));
    }
}
