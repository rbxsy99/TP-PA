package pt.isec.pa.apoio_poe.ui.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;

public class QuintaFaseUI extends BorderPane {
    GestaoManager model;
    Label labelTitle;
    Button btnfecharFase;
    ObservableList<PieChart.Data> pieChartData;
    PieChart pieChartRamos;
    Label labelPercPropDisp, labelPercPropAtrib, labelPercPropt1,labelPercPropt2,labelPercPropt3;
    BarChart<String, Number> barChart;
    XYChart.Series<String, Number> dataOrient;
    BarChart<String, Number> barChart2;
    XYChart.Series<String, Number> dataEmpresa;

    public QuintaFaseUI(GestaoManager model){
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.setStyle("-fx-background-color: #b3b0ab;");
        labelTitle = new Label("5ª Fase - Consulta");
        labelTitle.getStyleClass().add("labelTituloQuintaFase");

        btnfecharFase = new Button("Fechar fase");
        HBox hbox = new HBox(btnfecharFase);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        this.setBottom(hbox);

        pieChartData = FXCollections.observableArrayList(new PieChart.Data("Teste", 100));
        pieChartRamos = new PieChart(pieChartData);
        pieChartRamos.setData(pieChartData);
        pieChartRamos.setPadding(new Insets(5));
        pieChartRamos.setTitle("Propostas por Ramos");
        pieChartRamos.setClockwise(true);
        pieChartRamos.setLabelsVisible(true);
        pieChartRamos.setLegendVisible(false);

        labelPercPropAtrib = new Label();
        labelPercPropAtrib.getStyleClass().add("labelQuintaFase");
        labelPercPropDisp = new Label();
        labelPercPropDisp.getStyleClass().add("labelQuintaFase");
        labelPercPropt1 = new Label();
        labelPercPropt1.getStyleClass().add("labelQuintaFase");
        labelPercPropt2 = new Label();
        labelPercPropt2.getStyleClass().add("labelQuintaFase");
        labelPercPropt3 = new Label();
        labelPercPropt3.getStyleClass().add("labelQuintaFase");
        VBox vboxLabels = new VBox(labelPercPropAtrib,labelPercPropDisp,labelPercPropt1,labelPercPropt2,labelPercPropt3);
        vboxLabels.setAlignment(Pos.CENTER_LEFT);
        vboxLabels.setSpacing(10);

        //Diagrama de Barras - Orientadores
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Orientador");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Número");
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Top Orientadores");
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        dataOrient = new XYChart.Series<>();

        //Diagrama de Barras - Empresas
        CategoryAxis xAxis2 = new CategoryAxis();
        xAxis2.setLabel("Empresas");
        NumberAxis yAxis2 = new NumberAxis();
        yAxis2.setLabel("Número");
        barChart2 = new BarChart<>(xAxis2, yAxis2);
        barChart2.setTitle("Top Empresas");
        barChart2.setAnimated(false);
        barChart2.setLegendVisible(false);
        dataEmpresa = new XYChart.Series<>();

        HBox grupo1 = new HBox(pieChartRamos,vboxLabels);
        grupo1.setAlignment(Pos.CENTER);

        HBox grupo2 = new HBox(barChart,barChart2);
        grupo2.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(labelTitle,grupo1,grupo2);
        vbox.setAlignment(Pos.CENTER);

        this.setCenter(vbox);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> {update();});

        btnfecharFase.setOnAction(actionEvent -> {
            if(model.fecharFase()){
                model.proximaFase();
            }
        });


    }

    private void update() {
        this.setVisible(model != null && model.getState() == GestaoState.QUINTA_FASE);
        pieChartRamos.getData().clear();
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("DA",model.getTotalPropRamo("da")),
                new PieChart.Data("SI",model.getTotalPropRamo("si")),
                new PieChart.Data("RAS",model.getTotalPropRamo("ras")));
        pieChartRamos.setData(pieChartData);
        //Labels
        if(model.getPropostas() > 0){
            labelPercPropAtrib.setText("Propostas Atribuídas: " + ((model.getTotalAlunosCProp() * 100) / model.getPropostas()) + "%");
            labelPercPropDisp.setText("Propostas Disponíveis: " + ((model.getTotalPropDisp() * 100) / model.getPropostas()) + "%");
            labelPercPropt1.setText("Percentagem de Propostas de Estágio: " + ((model.getTotalPropTipo("t1") * 100) / model.getPropostas()) + "%");
            labelPercPropt2.setText("Percentagem de Propostas de Projeto: " + ((model.getTotalPropTipo("t2") * 100) / model.getPropostas()) + "%");
            labelPercPropt3.setText("Percentagem de AutoPropostas: " + ((model.getTotalPropTipo("t3") * 100) / model.getPropostas()) + "%");
        }

        if(model.getTotalOrientadores() > 0){
            barChart.getData().clear();
            dataOrient.getData().clear();
            //System.out.println(model.getTop5orientadores());
            for(int i=0;i<5;i++){
                dataOrient.getData().add(new XYChart.Data<>(model.getTop5orientadores().get(i),model.getNAlunosOrient(model.getTop5orientadores().get(i))));
            }
            barChart.getData().add(dataOrient);
        }

        if(model.getPropostas() > 0){
            barChart2.getData().clear();
            dataEmpresa.getData().clear();
            for(int i= 0;i<5;i++){
                dataEmpresa.getData().add(new XYChart.Data<>(model.getTop5empresas().get(i),model.getNPropEmpresa(model.getTop5empresas().get(i))));
            }
            barChart2.getData().add(dataEmpresa);
            //System.out.println(model.getTop5empresas());
        }

    }
}
