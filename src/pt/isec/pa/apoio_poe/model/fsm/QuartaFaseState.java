package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.io.IOException;
import java.util.List;

public class QuartaFaseState extends GestaoStateAdapter{

    protected QuartaFaseState(GestaoContext context, Gestao data) {
        super(context, data);
    }

    @Override
    public boolean AssociacaoAutomDocentes() {
        if(!data.isFase4_fechada()){
            return data.atribuiOrientadorAutomDocente();
        }
        return false;
    }

    @Override
    public List<String> ListasAlunosFiltros(String tipo) {
        return data.FiltrosAlunos(tipo);
    }

    @Override
    public List<String> ListasOrientadoresFiltros() {
        return data.FiltrosOrientadores();
    }

    @Override
    public List<String> ListasOrientadorEspecifico(String email) {
        return data.FiltrosOrientadorEspecifico(email);
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
    public void gestao(String tipo){
        if(!data.isFase4_fechada()){
            changeState(GestaoState.GESTAO_ORIENTADORES);
        }
    }

    @Override
    public void faseAnterior(){
        changeState(GestaoState.TERCEIRA_FASE);
    }

    @Override
    public boolean fecharFase(){
        data.setFase4_fechada(true);
        return true;
    }

    @Override
    public void proximaFase(){
        changeState(GestaoState.QUINTA_FASE);
    }

    @Override
    public GestaoState getState(){
        return GestaoState.QUARTA_FASE;
    }
}
