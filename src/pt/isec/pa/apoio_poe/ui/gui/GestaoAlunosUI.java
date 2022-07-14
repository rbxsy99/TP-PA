package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.configs.Aluno;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;
import pt.isec.pa.apoio_poe.ui.gui.util.ToastMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class GestaoAlunosUI extends BorderPane {
    GestaoManager model;
    Label labelTitle;
    Button btnAdd,btnImp,btnExp;
    ListView<String> alunos;
    MenuItem btnRem,btnEdit;
    Button btnUndo, btnRedo,btnVoltar;

    public GestaoAlunosUI(GestaoManager model){
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        btnAdd = new Button("Adicionar");
        btnEdit = new MenuItem("Editar");
        btnRem = new MenuItem("Remover");
        btnImp = new Button("Importar");
        btnExp = new Button("Exportar");
        btnUndo = new Button("Undo");
        btnRedo = new Button("Redo");
        HBox hbox = new HBox(btnAdd,btnImp,btnExp);
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        alunos = new ListView<>();
        alunos.setContextMenu(new ContextMenu(btnRem,btnEdit));
        //hbox.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,new BorderWidths(2))));
        this.setStyle("-fx-background-color: #b3b0ab;");
        labelTitle = new Label("Gestão de Alunos");
        labelTitle.getStyleClass().add("labelTitulo");
        VBox vbox = new VBox(labelTitle,alunos,hbox);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        this.setCenter(vbox);


    }
    public static class Results {

        String nome,Naluno,Mail,Curso,Ramo;
        String Classificacao;
        Boolean AcederEstagios;

        public Results(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios) {
            this.nome = nome;
            this.Naluno = naluno;
            this.Mail = mail;
            this.Curso = curso;
            this.Ramo = ramo;
            this.Classificacao = classificacao;
            this.AcederEstagios = acederEstagios;
        }
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> {update();});

        btnAdd.setOnAction(actionEvent -> {
            Dialog<Results> dialog = new Dialog<>();
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

            TextField Naluno = new TextField();
            Naluno.setPromptText("Nº aluno");

            TextField Mail = new TextField();
            Mail.setPromptText("Mail");

            TextField Curso = new TextField();
            Curso.setPromptText("Curso");

            TextField Ramo = new TextField();
            Ramo.setPromptText("Ramo");

            TextField Classificacao = new TextField();
            Classificacao.setPromptText("Classificacao");

            RadioButton AcederEstagios = new RadioButton();

            gridPane.add(new Label("Nome: "),0,0);
            gridPane.add(nome, 1, 0);

            gridPane.add(new Label("Nº Aluno: "),0,1);
            gridPane.add(Naluno, 1, 1);

            gridPane.add(new Label("Mail: "),0,2);
            gridPane.add(Mail, 1, 2);

            gridPane.add(new Label("Curso: "),0,3);
            gridPane.add(Curso, 1, 3);

            gridPane.add(new Label("Ramo: "),0,4);
            gridPane.add(Ramo, 1, 4);

            gridPane.add(new Label("Classificacao: "),0,5);
            gridPane.add(Classificacao, 1, 5);

            gridPane.add(new Label("AcederEstagios: "),0,6);
            gridPane.add(AcederEstagios, 1, 6);

            dialog.getDialogPane().setContent(gridPane);

            // Request focus on the username field by default.
            Platform.runLater(nome::requestFocus);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == loginButtonType) {
                    return new Results(nome.getText(),
                            Naluno.getText(), Mail.getText(),Curso.getText(),Ramo.getText(),Classificacao.getText(),AcederEstagios.isSelected());
                }
                return null;
            });

            Optional<Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((Results results) -> {
                if(results.nome != null && results.Naluno != null && results.Mail != null) {
                    if(model.adicionarAluno(results.nome,results.Naluno,results.Mail,results.Curso,results.Ramo,results.Classificacao,results.AcederEstagios)){
                        ToastMessage.show(getScene().getWindow(),"Aluno adicionado com sucesso");
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao adicionar o aluno");
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
                if(model.importarAlunos(GestaoManager.fileComponent(hFile.toString()))){
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
                if(model.exportarAlunos(GestaoManager.fileComponent(hFile.toString()))){
                    ToastMessage.show(getScene().getWindow(),"Ficheiro exportado com sucesso.");
                }else {
                    ToastMessage.show(getScene().getWindow(),"Erro ao exportar o ficheiro");
                }
            }
        });


        btnRem.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remover Aluno");
            alert.setContentText("Pretende remover este aluno?");
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == yesButton) {
                    if(model.removerAluno(getNAluno(alunos.getSelectionModel().getSelectedItem()))){
                        ToastMessage.show(getScene().getWindow(),"O aluno foi eliminado com sucesso");
                        update();
                    }else {
                        ToastMessage.show(getScene().getWindow(),"O aluno não foi eliminado, verifique os dados.");
                    }
                }
            });
        });
        btnEdit.setOnAction(actionEvent -> {
            Dialog<Results> dialog = new Dialog<>();
            dialog.setTitle("Editar");
            Aluno a = model.getAluno(getNAluno(alunos.getSelectionModel().getSelectedItem()));
            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Editar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField nome = new TextField();
            nome.setPromptText("Nome");
            nome.setText(a.getNome());

            TextField Naluno = new TextField();
            Naluno.setPromptText("Nº aluno");
            Naluno.setText(String.valueOf(a.getN_aluno()));
            Naluno.setDisable(true);

            TextField Mail = new TextField();
            Mail.setPromptText("Mail");
            Mail.setText(a.getEmail());
            Mail.setDisable(true);

            TextField Curso = new TextField();
            Curso.setPromptText("Curso");
            Curso.setText(a.getCurso());

            TextField Ramo = new TextField();
            Ramo.setPromptText("Ramo");
            Ramo.setText(a.getRamo());


            TextField Classificacao = new TextField();
            Classificacao.setPromptText("Classificacao");
            Classificacao.setText(String.valueOf(a.getClassificacao()));

            RadioButton AcederEstagios = new RadioButton();
            AcederEstagios.setSelected(a.isAceder_estagios());

            gridPane.add(new Label("Nome: "),0,0);
            gridPane.add(nome, 1, 0);

            gridPane.add(new Label("Nº Aluno: "),0,1);
            gridPane.add(Naluno, 1, 1);

            gridPane.add(new Label("Mail: "),0,2);
            gridPane.add(Mail, 1, 2);

            gridPane.add(new Label("Curso: "),0,3);
            gridPane.add(Curso, 1, 3);

            gridPane.add(new Label("Ramo: "),0,4);
            gridPane.add(Ramo, 1, 4);

            gridPane.add(new Label("Classificacao: "),0,5);
            gridPane.add(Classificacao, 1, 5);

            gridPane.add(new Label("AcederEstagios: "),0,6);
            gridPane.add(AcederEstagios, 1, 6);

            dialog.getDialogPane().setContent(gridPane);

            // Request focus on the username field by default.
            Platform.runLater(nome::requestFocus);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == loginButtonType) {
                    return new Results(nome.getText(),
                            Naluno.getText(), Mail.getText(),Curso.getText(),Ramo.getText(),Classificacao.getText(),AcederEstagios.isSelected());
                }
                return null;
            });

            Optional<Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((Results results) -> {
                if(results.nome != null && results.Naluno != null && results.Mail != null) {
                    if(model.editarAluno(results.nome,results.Naluno,results.Mail,results.Curso,results.Ramo,results.Classificacao,results.AcederEstagios)){
                        ToastMessage.show(getScene().getWindow(),"Aluno editado com sucesso");
                    }else {
                        ToastMessage.show(getScene().getWindow(),"Erro ao editar o aluno");
                    }
                }


            });
            update();
        });


    }

    private void update() {
        alunos.getItems().clear();
        if(model.ConsultarDados("alunos") != null) {
            alunos.getItems().addAll(model.ConsultarDados("alunos"));
        }
        this.setVisible(model != null && model.getState() == GestaoState.GESTAO_ALUNOS);
    }

    private String getNAluno(String a){
        int count = 0;
        ArrayList<Character> word = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if(a.contains("Nº Aluno:")){ //Aluno
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
}
