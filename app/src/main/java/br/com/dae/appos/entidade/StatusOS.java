package br.com.dae.appos.entidade;

public enum StatusOS {

    FAZER_ORCAMENTO("Fazer Orçamento"), // Para o processo inicial;
    REALIZAR_SERVICO("Realizar serviço"), // Quando este já foi fechado e o serviço será realizado
    EM_ANDAMENTO("Em andamento"), // Quando o processo está em andamento
    CONCLUIDO("Concluído"); // Quando o serviço já foi realizado e a ordem será finalizada


    private StatusOS(String descricao) {
        this.descrisao = descricao;
    }

    private String descrisao;

    public String getDescrisao() {
        return descrisao;
    }

    public static StatusOS getStatusOS(int pos) {
        for (StatusOS statusOS : StatusOS.values()) {
            if (statusOS.ordinal() == pos) {
                return statusOS;
            }
        }
        return null;
    }

}
