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
import javafx.stage.Popup;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;

public class PrimeiraFaseUI extends BorderPane {
    GestaoManager model;
    Label labelTitle,labelConsultaDados;
    Button btnAlunos,btnDocentes,btnPropostas;
    Button btnfecharFase;
    ComboBox<String> consultarDados;
    Popup popup;
    ListView<String> listview;

    public PrimeiraFaseUI(GestaoManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setStyle("-fx-background-color: #b3b0ab;");
        labelTitle = new Label("Gestão de Projetos/Estágios do DEIS");
        labelTitle.getStyleClass().add("labelTitulo");
        labelTitle.setPadding(new Insets(15));

        btnfecharFase = new Button("Fechar fase");

        HBox hbox = new HBox(btnfecharFase);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        this.setBottom(hbox);

        btnAlunos = new Button("Gestão de Alunos");
        btnDocentes = new Button("Gestão de Docentes");
        btnPropostas = new Button("Gestão de Propostas");

        HBox hbox1 = new HBox(btnAlunos,btnDocentes, btnPropostas);
        hbox1.setSpacing(10);
        hbox1.setAlignment(Pos.CENTER);

        labelConsultaDados = new Label("Consultar dados");
        labelConsultaDados.getStyleClass().add("labelInsideText");
        consultarDados = new ComboBox<>();
        consultarDados.getItems().addAll("Alunos","Docentes","Propostas");
        consultarDados.setPromptText("Opções");
        listview = new ListView<>();
        listview.setPrefHeight(400);
        listview.setPrefWidth(600);
        popup = new Popup();
        popup.getContent().add(listview);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        VBox vbox = new VBox(labelTitle,hbox1,labelConsultaDados,consultarDados);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);

        this.setCenter(vbox);
    }

    private void registerHandlers() {
        Point2D point = this.localToScene(0.0, 0.0);
        model.addPropertyChangeListener(evt -> {update();});
        btnAlunos.setOnAction(actionEvent -> {
            model.gestao("gestao_alunos");
        });
        btnDocentes.setOnAction(actionEvent -> {
            model.gestao("gestao_docentes");
        });
        btnPropostas.setOnAction(actionEvent -> {
            model.gestao("gestao_propostas");
        });
        btnfecharFase.setOnAction(actionEvent -> {
            if(model.fecharFase()){
                model.proximaFase();
            }
        });
        consultarDados.setOnAction(actionEvent -> {
            listview.getItems().clear();
            if(consultarDados.getSelectionModel().getSelectedItem().equalsIgnoreCase("alunos")){
                listview.getItems().addAll(model.ConsultarDados("alunos"));
            }else if(consultarDados.getSelectionModel().getSelectedItem().equalsIgnoreCase("docentes")){
                listview.getItems().addAll(model.ConsultarDados("docentes"));
            }else{
                listview.getItems().addAll(model.ConsultarDados("propostas"));
            }
            popup.show(consultarDados,point.getX() + 100, point.getY() + 100);
        });
    }

    private void update() {
        this.setVisible(model != null && model.getState() == GestaoState.PRIMEIRA_FASE);
    }
}


