package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.io.IOException;
import java.util.List;

public class GestaoCandidaturasState extends GestaoStateAdapter{


    protected GestaoCandidaturasState(GestaoContext context, Gestao data) {
        super(context, data);
    }

    @Override
    public boolean importarCandidaturas(String nome_fich) throws IOException{
        if(!data.isFase2_fechada()){
            try {
                return data.importarCandidaturas(nome_fich);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean exportarCandidaturas(String nome_fich) throws IOException{
        try {
            return data.exportarCandidaturas(nome_fich);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean adicionarCandidatura(String NAluno, String P1){
        return data.adicionarCandidatura(NAluno, P1);
    }

    @Override
    public boolean adicionarCandidaturaExist(String NAluno, String P1){
        return data.adicionarCandidaturaExist(NAluno,P1);
    }

    @Override
    public List<String> ConsultarDados(String opcao){
        return data.showCandidaturas();
    }

    @Override
    public void proximaFase(){
        changeState(GestaoState.SEGUNDA_FASE);
    }

    @Override
    public GestaoState getState() {
        return GestaoState.GESTAO_CANDIDATURAS;
    }
}
