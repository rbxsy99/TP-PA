package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;
import pt.isec.pa.apoio_poe.ui.gui.util.ToastMessage;

import java.util.ArrayList;
import java.util.Optional;

public class GestaoOrientadoresUI extends BorderPane {
    GestaoManager model;
    Label labelTitle;
    Button btnAdd;
    ListView<String> orientadores;
    Dialog<Results> dialog;
    ButtonType confirmarButtonType;
    MenuItem btnRemAl,btnTroca;

    public GestaoOrientadoresUI(GestaoManager model){
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        btnAdd = new Button("Atribuir");
        btnRemAl = new MenuItem("Remover aluno associado");
        btnTroca = new MenuItem("Trocar orientador");
        HBox hbox = new HBox(btnAdd);
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        //hbox.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,new BorderWidths(2)));
        orientadores = new ListView<>();
        orientadores.setContextMenu(new ContextMenu(btnRemAl,btnTroca));
        this.setStyle("-fx-background-color: #b3b0ab;");
        labelTitle = new Label("Gestão de Orientadores");
        labelTitle.getStyleClass().add("labelTitulo");
        VBox vbox = new VBox(labelTitle, orientadores,hbox);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        this.setCenter(vbox);

        dialog = new Dialog<>();
        // Set the button types.
        confirmarButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarButtonType, ButtonType.CANCEL);
    }

    public static class Results {

        String email_ori,naluno;

        public Results(String email_ori, String naluno) {
            this.email_ori = email_ori;
            this.naluno = naluno;
        }
    }
    public static class ResultsAl {
        String nAluno;
        String emailDocente;
        public ResultsAl(String emailDocente,String nAluno) {
            this.nAluno = nAluno;
            this.emailDocente = emailDocente;
        }
    }

    public static class ResultsTroca {
        String nAluno;
        String emailNvDocente,emailDocente;
        public ResultsTroca(String emailDocente,String emailNvDocente,String nAluno) {
            this.nAluno = nAluno;
            this.emailDocente = emailDocente;
            this.emailNvDocente = emailNvDocente;
        }
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> {update();});

        btnAdd.setOnAction(actionEvent -> {
            dialog.setTitle("Atribuir orientador");

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));
            ComboBox<String> emailorientador = new ComboBox<>();
            emailorientador.setPromptText("Orientador");
            emailorientador.getItems().clear();
            emailorientador.getItems().addAll(model.getEmailsOrientador());
            ComboBox<String> naluno = new ComboBox<>();
            naluno.setPromptText("Aluno");
            naluno.getItems().addAll(model.getNAlunosCProp());
            gridPane.add(new Label("Orientador: "),0,0);
            gridPane.add(emailorientador, 1, 0);
            gridPane.add(new Label("Número Aluno: "),0,1);
            gridPane.add(naluno, 1, 1);

            dialog.getDialogPane().setContent(gridPane);
            Platform.runLater(naluno::requestFocus);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == confirmarButtonType) {
                    return new Results(emailorientador.getSelectionModel().getSelectedItem(),naluno.getSelectionModel().getSelectedItem());
                }
                return null;
            });

            Optional<Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((results) -> {
                if(model.atribuirOrientador(results.email_ori,results.naluno)){
                    ToastMessage.show(getScene().getWindow(),"Atribuição realizada com sucesso");
                }else {
                    ToastMessage.show(getScene().getWindow(),"Erro ao atribuir o orientador");
                }
            });
            update();
        });
        btnRemAl.setOnAction(actionEvent -> {
            Dialog<ResultsAl> dialog = new Dialog<>();
            dialog.setTitle("Remover aluno orientado");

            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Remover", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField emailDocente = new TextField();
            emailDocente.setText(getDocenteEmail(orientadores.getSelectionModel().getSelectedItem()));
            emailDocente.setDisable(true);

            ComboBox<String> NAluno = new ComboBox<>();
            NAluno.getItems().setAll(model.getAlunosOrient(getDocenteEmail(orientadores.getSelectionModel().getSelectedItem())));
            NAluno.setPromptText("Nº Aluno: ");

            gridPane.add(new Label("Docente: "),0,0);
            gridPane.add(emailDocente, 1, 0);

            gridPane.add(new Label("Nº Aluno: "),0,1);
            gridPane.add(NAluno, 1, 1);

            dialog.getDialogPane().setContent(gridPane);

            // Request focus on the username field by default.
            Platform.runLater(NAluno::requestFocus);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == loginButtonType) {
                    return new ResultsAl(emailDocente.getText(),NAluno.getSelectionModel().getSelectedItem());
                }
                return null;
            });

            Optional<ResultsAl> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((ResultsAl results) -> {
                if(model.removerAlunoOrientado(results.emailDocente,results.nAluno)){
                    ToastMessage.show(getScene().getWindow(),"Aluno desassociado com sucesso");
                }else {
                    ToastMessage.show(getScene().getWindow(),"Erro ao remover aluno");
                }
            });
            update();
        });

        btnTroca.setOnAction(actionEvent -> {
            Dialog<ResultsTroca> dialog = new Dialog<>();
            dialog.setTitle("Trocar orientador");

            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Trocar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField emailDocente = new TextField();
            emailDocente.setText(getDocenteEmail(orientadores.getSelectionModel().getSelectedItem()));
            emailDocente.setDisable(true);

            ComboBox<String> emailDocente2 = new ComboBox<>();
            emailDocente2.getItems().clear();
            emailDocente2.getItems().addAll(model.getEmailsOrientador());

            ComboBox<String> NAluno = new ComboBox<>();
            NAluno.getItems().setAll(model.getAlunosOrient(getDocenteEmail(orientadores.getSelectionModel().getSelectedItem())));
            NAluno.setPromptText("Nº Aluno: ");

            gridPane.add(new Label("Docente: "),0,0);
            gridPane.add(emailDocente, 1, 0);

            gridPane.add(new Label("Novo Docente: "),0,1);
            gridPane.add(emailDocente2, 1, 1);

            gridPane.add(new Label("Nº Aluno: "),0,2);
            gridPane.add(NAluno, 1, 2);

            dialog.getDialogPane().setContent(gridPane);

            // Request focus on the username field by default.
            Platform.runLater(NAluno::requestFocus);

            dialog.setResultConverter((ButtonType button) -> {
                if (button == loginButtonType) {
                    return new ResultsTroca(emailDocente.getText(),emailDocente2.getSelectionModel().getSelectedItem(),NAluno.getSelectionModel().getSelectedItem());
                }
                return null;
            });

            Optional<ResultsTroca> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((ResultsTroca results) -> {
                if(model.alteracaoOrientador(results.emailDocente, results.emailNvDocente, results.nAluno)){
                    ToastMessage.show(getScene().getWindow(),"Aluno trocado com sucesso");
                }else {
                    ToastMessage.show(getScene().getWindow(),"Erro ao trocar orientador");
                }
            });
            update();
        });
    }

    private void update() {
        orientadores.getItems().clear();
        if(model.ListasOrientadoresFiltros()!= null){
            orientadores.getItems().addAll(model.ListasOrientadoresFiltros());
        }

        this.setVisible(model != null && model.getState() == GestaoState.GESTAO_ORIENTADORES);
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
