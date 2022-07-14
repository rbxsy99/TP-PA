package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public abstract class GestaoStateAdapter implements IGestaoState, Serializable {
    protected Gestao data;
    protected GestaoContext context;

    protected GestaoStateAdapter(GestaoContext context,Gestao data) {
        this.context = context;
        this.data = data;
    }

    public GestaoContext getContext() {
        return context;
    }

    public void setContext(GestaoContext context) {
        this.context = context;
    }

    protected void changeState(GestaoState newState){
        context.ChangeState(newState.createState(context,data));
    }

    @Override
    public void gestao(String tipo) { }
    @Override
    public List<String> ListasOrientadoresFiltros() {
        return null;
    }
    @Override
    public List<String> ListasOrientadorEspecifico(String email) {
        return null;
    }

    @Override
    public List<String> ConsultarDados(String opcao) { return null; }

    @Override
    public List<String> ListasAlunosFiltros(String tipo) { return null; }

    @Override
    public List<String> ListasPropostasFiltros(String tipo,int nfiltros) { return null; }

    @Override
    public List<String> AtribuicaoAutomDocentesAoAluno() { return null; }

    @Override
    public boolean AtribuicaoAutomAluno() { return false; }

    @Override
    public List<String> getPropostaseAlunosDisponiveis(){ return null;}

    @Override
    public boolean AtribuicaoManual(String id,String n_aluno) { return false; }

    @Override
    public List<String> getAlunosAtribuidosSemPropostasIniciais(){
        return null;
    }

    @Override
    public boolean RemocaoManualAtribuicao(String id,String n_aluno) { return false; }

    @Override
    public boolean RemocaoManualTodasAtribuicoes(){ return false;}

    @Override
    public boolean exportarAlunosAvancado(String nome_fich ){ return false;}

    @Override
    public boolean AssociacaoAutomDocentes() { return false; }

    @Override
    public boolean atribuirOrientador(String email,String n_aluno){
        return false;
    }

    @Override
    public List<String> consultaOrientador(String email){
        return null;
    }

    @Override
    public boolean alteracaoOrientador(String email_atual, String email_seguinte, String n_aluno){
        return false;
    }

    @Override
    public boolean eliminarOrientador(String email){
        return false;
    }

    @Override
    public boolean removerAlunoOrientado(String email, String naluno){return false;}

    @Override
    public boolean fecharFase(){ return false;}

    @Override
    public void proximaFase(){ }

    @Override
    public boolean adicionarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios) {
        return false;
    }

    @Override
    public boolean editarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios) {
        return false;
    }

    @Override
    public boolean removerAluno(String n_aluno) {
        return false;
    }

    @Override
    public boolean importarAlunos(String nome_fich) throws IOException {
        return false;
    }

    @Override
    public boolean exportarAlunos(String nome_fich) throws IOException {
        return false;
    }

    @Override
    public boolean adicionarDocente(String nome, String mail) {
        return false;
    }

    @Override
    public boolean editarDocente(String nome, String mail){
        return false;
    }

    @Override
    public boolean removerDocente(String email) {
        return false;
    }

    @Override
    public boolean importarDocentes(String nome_fich) throws IOException {
        return false;
    }

    @Override
    public boolean exportarDocentes(String nome_fich) throws IOException {
        return false;
    }

    @Override
    public boolean adicionarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente) {
        return false;
    }

    @Override
    public boolean editarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente) {
        return false;
    }

    @Override
    public boolean removerProposta(String id_prop) {
        return false;
    }

    @Override
    public boolean importarPropostas(String nome_fich) throws IOException {
        return false;
    }

    @Override
    public boolean exportarPropostas(String nome_fich) throws IOException {
        return false;
    }

    @Override
    public boolean adicionarCandidatura(String NAluno, String P1) {
        return false;
    }

    @Override
    public boolean adicionarCandidaturaExist(String NAluno, String P1){ return false;}

    @Override
    public boolean editarCandidatura() {
        return false;
    }

    @Override
    public boolean removerCandidatura(String NAluno) {
        return false;
    }

    @Override
    public String DadosFinais(){
        return null;
    }

    @Override
    public void faseAnterior(){}

    @Override
    public void realiza_atribuicoes(){}

    @Override
    public boolean importarCandidaturas(String nome_fich) throws IOException {
        return false;
    }

    @Override
    public boolean exportarCandidaturas(String nome_fich) throws IOException {
        return false;
    }
}
