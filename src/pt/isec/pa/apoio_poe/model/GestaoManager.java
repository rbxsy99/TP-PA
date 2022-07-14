package pt.isec.pa.apoio_poe.model;

import pt.isec.pa.apoio_poe.model.configs.Aluno;
import pt.isec.pa.apoio_poe.model.configs.Docente;
import pt.isec.pa.apoio_poe.model.configs.Propostas;
import pt.isec.pa.apoio_poe.model.fsm.GestaoContext;
import pt.isec.pa.apoio_poe.model.fsm.GestaoState;
import pt.isec.pa.apoio_poe.model.fsm.IGestaoState;
import pt.isec.pa.apoio_poe.model.memento.Caretaker;
import pt.isec.pa.apoio_poe.model.memento.IOriginator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.List;

public class GestaoManager implements Serializable{
    GestaoContext gest;
    Caretaker careTaker;
    //private static final long serialVersionUID = 1L;

    //UI Gráfico - 2ºmeta
    PropertyChangeSupport pcs;

    public GestaoManager(){
        gest = new GestaoContext();
        careTaker = new Caretaker(gest);
        pcs = new PropertyChangeSupport(this);
    }

    //gui
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void gestao(String tipo){
        gest.gestao(tipo);
        careTaker.save();
        pcs.firePropertyChange(null,null,null);
    }

    //Gestao Alunos
    public boolean adicionarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios){
        var ret = gest.adicionarAluno(nome,naluno,mail,curso,ramo,classificacao,acederEstagios);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean editarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios){
        var ret = gest.editarAluno(nome,naluno,mail,curso,ramo,classificacao,acederEstagios);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean removerAluno(String n_aluno){
        var ret = gest.removerAluno(n_aluno);
        careTaker.save();
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean importarAlunos(String nome_fich){
        var ret = gest.importarAlunos(nome_fich);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean exportarAlunos(String nome_fich){
        var ret = gest.exportarAlunos(nome_fich);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    //Gestao Docentes
    public boolean adicionarDocente(String nome, String mail){
        var ret = gest.adicionarDocente(nome,mail);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean editarDocente(String nome, String mail){
        var ret = gest.editarDocente(nome,mail);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean removerDocente(String email){
        var ret = gest.removerDocente(email);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean importarDocentes(String nome_fich){
        var ret = gest.importarDocentes(nome_fich);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean exportarDocentes(String nome_fich){
        var ret = gest.exportarDocentes(nome_fich);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    //Gestao Propostas
    public boolean adicionarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente){
        var ret = gest.adicionarProposta(tipo,id,ramosdestino,titulo,entidade,n_aluno,docente);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean editarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente){
        var ret = gest.editarProposta(tipo,id,ramosdestino,titulo,entidade,n_aluno,docente);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean removerProposta(String id_prop){
        var ret = gest.removerProposta(id_prop);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean importarPropostas(String nome_fich){
        var ret = gest.importarPropostas(nome_fich);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean exportarPropostas(String nome_fich){
        var ret = gest.exportarPropostas(nome_fich);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    //Gestao Candidaturas
    public boolean adicionarCandidatura(String NAluno, String P1){
        var ret = gest.adicionarCandidatura(NAluno,P1);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean adicionarCandidaturaExist(String NAluno, String P1){
        var ret = gest.adicionarCandidaturaExist(NAluno,P1);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean editarCandidatura(){
        var ret = gest.editarCandidatura();
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean removerCandidatura(String NAluno){
        var ret = gest.removerCandidatura(NAluno);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean importarCandidaturas(String nome_fich){
        var ret = gest.importarCandidaturas(nome_fich);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean exportarCandidaturas(String nome_fich){
        var ret = gest.exportarCandidaturas(nome_fich);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    //Consultar dados
    public List<String> ConsultarDados(String opcao){
        return gest.ConsultarDados(opcao);
    }
    public List<String> consultaOrientador(String email){
        return gest.consultaOrientador(email);
    }
    public List<String> ListasAlunosFiltros(String tipo){
        return gest.ListasAlunosFiltros(tipo);
    }
    public List<String> ListasPropostasFiltros(String tipo,int nfiltros){
        return gest.ListasPropostasFiltros(tipo,nfiltros);
    }
    public List<String> ListasOrientadoresFiltros() {
        return gest.ListasOrientadoresFiltros();
    }
    public List<String> ListasOrientadorEspecifico(String email) {
        return gest.ListasOrientadorEspecifico(email);
    }
    public List<String> getPropostaseAlunosDisponiveis(){ return gest.getPropostaseAlunosDisponiveis();}
    public String DadosFinais(){
        return gest.DadosFinais();
    }

    //Atribuições
    public void realiza_atribuicoes(){
        gest.realiza_atribuicoes();
        pcs.firePropertyChange(null,null,null);
    }

    public List<String> AtribuicaoAutomDocentesAoAluno(){
        return gest.AtribuicaoAutomDocentesAoAluno();
    }

    public boolean AtribuicaoAutomAluno(){
        var ret = gest.AtribuicaoAutomAluno();
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public boolean AtribuicaoManual(String id,String n_aluno){
        var ret = gest.AtribuicaoManual(id,n_aluno);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public List<String> getAlunosAtribuidosSemPropostasIniciais(){
        return gest.getAlunosAtribuidosSemPropostasIniciais();
    }

    public boolean RemocaoManualAtribuicao(String id,String n_aluno){
        var ret = gest.RemocaoManualAtribuicao(id,n_aluno);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public boolean RemocaoManualTodasAtribuicoes(){
        var ret = gest.RemocaoManualTodasAtribuicoes();
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public boolean AssociacaoAutomDocentes(){
        var ret = gest.AssociacaoAutomDocentes();
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    //Exportar alunos
    public boolean exportarAlunosAvancado(String nome_fich){
        var ret = gest.exportarAlunosAvancado(nome_fich);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    //Gestao Orientador
    public boolean atribuirOrientador(String email,String n_aluno){
        var ret = gest.atribuirOrientador(email,n_aluno);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean alteracaoOrientador(String email_atual, String email_seguinte, String n_aluno){
        var ret = gest.alteracaoOrientador(email_atual,email_seguinte,n_aluno);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public boolean eliminarOrientador(String email){
        var ret = gest.eliminarOrientador(email);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }
    public boolean removerAlunoOrientado(String email, String naluno){
        var ret = gest.removerAlunoOrientado(email,naluno);
        careTaker.save();  //Caretaker
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    //Fases
    public boolean fecharFase(){
        var ret = gest.fecharFase();
        if(ret){
            careTaker.reset();
        }
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public void proximaFase(){
        careTaker.reset();
        gest.proximaFase();
        pcs.firePropertyChange(null,null,null);
    }

    public void faseAnterior(){
        careTaker.reset();
        gest.faseAnterior();
        pcs.firePropertyChange(null,null,null);
    }

    public GestaoState getState(){
        if (gest == null)
            return null;
        return gest.getState();
    }

    //utils
    public String getCurrentMode(){return gest.getCurrentMode();}
    public boolean getFase1Fechada(){ return gest.getFase1Fechada();}
    public boolean getFase2Fechada(){ return gest.getFase2Fechada();}
    public boolean getFase3Fechada(){ return gest.getFase3Fechada();}
    public boolean getFase4Fechada(){ return gest.getFase4Fechada();}
    public boolean getFase5Fechada(){ return gest.getFase5Fechada();}
    public int getAlunos(){ return gest.getAlunos();}
    public int getDocentes(){return gest.getDocentes();}
    public int getCandidaturas(){return gest.getCandidaturas();}
    public int getPropostas(){ return gest.getPropostas();}
    public int getTotalPropDisp(){ return gest.getTotalPropDisp();}
    public int getTotalAlunosCProp(){ return gest.getTotalAlunosCProp();}
    public int getTotalOrientadores(){ return gest.getTotalOrientadores();}
    public int getTotalPropTipo(String tipo){ return gest.getTotalPropTipo(tipo);}
    public int getTotalPropRamo(String ramo){ return gest.getTotalPropRamo(ramo);}
    public List<String> getTop5orientadores(){ return gest.getTop5orientadores();}
    public List<String> getAlunosSemCandidatura(){ return gest.getAlunosSemCandidatura();}
    public List<String> getPropostasNaoAtribuidas(){return gest.getPropostasNaoAtribuidas();}
    public int getNAlunosOrient(String nome){ return gest.getNAlunosOrient(nome);}
    public List<String> getTop5empresas(){ return gest.getTop5empresas();}
    public int getNPropEmpresa(String nome){ return gest.getNPropEmpresa(nome);}
    public List<String> getEmailsOrientador(){ return gest.getEmailsOrientador();}
    public List<String> getNAlunosCProp(){ return gest.getNAlunosCProp();}
    public List<String> getPropostasAtribManual(){ return gest.getPropostasAtribManual();}
    public List<String> getAlunosOrient(String email){return gest.getAlunosOrient(email);}

    public Aluno getAluno(String n_aluno){ return gest.getAluno(n_aluno);}
    public Docente getDocente(String email){ return gest.getDocente(email);}
    public Propostas getProposta(String id){ return gest.getProposta(id);}

    //Load
    public boolean save(Object obj, String filename) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        return true;
    }

    //Save
    public Object load(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
        Object obj = (Object) ois.readObject();

        ois.close();
        return obj;
    }

    //CareTaker
    public void undo() {
        careTaker.undo();
        pcs.firePropertyChange(null,null,null);
    }

    public void redo() {
        careTaker.redo();
        pcs.firePropertyChange(null,null,null);
    }

    public void reset() {
        careTaker.reset();
    }

    public boolean hasUndo() {
        return careTaker.hasUndo();
    }

    public boolean hasRedo() {
        return careTaker.hasRedo();
    }

    //Utils
    public static String fileComponent(String fname) {
        int pos = fname.lastIndexOf(File.separator);
        if(pos > -1)
            return fname.substring(pos + 1);
        else
            return fname;
    }
}
