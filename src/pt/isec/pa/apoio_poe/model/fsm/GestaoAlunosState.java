package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.io.IOException;
import java.util.List;

public class GestaoAlunosState extends GestaoStateAdapter {


    protected GestaoAlunosState(GestaoContext context, Gestao data) {
        super(context, data);
    }

    @Override
    public boolean importarAlunos(String nome_fich) throws IOException {
        if (!data.isFase1_fechada()) {
            try {
                return data.importarAlunos(nome_fich);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean adicionarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios){
        return data.adicionarAluno(nome,naluno,mail,curso,ramo,classificacao,acederEstagios);
    }

    @Override
    public boolean editarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios){
        return data.editarAluno(nome,naluno,mail,curso,ramo,classificacao,acederEstagios);
    }


    @Override
    public boolean removerAluno(String n_aluno) {
        return data.removerAluno(n_aluno);
    }

        @Override
    public List<String> ConsultarDados(String opcao){
        return data.showAlunos();
    }

    @Override
    public boolean exportarAlunos(String nome_fich) throws IOException{
        try {
            return data.exportarAlunos(nome_fich);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void proximaFase(){
        changeState(GestaoState.PRIMEIRA_FASE);
    }

    @Override
    public GestaoState getState() {
        return GestaoState.GESTAO_ALUNOS;
    }
}
