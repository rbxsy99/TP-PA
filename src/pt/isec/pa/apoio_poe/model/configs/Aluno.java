package pt.isec.pa.apoio_poe.model.configs;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Aluno implements Serializable {
    private long n_aluno;
    private String nome;
    private String email;
    private String curso;
    private String ramo;
    private double classificacao;
    private boolean aceder_estagios; //Opção de aceder a estagios (true) ou apenas projetos (false)
    private boolean proposta_atribuida;
    private boolean autoproposta_propdocentes; // Com autopropostas ou propostas de docentes com aluno associado
    private static final long serialVersionUID = 4L;

    public Aluno(long n_aluno, String nome, String email, String curso, String ramo, double classificacao, boolean aceder_estagios) {
        this.n_aluno = n_aluno;
        this.nome = nome;
        this.email = email;
        this.curso = curso;
        this.ramo = ramo;
        this.classificacao = classificacao;
        this.aceder_estagios = aceder_estagios;
        this.proposta_atribuida = false;
        this.autoproposta_propdocentes = false;
    }

    public long getN_aluno() {
        return n_aluno;
    }

    public void setN_aluno(long n_aluno) {
        this.n_aluno = n_aluno;
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

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getRamo() {
        return ramo;
    }

    public void setRamo(String ramo) {
        this.ramo = ramo;
    }

    public double getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(double classificacao) {
        this.classificacao = classificacao;
    }

    public boolean isAceder_estagios() {
        return aceder_estagios;
    }

    public void setAceder_estagios(boolean aceder_estagios) {
        this.aceder_estagios = aceder_estagios;
    }

    public boolean isProposta_atribuida() {
        return proposta_atribuida;
    }

    public void setProposta_atribuida(boolean proposta_atribuida) {
        this.proposta_atribuida = proposta_atribuida;
    }

    public boolean isAutoproposta_propdocentes() {
        return autoproposta_propdocentes;
    }

    public void setAutoproposta_propdocentes(boolean autoproposta_propdocentes) {
        this.autoproposta_propdocentes = autoproposta_propdocentes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aluno aluno = (Aluno) o;
        return n_aluno == aluno.n_aluno && email.equalsIgnoreCase(aluno.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(n_aluno, email);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tNº Aluno:" + n_aluno + " Nome: " + nome + "\n");
        sb.append("\tEmail:" + email +" Curso:" +  curso + " Ramo: " +  ramo + "\n\tClassificacao: " + classificacao + "\n");
        if(aceder_estagios){
            sb.append("\t[INFO] O aluno pode aceder a estagios.\n");
        }else{
            sb.append("\t[INFO] O aluno não pode aceder a estagios.\n");
        }
        sb.append((proposta_atribuida ? "\tCom proposta atribuida.\n" : "\tSem proposta atribuida.\n"));
        if(autoproposta_propdocentes){
            sb.append("\tAluno com auto-proposta ou proposto por docente.\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
