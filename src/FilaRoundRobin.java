import java.util.LinkedList;
import java.util.Queue;

public class FilaRoundRobin implements Fila{
    private Queue<Processo> fila;
    private int quantum;
    private int index;

    public FilaRoundRobin(int quantum, int index) {
        fila = new LinkedList<>();
        this.quantum = quantum;
        this.index = index;
    }
    
    @Override
    public void adicionar(Processo p) {
        p.setUltimaFila(this);
        fila.add(p);
    }

    @Override
    public Processo proximo() {
        return fila.poll();
    }

    @Override
    public boolean vazia() {
        return fila.isEmpty();
    }

    @Override
    public int getQuantum() {
        return quantum;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
