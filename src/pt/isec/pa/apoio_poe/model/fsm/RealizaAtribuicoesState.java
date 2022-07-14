package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.util.List;

public class RealizaAtribuicoesState extends GestaoStateAdapter{

    protected RealizaAtribuicoesState(GestaoContext context, Gestao data) {
        super(context, data);
    }

    @Override
    public List<String> AtribuicaoAutomDocentesAoAluno(){
        if(data.isFase2_fechada()){
            return data.atribuicaoPropostaDisponivel();
        }
        return null;
    }

    @Override
    public boolean AtribuicaoAutomAluno(){
        data.atribuiPropostaAutomaticaAluno();
        return true;
    }

    @Override
    public List<String> getPropostaseAlunosDisponiveis(){
        return data.getPropostasEAlunosDisponiveis();
    }

    @Override
    public boolean AtribuicaoManual(String id, String n_aluno){
        if(data.isFase2_fechada()) {
            return data.atribuiPropostaManual(id, n_aluno);
        }
        return false;
    }

    @Override
    public List<String> getAlunosAtribuidosSemPropostasIniciais(){
        return data.getAlunosAtribuidosSemPropostaInicial();
    }

    @Override
    public boolean RemocaoManualAtribuicao(String id,String n_aluno){
        if(data.isFase2_fechada()) {
            return data.removeAlunoAtribuido(id, n_aluno);
        }
        return false;
    }

    @Override
    public boolean RemocaoManualTodasAtribuicoes(){
        if(data.isFase2_fechada()){
            return data.removeAlunosSemAutoproposta();
        }
        return false;
    }

    @Override
    public void proximaFase(){
        changeState(GestaoState.TERCEIRA_FASE);
    }

    @Override
    public GestaoState getState() {
        return GestaoState.REALIZA_ATRIBUICOES;
    }
}
