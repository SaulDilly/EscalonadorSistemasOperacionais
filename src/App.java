import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        Escalonador e = new Escalonador(getListaProcessos());
        e.simular();
    }

    public static List<Processo> getListaProcessos() {
        Processo p1 = new Processo("P1", 15, 3, 40, 1);
        Processo p2 = new Processo("P2", 0, 0, 70, 2);
        Processo p3 = new Processo("P3", 8, 2, 35, 3);

        List<Processo> lista = new ArrayList<>();

        lista.add(p1);
        lista.add(p2);
        lista.add(p3);

        return lista;
    }
}
