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
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;

import java.io.File;

public class TerceiraFaseUI extends BorderPane {
    GestaoManager model;
    Label labelTitle;
    Label labelFiltros;
    Button btnAtribuicoes;
    ComboBox<String> filtrosAln;
    ComboBox<String> filtrosProp;
    Popup popup;
    ListView<String> listview;
    Button btnfecharFase,btnExp;

    public TerceiraFaseUI(GestaoManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setStyle("-fx-background-color: #b3b0ab;");
        labelTitle = new Label("3ª Fase - Atribuição de Propostas");
        labelTitle.getStyleClass().add("labelTitulo");
        labelTitle.setPadding(new Insets(15));
        labelFiltros = new Label("Pesquisa por filtros");
        labelFiltros.getStyleClass().add("labelInsideText");

        btnfecharFase = new Button("Fechar fase");

        HBox hbox = new HBox(btnfecharFase);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        this.setBottom(hbox);

        filtrosAln = new ComboBox<>();
        filtrosAln.getItems().addAll("Com autoproposta","Com candidatura já registada","Com proposta atribuida","Não tem proposta atribuida");
        filtrosAln.setPromptText("Filtros de Alunos");
        filtrosProp = new ComboBox<>();
        filtrosProp.getItems().addAll("Autopropostas de alunos","Propostas de docentes","Propostas disponiveis","Propostas atribuidas");
        filtrosProp.setPromptText("Filtros de Propostas");

        listview = new ListView<>();
        listview.setPrefHeight(400);
        listview.setPrefWidth(600);

        popup = new Popup();
        popup.getContent().add(listview);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        btnAtribuicoes = new Button("Atribuições Propostas");
        btnExp = new Button("Exportar alunos");


        HBox hbox1 = new HBox(btnAtribuicoes,btnExp);
        hbox1.setSpacing(10);
        hbox1.setAlignment(Pos.CENTER);

        HBox hbox3 = new HBox(filtrosAln,filtrosProp);
        hbox3.setSpacing(10);
        hbox3.setAlignment(Pos.CENTER);
        hbox3.setPadding(new Insets(10));

        VBox vbox = new VBox(labelTitle,hbox1,labelFiltros,hbox3);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);

        this.setCenter(vbox);
    }

    private void registerHandlers() {
        Point2D point = this.localToScene(0.0, 0.0);
        model.addPropertyChangeListener(evt -> {update();});
        btnAtribuicoes.setOnAction(actionEvent -> {
            model.realiza_atribuicoes();
        });
        btnExp.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File Open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("File (*.csv)","*.csv")
            );
            File hFile = fileChooser.showSaveDialog(this.getScene().getWindow());
            if(hFile != null){
                model.exportarAlunos(GestaoManager.fileComponent(hFile.toString()));
            }
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
        this.setVisible(model != null && model.getState() == GestaoState.TERCEIRA_FASE);
    }

    private String getAlunosComboBoxSelectedItem(String a){
        switch(a){
            case "Com autoproposta" ->{ return "com_autoproposta";}
            case "Com candidatura já registada" ->{ return "com_candidatura";}
            case "Com proposta atribuida" ->{ return "com_proposta";}
            case "Não tem proposta atribuida" ->{ return "sem_proposta";}
        }
        return null;
    }

    private String getPropostasComboBoxSelectedItem(String a){
        switch (a){
            case "Autopropostas de alunos" ->{ return "auto_propostas";}
            case "Propostas de docentes" ->{ return "propostas_docentes";}
            case "Propostas disponiveis" ->{ return "propostas_disponiveis";}
            case "Propostas atribuidas" ->{ return "propostas_atribuidas";}
        }
        return null;
    }
}