package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.util.List;

public class GestaoOrientadoresState extends GestaoStateAdapter{

    protected GestaoOrientadoresState(GestaoContext context, Gestao data) {
        super(context, data);
    }

    @Override
    public boolean atribuirOrientador(String email,String n_aluno){
        if(!data.isFase4_fechada()){
            return data.atribuiOrientadorAluno(email,n_aluno);
        }
        return false;
    }

    @Override
    public List<String> ListasOrientadoresFiltros() {
        return data.FiltrosOrientadores();
    }

    @Override
    public List<String> consultaOrientador(String email){
        return data.getAlunosOrientadosporDoc(email);
    }

    @Override
    public boolean alteracaoOrientador(String email_atual, String email_seguinte, String n_aluno){
        if(!data.isFase4_fechada()) {
            return data.alterarOrientador(email_atual,email_seguinte,n_aluno);
        }
        return false;
    }

    @Override
    public boolean eliminarOrientador(String email){
        if(!data.isFase4_fechada()) {
            return data.eliminarOrientador(email);
        }
        return false;
    }
    @Override
    public boolean removerAlunoOrientado(String email, String naluno){
        return data.removerAlunoOrientado(email, naluno);
    }

    @Override
    public void proximaFase(){
        changeState(GestaoState.QUARTA_FASE);
    }

    @Override
    public GestaoState getState() {
        return GestaoState.GESTAO_ORIENTADORES;
    }
}
