package pt.isec.pa.apoio_poe.model.data;

import pt.isec.pa.apoio_poe.model.configs.Aluno;
import pt.isec.pa.apoio_poe.model.configs.Candidaturas;
import pt.isec.pa.apoio_poe.model.configs.Docente;
import pt.isec.pa.apoio_poe.model.configs.Propostas;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//Dados
public class Gestao implements Serializable {
    private ArrayList<Aluno> alunos;
    private ArrayList<Docente> docentes;
    private ArrayList<Propostas> propostas;
    private ArrayList<Candidaturas> candidaturas;
    //Controla estado das fases
    private boolean fase1_fechada;
    private boolean fase2_fechada;
    private boolean fase3_fechada;
    private boolean fase4_fechada;
    private boolean fase5_fechada;
    private static final long serialVersionUID = 3L;

    //Gestao de Alunos / Docentes / Propostas de Estágio ou Projeto
    private String ModoAtual;

    public Gestao(){
        alunos = new ArrayList<>();
        docentes = new ArrayList<>();
        propostas = new ArrayList<>();
        candidaturas = new ArrayList<>();
        fase1_fechada = false;
        fase2_fechada = false;
        fase3_fechada = false;
        fase4_fechada = false;
        fase5_fechada = false;
    }

    //Importar de ficheiros .csv

    public boolean importarAlunos(String nome_fich) throws IOException {
        String splitBy = ",";
        String line = "";
        int size_alunos = alunos.size();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(nome_fich), StandardCharsets.UTF_8));
        while ((line = br.readLine()) != null)   //returns a Boolean value
        {
            String[] dados = line.split(splitBy);    // use comma as separator
            //Verifica dados aluno
            if(dados[0].charAt(0) == '2' && checkNAluno(dados[0]) && !alunoRegistado(Long.parseLong(dados[0])) && checkEmail(dados[2]) && (Double.parseDouble(dados[5]) > 0 && Double.parseDouble(dados[5]) < 1)
                    && checkRamo(dados[4]) && checkCurso(dados[3]) && (Boolean.parseBoolean(dados[6]) || !Boolean.parseBoolean(dados[6])) && dados.length == 7){
                Aluno aluno = new Aluno(Long.parseLong(dados[0]),dados[1],dados[2],dados[3],dados[4],Double.parseDouble(dados[5]),Boolean.parseBoolean(dados[6]));
                alunos.add(aluno);
            }
        }
        //Alterou o tamanho do arraylist /inseriu
        return size_alunos < alunos.size();
    }

    public boolean importarDocentes(String nome_fich) throws IOException {
        String splitBy = ",";
        String line = "";
        int size_docentes = docentes.size();
        BufferedReader br = new BufferedReader(new FileReader(nome_fich));
        while ((line = br.readLine()) != null)   //returns a Boolean value
        {
            String[] dados = line.split(splitBy);    // use comma as separator
            if(checkEmail(dados[1]) && !docenteRegistado(dados[1]) && dados.length == 2){
                Docente docente = new Docente(dados[0],dados[1]);
                docentes.add(docente);
            }
        }
        if(size_docentes < docentes.size()){
            return true;
        }
        return false;
    }

    public boolean importarPropostas(String nome_fich) throws IOException {
        String splitBy = ",";
        String line = "";
        int size_propostas = propostas.size();
        BufferedReader br = new BufferedReader(new FileReader(nome_fich));
        while ((line = br.readLine()) != null)   //returns a Boolean value
        {
            String[] dados = line.split(splitBy);    // use comma as separator
            //
            if(dados[0].equalsIgnoreCase("t1") && !checkIdProposta(dados[1]) && dados.length > 4 &&  checkRamoProposta(dados[2])){
                if(dados.length == 6 && !dados[5].isEmpty() && alunoRegistado(Long.parseLong(dados[5])) && !alunoProposta(Long.parseLong(dados[5]))){
                    Propostas proposta = new Propostas(dados[0],dados[1],dados[2],dados[3],dados[4],Long.parseLong(dados[5]),null);
                    propostas.add(proposta);
                }else{
                    Propostas proposta = new Propostas(dados[0],dados[1],dados[2],dados[3],dados[4],0,null);
                    propostas.add(proposta);
                }
            }else if(dados[0].equalsIgnoreCase("t2")  && !checkIdProposta(dados[1]) && dados.length > 5 &&  checkRamoProposta(dados[2])){
                if(docenteRegistado(dados[4])){
                    if(dados.length == 6 && !dados[5].isEmpty() && alunoRegistado(Long.parseLong(dados[5])) && !alunoProposta(Long.parseLong(dados[5]))){
                        Propostas proposta = new Propostas(dados[0],dados[1],dados[2],dados[3],null,Long.parseLong(dados[5]),dados[4]);
                        propostas.add(proposta);
                    }else{
                        Propostas proposta = new Propostas(dados[0],dados[1],dados[2],dados[3],null,0,dados[4]);
                        propostas.add(proposta);
                    }
                }

            }else if(dados[0].equalsIgnoreCase("t3") ){
                if(alunoRegistado(Long.parseLong(dados[3])) && !checkIdProposta(dados[1]) && !alunoProposta(Long.parseLong(dados[3])) ){
                    Propostas proposta = new Propostas(dados[0],dados[1],null,dados[2],null,Long.parseLong(dados[3]),null);
                    propostas.add(proposta);
                }
            }
        }
        if(size_propostas < propostas.size()){
            return true;
        }
        return false;
    }

    public boolean importarCandidaturas(String nome_fich) throws IOException {
        String splitBy = ",";
        String line = "";
        int size_cand = candidaturas.size();
        BufferedReader br = new BufferedReader(new FileReader(nome_fich));
        //Possibilidades de propostas
        ArrayList<String> propostas = new ArrayList<>();
        while ((line = br.readLine()) != null)   //returns a Boolean value
        {
            String[] dados = line.split(splitBy);    // use comma as separator

            if(alunoRegistado(Long.parseLong(dados[0])) && !checkAlunoCandi(Long.parseLong(dados[0])) && dados.length >= 2){
                for(int i=0;i<dados.length;i++) {
                    if((i+1)<dados.length){
                        if(checkIdProposta(dados[i+1])) {
                            propostas.add(dados[i+1]);
                        }
                    }
                }
                Candidaturas cand = new Candidaturas(Long.parseLong(dados[0]),propostas);
                candidaturas.add(cand);
            }
            propostas.clear();
        }
        if(size_cand < candidaturas.size()){
            return true;
        }
        return false;
    }

    //Exportar para .csv

    public boolean exportarAlunos(String nome_fich) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nome_fich), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            for (Aluno a : alunos) {
                sb.append(a.getN_aluno());
                sb.append(',');
                sb.append(a.getNome());
                sb.append(',');
                sb.append(a.getEmail());
                sb.append(',');
                sb.append(a.getCurso());
                sb.append(',');
                sb.append(a.getRamo());
                sb.append(',');
                sb.append(a.getClassificacao());
                sb.append(',');
                sb.append(a.isAceder_estagios());
                sb.append('\n');
            }
            writer.write(sb.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public String getPropostasCandidaturas(String id){
        StringBuilder sb = new StringBuilder();
        for(Propostas p : propostas) {
            if(id.equalsIgnoreCase(p.getId())){
                sb.append(',');
                sb.append(p.getId());
                sb.append(',');
                sb.append(p.getTitulo());
                sb.append(',');
                if(p.getDocenteproponente() != null){
                    sb.append(p.getDocenteproponente());
                    sb.append(',');
                }
                if(p.getRamosdestino() != null) {
                    sb.append(p.getRamosdestino());
                    sb.append(',');
                }
                sb.append(p.getTipo());
            }
        }
        return sb.toString();
    }

    public boolean exportarAlunosInfoCandidaturas(String nome_fich) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nome_fich), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            for (Aluno a : alunos) {
                sb.append(a.getN_aluno());
                sb.append(',');
                sb.append(a.getNome());
                sb.append(',');
                sb.append(a.getEmail());
                sb.append(',');
                sb.append(a.getCurso());
                sb.append(',');
                sb.append(a.getRamo());
                sb.append(',');
                sb.append(a.getClassificacao());
                sb.append(',');
                sb.append(a.isAceder_estagios());
                for(Candidaturas c : candidaturas){
                    if (c.getN_aluno() == a.getN_aluno()) {
                        for(int i= 0;i<c.getTotalPropostas();i++) {
                            sb.append(getPropostasCandidaturas(c.getIdProp(i)));
                        }
                    }
                }
                if(getEmailOrientador(a.getN_aluno()) != null) {
                    sb.append(',');
                    sb.append(getEmailOrientador(a.getN_aluno()));
                }
                sb.append('\n');
            }
            writer.write(sb.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean exportarDocentes(String nome_fich) throws IOException {
        try (PrintWriter writer = new PrintWriter(nome_fich)) {
            StringBuilder sb = new StringBuilder();
            for(Docente d : docentes){
                sb.append(d.getNome());
                sb.append(',');
                sb.append(d.getEmail());
                sb.append('\n');
            }
            writer.write(sb.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean exportarCandidaturas(String nome_fich) throws IOException {
        try (PrintWriter writer = new PrintWriter(nome_fich)) {
            StringBuilder sb = new StringBuilder();
            for(Candidaturas c : candidaturas){
                sb.append(c.getN_aluno());
                for(int i= 0;i<c.getTotalPropostas();i++) {
                    sb.append(',');
                    sb.append(c.getIdProp(i));
                }
                sb.append('\n');
            }
            writer.write(sb.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean exportarPropostas(String nome_fich) throws IOException {
        try (PrintWriter writer = new PrintWriter(nome_fich)) {
            StringBuilder sb = new StringBuilder();
            for(Propostas p : propostas){
                if(p.getTipo().equalsIgnoreCase("T1")){
                    sb.append(p.getId());
                    sb.append(',');
                    sb.append(p.getRamosdestino());
                    sb.append(',');
                    sb.append(p.getEntidadeacolhimento());
                    if(p.getN_aluno()!=0){
                        sb.append(',');
                        sb.append(p.getN_aluno());
                    }
                    sb.append('\n');
                }else if(p.getTipo().equalsIgnoreCase("T2")){
                    sb.append(p.getId());
                    sb.append(',');
                    sb.append(p.getRamosdestino());
                    sb.append(',');
                    sb.append(p.getTitulo());
                    sb.append(',');
                    sb.append(p.getDocenteproponente());
                    if(p.getN_aluno()!=0){
                        sb.append(',');
                        sb.append(p.getN_aluno());
                    }
                    sb.append('\n');
                }else{
                    sb.append(p.getId());
                    sb.append(',');
                    sb.append(p.getTitulo());
                    sb.append(',');
                    sb.append(p.getN_aluno());
                    sb.append('\n');
                }
            }
            writer.write(sb.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    //Gestão de Alunos

    public boolean adicionarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios){
        if(checkNAluno(naluno) && !alunoRegistado(Long.parseLong(naluno)) && checkEmail(mail) && (Double.parseDouble(classificacao) > 0 && Double.parseDouble(classificacao) < 1)
                && checkRamo(ramo) && checkCurso(curso)){
            Aluno aluno = new Aluno(Long.parseLong(naluno),nome,mail,curso,ramo,Double.parseDouble(classificacao),acederEstagios);
            alunos.add(aluno);
            return true;
        }
        return false;
    }

    public boolean editarAluno(String nome, String naluno, String mail, String curso, String ramo, String classificacao, Boolean acederEstagios){
        for(Aluno a : alunos){
            if(Long.parseLong(naluno)==a.getN_aluno()){
                System.out.println(nome);
                a.setNome(nome);
                a.setN_aluno(Long.parseLong(naluno));
                a.setEmail(mail);
                a.setCurso(curso);
                a.setRamo(ramo);
                a.setClassificacao(Double.parseDouble(classificacao));
                a.setAceder_estagios(acederEstagios);

                return true;
            }
        }

        return false;
    }


    public boolean removerAluno(String n_aluno){
        if(checkNAluno(n_aluno)){
            for(Aluno a : alunos){
                if(a.getN_aluno() == Long.parseLong(n_aluno) && !a.isProposta_atribuida()){
                    alunos.remove(a);
                    return true;
                }
            }
        }
        return false;
    }

    //Gestão de Docentes
    public boolean adicionarDocente(String nome, String mail){
        if(checkEmail(mail) && !docenteRegistado(mail)){
            Docente docente = new Docente(nome,mail);
            docentes.add(docente);
            return true;
        }
        return false;
    }

    public boolean removerDocente(String email){
        if(docenteRegistado(email)){
            for(Docente d: docentes){
                if(d.getEmail().equalsIgnoreCase(email) && d.getNAlunos() == 0){
                    docentes.remove(d);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean editarDocente(String nome, String email){
        for(Docente d : docentes){
            if(d.getEmail().equalsIgnoreCase(email)){
                d.setNome(nome);
                return true;
            }
        }
        return false;
    }

    //Gestao de Propostas
    public boolean adicionarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente){
        if(tipo.equalsIgnoreCase("t1") && !checkIdProposta(id) && checkRamoProposta(ramosdestino)){
            if(alunoRegistado(Long.parseLong(n_aluno)) && !alunoProposta(Long.parseLong(n_aluno)) && checkNAluno(n_aluno)){
                Propostas proposta = new Propostas(tipo,id,ramosdestino,titulo,entidade,Long.parseLong(n_aluno),null);
                propostas.add(proposta);
                return true;
            }else{
                Propostas proposta = new Propostas(tipo,id,ramosdestino,titulo,entidade,0,null);
                propostas.add(proposta);
                return true;
            }
        }else if(tipo.equalsIgnoreCase("t2") && !checkIdProposta(id) && checkRamoProposta(ramosdestino)){
            if(docenteRegistado(docente)){
                if(alunoRegistado(Long.parseLong(n_aluno)) && !alunoProposta(Long.parseLong(n_aluno)) && checkNAluno(n_aluno)){
                    Propostas proposta = new Propostas(tipo,id,ramosdestino,titulo,null,Long.parseLong(n_aluno),docente);
                    propostas.add(proposta);
                    return true;
                }else{
                    Propostas proposta = new Propostas(tipo,id,ramosdestino,titulo,null,0,docente);
                    propostas.add(proposta);
                    return true;
                }
            }
        }else{
            if(alunoRegistado(Long.parseLong(n_aluno)) && !alunoProposta(Long.parseLong(n_aluno)) && checkNAluno(n_aluno) && !alunoProposta(Long.parseLong(n_aluno))){
                Propostas proposta = new Propostas(tipo,id,null,titulo,null,Long.parseLong(n_aluno),null);
                propostas.add(proposta);
                return true;
            }
        }

        return false;
    }

    public boolean editarProposta(String tipo, String id, String ramosdestino,String titulo, String entidade, String n_aluno, String docente){
        for(Propostas p: propostas){
            if(p.getId().equalsIgnoreCase(id)){
                p.setTipo(tipo);
                p.setRamosdestino(ramosdestino);
                p.setTitulo(titulo);
                p.setEntidadeacolhimento(entidade);
                if(docenteRegistado(docente)){
                    p.setDocenteproponente(docente);
                }
                return true;
            }
        }
        return false;
    }

    public boolean removerProposta(String id){
        if(checkIdProposta(id)){
            for(Propostas p: propostas){
                if(p.getId().equalsIgnoreCase(id) && p.getN_aluno() == 0){
                    propostas.remove(p);
                    return true;
                }
            }
        }
        return false;
    }

    //Gestão de Candidaturas

    public boolean adicionarCandidatura(String NAluno, String P1){
        ArrayList<String> propostas = new ArrayList<>();
        if(alunoRegistado(Long.parseLong(NAluno)) && !checkAlunoCandi(Long.parseLong(NAluno))){
            if(checkIdProposta(P1) && !P1.equals("")) {
                propostas.add(P1);
                Candidaturas cand = new Candidaturas(Long.parseLong(NAluno),propostas);
                candidaturas.add(cand);
                return true;
            }
        }
        return false;
    }

    public boolean adicionarCandidaturaExist(String NAluno, String P1){
        if(alunoRegistado(Long.parseLong(NAluno)) && checkAlunoCandi(Long.parseLong(NAluno))){
            for(Candidaturas c : candidaturas){
                if(c.getN_aluno() == Long.parseLong(NAluno)){
                    c.addProposta(P1);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removerCandidatura(String NAluno){
        if(alunoRegistado(Long.parseLong(NAluno)) && checkAlunoCandi(Long.parseLong(NAluno))){
            for(Candidaturas c : candidaturas){
                if(c.getN_aluno() == Long.parseLong(NAluno)){
                    candidaturas.remove(c);
                    return true;
                }
            }
        }
        return false;
    }

    //Gestão de Orientadores

    public boolean atribuiOrientadorAutomDocente(){
        boolean alt = false;
        for(Propostas p: propostas){
            for(Docente d: docentes){
                if(d.getEmail().equalsIgnoreCase(p.getDocenteproponente())){
                    d.addAluno(p.getN_aluno());
                    alt = true;
                }
            }
        }
        return alt;
    }

    public boolean atribuiOrientadorAluno(String email,String n_aluno){
        boolean result = false;
        //Se o aluno existe e tem proposta atribuida
        if(alunoRegistado(Long.parseLong(n_aluno)) && checkAlunopropAtrib(Long.parseLong(n_aluno))){
            for(Docente d: docentes){
                if(d.checkAluno(Long.parseLong(n_aluno))){ //Verificar se o aluno já está a ser orientado por outro docente
                    result = true;
                }
            }
            for(Docente d: docentes){
                if(d.getEmail().equalsIgnoreCase(email) && !result){
                    d.addAluno(Long.parseLong(n_aluno));
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getAlunosOrientadosporDoc(String email){
        List<String> dados = new ArrayList<>();
        //StringBuilder sb = new StringBuilder();
        if(docenteRegistado(email)){
            dados.add("Lista dos alunos orientados por " + email + " \n");
            for(Docente d : docentes){
                if(d.getNAlunos() > 0 && d.getEmail().equalsIgnoreCase(email)){
                    dados.add(d.getAlunosOrie());
                }
            }
        }else{
            dados.add("Docente não encontrado.\n");
        }
        return dados;
    }

    public boolean alterarOrientador(String email_atual, String email_seguinte, String n_aluno){
        //Verificar dados
        if(alunoRegistado(Long.parseLong(n_aluno)) && checkAlunopropAtrib(Long.parseLong(n_aluno)) && docenteRegistado(email_atual) && docenteRegistado(email_seguinte)){
            for(Docente d : docentes){
                if(d.getEmail().equalsIgnoreCase(email_atual) && d.getNAlunos() > 0){
                    d.removeAluno(Long.parseLong(n_aluno));
                }
                if(d.getEmail().equalsIgnoreCase(email_seguinte)){
                    d.addAluno(Long.parseLong(n_aluno));
                }
            }
            return true;
        }
        return false;
    }

    public boolean eliminarOrientador(String email){
        if(docenteRegistado(email)){
            for(Docente d:docentes){
                if(d.getEmail().equalsIgnoreCase(email) && d.getNAlunos() > 0){
                    d.apagaAlunos();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removerAlunoOrientado(String email, String naluno){
        if(docenteRegistado(email)){
            for(Docente d : docentes){
                if(d.getEmail().equalsIgnoreCase(email) && d.getNAlunos() > 0){
                    d.removeAluno(Long.parseLong(naluno));
                    return true;
                }
            }
        }
        return false;
    }

    //Atribuições

    public void atribuiPropostaAutomaticaAluno(){
        for(Propostas p: propostas){
            for(Aluno a: alunos){
                if(p.getN_aluno() != 0){
                    if(a.getN_aluno() == p.getN_aluno()){
                        a.setProposta_atribuida(true);
                        a.setAutoproposta_propdocentes(true);
                    }
                }
            }
        }
    }


    //Caso haja empate devolve ao utilizador para escolher entre os alunos
    public List<String> atribuicaoPropostaDisponivel() {
        List<String> dados = new ArrayList<>();
        double aux = 0;
        long naluno=0;
        boolean mais_alto=false;
        for (Aluno a : alunos) {
            if (aux <= a.getClassificacao() && !(a.isProposta_atribuida()) && a.isAceder_estagios()) {
                aux = a.getClassificacao();
                naluno = a.getN_aluno();
            }
        }
        for(Aluno a : alunos){
            if(aux == a.getClassificacao() && !(a.isProposta_atribuida()) && a.isAceder_estagios()){
                if(a.getN_aluno()!=naluno){
                    for(Propostas p: propostas) {
                        if(p.getN_aluno()==0) {
                            dados.add("Conflitos de Classificações - ID Proposta");
                            dados.add(p.getId());
                            dados.add("\nOs números seguintes apresentaram um conflito de resultados, escolha um desses:\n");
                            dados.add(String.valueOf(a.getN_aluno()));
                            dados.add(String.valueOf(naluno));
                            return dados;
                        }
                    }
                }else { //Caso não haja empate
                    for (Propostas p : propostas) {
                        if (p.getN_aluno() == 0) {
                            if (naluno == a.getN_aluno()) {
                                p.setN_aluno(a.getN_aluno());
                                a.setProposta_atribuida(true);
                                mais_alto = true;
                                break;
                            }
                        }
                    }
                }
            }else if(mais_alto){
                for (Aluno a2 : alunos) {
                    if (aux <= a2.getClassificacao() && !(a2.isProposta_atribuida()) && a2.isAceder_estagios()) {
                        aux = a2.getClassificacao();
                        naluno = a2.getN_aluno();
                    }
                }
                mais_alto = false;
            }
        }
        System.out.println(dados);
        return dados;
    }

    public boolean atribuiPropostaManual(String id,String n_aluno){
        for(Propostas p:propostas){
            if(p.getId().equalsIgnoreCase(id)){
                p.setN_aluno(Long.parseLong(n_aluno));
            }
        }
        for(Aluno a:alunos){
            if(a.getN_aluno() == Long.parseLong(n_aluno)){
                a.setProposta_atribuida(true);
                return true;
            }
        }
        return false;
    }

    public List<String> getAlunosAtribuidosSemPropostaInicial() {
        List<String> dados = new ArrayList<>();
        //StringBuilder sb = new StringBuilder("Alunos atribuídos sem propostas iniciais\n");
        dados.add("Alunos atribuídos sem propostas iniciais\n");
        for (Aluno a : alunos) {
            if(a.isProposta_atribuida() && !a.isAutoproposta_propdocentes()){
                for(Propostas p:propostas){
                    if(a.getN_aluno() == p.getN_aluno()){
                        dados.add("ID Proposta: " + p.getId());
                    }
                }
                dados.add("\n");
                dados.add(a.toString());
            }
        }
        return dados;
    }
    public List<String> getAlunosSemCandidatura() {
        List<String> dados = new ArrayList<>();
        int aux = 0;
        for (Aluno a : alunos) {
            for(Candidaturas c : candidaturas){
                if (a.getN_aluno() == c.getN_aluno()) {
                   aux++;
                   break;
                }
            }
            if(aux==0){
                dados.add(String.valueOf(a.getN_aluno()));
            }
            aux = 0;
        }
        return dados;
    }

    public boolean removeAlunoAtribuido(String id, String n_aluno){
        for(Propostas p : propostas){
            if(p.getId().equalsIgnoreCase(id) && p.getN_aluno() == Long.parseLong(n_aluno)){
                for(Aluno a:alunos){
                    if(a.getN_aluno() == Long.parseLong(n_aluno) && !a.isAutoproposta_propdocentes() && a.isProposta_atribuida()){
                        a.setProposta_atribuida(false);
                        p.setN_aluno(0);
                        //Remover da lista do orientador se já tiver algum orientador associado
                        removeAlunoOrientador(a.getN_aluno());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Return de todas as propostas para serem removidas manualmente
    public List<String> getPropostasAtribManual(){
        List<String> dados = new ArrayList<>();
        for(Propostas p : propostas){
            if(!p.getTipo().equalsIgnoreCase("t3") && p.getN_aluno() != 0){
                dados.add(p.toString());
            }
        }
        return dados;
    }

    public boolean removeAlunosSemAutoproposta(){
        if(propostas.isEmpty() && alunos.isEmpty()){
            return false;
        }
        for(Propostas p: propostas){
            for(Aluno a: alunos){
                if(a.getN_aluno() == p.getN_aluno() && !a.isAutoproposta_propdocentes() && a.isProposta_atribuida()){
                    a.setProposta_atribuida(false);
                    p.setN_aluno(0);
                    //Remover da lista do orientador se já tiver algum orientador associado
                    removeAlunoOrientador(a.getN_aluno());
                }
            }
        }
        return true;
    }


    //LISTAGEM COM FILTROS

    public List<String> FiltrosPropostas(String tipo){
        List<String> dados = new ArrayList<>();
        //StringBuilder sb = new StringBuilder();
        dados.add("Propostas Filtradas:\n");
        tipo=tipo.toLowerCase();
        int aux=0;
        switch(tipo){
            case "auto_propostas" -> {
                for(Propostas p : propostas){
                    if(p.getTipo().equalsIgnoreCase("T3"))
                        dados.add(p.toString());
                }
            }
            case "propostas_docentes" -> {
                for(Propostas p : propostas){
                    if(p.getDocenteproponente() != null)
                        dados.add(p.toString());
                }
            }
            case "propostas_com_candidaturas" -> {
                for(Propostas p : propostas){
                    for(Candidaturas c : candidaturas) {
                        if (c.getN_aluno() == p.getN_aluno())
                            dados.add(p.toString());
                    }
                }
            }
            case "propostas_sem_candidaturas" -> {
                for(Propostas p : propostas){
                    for(Candidaturas c : candidaturas) {
                        if (c.getN_aluno() == p.getN_aluno()) {
                            aux = 1;
                            break;
                        }
                    }
                    //Senão existir a proposta do aluno nas candidaturas
                    if(aux==0){
                        dados.add(p.toString());
                    }
                    aux=0;
                }
            }
            case "propostas_disponiveis" -> {
                for(Propostas p : propostas){
                    if(p.getN_aluno() == 0)
                        dados.add(p.toString());
                }
            }
            case "propostas_atribuidas" -> {
                for(Propostas p : propostas){
                    if(p.getN_aluno() != 0)
                        dados.add(p.toString());
                }
            }

        }
        return dados;
    }

    public List<String> FiltrosAlunos(String tipo){
        List<String> dados = new ArrayList<>();
        //StringBuilder sb = new StringBuilder();
        dados.add("Alunos Filtrados:\n");
        tipo=tipo.toLowerCase();
        int aux=0;
        switch(tipo){
            case "com_autoproposta" -> {
                for(Aluno a : alunos){
                    if(a.isAutoproposta_propdocentes()){
                        dados.add(a.toString());
                    }
                }
            }
            case "com_candidatura" -> {
                for(Aluno a : alunos) {
                    for (Candidaturas c : candidaturas) {
                        if (a.getN_aluno()==c.getN_aluno())
                            dados.add(a.toString());
                    }
                }
            }
            case "sem_candidatura" -> {
                for(Aluno a : alunos){
                    for(Candidaturas c : candidaturas) {
                        if (a.getN_aluno()==c.getN_aluno()) {
                            aux = 1;
                            break;
                        }
                    }
                    //Senão existir o aluno com candidatura
                    if(aux==0){
                        dados.add(a.toString());
                    }
                    aux=0;
                }
            }
            case "com_proposta" -> {
                for(Aluno a : alunos){
                    if(a.isProposta_atribuida()) {
                        dados.add(a.toString());
                    }
                }
            }
            case "sem_proposta" -> {
                for(Aluno a : alunos){
                    if(!a.isProposta_atribuida()){
                        dados.add(a.toString());
                    }
                }
            }
            case "sem_proposta_com_candidatura" ->{
                for(Aluno a : alunos){
                    for(Candidaturas c:candidaturas){
                        if(!a.isProposta_atribuida() && a.getN_aluno() == c.getN_aluno()){
                            dados.add(a.toString());
                        }
                    }
                }
            }
            case "com_proposta_e_orientador" -> {
                for(Aluno a : alunos){
                    if(a.isProposta_atribuida()){
                        for(Docente d : docentes){
                            if(d.checkAluno(a.getN_aluno()))
                                dados.add(a.toString());
                        }
                    }
                }
            }
            case "com_proposta_sem_orientador" -> {
                for(Aluno a : alunos){
                    if(a.isProposta_atribuida()){
                        for(Docente d : docentes){
                            if(!d.checkAluno(a.getN_aluno()))
                                dados.add(a.toString());
                        }
                    }
                }
            }
        }
        return dados;
    }

    public List<String> FiltrosOrientadores(){
        List<String> dados = new ArrayList<>();
        //StringBuilder sb = new StringBuilder();
        dados.add("Docentes Dados:\n");
        int auxmed=0,auxmin=alunos.size(),auxmax=0;
        double media=0;
        for (Docente d : docentes) {
            if(d.getNAlunos() > 0) {
                dados.add("Docente: "+ d.getNome() + " Email: " + d.getEmail() + " Número de orientações: " + d.getNAlunos()+"\n");
                media += d.getNAlunos();
                auxmed++;
            }
            if(d.getNAlunos() > 0 && d.getNAlunos()<auxmin) {
                auxmin = d.getNAlunos();
            }
            if(d.getNAlunos() > 0 && d.getNAlunos()>auxmax) {
                auxmax = d.getNAlunos();
            }
        }
        media = media /auxmed;
        dados.add("\nMédia de orientações: " + media);
        dados.add("\nMínimo de orientações: " + auxmin);
        dados.add("\nMáximo de orientações: " + auxmax);
        return dados;
    }

    public List<String> FiltrosOrientadorEspecifico(String email){
        List<String> dados = new ArrayList<>();
        //StringBuilder sb = new StringBuilder("Docente especifico:" + email +"\n");
        dados.add("Docente especifico:" + email +"\n");
        for (Docente d : docentes) {
            if(d.getEmail().equalsIgnoreCase(email) && d.getNAlunos() > 0) {
                dados.add(d.toString());
            }
        }
        return dados;
    }

    public String dadosFinais(){
        StringBuilder sb = new StringBuilder("Dados Adicionais:\n");
        sb.append("Total Alunos: " + alunos.size() + " Propostas Atribuidas:" + getTotalPropostasAtrib() + " Propostas Disponiveis:" + (propostas.size()-getTotalPropostasAtrib()) +"\n");
        sb.append("Percentagem de Propostas Atribuidas: " + (getTotalPropostasAtrib()*100)/propostas.size() + "%\n");
        sb.append("Total de Propostas por ramo-> DA: " + getTotalPropostaRamo("da") + " SI: " + getTotalPropostaRamo("si") + " RAS: "+ getTotalPropostaRamo("ras") + " \n");
        sb.append("Total Docentes: " + docentes.size() + " Nº Orientadores: " + getTotalOrientadores() + "\n");
        sb.append("Total Candidaturas: " + candidaturas.size() + " \n");
        return sb.toString();
    }

    //UTILS

    public int getTotalPropostasAtrib(){
        int count = 0;
        for(Aluno a: alunos){
            if(a.isProposta_atribuida()){
                count++;
            }
        }
        return count;
    }

    public int getTotalOrientadores(){
        int count = 0;
        for(Docente d: docentes){
            if(d.getNAlunos() > 0){
                count++;
            }
        }
        return count;
    }

    public int getTotalPropTipo(String tipo){
        int count = 0;
        if(tipo.equalsIgnoreCase("t1") || tipo.equalsIgnoreCase("t2") || tipo.equalsIgnoreCase("t3")){
            for(Propostas p:propostas){
                if(p.getTipo().equalsIgnoreCase(tipo)){
                    count++;
                }
            }
        }
        return count;
    }

    //Verificar se todos os alunos com candidaturas tem projetos atribuidos
    public boolean checkNAluno(String naluno){
        if(naluno.length() == 10){
            return true;
        }
        return false;
    }

    public boolean checkCandidaturasAtribuidas(){

        int count_cand = candidaturas.size();
        int count_alunosatr = 0;
        for(Candidaturas c:candidaturas){
            for(Aluno a : alunos){
                if(a.getN_aluno() == c.getN_aluno() && a.isProposta_atribuida()){
                    count_alunosatr++;
                }
            }
        }
        if(count_cand == count_alunosatr){
            return true;
        }
        count_alunosatr = 0;
        for(Propostas p : propostas){
            if(p.getN_aluno() == 0)
                count_alunosatr++;
        }
        return count_alunosatr == 0;
    }

    public boolean docenteRegistado(String email_doc){
        for(Docente d: docentes){
            if(d.getEmail().equalsIgnoreCase(email_doc)){
                return true;
            }
        }
        return false;
    }

    public boolean alunoRegistado(long n_aluno){
        for(Aluno a : alunos){
            if(a.getN_aluno() == n_aluno){
                return true;
            }
        }
        return false;
    }

    public boolean checkRamo(String ramo){
        if(ramo.equalsIgnoreCase("da") || ramo.equalsIgnoreCase("si") || ramo.equalsIgnoreCase("ras")){
            return true;
        }
        return false;
    }

    public boolean checkEmail(String email){
        if(email.contains("@isec.pt")){
            return true;
        }
        return false;
    }

    public boolean checkRamoProposta(String ramo){
        if(ramo.toLowerCase().contains("da") || ramo.toLowerCase().contains("si") || ramo.toLowerCase().contains("ras")){
            return true;
        }
        return false;
    }

    //Verificar se um aluno ja tem candidatura registada
    public boolean checkAlunoCandi(Long n_aluno){
        if(candidaturas.size() > 0){
            for(Candidaturas c :candidaturas){
                if(c.getN_aluno() == n_aluno){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkCurso(String curso){
        if(curso.equalsIgnoreCase("lei") || curso.equalsIgnoreCase("lei-pl") || curso.equalsIgnoreCase("lei-eu")){
            return true;
        }
        return false;
    }

    //Verificar se o aluno já tem uma auto proposta / proposta associada
    public boolean alunoProposta(long n_aluno){
        for(Propostas p: propostas){
            if(p.getN_aluno() == n_aluno){
                return true;
            }
        }
        return false;
    }

    //Verificar se o aluno ja tem uma proposta atribuida
    public boolean checkAlunopropAtrib(long n_aluno){
        for(Aluno a :alunos){
            if(a.getN_aluno() == n_aluno && a.isProposta_atribuida()){
                return true;
            }
        }
        return false;
    }

    public boolean checkIdProposta(String id){
        if(propostas.size() != 0){
            for(Propostas p : propostas){
                if(p.getId().equalsIgnoreCase(id)){
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> showAlunos() {
        List<String> dados = new ArrayList<>();
        //StringBuilder sb = new StringBuilder();
        dados.add("Alunos " + alunos.size() + "\n");
        for (Aluno a : alunos) {
            dados.add(a.toString());
        }
        return dados;
    }

    public List<String> showDocentes(){
        List<String> dados = new ArrayList<>();
        //StringBuilder sb = new StringBuilder();
        dados.add("Docentes " + docentes.size() + "\n");
        for(Docente d: docentes){
            dados.add(d.toString());
        }
        return dados;
    }

    public List<String> showPropostas(){
        List<String> dados = new ArrayList<>();
        //StringBuilder sb = new StringBuilder("Propostas\n");
        dados.add("Propostas " + propostas.size() + "\n");
        for(Propostas p: propostas){
            dados.add(p.toString());
        }
        return dados;
    }

    public List<String> showCandidaturas(){
        List<String> dados = new ArrayList<>();
        //StringBuilder sb = new StringBuilder("Candidaturas:\n");
        dados.add("Candidaturas: " + candidaturas.size() + "\n");
        for(Candidaturas c : candidaturas){
            dados.add(c.toString());
        }
        return dados;
    }

    public List<String> getTop5orientadores(){
        int count = 0;
        List<Integer> nAlunosOrientados = new ArrayList<>();
        List<String> dados = new ArrayList<>();
        for(Docente d: docentes){
            nAlunosOrientados.add(d.getNAlunos());
        }
        Collections.sort(nAlunosOrientados);
        for(Docente d: docentes){
            for(int i: nAlunosOrientados){
                if(d.getNAlunos() == i && count < 5){
                    dados.add(d.getNome());
                    count++;
                    break;
                }
            }
        }
        return dados;
    }

    public int getNAlunosOrient(String nome){ //Número de alunos orientados por um docente
        for(Docente d: docentes){
            if(d.getNome().equalsIgnoreCase(nome)){
                return d.getNAlunos();
            }
        }

        return 0;
    }

    public List<String> getAlunosOrient(String mail){ //Número dos alunos de um orientador
        List<String> dados = new ArrayList<>();
        for(Docente d: docentes){
            if(d.getEmail().equalsIgnoreCase(mail)){
                dados.addAll(d.exportAlunos());
            }
        }

        return dados;
    }


    public List<String> getTop5empresas(){
        List<String> dados = new ArrayList<>();
        List<String> empresas = new ArrayList<>();
        for(Propostas p: propostas){
            if(p.getEntidadeacolhimento() != null){
                empresas.add(p.getEntidadeacolhimento());
            }
        }
        List<String> aux = empresas.stream().distinct().collect(Collectors.toList()); //Empresas não repetidas
        List<Integer> auxn = new ArrayList<>(); //Nº de ocorrências
        for(String s : aux){
            auxn.add(Collections.frequency(empresas, s));
        }
        auxn.sort(Collections.reverseOrder()); //Coloca do maior para o menor 10,8,2
        for(Integer i: auxn ) {
            for(String s : aux){
                if(i == Collections.frequency(empresas, s)) {
                    dados.add(s);
                    aux.remove(s);
                    break;
                }
            }
        }

        return dados;
    }

    public int getNPropEmpresa(String nome){
        int count = 0;
        for(Propostas p : propostas){
            if(p.getEntidadeacolhimento() != null && p.getEntidadeacolhimento().equalsIgnoreCase(nome)){
                count++;
            }
        }
        return count;
    }

    public int getTotalPropostasPorRamo(){
        int da = getTotalPropostaRamo("DA");
        int ras = getTotalPropostaRamo("RAS");
        int si = getTotalPropostaRamo("SI");
        return (da+ras+si);
    }

    public int getTotalPropostaRamo(String ramo){
        int count = 0;
        for(Propostas p:propostas){
            if(p.getRamosdestino() != null){
                if(p.getRamosdestino().toLowerCase().contains(ramo.toLowerCase())){
                    count++;
                }
            }
        }
        return count;
    }
    public List<String> getPropostasNaoAtribuidas(){
        List<String> dados = new ArrayList<>();
        for(Propostas p:propostas){
            if(p.getN_aluno() == 0){
                dados.add(p.getId());
            }
        }
        return dados;
    }

    public List<String> getPropostasEAlunosDisponiveis(){
        List<String> dados = new ArrayList<>();
        dados.add("Propostas disponíveis e Alunos sem propostas atribuídas:\n");
        for(Propostas p: propostas){
            if(p.getN_aluno() == 0){
                dados.add(p.toString() + "\n");
            }
        }
        for(Aluno a:alunos){
            if(!a.isProposta_atribuida()){
                dados.add(a.toString() + "\n");
            }
        }
        return dados;
    }

    public List<String> getEmailsOrientador(){
        List<String> dados = new ArrayList<>();
        for(Docente d: docentes){
            dados.add(d.getEmail());
        }
        return dados;
    }

    public List<String> getNAlunosCProp(){
        List<String> dados = new ArrayList<>();
        for(Aluno a: alunos){
            if(a.isProposta_atribuida()){
                dados.add(String.valueOf(a.getN_aluno()));
            }
        }
        return dados;
    }

    public String getEmailOrientador(Long n_aluno){
        for(Docente d: docentes) {
            if(d.getNAlunos() > 0 && d.checkAluno(n_aluno)) {
                return d.getEmail();
            }
        }
        return null;
    }

    //Remove o numero do aluno ao ser removido a sua proposta do grupo de alunos do orientador
    public void removeAlunoOrientador(Long n_aluno){
        for(Docente d: docentes) {
            if(d.getNAlunos() > 0 && d.checkAluno(n_aluno)) {
                d.removeAluno(n_aluno);
            }
        }
    }

    //Get Objetos das Classes
    public Aluno getAluno(String n_aluno){
        for(Aluno a : alunos){
            if(a.getN_aluno() == Long.parseLong(n_aluno)){
                return a;
            }
        }
        return null;
    }

    public Docente getDocente(String email){
        for(Docente d: docentes){
            if(d.getEmail().equalsIgnoreCase(email)){
                return d;
            }
        }
        return null;
    }

    public Propostas getProposta(String id){
        for(Propostas p: propostas){
            if(p.getId().equalsIgnoreCase(id)){
                return p;
            }
        }
        return null;
    }

    //Getters & Setters

    public String getModoAtual(){return ModoAtual;}

    public void setModoAtual(String ModoAtual) {this.ModoAtual = ModoAtual;}

    public int totalAlunos(){
        return alunos.size();
    }

    public int totalDocentes(){
        return docentes.size();
    }

    public int totalPropostas(){
        return propostas.size();
    }

    public int totalCandidaturas(){
        return candidaturas.size();
    }

    public int getTotalPropDisp(){
        int count = 0;
        for(Propostas p : propostas){
            if(p.getN_aluno() == 0){
                count++;
            }
        }
        return count;
    }

    public boolean isFase1_fechada() {
        return fase1_fechada;
    }

    public void setFase1_fechada(boolean fase1_fechada) {
        this.fase1_fechada = fase1_fechada;
    }

    public boolean isFase2_fechada() {
        return fase2_fechada;
    }

    public void setFase2_fechada(boolean fase2_fechada) {
        this.fase2_fechada = fase2_fechada;
    }

    public boolean isFase3_fechada() {
        return fase3_fechada;
    }

    public void setFase3_fechada(boolean fase3_fechada) {
        this.fase3_fechada = fase3_fechada;
    }

    public boolean isFase4_fechada() {
        return fase4_fechada;
    }

    public void setFase4_fechada(boolean fase4_fechada) {
        this.fase4_fechada = fase4_fechada;
    }

    public boolean isFase5_fechada() {
        return fase5_fechada;
    }

    public void setFase5_fechada(boolean fase5_fechada) {
        this.fase5_fechada = fase5_fechada;
    }

    //To String

    @Override
    public String toString() {return "Gestao:Modo Atual="+ModoAtual;}
}
