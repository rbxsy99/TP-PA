package pt.isec.pa.apoio_poe.model.configs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Docente implements Serializable {
    private String nome;
    private String email;
    private ArrayList<Long> alunos_ori; //Lista dos número de alunos que o docente orienta
    private static final long serialVersionUID = 5L;

    public Docente(String nome, String email) {
        this.nome = nome;
        this.email = email;
        alunos_ori = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addAluno(Long n_aluno){
        if(!alunos_ori.contains(n_aluno)){
            alunos_ori.add(n_aluno);
        }
    }

    public void removeAluno(Long n_aluno){
        if(getNAlunos() > 0) {
            alunos_ori.remove(n_aluno);
        }
    }

    public void apagaAlunos(){
        alunos_ori.clear();
    }

    public boolean checkAluno(Long n_aluno){
        return alunos_ori.contains(n_aluno);
    }

    // 0 -> Não é orientador
    public int getNAlunos(){
        return alunos_ori.size();
    }

    public String getAlunosOrie(){
        if(getNAlunos() > 0) {
            StringBuilder sb = new StringBuilder("Alunos orientados por " + nome + " | " + email + " \n");
            for(Long l:alunos_ori){
                sb.append("\t" + l + "\n");
            }
            return sb.toString();
        }
        return null;
    }

    public List<String> exportAlunos(){
        List<String> dados = new ArrayList<>();
        for(Long i : alunos_ori){
            dados.add(String.valueOf(i));
        }
        return dados;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Docente docente = (Docente) o;
        return email.equalsIgnoreCase(docente.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\tNome: " + nome + " Email: " + email + (getNAlunos() > 0 ? " \n\tOrientador: Sim - Nº Alunos: " + alunos_ori.size() + " \n": "\n\t Orientador: Não \n"));
        sb.append("\n");
        return sb.toString();
    }
}
