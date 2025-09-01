public class Processo {
    private String nome;
    private int surtoCPU;
    private int tempoES;
    private int tempoTotal;
    private int prioridade;
    private Estado estado;
    private Fila ultimaFila;

    private int tempoBloqueado;
    private int surtoCPUrestante;

    public Processo(String nome, int surtoCPU, int tempoES, int tempoTotal, int prioridade) {
        this.nome = nome;
        this.surtoCPU = surtoCPU;
        this.tempoES = tempoES;
        this.tempoTotal = tempoTotal;
        this.prioridade = prioridade;
        this.estado = Estado.PRONTO;
        this.tempoBloqueado = 0;
        this.ultimaFila = null;
        this.surtoCPUrestante = surtoCPU;
    }
    
    public void inicializarSurtoRestante() {
        this.surtoCPUrestante = this.surtoCPU;
    }

    public Fila getUltimaFila() {
        return ultimaFila;
    }

    public int getSurtoCPUrestante() {
        return surtoCPUrestante;
    }

    public void setSurtoCPUrestante(int surtoCPUrestante) {
        this.surtoCPUrestante = surtoCPUrestante;
    }

    public void setUltimaFila(Fila ultimaFila) {
        this.ultimaFila = ultimaFila;
    }

    public int getTempoBloqueado() {
        return tempoBloqueado;
    }

    public void setTempoBloqueado(int tempoBloqueado) {
        this.tempoBloqueado = tempoBloqueado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getSurtoCPU() {
        return surtoCPU;
    }

    public void setSurtoCPU(int surtoCPU) {
        this.surtoCPU = surtoCPU;
    }

    public Estado getEstado() {
        return estado;
    }

    public int getTempoES() {
        return tempoES;
    }

    public void setTempoES(int tempoES) {
        this.tempoES = tempoES;
    }

    public int getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(int tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
