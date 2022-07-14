package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;
import pt.isec.pa.apoio_poe.ui.gui.util.ToastMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class GestaoCandidaturasUI extends BorderPane {
    GestaoManager model;
    Label labelTitle;
    Button btnAdd,btnImp,btnExp;
    ListView<String> candidaturas;
    MenuItem btnRem, btnAddOutra;

    public GestaoCandidaturasUI(GestaoManager model){
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        btnAdd = new Button("Adicionar");
        btnImp = new Button("Importar");
        btnExp = new Button("Exportar");
        btnRem = new MenuItem("Remover");
        btnAddOutra = new MenuItem("Adicionar nova candidatura");
        HBox hbox = new HBox(btnAdd,btnImp,btnExp);
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        //hbox.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,new BorderWidths(2)));
        candidaturas = new ListView<>();
        candidaturas.setContextMenu(new ContextMenu(btnRem,btnAddOutra));
        this.setStyle("-fx-background-color: #b3b0ab;");
        labelTitle = new Label("Gestão de Candidaturas");
        labelTitle.getStyleClass().add("labelTitulo");
        VBox vbox = new VBox(labelTitle, candidaturas,hbox);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        this.setCenter(vbox);
    }
    public static class Results {

        String NAluno,P1;

        public Results(String NAluno, String P1) {
            this.NAluno = NAluno;
            this.P1 = P1;
        }
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> {update();});
        btnAdd.setOnAction(actionEvent -> {
            Dialog<GestaoCandidaturasUI.Results> dialog = new Dialog<>();
            dialog.setTitle("Inserir");

            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Inserir", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            ComboBox<String> NAluno = new ComboBox<>();
            NAluno.getItems().setAll(model.getAlunosSemCandidatura());
            NAluno.setPromptText("Nº Aluno");

            ComboBox<String> P1 = new ComboBox<>();
            P1.getItems().setAll(model.getPropostasNaoAtribuidas());
            P1.setPromptText("Proposta 1");

            gridPane.add(new Label("Nº aluno: "),0,0);
            gridPane.add(NAluno, 1, 0);

            gridPane.add(new Label("Proposta 1: "),0,1);
            gridPane.add(P1, 1, 1);

            dialog.getDialogPane().setContent(gridPane);

            // Request focus on the username field by default.
            Platform.runLater(NAluno::requestFocus);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == loginButtonType) {
                    return new GestaoCandidaturasUI.Results(NAluno.getSelectionModel().getSelectedItem(),P1.getSelectionModel().getSelectedItem());
                }
                return null;
            });

            Optional<GestaoCandidaturasUI.Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((GestaoCandidaturasUI.Results results) -> {
                if(model.adicionarCandidatura(results.NAluno,results.P1)){
                    ToastMessage.show(getScene().getWindow(),"Candidatura adicionada com sucesso");
                }else {
                    ToastMessage.show(getScene().getWindow(),"Erro ao adicionar a candidatura");
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
                if(model.importarCandidaturas(GestaoManager.fileComponent(hFile.toString()))){
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
                if(model.exportarCandidaturas(GestaoManager.fileComponent(hFile.toString()))){
                    ToastMessage.show(getScene().getWindow(),"Ficheiro exportado com sucesso.");
                }else {
                    ToastMessage.show(getScene().getWindow(),"Erro ao exportar o ficheiro");
                }
            }
        });

        btnAddOutra.setOnAction(actionEvent -> {
            Dialog<GestaoCandidaturasUI.Results> dialog = new Dialog<>();
            dialog.setTitle("Inserir");

            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Inserir", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField NAluno = new TextField();
            NAluno.setText(getNAluno(candidaturas.getSelectionModel().getSelectedItem()));
            NAluno.setDisable(true);

            ComboBox<String> P1 = new ComboBox<>();
            P1.getItems().setAll(model.getPropostasNaoAtribuidas());
            P1.setPromptText("Proposta 1");

            gridPane.add(new Label("Nº aluno: "),0,0);
            gridPane.add(NAluno, 1, 0);

            gridPane.add(new Label("Proposta 1: "),0,1);
            gridPane.add(P1, 1, 1);

            dialog.getDialogPane().setContent(gridPane);

            // Request focus on the username field by default.
            Platform.runLater(NAluno::requestFocus);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == loginButtonType) {
                    return new GestaoCandidaturasUI.Results(NAluno.getText(),P1.getSelectionModel().getSelectedItem());
                }
                return null;
            });

            Optional<GestaoCandidaturasUI.Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((GestaoCandidaturasUI.Results results) -> {
                if(model.adicionarCandidaturaExist(results.NAluno,results.P1)){
                    ToastMessage.show(getScene().getWindow(),"Candidatura adicionada com sucesso");
                }else {
                    ToastMessage.show(getScene().getWindow(),"Erro ao adicionar a candidatura");
                }
            });
            update();
        });

        btnRem.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remover Candidatura");
            alert.setContentText("Pretende remover esta candidatura?");
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == yesButton) {
                    if(model.removerCandidatura(getNAluno(candidaturas.getSelectionModel().getSelectedItem()))){
                        ToastMessage.show(getScene().getWindow(),"Candidatura foi eliminada com sucesso");
                        update();
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao eliminar candidatura.");
                    }
                }
            });
        });
    }

    private void update() {
        candidaturas.getItems().clear();
        if(model.ConsultarDados("") != null){
            candidaturas.getItems().addAll(model.ConsultarDados(""));
        }
        this.setVisible(model != null && model.getState() == GestaoState.GESTAO_CANDIDATURAS);
    }

    private String getNAluno(String a){
        int count = 0;
        ArrayList<Character> word = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if(a != null){
            if(a.contains("NºAluno:")){ //Aluno
                for(char t : a.toCharArray()) {
                    if(Character.isDigit(t) && count < 10){
                        word.add(t);
                        count++;
                    }
                }
            }
        }else{
            return null;
        }

        for(Character c : word){
            sb.append(c);
        }
        return sb.toString();
    }
}
