package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Popup;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;

public class SegundaFaseUI extends BorderPane {
    GestaoManager model;
    Popup popup;
    ListView<String> listview;
    Label labelTitle;
    Label labelFiltros;
    ComboBox<String> filtrosAln;
    ComboBox<String> filtrosProp;
    Button btnCandidaturas;
    Button btnfecharFase;

    public SegundaFaseUI(GestaoManager model){
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews(){
        this.setStyle("-fx-background-color: #b3b0ab;");
        labelTitle = new Label("2º Fase - Opções de Candidaturas");
        labelTitle.setFont(new Font("Arial", 30));
        labelTitle.getStyleClass().add("labelTitulo");
        labelFiltros = new Label("Pesquisa por filtros");
        labelFiltros.getStyleClass().add("labelInsideText");
        //labelFiltros.setPadding(new Insets(5));

        btnfecharFase = new Button("Fechar fase");

        HBox hbox = new HBox(btnfecharFase);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        this.setBottom(hbox);

        filtrosAln = new ComboBox<>();
        filtrosAln.getItems().addAll("Com autoproposta","Com candidatura já registada","Sem Candidatura registada");
        filtrosAln.setPromptText("Filtros de Alunos");
        filtrosProp = new ComboBox<>();
        filtrosProp.getItems().addAll("Autopropostas de alunos","Propostas de docentes","Propostas com candidaturas","Propostas sem candidatura");
        filtrosProp.setPromptText("Filtros de Propostas");

        listview = new ListView<>();
        listview.setPrefHeight(400);
        listview.setPrefWidth(600);

        popup = new Popup();
        popup.getContent().add(listview);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        btnCandidaturas = new Button("Gestão de Candidaturas");

        HBox hbox1 = new HBox(btnCandidaturas);
        hbox1.setSpacing(10);
        hbox1.setAlignment(Pos.CENTER);

        HBox hbox2 = new HBox(filtrosAln,filtrosProp);
        hbox2.setSpacing(10);
        hbox2.setAlignment(Pos.CENTER);
        hbox2.setPadding(new Insets(10));

        VBox vbox = new VBox(labelTitle,hbox1,labelFiltros,hbox2);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);

        this.setCenter(vbox);
    }

    private void registerHandlers() {
        Point2D point = this.localToScene(0.0, 0.0);
        model.addPropertyChangeListener(evt -> {update();});

        btnCandidaturas.setOnAction(actionEvent -> {
            model.gestao("");
        });

        filtrosAln.setOnAction(actionEvent -> {
            listview.getItems().clear();
            listview.getItems().addAll(model.ListasAlunosFiltros(getAlunosComboBoxSelectedItem(filtrosAln.getSelectionModel().getSelectedItem())));
            popup.show(filtrosAln,point.getX() + 100, point.getY() + 100);
        });

        filtrosProp.setOnAction(actionEvent -> {
            listview.getItems().clear();
            listview.getItems().addAll(model.ListasPropostasFiltros(getPropostasComboBoxSelectedItem(filtrosProp.getSelectionModel().getSelectedItem()),0));
            popup.show(filtrosProp,point.getX() + 100,point.getY() + 100);

        });

        btnfecharFase.setOnAction(actionEvent -> {
            if(model.fecharFase()){
                model.proximaFase();
            }
        });
    }

    private void update() {
        this.setVisible(model != null && model.getState() == GestaoState.SEGUNDA_FASE);
    }

    private String getAlunosComboBoxSelectedItem(String a){
        switch(a){
            case "Com autoproposta" ->{ return "com_autoproposta";}
            case "Com candidatura já registada" ->{ return "com_candidatura";}
            case "Sem Candidatura registada" ->{ return "sem_candidatura";}
        }
        return null;
    }

    private String getPropostasComboBoxSelectedItem(String a){
        switch (a){
            case "Autopropostas de alunos" ->{ return "auto_propostas";}
            case "Propostas de docentes" ->{ return "propostas_docentes";}
            case "Propostas com candidaturas" ->{ return "propostas_com_candidaturas";}
            case "Propostas sem candidatura" ->{ return "propostas_sem_candidaturas";}
        }
        return null;
    }
}
