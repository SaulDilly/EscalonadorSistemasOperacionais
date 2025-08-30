import java.util.LinkedList;
import java.util.Queue;

public class FilaFCFS implements Fila {
    private Queue<Processo> fila;
    
    public FilaFCFS() {
        fila = new LinkedList<>();
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
        return -1;
    }

    @Override
    public int getIndex() {
        return 3;
    }
}
