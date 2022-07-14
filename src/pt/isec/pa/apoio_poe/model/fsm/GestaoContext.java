package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.configs.Aluno;
import pt.isec.pa.apoio_poe.model.configs.Docente;
import pt.isec.pa.apoio_poe.model.configs.Propostas;
import pt.isec.pa.apoio_poe.model.data.Gestao;
import pt.isec.pa.apoio_poe.model.memento.IMemento;
import pt.isec.pa.apoio_poe.model.memento.IOriginator;
import pt.isec.pa.apoio_poe.model.memento.Memento;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GestaoContext implements Serializable, IOriginator {
    Gestao gestao;
    IGestaoState state;
    private static final long serialVersionUID = 1L;

    public GestaoContext(){
        gestao = new Gestao();
        state = GestaoState.PRIMEIRA_FASE.createState(this,gestao);
    }

    void ChangeState(IGestaoState newState){ this.state=newState;}

    // State interface methods

    public void gestao(String tipo){
        state.gestao(tipo);
    }

    //Gestao Alunos
    public boolean adicionarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios){
        return state.adicionarAluno(nome,naluno,mail,curso,ramo,classificacao,acederEstagios);
    }
    public boolean editarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios){
        return state.editarAluno(nome,naluno,mail,curso,ramo,classificacao,acederEstagios);
    }
    public boolean removerAluno(String n_aluno){
        return state.removerAluno(n_aluno);
    }
    public boolean importarAlunos(String nome_fich){
        try {
            return state.importarAlunos(nome_fich);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean exportarAlunos(String nome_fich) {
        try {
            return state.exportarAlunos(nome_fich);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Gestao Docentes
    public boolean adicionarDocente(String nome, String mail){
        return state.adicionarDocente(nome,mail);
    }
    public boolean editarDocente(String nome, String mail){
        return state.editarDocente(nome,mail);
    }
    public boolean removerDocente(String email){
        return state.removerDocente(email);
    }
    public boolean importarDocentes(String nome_fich){
        try {
            return state.importarDocentes(nome_fich);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean exportarDocentes(String nome_fich){
        try {
            return state.exportarDocentes(nome_fich);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Gestao Propostas
    public boolean adicionarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente){
        return state.adicionarProposta(tipo,id,ramosdestino,titulo,entidade,n_aluno,docente);
    }
    public boolean editarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente){
        return state.editarProposta(tipo,id,ramosdestino,titulo,entidade,n_aluno,docente);
    }
    public boolean removerProposta(String id_prop){
        return state.removerProposta(id_prop);
    }
    public boolean importarPropostas(String nome_fich){
        try{
            return state.importarPropostas(nome_fich);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean exportarPropostas(String nome_fich){
        try{
            return state.exportarPropostas(nome_fich);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Gestao Candidaturas
    public boolean adicionarCandidatura(String NAluno, String P1){
        return state.adicionarCandidatura(NAluno, P1);
    }
    public boolean adicionarCandidaturaExist(String NAluno, String P1){
        return state.adicionarCandidaturaExist(NAluno,P1);
    }
    public boolean editarCandidatura(){
        return state.editarCandidatura();
    }
    public boolean removerCandidatura(String NAluno){
        return state.removerCandidatura(NAluno);
    }
    public boolean importarCandidaturas(String nome_fich){
        try{
            return state.importarCandidaturas(nome_fich);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean exportarCandidaturas(String nome_fich){
        try{
            return state.exportarCandidaturas(nome_fich);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> ConsultarDados(String opcao){
        return state.ConsultarDados(opcao);
    }

    public List<String> consultaOrientador(String email){
        return state.consultaOrientador(email);
    }

    public List<String> ListasAlunosFiltros(String tipo){
        return state.ListasAlunosFiltros(tipo);
    }

    public List<String> ListasPropostasFiltros(String tipo,int nfiltros){
        return state.ListasPropostasFiltros(tipo,nfiltros);
    }
    public List<String> ListasOrientadoresFiltros() {
        return state.ListasOrientadoresFiltros();
    }
    public List<String> ListasOrientadorEspecifico(String email) {
        return state.ListasOrientadorEspecifico(email);
    }

    public List<String> AtribuicaoAutomDocentesAoAluno(){
        return state.AtribuicaoAutomDocentesAoAluno();
    }

    public boolean AtribuicaoAutomAluno(){
        return state.AtribuicaoAutomAluno();
    }

    public boolean AtribuicaoManual(String id,String n_aluno){
        return state.AtribuicaoManual(id,n_aluno);
    }

    public List<String> getAlunosAtribuidosSemPropostasIniciais(){
        return state.getAlunosAtribuidosSemPropostasIniciais();
    }

    public boolean RemocaoManualAtribuicao(String id,String n_aluno){
        return state.RemocaoManualAtribuicao(id,n_aluno);
    }

    public boolean RemocaoManualTodasAtribuicoes(){
        return state.RemocaoManualTodasAtribuicoes();
    }

    public boolean exportarAlunosAvancado(String nome_fich){ return state.exportarAlunosAvancado(nome_fich);}

    public boolean AssociacaoAutomDocentes(){
        return state.AssociacaoAutomDocentes();
    }

    public boolean atribuirOrientador(String email,String n_aluno){
        return state.atribuirOrientador(email,n_aluno);
    }

    public void realiza_atribuicoes(){state.realiza_atribuicoes();}


    public boolean alteracaoOrientador(String email_atual, String email_seguinte, String n_aluno){
        return state.alteracaoOrientador(email_atual,email_seguinte,n_aluno);
    }

    public boolean eliminarOrientador(String email){
        return state.eliminarOrientador(email);
    }

    public boolean removerAlunoOrientado(String email, String naluno){return state.removerAlunoOrientado(email,naluno);}

    public List<String> getPropostaseAlunosDisponiveis(){ return state.getPropostaseAlunosDisponiveis();}

    public boolean fecharFase(){ return state.fecharFase();}

    public void proximaFase(){ state.proximaFase();}

    public void faseAnterior(){
        state.faseAnterior();
    }


    public GestaoState getState(){
        if (state == null)
            return null;
        return state.getState();
    }

    public String DadosFinais(){ return state.DadosFinais();}

    // get data

    public String getCurrentMode(){return gestao.getModoAtual();}
    public boolean getFase1Fechada(){ return gestao.isFase1_fechada();}
    public boolean getFase2Fechada(){ return gestao.isFase2_fechada();}
    public boolean getFase3Fechada(){ return gestao.isFase3_fechada();}
    public boolean getFase4Fechada(){ return gestao.isFase4_fechada();}
    public boolean getFase5Fechada(){ return gestao.isFase5_fechada();}

    public int getAlunos(){
        return gestao.totalAlunos();
    }
    public int getDocentes(){
        return gestao.totalDocentes();
    }
    public int getCandidaturas(){
        return gestao.totalCandidaturas();
    }
    public int getPropostas(){ return gestao.totalPropostas();}
    public int getTotalPropDisp(){ return gestao.getTotalPropDisp();}
    public int getTotalAlunosCProp(){ return gestao.getTotalPropostasAtrib();}
    public int getTotalOrientadores(){ return gestao.getTotalOrientadores();}
    public int getTotalPropTipo(String tipo){ return gestao.getTotalPropTipo(tipo);}
    public int getTotalPropRamo(String ramo){ return gestao.getTotalPropostaRamo(ramo);}
    public List<String> getTop5orientadores(){ return gestao.getTop5orientadores();}
    public int getNAlunosOrient(String nome){ return gestao.getNAlunosOrient(nome);}
    public List<String> getTop5empresas(){ return gestao.getTop5empresas();}
    public List<String> getAlunosSemCandidatura(){ return gestao.getAlunosSemCandidatura();}
    public int getNPropEmpresa(String nome){ return gestao.getNPropEmpresa(nome);}
    public List<String> getEmailsOrientador(){ return gestao.getEmailsOrientador();}
    public List<String> getNAlunosCProp(){ return gestao.getNAlunosCProp();}
    public List<String> getPropostasAtribManual(){ return gestao.getPropostasAtribManual();}
    public List<String> getPropostasNaoAtribuidas(){return gestao.getPropostasNaoAtribuidas();}
    public List<String> getAlunosOrient(String email){return gestao.getAlunosOrient(email);}

    public Aluno getAluno(String n_aluno){ return gestao.getAluno(n_aluno);}
    public Docente getDocente(String email){ return gestao.getDocente(email);}
    public Propostas getProposta(String id){ return gestao.getProposta(id);}


    @Override
    public IMemento save() {
        return new Memento(this);
    }

    @Override
    public void restore(IMemento memento) {
        Object obj = memento.getSnapshot();
        if (obj instanceof GestaoContext m) {
            state = m.state;
            gestao = m.gestao;
            state.setContext(this);
        }
    }
}
