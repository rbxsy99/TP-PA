package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.io.IOException;
import java.util.List;

public class GestaoDocentesState extends GestaoStateAdapter{

    protected GestaoDocentesState(GestaoContext context, Gestao data) {
        super(context, data);
    }

    @Override
    public boolean importarDocentes(String nome_fich) throws IOException{
        if(!data.isFase1_fechada()){
            try {
                return data.importarDocentes(nome_fich);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean exportarDocentes(String nome_fich) throws IOException{
        try {
            return data.exportarDocentes(nome_fich);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removerDocente(String email) {
        return data.removerDocente(email);
    }

    @Override
    public boolean adicionarDocente(String nome, String mail){
        return data.adicionarDocente(nome,mail);
    }

    @Override
    public boolean editarDocente(String nome, String mail){
        return data.editarDocente(nome,mail);
    }


    @Override
    public List<String> ConsultarDados(String opcao){
        return data.showDocentes();
    }

    @Override
    public void proximaFase(){
        changeState(GestaoState.PRIMEIRA_FASE);
    }


    @Override
    public GestaoState getState() {
        return GestaoState.GESTAO_DOCENTES;
    }
}
