package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.io.IOException;
import java.util.List;

public class PrimeiraFaseState extends GestaoStateAdapter{

    protected PrimeiraFaseState(GestaoContext context, Gestao data) {
        super(context, data);
    }

    //Define o tipo (gestao alunos/docentes/propostas)

    @Override
    public void gestao(String tipo){
        if(!data.isFase1_fechada()){
            data.setModoAtual(tipo);
            switch (context.getCurrentMode()){
                case "gestao_alunos" :{
                    changeState(GestaoState.GESTAO_ALUNOS);
                    break;
                }
                case "gestao_docentes":{
                    changeState(GestaoState.GESTAO_DOCENTES);
                    break;
                }
                case "gestao_propostas":{
                    changeState(GestaoState.GESTAO_PROPOSTAS);
                    break;
                }
            }
        }
    }

    @Override
    public List<String> ConsultarDados(String opcao){
        opcao = opcao.toLowerCase();
        switch (opcao){
            case "alunos": return data.showAlunos();
            case "docentes" : return data.showDocentes();
            case "propostas" : return data.showPropostas();
        }
        return null;
    }

    @Override
    public boolean fecharFase(){
        if(data.getTotalPropostasPorRamo() >= data.totalAlunos() && data.totalAlunos() > 0 && data.totalPropostas() > 0 && data.totalDocentes() > 0){
            data.setFase1_fechada(true);
            return true;
        }
        return false;
    }

    @Override
    public void proximaFase(){
        changeState(GestaoState.SEGUNDA_FASE);
    }

    @Override
    public GestaoState getState(){
        return GestaoState.PRIMEIRA_FASE;
    }
}
