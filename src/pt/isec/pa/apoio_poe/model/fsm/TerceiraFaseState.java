package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.io.IOException;
import java.util.List;

public class TerceiraFaseState extends GestaoStateAdapter{


    protected TerceiraFaseState(GestaoContext context, Gestao data) {
        super(context, data);
    }


    @Override
    public List<String> ListasAlunosFiltros(String tipo) {
        return data.FiltrosAlunos(tipo);
    }

    @Override
    public List<String> ListasPropostasFiltros(String tipo,int nfiltros) {
        return data.FiltrosPropostas(tipo);
    }



    @Override
    public void realiza_atribuicoes(){
        if(!data.isFase3_fechada()){
            changeState(GestaoState.REALIZA_ATRIBUICOES);
        }
    }

    @Override
    public boolean exportarAlunosAvancado(String nome_fich){
        try {
            return data.exportarAlunosInfoCandidaturas(nome_fich);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void faseAnterior(){
        changeState(GestaoState.SEGUNDA_FASE);
    }

    @Override
    public boolean fecharFase(){
        //Se todos os alunos com candidaturas submetidas possuirem projeto atribuido fecha fase3
        if(data.checkCandidaturasAtribuidas()){
            data.setFase3_fechada(true);
            return true;
        }
        return false;
    }

    @Override
    public void proximaFase(){
        changeState(GestaoState.QUARTA_FASE);
    }


    @Override
    public GestaoState getState(){
        return GestaoState.TERCEIRA_FASE;
    }

}
