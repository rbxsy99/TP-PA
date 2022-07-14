package pt.isec.pa.apoio_poe.ui.text;

import pt.isec.pa.apoio_poe.model.GestaoManager;
import pt.isec.pa.apoio_poe.model.ModelLog;

import java.io.IOException;

public class GestaoUI {
    GestaoManager fsm;
    //Variáveis auxiliares
    private int op,op2;
    private String nome_fich;
    private String tipo_gestao;
    private boolean status;
    private ModelLog log;

    public GestaoUI(GestaoManager fsm){
        this.fsm = fsm;
        log = ModelLog.getInstance();
    }

    private boolean finish = false;

    public void start() {
        while(!finish){
            showInfo();
            if (fsm.getState() == null) System.exit(-1);
            switch (fsm.getState()) {
                case PRIMEIRA_FASE ->{
                    primeirafase();
                }
                case GESTAO_ALUNOS -> {
                    gestao_alun();
                }
                case GESTAO_DOCENTES -> {
                    gestao_docen();
                }
                case GESTAO_PROPOSTAS -> {
                   gestao_prop();
                }
                case SEGUNDA_FASE -> {
                    segundafase();
                }
                case GESTAO_CANDIDATURAS -> {
                    gestao_candi();
                }
                case TERCEIRA_FASE -> {
                    terceirafase();
                }
                case REALIZA_ATRIBUICOES -> {
                    realiza_atrib();
                }
                case QUARTA_FASE -> {
                    quartafase();
                }
                case GESTAO_ORIENTADORES -> {
                    gestao_orient();
                }
                case QUINTA_FASE -> {
                    quintafase();
                }
            }
        }
        //Exportar para .txt o log
        try{
            log.exportLog();
            System.out.println("Log exportado com sucesso.");
        }catch (IOException e){
            System.out.println("Erro ao exportar o log: " + e);
        }
    }

    private void primeirafase(){
        //Escolher opções
        op = PAInput.chooseOption("Opções disponíveis na Fase 1:","Gestão","Consultar dados","Importar estado", "Exportar estado","Proxima Fase","Fechar fase");
        switch (op){
            case 1 ->{
                //Escolha do tipo de gestao
                op2 = PAInput.chooseOption("Escolha o tipo de gestão:","Gestão de Alunos","Gestão de Docentes","Gestão de Propostas");
                switch (op2){
                    case 1->{
                        tipo_gestao = "gestao_alunos";
                        if(status){
                            System.out.println("Gestão de Alunos escolhida com sucesso.\n");
                            log.add("Fase 1 -> Selecionada a gestão de alunos.");
                        }

                    }
                    case 2->{
                        tipo_gestao = "gestao_docentes";
                        if(status){
                            System.out.println("Gestão de Docentes escolhida com sucesso.\n");
                            log.add("Fase 1 -> Selecionada a gestão de docentes.");
                        }
                    }
                    case 3->{
                        tipo_gestao = "gestao_propostas";
                        if(status){
                            System.out.println("Gestão de Propostas escolhida com sucesso.\n");
                            log.add("Fase 1 -> Selecionada a gestão de propostas.");
                        }
                    }
                }
                log.add("Fase 1 -> Redirecionada para a área de gestão.");
                fsm.gestao(tipo_gestao);
            }
            case 2 ->{
                op2 = PAInput.chooseOption("Consultar dados:","Alunos","Docentes","Propostas");
                switch (op2){
                    case 1 ->{
                        for(String sb: fsm.ConsultarDados("alunos")){
                            System.out.println(sb);
                        }
                        log.add("Fase 1 -> Consultado dados dos alunos.");
                    }
                    case 2 ->{
                        for(String sb: fsm.ConsultarDados("docentes")){
                            System.out.println(sb);
                        }
                        log.add("Fase 1 -> Consultado dados dos docentes.");
                    }
                    case 3 ->{
                        for(String sb: fsm.ConsultarDados("propostas")){
                            System.out.println(sb);
                        }
                        log.add("Fase 1 -> Consultado dados das propostas.");
                    }
                }
            }
            case 3 ->{
                load();
            }
            case 4 -> {
                save();
            }
            case 5 ->{
                log.add("Fase 1 -> Redirecionado para a fase seguinte.");
                fsm.proximaFase();
            }
            case 6 ->{
                if(fsm.fecharFase()){
                    System.out.println("Fase 1 fechada com sucesso.\n");
                    log.add("Fase 1 -> Fase 1 fechada com sucesso.");
                    fsm.proximaFase();
                }else{
                    System.out.println("Erro ao fechar a fase 1, verifique os requisitos necessários.\n");
                    log.add("Fase 1 -> Tentativa falhada ao fechar a fase 1.");
                }
            }
        }
    }

    private void gestao_alun(){
        op = PAInput.chooseOption("Gestão de alunos | Opções","Adicionar","Editar","Consultar dados","Remover","Importar","Exportar","Voltar atrás");
        switch (op){
            case 1,2,4 ->{
                System.out.println("Não implementado.\n");
            }
            case 3 ->{
                for(String sb: fsm.ConsultarDados("")){
                    System.out.println(sb);
                }
                log.add("Gestão de Alunos -> Consultado dados dos alunos.");
            }
            case 5 ->{
                nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende importar",true);
                status = fsm.importarAlunos(nome_fich);
                if(status){
                    System.out.println("Ficheiro de alunos importado com sucesso.\n");
                    log.add("Gestão de Alunos -> Ficheiro de alunos importado com sucesso.");
                }else{
                    System.out.println("Erro ao carregar o ficheiro.\n");
                    log.add("Gestão de Alunos -> Tentativa falhada ao importar ficheiros de alunos.");
                }
            }
            case 6 ->{
                nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende exportar:",true);
                status = fsm.exportarAlunos(nome_fich);
                if(status){
                    System.out.println("Ficheiro de alunos exportado com sucesso.\n");
                    log.add("Gestão de Alunos -> Exportado com sucesso o ficheiro dos alunos.");
                }else{
                    System.out.println("Erro ao exportar o ficheiro.\n");
                    log.add("Gestão de Alunos -> Tentativa falhada ao exportar ficheiros dos alunos.");
                }
            }
            case 7 -> {
                log.add("Gestão de Alunos -> Retornado à fase 1.");
                fsm.proximaFase();
            }
        }
    }

    private void gestao_docen(){
        op = PAInput.chooseOption("Gestão de docentes | Opções","Adicionar","Editar","Consultar dados","Remover","Importar","Exportar","Voltar atrás");
        switch (op) {
            case 1, 2, 4 -> {
                System.out.println("Não implementado.\n");
            }
            case 3 ->{
                for(String sb: fsm.ConsultarDados("")){
                    System.out.println(sb);
                }
                log.add("Gestão de Docentes -> Consultado dados dos docentes.");
            }
            case 5 ->{
                nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende importar",true);
                status = fsm.importarDocentes(nome_fich);
                if(status){
                    System.out.println("Ficheiro de docentes importado com sucesso.\n");
                    log.add("Gestão de Docentes -> Ficheiro de docentes importado com sucesso.");
                }else{
                    System.out.println("Erro ao carregar o ficheiro.\n");
                    log.add("Gestão de Docentes -> Tentativa falhada ao importar ficheiros dos docentes.");
                }
            }
            case 6 -> {
                nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende exportar:", true);
                status = fsm.exportarDocentes(nome_fich);
                if (status) {
                    System.out.println("Ficheiro de docentes exportado com sucesso.\n");
                    log.add("Gestão de Docentes -> Exportado com sucesso o ficheiro dos docentes.");
                } else {
                    System.out.println("Erro ao exportar o ficheiro.\n");
                    log.add("Gestão de Docentes -> Tentativa falhada ao exportar ficheiros dos docentes.");
                }
            }
            case 7 -> {
                log.add("Gestão de Docentes -> Retornado à fase 1.");
                fsm.proximaFase();
            }
        }
    }

    private void gestao_prop(){
        op = PAInput.chooseOption("Gestão de propostas | Opções","Adicionar","Editar","Consultar dados","Remover","Importar","Exportar","Voltar atrás");
        switch (op) {
            case 1, 2, 4 -> {
                System.out.println("Não implementado.\n");
            }
            case 3 ->{
                for(String sb: fsm.ConsultarDados("")){
                    System.out.println(sb);
                }
                log.add("Gestão de Propostas -> Consultado dados das propostas.");
            }
            case 5 ->{
                nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende importar",true);
                status = fsm.importarPropostas(nome_fich);
                if(status){
                    System.out.println("Ficheiro de propostas importado com sucesso.\n");
                    log.add("Gestão de Propostas -> Ficheiro de propostas importado com sucesso.");
                }else{
                    System.out.println("Erro ao carregar o ficheiro.\n");
                    log.add("Gestão de Propostas -> Tentativa falhada ao importar ficheiros das propostas.");
                }
            }
            case 6 ->{
                nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende exportar:",true);
                status = fsm.exportarPropostas(nome_fich);
                if(status){
                    System.out.println("Ficheiro de propostas exportado com sucesso.\n");
                    log.add("Gestão de Propostas -> Exportado com sucesso o ficheiro das propostas.");
                }else{
                    System.out.println("Erro ao exportar o ficheiro.\n");
                    log.add("Gestão de Propostas -> Tentativa falhada ao exportar ficheiros das propostas.");
                }
            }
            case 7 -> {
                log.add("Gestão de Propostas -> Retornado à fase 1.");
                fsm.proximaFase();
            }
        }
    }

    private void segundafase(){
        op = PAInput.chooseOption("Opções disponíveis na Fase 2:","Gestão de Candidaturas","Consultar dados de candidaturas","Listas de Alunos c/ Filtros",
                "Listas de Propostas c/ Filtros","Importar estado", "Exportar estado","Proxima Fase","Fase Anterior","Fechar fase");
        switch (op){
            case 1 ->{
                log.add("Fase 2 -> Redirecionada para a área de gestão de candidaturas.");
                fsm.gestao("");
            }
            case 2->{
                System.out.println(fsm.ConsultarDados(null));
                log.add("Fase 2 -> Consultado dados das candidaturas.");
            }
            case 3 ->{
                op2 = PAInput.chooseOption("Listas de Alunos","Com autoproposta","Com candidatura já registada","Sem Candidatura registada");
                log.add("Fase 2 -> Listado alunos consoante os filtros escolhidos.");
                switch(op2){
                    case 1 ->{
                        for(String sb: fsm.ListasAlunosFiltros("com_autoproposta")){
                            System.out.println(sb);
                        }
                    }
                    case 2 ->{
                        for(String sb: fsm.ListasAlunosFiltros("com_candidatura")){
                            System.out.println(sb);
                        }
                    }
                    case 3 ->{
                        for(String sb: fsm.ListasAlunosFiltros("sem_candidatura")){
                            System.out.println(sb);
                        }
                    }
                }
            }
            case 4 ->{
                op2 = PAInput.chooseOption("Listas de Propostas","Autopropostas de alunos","Propostas de docentes","Propostas com candidaturas"
                        ,"Propostas sem candidatura");
                log.add("Fase 2 -> Listado propostas consoante os filtros escolhidos.");
                switch (op2){
                    case 1 ->{
                        for(String sb: fsm.ListasPropostasFiltros("auto_propostas",0)){
                            System.out.println(sb);
                        }
                    }
                    case 2 ->{
                        for(String sb: fsm.ListasPropostasFiltros("propostas_docentes",0)){
                            System.out.println(sb);
                        }
                    }
                    case 3 ->{
                        for(String sb: fsm.ListasPropostasFiltros("propostas_com_candidaturas",0)){
                            System.out.println(sb);
                        }
                    }
                    case 4->{
                        for(String sb: fsm.ListasPropostasFiltros("propostas_sem_candidaturas",0)){
                            System.out.println(sb);
                        }
                    }
                }
            }
            case 5 ->{
                load();
            }
            case 6 ->{
                save();
            }
            case 7 ->{
                log.add("Fase 2 -> Redirecionado à fase 1.");
                fsm.faseAnterior();
            }
            case 8 ->{
                log.add("Fase 2 -> Redirecionado para a fase 3.");
                fsm.proximaFase();
            }
            case 9 ->{
                if(fsm.fecharFase()){
                    System.out.println("Fase 2 fechada com sucesso.\n");
                    log.add("Fase 2 -> Fase 2 fechada com sucesso.");
                    fsm.proximaFase();
                }else{
                    System.out.println("Erro ao fechar a fase 2, verifique os requisitos necessários.\n");
                    log.add("Fase 2 -> Tentativa falhada ao fechar a fase 2.");
                }
            }
        }
    }

    private void gestao_candi(){
        op = PAInput.chooseOption("Gestão de Candidaturas | Opções","Adicionar","Editar","Remover","Importar","Exportar","Voltar atrás");
        switch (op){
            case 1, 2, 3 -> {
                System.out.println("Não implementado.\n");
            }
            case 4 ->{
                nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende importar",true);
                status = fsm.importarCandidaturas(nome_fich);
                if(status){
                    System.out.println("Ficheiro de candidaturas importado com sucesso.\n");
                    log.add("Gestão de Candidaturas -> Ficheiro de candidaturas importado com sucesso.");
                }else{
                    System.out.println("Erro ao carregar o ficheiro.\n");
                    log.add("Gestão de Candidaturas -> Erro ao carregar o ficheiro das candidaturas.");
                }
            }
            case 5 ->{
                nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende exportar:",true);
                status = fsm.exportarCandidaturas(nome_fich);
                if(status){
                    System.out.println("Ficheiro de candidaturas exportado com sucesso.\n");
                    log.add("Gestão de Candidaturas -> Ficheiro de candidaturas exportado com sucesso.");
                }else{
                    System.out.println("Erro ao exportar o ficheiro.\n");
                    log.add("Gestão de Candidaturas -> Erro ao exportar o ficheiro.");
                }
            }
            case 6 -> {
                log.add("Gestão de Candidaturas -> Retornado à fase 2.");
                fsm.proximaFase();
            }
        }
    }

    private void terceirafase(){
        op = PAInput.chooseOption("Opções disponíveis na Fase 3:","Atribuições de Propostas","Listas de Alunos c/ Filtros",
                "Listas de Propostas c/ Filtros","Exportar alunos para ficheiro .csv","Importar estado", "Exportar estado","Proxima Fase","Fase Anterior","Fechar fase");
        switch (op){
            case 1 ->{
                log.add("Fase 3 -> Escolhido a atribuição de propostas.");
                fsm.realiza_atribuicoes();
            }
            case 2 ->{
                op2 = PAInput.chooseOption("Listas de Alunos","Com autoproposta","Com candidatura já registada"
                        ,"Com proposta atribuida", "Não tem proposta atribuida");
                log.add("Fase 3 -> Listado alunos consoante os filtros escolhidos.");
                switch(op2){
                    case 1 ->{
                        for(String sb: fsm.ListasAlunosFiltros("com_autoproposta")){
                            System.out.println(sb);
                        }
                    }
                    case 2 ->{
                        for(String sb: fsm.ListasAlunosFiltros("com_candidatura")){
                            System.out.println(sb);
                        }
                    }
                    case 3 ->{
                        for(String sb: fsm.ListasAlunosFiltros("com_proposta")){
                            System.out.println(sb);
                        }
                    }
                    case 4 ->{
                        for(String sb: fsm.ListasAlunosFiltros("sem_proposta")){
                            System.out.println(sb);
                        }
                    }
                }
            }
            case 3 ->{
                op2 = PAInput.chooseOption("Listas de Propostas","Autopropostas de alunos","Propostas de docentes","Propostas disponiveis"
                        ,"Propostas atribuidas");
                log.add("Fase 3 -> Listado propostas consoante os filtros escolhidos.");
                switch (op2){
                    case 1 ->{
                        for(String sb: fsm.ListasPropostasFiltros("auto_propostas",0)){
                            System.out.println(sb);
                        }
                    }
                    case 2 ->{
                        for(String sb: fsm.ListasPropostasFiltros("propostas_docentes",0)){
                            System.out.println(sb);
                        }
                    }
                    case 3 ->{
                        for(String sb: fsm.ListasPropostasFiltros("propostas_disponiveis",0)){
                            System.out.println(sb);
                        }
                    }
                    case 4->{
                        for(String sb: fsm.ListasPropostasFiltros("propostas_atribuidas",0)){
                            System.out.println(sb);
                        }
                    }
                }
            }
            case 4 ->{
                nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende exportar",true);
                status = fsm.exportarAlunosAvancado(nome_fich);
                if(status){
                    System.out.println("Ficheiro de alunos exportado com sucesso.\n");
                    log.add("Fase 3 -> Ficheiro de alunos exportado com sucesso.");
                }else{
                    System.out.println("Erro ao carregar o ficheiro.\n");
                    log.add("Fase 3 -> Tentativa falhada ao exportar o ficheiro dos alunos.");
                }
            }
            case 5 ->{
                load();
            }
            case 6 ->{
                save();
            }
            case 7 ->{
                log.add("Fase 3 -> Redirecionado para a fase 4.");
                fsm.proximaFase();
            }
            case 8 ->{
                log.add("Fase 3 -> Redirecionado à fase 2.");
                fsm.faseAnterior();
            }
            case 9 ->{
                if(fsm.fecharFase()){
                    System.out.println("Fase 3 fechada com sucesso.\n");
                    log.add("Fase 3 -> Fase 3 fechada com sucesso.");
                    fsm.proximaFase();
                }else{
                    System.out.println("Erro ao fechar a fase 3, verifique os requisitos necessários.\n");
                    log.add("Fase 3 -> Tentativa falhada ao fechar a fase 3.");
                }
            }
        }
    }

    private void realiza_atrib(){
        op = PAInput.chooseOption("Opções de Atribuições:","Atribuições Automáticas das Autopropostas",
                "Atribuições Automáticas de proposta disponível aos alunos disponiveis", "Atribuição Manual","Remover uma atribuição"
                ,"Remover todas as atribuições (excepto auto-propostas)","Voltar atrás");
        switch (op){
            case 1 ->{
                if(fsm.AtribuicaoAutomAluno()){
                    System.out.println("Atribuições realizadas com sucesso.\n");
                    log.add("Atribuições de Propostas -> Atribuições automáticas ao aluno por autoproposta realizadas com sucesso.");
                }else{
                    System.out.println("Erro ao atribuir autopropostas.\n");
                    log.add("Atribuições de Propostas -> Tentativa falhada ao atribuir autopropostas.");
                }
            }
            case 2 ->{
                while(fsm.AtribuicaoAutomDocentesAoAluno() != null){
                    for(String a : fsm.AtribuicaoAutomDocentesAoAluno()) {
                        System.out.println(a);
                    }
                    String id = PAInput.readString("Introduza o id da proposta:",true);
                    String n_aluno = PAInput.readString("Introduza o número do aluno:",true);
                    if(fsm.AtribuicaoManual(id,n_aluno)){
                        System.out.println("Atribuicao Manual atribuída com sucesso.\n");
                        log.add("Atribuições de Propostas -> Atribuição manual realizada com sucesso após empate.");
                    }else{
                        System.out.println("Erro ao atribuir manualmente.\n");
                        log.add("Atribuições de Propostas -> Erro ao atribuir manualmente após empate.");
                    }
                }
                System.out.println("Atribuições automáticas realizadas com sucesso");
                log.add("Atribuições de Propostas -> Atribuições automáticas realizadas com sucesso.");
            }
            case 3 ->{
                for(String a : fsm.getPropostaseAlunosDisponiveis()) {
                    System.out.println(a);
                }
                String id = PAInput.readString("Introduza o id da proposta:",true);
                String n_aluno = PAInput.readString("Introduza o número do aluno:",true);
                if(fsm.AtribuicaoManual(id,n_aluno)){
                    System.out.println("Atribuicao Manual atribuída com sucesso.\n");
                    log.add("Atribuições de Propostas -> Atribuicao Manual atribuída com sucesso.");
                }else{
                    System.out.println("Erro ao atribuir manualmente.\n");
                    log.add("Atribuições de Propostas -> Tentativa falhada ao atribuir manualmente.");
                }
            }
            case 4 ->{
                //Mostrar todos os alunos atribuidos sem propostas Iniciais
                for(String sb : fsm.getAlunosAtribuidosSemPropostasIniciais()) {
                    System.out.println(sb);
                }
                String id = PAInput.readString("Introduza o id da proposta:",true);
                String n_aluno = PAInput.readString("Introduza o número do aluno que pretende remover:",true);
                if(fsm.RemocaoManualAtribuicao(id,n_aluno)){
                    System.out.println("Atribuicao removida com sucesso.\n");
                    log.add("Atribuições de Propostas -> Atribuição removida manualmente realizada com sucesso.");
                }else{
                    System.out.println("Erro ao remover a atribuição.\n");
                    log.add("Atribuições de Propostas -> Erro ao remover atribuição manualmente.");
                }
            }
            case 5 ->{
                if(fsm.RemocaoManualTodasAtribuicoes()){
                    System.out.println("As atribuições possíveis foram removidas com sucesso.\n");
                    log.add("Atribuições de Propostas -> As atribuições possíveis foram removidas com sucesso.");
                }else{
                    System.out.println("Erro ao remover as atribuições.");
                    log.add("Atribuições de Propostas -> Tentativa falhada ao remover as atribuições.");
                }
            }
            case 6 ->{
                log.add("Atribuições de Propostas -> Retornado para a fase 3.");
                fsm.proximaFase();
            }
        }
    }

    private void quartafase(){
        op = PAInput.chooseOption("Opções disponíveis na Fase 4:","Associação automática dos docentes proponentes","Gestao de Orientadores",
                "Obter dados","Exportar alunos para ficheiro .csv","Importar estado", "Exportar estado","Fase Anterior","Fechar fase");
        switch (op){
            case 1 ->{
                if(fsm.AssociacaoAutomDocentes()){
                    System.out.println("Associações criadas com sucesso.\n");
                    log.add("Fase 4 -> Associações de orientadores automáticas atribuidas com sucesso.");
                }else{
                    System.out.println("Erro ao associar orientadores.\n");
                    log.add("Fase 4 -> Tentativa falhada ao associar orientadores automaticamente.");
                }
            }
            case 2 ->{
                log.add("Fase 4 -> Redirecionado para a Gestão de orientadores.");
                fsm.gestao("");
            }
            case 3 ->{
                op2 = PAInput.chooseOption("Escolha a opção pretendida:","Lista de Alunos c/ proposta atribuida e com orientador",
                        "Lista de Alunos c/ proposta atribuida mas sem orientador","Informações Orientadores","Informações Orientador em especifico");
                log.add("Fase 4 -> Listado alunos consoante os filtros escolhidos.");
                switch (op2){
                    case 1 ->{
                        for(String sb: fsm.ListasAlunosFiltros("com_proposta_e_orientador")){
                            System.out.println(sb);
                        }
                    }
                    case 2 ->{
                        for(String sb: fsm.ListasAlunosFiltros("com_proposta_sem_orientador")){
                            System.out.println(sb);
                        }
                    }
                    case 3 ->{
                        for(String sb: fsm.ListasOrientadoresFiltros()){
                            System.out.println(sb);
                        }
                    }
                    case 4 ->{
                        String email = PAInput.readString("Introduza o email do orientador:",true);
                        for(String sb: fsm.ListasOrientadorEspecifico(email)){
                            System.out.println(sb);
                        }
                    }
                }
            }
            case 4 ->{
                nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende exportar",true);
                status = fsm.exportarAlunosAvancado(nome_fich);
                if(status){
                    System.out.println("Ficheiro de alunos exportado com sucesso.\n");
                    log.add("Fase 4 -> Ficheiro de alunos exportado com sucesso.");
                }else{
                    System.out.println("Erro ao carregar o ficheiro.\n");
                    log.add("Fase 4 -> Tentativa falhado ao exportar o ficheiro dos alunos.");
                }
            }
            case 5 ->{
                load();
            }
            case 6 ->{
                save();
            }
            case 7 ->{
                log.add("Fase 4 -> Redirecionado para a fase 3.");
                fsm.faseAnterior();
            }
            case 8 ->{
                if(fsm.fecharFase()){
                    System.out.println("Fase 4 fechada com sucesso.\n");
                    log.add("Fase 4 -> Fase 4 fechada com sucesso.");
                    fsm.proximaFase();
                }else{
                    System.out.println("Erro ao fechar a fase 4, verifique os requisitos necessários.\n");
                    log.add("Fase 4 -> Tentativa falhada ao fechar a fase 4.");
                }
            }

        }
    }

    private void gestao_orient(){
        op = PAInput.chooseOption("Gestão Orientadores:","Atribuir orientador","Consultar orientador",
                "Alterar orientador","Eliminar orientador","Voltar atrás");
        switch (op){
            case 1 ->{
                String email = PAInput.readString("Introduza o email do docente:",true);
                String n_aluno = PAInput.readString("Introduza o número do aluno que pretende atribuir:",true);
                if(fsm.atribuirOrientador(email,n_aluno)){
                    System.out.println("Orientador atribuido com sucesso.\n");
                    log.add("Gestão de Orientadores -> Orientador atribuido com sucesso.");
                }else{
                    System.out.println("Erro ao atribuir orientador.\n");
                    log.add("Gestão de Orientadores -> Tentativa falhada ao atribuir orientador.");
                }
            }
            case 2 ->{
                String email = PAInput.readString("Introduza o email do docente:",true);
                System.out.println(fsm.consultaOrientador(email));
                log.add("Gestão de Orientadores -> Consultado dados do orientador.");
            }
            case 3 ->{
                String email_atual = PAInput.readString("Introduza o email do orientador atual:",true);
                String email_seguinte = PAInput.readString("Introduza o email do orientador que pretende:",true);
                String n_aluno = PAInput.readString("Introduza o número do aluno em questão:",true);
                if(fsm.alteracaoOrientador(email_atual,email_seguinte,n_aluno)){
                    System.out.println("Alteração realizada com sucesso.\n");
                    log.add("Gestão de Orientadores -> Alteração de orientador realizada com sucesso.");
                }else{
                    System.out.println("Erro ao realizar a alteração.\n");
                    log.add("Gestão de Orientadores -> Tentativa falhada ao alterar orientador.");
                }
            }
            case 4 -> {
                String email = PAInput.readString("Introduza o email do orientador que pretende remover:",true);
                if(fsm.eliminarOrientador(email)){
                    System.out.println("Orientador removido com sucesso.\n");
                    log.add("Gestão de Orientadores -> Orientador removido com sucesso.");
                }else{
                    System.out.println("Erro ao eliminar orientador.\n");
                    log.add("Gestão de Orientadores -> Tentativa falhado ao eliminar orientador.");
                }
            }
            case 5 ->{
                log.add("Gestão de Orientadores -> Retornado à fase 4.");
                fsm.proximaFase();
            }
        }
    }

    private void quintafase(){
        op = PAInput.chooseOption("Opções disponíveis na Fase 5:","Listas de alunos com propostas atribuidas"
                ,"Listas de alunos sem propostas atribuidas e com opções de candidatura",
                "Conjunto de Propostas disponíveis","Conjunto de Propostas atribuidas",
                "Informações Orientadores","Informações Orientador em especifico","Dados adicionais","Exportar alunos para ficheiro .csv","Importar estado"
                ,"Exportar estado","Terminar");
        switch (op){
            case 1->{
                for(String sb: fsm.ListasAlunosFiltros("com_proposta")){
                    System.out.println(sb);
                }
                log.add("Fase 5 -> Listado alunos com propostas atribuidas.");
            }
            case 2->{
                for(String sb: fsm.ListasAlunosFiltros("sem_proposta_com_candidatura")){
                    System.out.println(sb);
                }
                log.add("Fase 5 -> Listado alunos sem propostas atribuidas e com opções de candidatura");
            }
            case 3 ->{
                for(String sb: fsm.ListasPropostasFiltros("propostas_disponiveis",0)){
                    System.out.println(sb);
                }
                log.add("Fase 5 -> Listado conjunto de propostas disponiveis.");
            }
            case 4 ->{
                for(String sb: fsm.ListasPropostasFiltros("propostas_atribuidas",0)){
                    System.out.println(sb);
                }
                log.add("Fase 5 -> Listado conjunto de propostas atribuidas.");
            }
            case 5 ->{
                for(String sb: fsm.ListasOrientadoresFiltros()){
                    System.out.println(sb);
                }
                log.add("Fase 5 -> Listado dados dos orientadores.");
            }
            case 6 ->{
                String email = PAInput.readString("Introduza o email do orientador:",true);
                for(String sb: fsm.ListasOrientadorEspecifico(email)){
                    System.out.println(sb);
                }
                log.add("Fase 5 -> Listado um orientador em especifico pelo seu email.");
            }
            case 7 ->{
                System.out.println(fsm.DadosFinais());
                log.add("Fase 5 -> Listado dados finais.");
            }
            case 8 ->{
                nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende exportar",true);
                status = fsm.exportarAlunosAvancado(nome_fich);
                if(status){
                    System.out.println("Ficheiro de alunos exportado com sucesso.\n");
                    log.add("Fase 5 -> Ficheiro de alunos exportado com sucesso.");
                }else{
                    System.out.println("Erro ao carregar o ficheiro.\n");
                    log.add("Fase 5 -> Tentativa falhada ao exportar ficheiro dos alunos.");
                }
            }
            case 9 ->{
                load();
            }
            case 10 ->{
                save();
            }
            case 11 ->{
                log.add("Fase 5 -> Aplicação terminada com sucesso.");
                finish = true;
            }
        }
    }

    private void save(){
        String nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende guardar",true);
        try{
            if(fsm.save(fsm,nome_fich)){
                System.out.println("Estado gravado com sucesso");
                log.add("Estado da aplicação gravado com sucesso.");
            }
        }catch (IOException e){
            System.out.println("Erro ao guardar o estado: " + e);
            log.add("Tentativa falhado ao guardar o estado da aplicação.");
        }
    }

    private void load(){
        String nome_fich = PAInput.readString("Introduza o nome do ficheiro que pretende importar",true);
        try{
            fsm = (GestaoManager) fsm.load(nome_fich);
            log.add("Carregamento de dados da aplicação efetuado com sucesso.");
        }catch (ClassNotFoundException | IOException e){
            System.out.println("Erro ao importar o estado: " + e);
            log.add("Tentativa falhada ao importar o estado da aplicação.");
        }
    }

    private void showInfo(){
        System.out.println("\nGestão de Projetos e Estágios do DEIS\n");
        System.out.println("\t\tModo: " + (fsm.getCurrentMode() == null ? " " : fsm.getCurrentMode()));
        System.out.println("\t\tEstado Fase1: " + (fsm.getFase1Fechada() ? "FECHADA" : "ABERTA"));
        System.out.println("\t\tEstado Fase2: " + (fsm.getFase2Fechada() ? "FECHADA" : "ABERTA"));
        System.out.println("\t\tEstado Fase3: " + (fsm.getFase3Fechada() ? "FECHADA" : "ABERTA"));
        System.out.println("\t\tEstado Fase4: " + (fsm.getFase4Fechada() ? "FECHADA" : "ABERTA"));
        System.out.println("\t\tEstado Fase5: " + (fsm.getFase5Fechada() ? "FECHADA" : "ABERTA"));
    }
}
