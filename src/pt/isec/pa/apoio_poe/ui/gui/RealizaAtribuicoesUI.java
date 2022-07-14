package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;
import pt.isec.pa.apoio_poe.ui.gui.util.ToastMessage;

import java.util.ArrayList;
import java.util.Optional;

public class RealizaAtribuicoesUI extends BorderPane {
    GestaoManager model;
    Label labelTitle;
    Button btnAtribAutom_Auto,btnAtribAutom_Disp,btnAtribManual,btnRemAtrib,btnRemTdAtrib;
    ListView<String> listview;
    Dialog<Results> dialog;
    ButtonType confirmarButtonType;
    //Remover atribuições manualmente
    Popup popup;
    ListView<String> listViewRemover;
    MenuItem btnRem;


    public RealizaAtribuicoesUI(GestaoManager model){
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews(){
        this.setStyle("-fx-background-color: #b3b0ab;");
        labelTitle = new Label("Realiza atribuições");
        labelTitle.setFont(new Font("Arial", 30));
        labelTitle.setPadding(new Insets(15));

        btnAtribAutom_Auto = new Button("Atribuição Automática de Autopropostas");
        btnAtribAutom_Disp = new Button("Atribuição Automática de Propostas Disponíveis");
        btnAtribManual = new Button("Atribuição Manual");
        btnRemAtrib = new Button("Remover atribuição");
        btnRemTdAtrib = new Button("Remover atribuições (exceto autopropostas");

        listview = new ListView<>();
        listview.setPrefHeight(400);
        listview.setPrefWidth(600);

        //Remover atribuições manualmente
        listViewRemover = new ListView<>();
        listViewRemover.setPrefHeight(400);
        listViewRemover.setPrefWidth(600);
        btnRem = new MenuItem("Remover");
        listViewRemover.setContextMenu(new ContextMenu(btnRem));
        popup = new Popup();
        popup.getContent().add(listViewRemover);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        HBox hbox = new HBox(btnAtribAutom_Auto,btnAtribAutom_Disp);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10));

        HBox hbox1 = new HBox(btnAtribManual,btnRemAtrib);
        hbox1.setSpacing(10);
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setPadding(new Insets(10));

        HBox hbox2 = new HBox(btnRemTdAtrib);
        hbox2.setSpacing(10);
        hbox2.setAlignment(Pos.CENTER);
        hbox2.setPadding(new Insets(10));

        VBox vbox = new VBox(labelTitle,hbox,hbox1,hbox2);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);

        this.setCenter(vbox);

        dialog = new Dialog<>();
        // Set the button types.
        confirmarButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarButtonType, ButtonType.CANCEL);
    }

    public static class Results {

        String idprop,naluno;

        public Results(String idprop, String naluno) {
            this.idprop = idprop;
            this.naluno = naluno;
        }
    }

    private void registerHandlers() {
        Point2D point = this.localToScene(0.0, 0.0);
        model.addPropertyChangeListener(evt -> {update();});

        btnAtribAutom_Auto.setOnAction(actionEvent -> {
            if(model.AtribuicaoAutomAluno()){
                ToastMessage.show(getScene().getWindow(),"Propostas atribuídas com sucesso");
                update();
            }else {
                ToastMessage.show(getScene().getWindow(),"Erro ao atribuir as propostas");
            }
        });

        btnAtribAutom_Disp.setOnAction(actionEvent -> {
            if(model.AtribuicaoAutomDocentesAoAluno() != null) { //Houve empate e pede ao utilizador para escolher
                listview.getItems().clear();
                listview.getItems().addAll(model.AtribuicaoAutomDocentesAoAluno());

                dialog.setTitle("Atribuir manualmente");

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(20, 150, 10, 10));
                TextField idprop = new TextField();
                if(!listview.getItems().isEmpty()){
                    idprop.setText(listview.getItems().get(1).toString());
                }
                idprop.setDisable(true);
                ComboBox<String> naluno = new ComboBox<>();
                naluno.setPromptText("Aluno");
                if(!listview.getItems().isEmpty()){
                    listview.getItems().remove(0,3);
                }
                naluno.getItems().addAll(listview.getItems());
                gridPane.add(new Label("ID Proposta: "),0,0);
                gridPane.add(idprop, 1, 0);
                gridPane.add(new Label("Número Aluno: "),0,1);
                gridPane.add(naluno, 1, 1);
                dialog.getDialogPane().setContent(gridPane);

                // Request focus on the username field by default.
                Platform.runLater(naluno::requestFocus);

                dialog.setResultConverter((ButtonType button) -> {
                    if (button == confirmarButtonType) {
                        return new Results(idprop.getText(),naluno.getSelectionModel().getSelectedItem());
                    }
                    return null;
                });

                Optional<Results> optionalResult = dialog.showAndWait();
                optionalResult.ifPresent((results) -> {
                    if(model.AtribuicaoManual(results.idprop,results.naluno)){
                        ToastMessage.show(getScene().getWindow(),"Proposta manual atribuída com sucesso");
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao atribuir a proposta");
                    }
                });
                update();
            }
        });

        btnAtribManual.setOnAction(actionEvent -> {

            dialog.setTitle("Atribuir manualmente");
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            ComboBox<String> idProp = new ComboBox<>();
            idProp.setPromptText("ID Proposta");
            //Adicionar dados às comboBox
            for(String a: model.getPropostaseAlunosDisponiveis()) {
                if(getDadosIDPropNAluno(a).length() == 4){ //ID Proposta (P000)
                    idProp.getItems().add(getDadosIDPropNAluno(a));
                }
            }
            TextField naluno = new TextField();
            naluno.setPromptText("Nº Aluno");
            gridPane.add(new Label("ID Proposta: "),0,0);
            gridPane.add(idProp, 1, 0);
            gridPane.add(new Label("Número Aluno: "),0,1);
            gridPane.add(naluno, 1, 1);
            dialog.getDialogPane().setContent(gridPane);

            // Request focus on the username field by default.
            Platform.runLater(idProp::requestFocus);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == confirmarButtonType) {
                    return new Results(idProp.getSelectionModel().getSelectedItem(),naluno.getText());
                }
                return null;
            });

            Optional<Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((results) -> {
                if(results.naluno != null && results.naluno.length() == 10) {
                    if(model.AtribuicaoManual(results.idprop,results.naluno)){
                        ToastMessage.show(getScene().getWindow(),"Proposta manual atribuída com sucesso");
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao atribuir a proposta");
                    }
                }

            });
            update();
        });

        btnRemTdAtrib.setOnAction(actionEvent -> {
            model.RemocaoManualTodasAtribuicoes();
        });

        btnRemAtrib.setOnAction(actionEvent -> {
            listViewRemover.getItems().clear();
            listViewRemover.getItems().addAll(model.getPropostasAtribManual());
            popup.show(btnRemAtrib,point.getX() + 100,point.getY() + 100);
        });

        btnRem.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remover Atribuição Manual");
            alert.setContentText("Pretende remover esta atribuição?");
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == yesButton) {
                    //GET IDPROP & NALUNO
                    String aux = listViewRemover.getSelectionModel().getSelectedItem();
                    final int mid = aux.length() / 2; //get the middle of the String
                    String[] parts = {aux.substring(0, mid),aux.substring(mid)}; //Dividir a string para conseguir o id da prop e naluno
                    if(model.RemocaoManualAtribuicao(getDadosIDPropNAluno(parts[0]),getDadosIDPropNAluno(parts[1]))){
                        ToastMessage.show(getScene().getWindow(),"Atribuição foi eliminada com sucesso");
                        update();
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao eliminar atribuição.");
                    }
                }
            });
        });
    }

    private String getDadosIDPropNAluno(String a){ //Retorna o ID Proposta ou Nº Aluno
        int count = 0;
        ArrayList<Character> word = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if(a.contains("ID:")){ //Proposta
            word.add('P');
            for(char t : a.toCharArray()) {
                if(Character.isDigit(t) && count < 4 && count != 0){
                    word.add(t);
                    count++;
                }else if(Character.isDigit(t)){
                    count++;
                }
            }
        }else if(a.contains("Nº Aluno:")){ //Aluno
            for(char t : a.toCharArray()) {
                if(Character.isDigit(t) && count < 10){
                    word.add(t);
                    count++;
                }
            }
        }
        for(Character c : word){
            sb.append(c);
        }
        return sb.toString();
    }

    private void update() {
        this.setVisible(model != null && model.getState() == GestaoState.REALIZA_ATRIBUICOES);

        if(model.getPropostasAtribManual() != null){
            listViewRemover.getItems().clear();
            listViewRemover.getItems().addAll(model.getPropostasAtribManual());
        }
    }
}
