package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Gestao;

public enum GestaoState {
    //Estados
    PRIMEIRA_FASE, GESTAO_ALUNOS, GESTAO_DOCENTES,GESTAO_PROPOSTAS,SEGUNDA_FASE,GESTAO_CANDIDATURAS,TERCEIRA_FASE,REALIZA_ATRIBUICOES, QUARTA_FASE, GESTAO_ORIENTADORES,QUINTA_FASE;


    public IGestaoState createState(GestaoContext context, Gestao data) {
        return switch (this) {
            case PRIMEIRA_FASE -> new PrimeiraFaseState(context,data);
            case GESTAO_ALUNOS -> new GestaoAlunosState(context,data);
            case GESTAO_DOCENTES -> new GestaoDocentesState(context,data);
            case GESTAO_PROPOSTAS -> new GestaoPropostasState(context,data);
            case SEGUNDA_FASE -> new SegundaFaseState(context,data);
            case GESTAO_CANDIDATURAS -> new GestaoCandidaturasState(context,data);
            case TERCEIRA_FASE -> new TerceiraFaseState(context,data);
            case REALIZA_ATRIBUICOES -> new RealizaAtribuicoesState(context,data);
            case QUARTA_FASE -> new QuartaFaseState(context,data);
            case GESTAO_ORIENTADORES -> new GestaoOrientadoresState(context,data);
            case QUINTA_FASE -> new QuintaFaseState(context,data);
            default -> null;
        };
    }
}
