package pt.isec.pa.apoio_poe.model.configs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Candidaturas implements Serializable {
    private long n_aluno;
    //Guardar os ids de todas as propostas indicadas
    private ArrayList<String> id_propostas = new ArrayList<>();
    private static final long serialVersionUID = 7L;

    public Candidaturas(long n_aluno,ArrayList<String> propostas){
        this.n_aluno = n_aluno;
        id_propostas = (ArrayList<String>) propostas.clone();
    }

    public long getN_aluno() {
        return n_aluno;
    }

    public void setN_aluno(long n_aluno) {
        this.n_aluno = n_aluno;
    }

    public ArrayList<String> getId_propostas() {
        return id_propostas;
    }

    public void setId_propostas(ArrayList<String> id_propostas) {
        this.id_propostas = id_propostas;
    }

    public int getTotalPropostas(){
        return id_propostas.size();
    }

    public String getIdProp(int i){
        return id_propostas.get(i);
    }

    public void addProposta(String id){
        id_propostas.add(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidaturas that = (Candidaturas) o;
        return n_aluno == that.n_aluno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(n_aluno);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tNºAluno: " + n_aluno +" \n");
        for(int i=0;i<id_propostas.size();i++) {
            sb.append("\t\tID Proposta " + (i+1) +":" + id_propostas.get(i) + "\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
