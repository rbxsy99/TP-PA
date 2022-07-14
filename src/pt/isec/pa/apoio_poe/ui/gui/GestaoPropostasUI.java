package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.configs.Propostas;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;
import pt.isec.pa.apoio_poe.ui.gui.util.ToastMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class GestaoPropostasUI extends BorderPane {
    GestaoManager model;
    Label labelTitle;
    Button btnAdd,btnImp,btnExp;
    ListView<String> propostas;
    MenuItem btnRem,btnEdit;

    public GestaoPropostasUI(GestaoManager model){
        this.model = model;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        btnAdd = new Button("Adicionar");
        btnImp = new Button("Importar");
        btnExp = new Button("Exportar");
        btnEdit = new MenuItem("Editar");
        btnRem = new MenuItem("Remover");
        HBox hbox = new HBox(btnAdd,btnImp,btnExp);
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        //hbox.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,new BorderWidths(2))));
        btnAdd.getStyleClass().add("testButton");
        btnEdit.getStyleClass().add("testButton");
        propostas = new ListView<>();
        propostas.setContextMenu(new ContextMenu(btnRem,btnEdit));
        this.setStyle("-fx-background-color: #b3b0ab;");
        labelTitle = new Label("Gestão de Propostas");
        labelTitle.getStyleClass().add("labelTitulo");
        VBox vbox = new VBox(labelTitle, propostas,hbox);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        this.setCenter(vbox);
    }

    public static class Results {

        String tipo,id,ramos,titulo,entidade,naluno,docente;

        public Results(String tipo, String id, String ramos, String titulo, String entidade, String naluno, String docente) {
            this.tipo = tipo;
            this.id = id;
            this.ramos = ramos;
            this.titulo = titulo;
            this.entidade = entidade;
            this.naluno = naluno;
            this.docente = docente;
        }
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> {update();});

        btnAdd.setOnAction(actionEvent -> {
            Dialog<Results> dialog = new Dialog<>();
            dialog.setTitle("Inserir");
            // Set the button types.
            ButtonType inserirButtonType = new ButtonType("Inserir", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(inserirButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            ComboBox<String> tipo = new ComboBox<>();
            tipo.getItems().addAll("T1","T2","T3");
            tipo.setPromptText("Tipo");

            TextField id = new TextField();
            id.setPromptText("ID");

            ComboBox<String> ramosdestino = new ComboBox<>();
            ramosdestino.getItems().setAll("DA","SI","RAS","DA|SI","DA|RAS","SI|RAS","DA|SI|RAS");
            ramosdestino.setPromptText("Ramos");

            TextField titulo = new TextField();
            titulo.setPromptText("Titulo");

            TextField entidade = new TextField();
            entidade.setPromptText("Entidade de Acolhimento");

            TextField Naluno = new TextField();
            Naluno.setPromptText("Nº aluno");

            TextField docente = new TextField();
            docente.setPromptText("Docente");

            gridPane.add(new Label("Tipo: "),0,0);
            gridPane.add(tipo, 1, 0);

            gridPane.add(new Label("ID: "),0,1);
            gridPane.add(id, 1, 1);

            gridPane.add(new Label("Ramos: "),0,2);
            gridPane.add(ramosdestino, 1, 2);

            gridPane.add(new Label("Título: "),0,3);
            gridPane.add(titulo, 1, 3);

            gridPane.add(new Label("Entidade: "),0,4);
            gridPane.add(entidade, 1, 4);

            gridPane.add(new Label("Nº aluno: "),0,5);
            gridPane.add(Naluno, 1, 5);

            gridPane.add(new Label("Docente: "),0,6);
            gridPane.add(docente, 1, 6);

            dialog.getDialogPane().setContent(gridPane);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == inserirButtonType) {
                    if(tipo.getSelectionModel().getSelectedItem().equalsIgnoreCase("t1")){
                        return new Results(tipo.getSelectionModel().getSelectedItem(),id.getText(),ramosdestino.getSelectionModel().getSelectedItem()
                                ,titulo.getText(),entidade.getText(),Naluno.getText(),null);
                    }else if(tipo.getSelectionModel().getSelectedItem().equalsIgnoreCase("t2")){
                        return new Results(tipo.getSelectionModel().getSelectedItem(),id.getText(),ramosdestino.getSelectionModel().getSelectedItem()
                                ,titulo.getText(),null,Naluno.getText(),docente.getText());
                    }else{
                        return new Results(tipo.getSelectionModel().getSelectedItem(),id.getText(),null,titulo.getText(),null,Naluno.getText(),null);
                    }
                }
                return null;
            });

            Optional<Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((results) -> {
                if(results.tipo!= null && results.id != null){
                    if(model.adicionarProposta(results.tipo,results.id,results.ramos,results.titulo,results.entidade,results.naluno,results.docente)){
                        ToastMessage.show(getScene().getWindow(),"Proposta adicionada com sucesso");
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao adicionar a proposta");
                    }
                }
            });

            update();
        });
        btnEdit.setOnAction(actionEvent -> {
            Dialog<Results> dialog = new Dialog<>();
            dialog.setTitle("Editar");
            // Set the button types.
            ButtonType editButtonType = new ButtonType("Editar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

            Propostas p = model.getProposta(getIDProp(propostas.getSelectionModel().getSelectedItem()));
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            ComboBox<String> tipo = new ComboBox<>();
            tipo.getItems().addAll("T1","T2","T3");
            tipo.setPromptText("Tipo");
            tipo.setValue(p.getTipo());


            TextField id = new TextField();
            id.setPromptText("ID");
            id.setText(p.getId());
            id.setDisable(true);

            ComboBox<String> ramosdestino = new ComboBox<>();
            ramosdestino.getItems().setAll("DA","SI","RAS","DA|SI","DA|RAS","SI|RAS","DA|SI|RAS");
            ramosdestino.setPromptText("Ramos");
            ramosdestino.setValue(p.getRamosdestino());

            TextField titulo = new TextField();
            titulo.setPromptText("Titulo");
            titulo.setText(p.getTitulo());

            TextField entidade = new TextField();
            entidade.setPromptText("Entidade de Acolhimento");
            entidade.setText(p.getEntidadeacolhimento());

            TextField Naluno = new TextField();
            Naluno.setPromptText("Nº aluno");
            Naluno.setText(String.valueOf(p.getN_aluno()));
            Naluno.setDisable(true);

            TextField docente = new TextField();
            docente.setPromptText("Docente");
            docente.setText(p.getDocenteproponente());

            gridPane.add(new Label("Tipo: "),0,0);
            gridPane.add(tipo, 1, 0);

            gridPane.add(new Label("ID: "),0,1);
            gridPane.add(id, 1, 1);

            gridPane.add(new Label("Ramos: "),0,2);
            gridPane.add(ramosdestino, 1, 2);

            gridPane.add(new Label("Título: "),0,3);
            gridPane.add(titulo, 1, 3);

            gridPane.add(new Label("Entidade: "),0,4);
            gridPane.add(entidade, 1, 4);

            gridPane.add(new Label("Nº aluno: "),0,5);
            gridPane.add(Naluno, 1, 5);

            gridPane.add(new Label("Docente: "),0,6);
            gridPane.add(docente, 1, 6);

            dialog.getDialogPane().setContent(gridPane);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == editButtonType) {
                    if(tipo.getSelectionModel().getSelectedItem().equalsIgnoreCase("t1")){
                        return new Results(tipo.getSelectionModel().getSelectedItem(),id.getText(),ramosdestino.getSelectionModel().getSelectedItem()
                                ,titulo.getText(),entidade.getText(),Naluno.getText(),null);
                    }else if(tipo.getSelectionModel().getSelectedItem().equalsIgnoreCase("t2")){
                        return new Results(tipo.getSelectionModel().getSelectedItem(),id.getText(),ramosdestino.getSelectionModel().getSelectedItem()
                                ,titulo.getText(),null,Naluno.getText(),docente.getText());
                    }else{
                        return new Results(tipo.getSelectionModel().getSelectedItem(),id.getText(),null,titulo.getText(),null,Naluno.getText(),null);
                    }
                }
                return null;
            });

            Optional<Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((results) -> {
                if(results.tipo!= null && results.id != null){
                    if(model.editarProposta(results.tipo,results.id,results.ramos,results.titulo,results.entidade,results.naluno,results.docente)){
                        ToastMessage.show(getScene().getWindow(),"Proposta editada com sucesso");
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao editar a proposta");
                    }
                }
            });

            update();
        });

        btnImp.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File Open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("File (*.csv)","*.csv")
            );
            File hFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if(hFile != null){
                if(model.importarPropostas(GestaoManager.fileComponent(hFile.toString()))){
                    ToastMessage.show(getScene().getWindow(),"Ficheiro importado com sucesso.");
                }else {
                    ToastMessage.show(getScene().getWindow(),"Erro ao importar o ficheiro");
                }
                //System.out.println(fileComponent(hFile.toString()));
            }
            update();
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
                if(model.exportarPropostas(GestaoManager.fileComponent(hFile.toString()))){
                    ToastMessage.show(getScene().getWindow(),"Ficheiro exportado com sucesso.");
                }else {
                    ToastMessage.show(getScene().getWindow(),"Erro ao exportar o ficheiro");
                }
            }
        });

        btnRem.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remover proposta");
            alert.setContentText("Pretende remover esta proposta?");
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == yesButton) {
                    //System.out.println(getIDProp(propostas.getSelectionModel().getSelectedItem()));
                    if(model.removerProposta(getIDProp(propostas.getSelectionModel().getSelectedItem()))){
                        ToastMessage.show(getScene().getWindow(),"Proposta removida com sucesso.");
                        update();
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao remover a proposta, verifique os dados.");
                    }
                }
            });
        });
    }

    private void update() {
        propostas.getItems().clear();
        if(model.ConsultarDados("propostas") != null) {
            propostas.getItems().addAll(model.ConsultarDados("propostas"));
        }
        this.setVisible(model != null && model.getState() == GestaoState.GESTAO_PROPOSTAS);
    }

    private String getIDProp(String a){ //Retorna o ID Proposta ou Nº Aluno
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
        }
        for(Character c : word){
            sb.append(c);
        }
        return sb.toString();
    }
}
