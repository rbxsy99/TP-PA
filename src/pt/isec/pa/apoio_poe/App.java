package pt.isec.pa.apoio_poe;

import javafx.application.Application;
import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.fsm.GestaoContext;
import pt.isec.pa.apoio_poe.ui.gui.MainJFX;
import pt.isec.pa.apoio_poe.ui.text.GestaoUI;
import pt.isec.pa.apoio_poe.ui.text.PAInput;

public class App {
    public static void main(String[] args){
        int op;
        op = PAInput.chooseOption("Escolha o tipo de execução:","Interface de Consola","Interface Gráfica");
        switch (op) {
            case 1 -> {
                //GestaoContext fsm = new GestaoContext();
                GestaoManager gest = new GestaoManager();
                GestaoUI ui = new GestaoUI(gest);
                ui.start();
            }
            case 2 ->{
                //UI Grafico
                Application.launch(MainJFX.class,args);
            }
        }


        //UI Grafico
        //Application.launch(MainJFX.class,args);

    }
}
