package pt.isec.pa.apoio_poe.model.fsm;

import java.io.IOException;
import java.util.List;

public interface IGestaoState {

    public GestaoContext getContext();

    public void setContext(GestaoContext context);
    //Métodos
    void gestao(String tipo);
    //Gestao Alunos
    boolean adicionarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios);
    boolean editarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios);
    boolean removerAluno(String n_aluno);
    boolean importarAlunos(String nome_fich) throws IOException;
    boolean exportarAlunos(String nome_fich) throws IOException;
    //Gestao Docentes
    boolean adicionarDocente(String nome, String mail);
    boolean editarDocente(String nome, String mail);
    boolean removerDocente(String email);
    boolean importarDocentes(String nome_fich) throws IOException;
    boolean exportarDocentes(String nome_fich) throws IOException;
    //Gestao Propostas
    boolean adicionarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente);
    boolean editarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente);
    boolean removerProposta(String id_prop);
    boolean importarPropostas(String nome_fich) throws IOException;
    boolean exportarPropostas(String nome_fich) throws IOException;
    //Gestao Candidaturas
    boolean adicionarCandidatura(String NAluno, String P1);
    boolean adicionarCandidaturaExist(String NAluno, String P1);
    boolean editarCandidatura();
    boolean removerCandidatura(String NAluno);
    boolean importarCandidaturas(String nome_fich) throws IOException;
    boolean exportarCandidaturas(String nome_fich) throws IOException;
    //Mostrar dados
    List<String> ConsultarDados(String opcao);
    List<String> ListasAlunosFiltros(String tipo);
    List<String> ListasPropostasFiltros(String tipo,int nfiltros);
    List<String> ListasOrientadoresFiltros();
    List<String> ListasOrientadorEspecifico(String email);

    //Atribuicoes
    void realiza_atribuicoes();
    List<String> AtribuicaoAutomDocentesAoAluno();
    boolean AtribuicaoAutomAluno();
    List<String> getPropostaseAlunosDisponiveis();
    boolean AtribuicaoManual(String id,String n_aluno);
    List<String> getAlunosAtribuidosSemPropostasIniciais();
    boolean RemocaoManualAtribuicao(String id,String n_aluno);
    boolean RemocaoManualTodasAtribuicoes();
    boolean exportarAlunosAvancado(String nome_fich);
    boolean AssociacaoAutomDocentes(); //Atribuir orientador ao docente já auto-proposto
    //Gestao de Orientadores
    boolean atribuirOrientador(String email,String n_aluno);
    List<String> consultaOrientador(String email);
    boolean alteracaoOrientador(String email_atual, String email_seguinte, String n_aluno);
    boolean eliminarOrientador(String email);
    public boolean removerAlunoOrientado(String email, String naluno);
    //Calculos finais
    String DadosFinais();
    //Opcao de fechar fase
    boolean fecharFase();
    void proximaFase();
    void faseAnterior();
    //Receber o estado atual
    GestaoState getState();

}
