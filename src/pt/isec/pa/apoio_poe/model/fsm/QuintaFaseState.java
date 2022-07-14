package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.io.IOException;
import java.util.List;

public class QuintaFaseState extends GestaoStateAdapter{

    protected QuintaFaseState(GestaoContext context, Gestao data) {
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
    public List<String> ListasOrientadoresFiltros() {
        return data.FiltrosOrientadores();
    }

    @Override
    public List<String> ListasOrientadorEspecifico(String email) {
        return data.FiltrosOrientadorEspecifico(email);
    }

    @Override
    public String DadosFinais(){
        return data.dadosFinais();
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
    public GestaoState getState(){
        return GestaoState.QUINTA_FASE;
    }
}
