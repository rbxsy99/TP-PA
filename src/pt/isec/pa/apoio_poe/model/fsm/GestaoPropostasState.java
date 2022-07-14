package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.io.IOException;
import java.util.List;

public class GestaoPropostasState extends GestaoStateAdapter{


    protected GestaoPropostasState(GestaoContext context, Gestao data) {
        super(context, data);
    }

    @Override
    public boolean importarPropostas(String nome_fich) throws IOException{
        if(!data.isFase1_fechada()){
            try {
                return data.importarPropostas(nome_fich);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean exportarPropostas(String nome_fich) throws IOException{
        try {
            return data.exportarPropostas(nome_fich);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean adicionarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente) {
        return data.adicionarProposta(tipo,id,ramosdestino,titulo,entidade,n_aluno,docente);
    }
    @Override
    public boolean editarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente) {
        return data.editarProposta(tipo,id,ramosdestino,titulo,entidade,n_aluno,docente);
    }

    @Override
    public boolean removerProposta(String id_prop){
        return data.removerProposta(id_prop);
    }

    @Override
    public List<String> ConsultarDados(String opcao){
        return data.showPropostas();
    }

    @Override
    public void proximaFase(){
        changeState(GestaoState.PRIMEIRA_FASE);
    }

    @Override
    public GestaoState getState() {
        return GestaoState.GESTAO_PROPOSTAS;
    }
}
