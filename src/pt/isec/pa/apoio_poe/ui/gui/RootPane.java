package pt.isec.pa.apoio_poe.ui.gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import pt.isec.pa.apoio_poe.model.GestaoManager;

public class RootPane extends BorderPane {
    GestaoManager model;

    public RootPane(GestaoManager model){
        this.model = model;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews(){
        //Colocar todos os objetos no stackPane
        StackPane stackPane = new StackPane(new PrimeiraFaseUI(model),new GestaoAlunosUI(model),
                        new GestaoDocentesUI(model),new GestaoPropostasUI(model), new SegundaFaseUI(model),
                        new GestaoCandidaturasUI(model),new TerceiraFaseUI(model), new RealizaAtribuicoesUI(model),
                        new QuartaFaseUI(model),new GestaoOrientadoresUI(model),new QuintaFaseUI(model));

        this.setCenter(stackPane);
        this.setTop(new NavigationToolBar(model));
        this.setBottom(new StatusBar(model));
        this.setRight(new LateralBar(model));
    }

    private void registerHandlers(){}

    private void update(){}
}
