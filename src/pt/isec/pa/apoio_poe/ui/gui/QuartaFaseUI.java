package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;
import pt.isec.pa.apoio_poe.ui.gui.util.ToastMessage;

import java.io.File;
import java.util.Objects;

public class QuartaFaseUI extends BorderPane {
    GestaoManager model;
    Label labelTitle;
    Label labelFiltros;
    ComboBox<String> filtrosDados;
    Popup popup;
    ListView<String> listview;
    Button btnAtribuicoesDocentes;
    Button btnfecharFase,btnGestaoOrientadores,btnExp;

    public QuartaFaseUI(GestaoManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setStyle("-fx-background-color: #b3b0ab;");
        labelTitle = new Label("4ª Fase - Atribuição de Orientadores");
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

        filtrosDados = new ComboBox<>();
        filtrosDados.getItems().addAll("Alunos c/ proposta atribuida e com orientador","Alunos c/ proposta atribuida mas sem orientador",
                "Informações Orientadores","Informações Orientador em especifico");
        filtrosDados.setPromptText("Mostrar Dados");
        listview = new ListView<>();
        listview.setPrefHeight(400);
        listview.setPrefWidth(600);

        popup = new Popup();
        popup.getContent().add(listview);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        btnAtribuicoesDocentes = new Button("Atribuição Automática Docentes");
        btnExp = new Button("Exportar alunos");
        btnGestaoOrientadores = new Button("Gestão Orientadores");


        HBox hbox1 = new HBox(btnAtribuicoesDocentes,btnExp,btnGestaoOrientadores);
        hbox1.setSpacing(10);
        hbox1.setAlignment(Pos.CENTER);

        HBox hbox3 = new HBox(filtrosDados);
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
        btnAtribuicoesDocentes.setOnAction(actionEvent -> {

            if(model.AssociacaoAutomDocentes()){
                ToastMessage.show(getScene().getWindow(),"Associações realizadas com sucesso");
            }else {
                ToastMessage.show(getScene().getWindow(),"Erro ao atribuir os orientadores");
            }
        });
        btnGestaoOrientadores.setOnAction(actionEvent -> {
            model.gestao("");
        });
        filtrosDados.setOnAction(actionEvent -> {
            listview.getItems().clear();
            if(getDadosComboBoxSelectedItem(filtrosDados.getSelectionModel().getSelectedItem()).equals("orientadores_geral")){
                listview.getItems().addAll(model.ListasOrientadoresFiltros());
            }else if(getDadosComboBoxSelectedItem(filtrosDados.getSelectionModel().getSelectedItem()).equals("orientadores_especifico")){
                TextInputDialog dialog = new TextInputDialog();
                dialog.setHeight(400);
                dialog.setWidth(400);
                dialog.setTitle("Introduza o email do orientador a procurar");
                dialog.showAndWait().ifPresent(string -> listview.getItems().addAll(model.ListasOrientadorEspecifico(string)));
            }else{
                listview.getItems().addAll(model.ListasAlunosFiltros(getDadosComboBoxSelectedItem(filtrosDados.getSelectionModel().getSelectedItem())));
            }

            popup.show(filtrosDados,point.getX() + 100, point.getY() + 100);
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
        btnfecharFase.setOnAction(actionEvent -> {
            if(model.fecharFase()){
                model.proximaFase();
            }
        });
    }

    private void update() {
        this.setVisible(model != null && model.getState() == GestaoState.QUARTA_FASE);
    }

    private String getDadosComboBoxSelectedItem(String a){
        switch(a){
            case "Alunos c/ proposta atribuida e com orientador" ->{ return "com_proposta_e_orientador";}
            case "Alunos c/ proposta atribuida mas sem orientador" ->{ return "com_proposta_sem_orientador";}
            case "Informações Orientadores" ->{ return "orientadores_geral";}
            case "Informações Orientador em especifico" ->{ return "orientadores_especifico";}
        }
        return null;
    }

}
