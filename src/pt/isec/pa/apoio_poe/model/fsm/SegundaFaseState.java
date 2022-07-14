package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.io.IOException;
import java.util.List;

public class SegundaFaseState extends GestaoStateAdapter{

    protected SegundaFaseState(GestaoContext context, Gestao data) {
        super(context, data);
    }

    @Override
    public List<String> ListasAlunosFiltros(String tipo) {
        return data.FiltrosAlunos(tipo);
    }

    @Override
    public List<String> ListasPropostasFiltros(String tipo, int nfiltros) {
        return data.FiltrosPropostas(tipo);
    }

    @Override
    public void gestao(String tipo){
        if(!data.isFase2_fechada()){
            changeState(GestaoState.GESTAO_CANDIDATURAS);
        }
    }

    @Override
    public List<String> ConsultarDados(String opcao) {
        return data.showCandidaturas();
    }

    @Override
    public boolean fecharFase(){
        if(data.isFase1_fechada()){
            data.setFase2_fechada(true);
            return true;
        }
        return false;
    }


    @Override
    public void faseAnterior(){
        changeState(GestaoState.PRIMEIRA_FASE);
    }

    @Override
    public void proximaFase(){
        changeState(GestaoState.TERCEIRA_FASE);
    }

    @Override
    public GestaoState getState(){
        return GestaoState.SEGUNDA_FASE;
    }
}
