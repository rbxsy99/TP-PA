package pt.isec.pa.apoio_poe.model.configs;


import java.io.Serializable;
import java.util.Objects;

public class Propostas implements Serializable {
    private String tipo;
    private String id;
    private String ramosdestino;
    private String titulo;
    private String entidadeacolhimento;
    private long n_aluno;
    private String docenteproponente;
    private static final long serialVersionUID = 6L;


    public Propostas(String tipo, String id, String ramos,String titulo,String entidade, long naluno, String docenteProponente){
        this.tipo = tipo;
        this.id = id;
        this.ramosdestino = ramos;
        this.titulo = titulo;
        this.entidadeacolhimento = entidade;
        this.n_aluno = naluno;
        this.docenteproponente = docenteProponente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRamosdestino() {
        return ramosdestino;
    }

    public void setRamosdestino(String ramosdestino) {
        this.ramosdestino = ramosdestino;
    }

    public String getEntidadeacolhimento() {
        return entidadeacolhimento;
    }

    public void setEntidadeacolhimento(String entidadeacolhimento) {
        this.entidadeacolhimento = entidadeacolhimento;
    }

    public String getDocenteproponente() {
        return docenteproponente;
    }

    public void setDocenteproponente(String docenteproponente) {
        this.docenteproponente = docenteproponente;
    }

    public long getN_aluno() {
        return n_aluno;
    }

    public void setN_aluno(long n_aluno) {
        this.n_aluno = n_aluno;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Propostas propostas = (Propostas) o;
        return n_aluno == propostas.n_aluno && Objects.equals(id, propostas.id) && Objects.equals(titulo, propostas.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, n_aluno);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tTipo: " + tipo + " ID: " + id + "\n");
        if(tipo.equalsIgnoreCase("T1")){
            sb.append("\tRamo: " + ramosdestino + " Titulo:" + titulo + " Entidade: " + entidadeacolhimento + "\n");
            if(n_aluno != 0){
                sb.append("\tNº Aluno:" + n_aluno + "\n");
            }
        }else if(tipo.equalsIgnoreCase("T2")){
            sb.append("\tRamo: " + ramosdestino + " Titulo:" + titulo + "\n");
            sb.append("\tDocente Proponente: " + docenteproponente + "\n");
            if(n_aluno != 0){
                sb.append("\tNº Aluno: " + n_aluno + "\n");
            }
        }else if(tipo.equalsIgnoreCase("T3")){
            sb.append("\tTitulo: " +  titulo + " Aluno:" + n_aluno + "\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
