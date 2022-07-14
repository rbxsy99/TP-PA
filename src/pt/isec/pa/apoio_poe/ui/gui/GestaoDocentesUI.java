package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.configs.Docente;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;
import pt.isec.pa.apoio_poe.ui.gui.util.ToastMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class GestaoDocentesUI extends BorderPane {
    GestaoManager model;
    Label labelTitle;
    Button btnAdd,btnImp,btnExp;
    ListView<String> docentes;
    MenuItem btnRem,btnEdit;

    public GestaoDocentesUI(GestaoManager model){
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
        //hbox.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,new BorderWidths(2)));
        docentes = new ListView<>();
        docentes.setContextMenu(new ContextMenu(btnRem,btnEdit));
        this.setStyle("-fx-background-color: #b3b0ab;");
        labelTitle = new Label("Gestão de Docentes");
        labelTitle.getStyleClass().add("labelTitulo");
        VBox vbox = new VBox(labelTitle, docentes,hbox);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        this.setCenter(vbox);
    }

    public static class Results {

        String nome,Mail;

        public Results(String nome, String mail) {
            this.nome = nome;
            this.Mail = mail;
        }
    }


    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> {update();});
        btnAdd.setOnAction(actionEvent -> {
            Dialog<GestaoDocentesUI.Results> dialog = new Dialog<>();
            dialog.setTitle("Inserir");

            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Inserir", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField nome = new TextField();
            nome.setPromptText("Nome");

            TextField Mail = new TextField();
            Mail.setPromptText("Mail");

            gridPane.add(new Label("Nome: "),0,0);
            gridPane.add(nome, 1, 0);

            gridPane.add(new Label("Mail: "),0,1);
            gridPane.add(Mail, 1, 1);

            dialog.getDialogPane().setContent(gridPane);

            // Request focus on the username field by default.
            Platform.runLater(nome::requestFocus);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == loginButtonType) {
                    return new Results(nome.getText(),Mail.getText());
                }
                return null;
            });

            Optional<GestaoDocentesUI.Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((GestaoDocentesUI.Results results) -> {
                if(results.nome != null && results.Mail != null) {
                    if(model.adicionarDocente(results.nome,results.Mail)){
                        ToastMessage.show(getScene().getWindow(),"Docente adicionado com sucesso");
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao adicionar o docente");
                    }
                }
            });
            update();
        });

        btnEdit.setOnAction(actionEvent -> {
            Dialog<GestaoDocentesUI.Results> dialog = new Dialog<>();
            dialog.setTitle("Editar");
            Docente d = model.getDocente(getDocenteEmail(docentes.getSelectionModel().getSelectedItem()));

            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Editar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField nome = new TextField();
            nome.setPromptText("Nome");
            nome.setText(d.getNome());

            TextField Mail = new TextField();
            Mail.setPromptText("Mail");
            Mail.setText(d.getEmail());
            Mail.setDisable(true);

            gridPane.add(new Label("Nome: "),0,0);
            gridPane.add(nome, 1, 0);

            gridPane.add(new Label("Mail: "),0,1);
            gridPane.add(Mail, 1, 1);

            dialog.getDialogPane().setContent(gridPane);

            // Request focus on the username field by default.
            Platform.runLater(nome::requestFocus);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == loginButtonType) {
                    return new Results(nome.getText(),Mail.getText());
                }
                return null;
            });

            Optional<GestaoDocentesUI.Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((GestaoDocentesUI.Results results) -> {
                if(results.nome != null && results.Mail != null) {
                    if(model.editarDocente(results.nome,results.Mail)){
                        ToastMessage.show(getScene().getWindow(),"Docente editado com sucesso");
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao editar o docente");
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
                if(model.importarDocentes(GestaoManager.fileComponent(hFile.toString()))){
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
                if(model.exportarDocentes(GestaoManager.fileComponent(hFile.toString()))){
                    ToastMessage.show(getScene().getWindow(),"Ficheiro exportado com sucesso.");
                }else {
                    ToastMessage.show(getScene().getWindow(),"Erro ao exportar o ficheiro");
                }
            }
        });

        btnRem.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remover Docente");
            alert.setContentText("Pretende remover este docente?");
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == yesButton) {
                    if(model.removerDocente(getDocenteEmail(docentes.getSelectionModel().getSelectedItem()))){
                        ToastMessage.show(getScene().getWindow(),"Docente removido com sucesso.");
                        update();
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao remover o docente, verifique os dados.");
                    }
                }
            });
        });



    }

    private void update() {
        docentes.getItems().clear();
        if(model.ConsultarDados("docentes") != null) {
            docentes.getItems().addAll(model.ConsultarDados("docentes"));
        }
        this.setVisible(model != null && model.getState() == GestaoState.GESTAO_DOCENTES);
    }

    private String getDocenteEmail(String a){
        ArrayList<Character> emailrev = new ArrayList<>();
        ArrayList<Character> email = new ArrayList<>();
        ArrayList<Character> word = new ArrayList<>();
        for(char c : a.toCharArray()){
            word.add(c);
        }
        int index = word.indexOf('@');
        StringBuilder sb = new StringBuilder();
        for(int i = index;i > 0 ;i--){
            if(word.get(i) == ' '){ //Se o email  acabou
                break;
            }
            emailrev.add(word.get(i)); //Ordem inversa
        }
        for(int i= emailrev.size() -1;i >= 0;i--){
            email.add(emailrev.get(i));
        }
        for(char c:email){
            sb.append(c);
        }
        sb.append("isec.pt");
        return sb.toString();
    }

}
