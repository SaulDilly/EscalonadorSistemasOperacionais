import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Escalonador {
    private FilaRoundRobin fila1;
    private FilaRoundRobin fila2;
    private FilaFCFS fila3;

    private List<Processo> bloqueados; 
    private List<Processo> todos;

    public Escalonador (List<Processo> lista, int quantumFila1, int quantumFila2) {
        this.fila1 = new FilaRoundRobin(quantumFila1, 1);
        this.fila2 = new FilaRoundRobin(quantumFila2, 2);
        this.fila3 = new FilaFCFS();
        this.bloqueados = new ArrayList<>();
        this.todos = lista;
        
        // Inicializa o surto restante para os processos que vieram do JSON
        for (Processo p : this.todos) {
            p.inicializarSurtoRestante();
        }

        escalonaProcessos();
    }

    public void simular() {

        int tempoTotal = 0;

        while (true) {
            // Fila da iteração
            Fila filaIt = getProximaFila();

            // Se não tem nenhum processo em nenhuma fila
            if (filaIt == null) {
                // E não tem mais processos bloqueados, a simulação terminou
                if (bloqueados.isEmpty()) {
                    System.out.println("--------------------");
                    System.out.println("A simulacao terminou em " + tempoTotal + "ms!");
                    break;
                } else { // Se tem processos bloqueados, desbloqueia o primeiro para executar
                    // Retorna o processo com menor tempo de bloqueio
                    Processo procMenorTempoBloqueado = Collections.min(bloqueados, Comparator.comparingInt(Processo::getTempoBloqueado));
                    int menor = procMenorTempoBloqueado.getTempoBloqueado();
                    // Atualiza a lista de bloqueados, removendo aqueles que já cumpriram seu tempo de bloqueio, inserindo na respectiva lista
                    atualizaBloqueados(menor);
                    continue;
                }
            } 
            
            Processo p = filaIt.proximo();
            int quantum = filaIt.getQuantum();
            
            System.out.println("--------------------");
            System.out.println("Executando processo " + p.getNome() + " na fila " + p.getUltimaFila().getIndex());

            // Salva estado
            p.setEstado(Estado.EXECUTANDO);

            // Tempo que será consumido nessa iteração
            int slice = 0;
            // Indica se executou o tempo total do quantum (deve saltar de fila)
            boolean execQuantum = false; 
            // Indica se executou o tempo de CPU burst (deve ser inserido na lista de bloqueados)
            boolean execCPUBurst = false; 

            // Se é quantum negativo, está executando processo da terceira fila
            if (quantum == -1) {
                // Se não tem tempo de E/S, pode executar até o fim
                // Se tem, executa o menor entre o tempo total e o CPU burst
                slice = p.getTempoES() == 0 ? p.getTempoTotal() : p.getSurtoCPUrestante();
            } else {
                // Verifica o menor tempo entre o quantum, tempo total do processo e surto de CPU
                slice = Math.min(quantum, p.getTempoTotal());
                // Só contabiliza o surto se possuir E/S
                if (p.getTempoES() != 0) slice = Math.min (slice, p.getSurtoCPUrestante());
                if (slice == p.getSurtoCPUrestante()) execCPUBurst = true;
                if (slice == quantum) execQuantum = true;
            }

            // Desconta o slice do tempo total do processo
            p.setTempoTotal(p.getTempoTotal() - slice);
            
            System.out.println("Executei por " + slice + "ms!");

            // Acrescenta o slice ao tempo de simulação
            tempoTotal += slice;

            System.out.println("Tempo total: " + tempoTotal + "ms.");

            // Atualiza tempo de bloqueio dos bloqueados
            atualizaBloqueados(slice);

            // Se o processo já executou completamente, pode seguir para a próxima iteração (não insere de volta no sistema)
            if (p.getTempoTotal() == 0) {
                System.out.println("Processo finalizou.");
                p.setEstado(Estado.FINALIZADO);
                continue;
            } 

            // Se possui tempo de E/S
            if (p.getTempoES() != 0) {
                // Executou CPUBurst, deve bloquear
                if (execCPUBurst) {
                    System.out.println("Processo executou CPU burst e possui E/S. Ficara bloqueado.");
                    p.setEstado(Estado.BLOQUEADO);
                    bloqueados.add(p);
                    continue;
                } else { // Não executou todo CPUBurst
                    p.setSurtoCPUrestante(p.getSurtoCPUrestante() - slice);
                    System.out.println("Processo ainda possui " + p.getSurtoCPUrestante() + " de surto de CPU restante.");
                }
            }

            p.setEstado(Estado.PRONTO);

            // Se executou o tamanho do quantum, passa o processo para a próxima fila
            if (execQuantum) {
                if (((FilaRoundRobin) filaIt).getIndex() == 1) fila2.adicionar(p); 
                else fila3.adicionar(p);
                System.out.println("Mudou para a fila " + p.getUltimaFila().getIndex());
                continue;
            }
        }
    }

    private Fila getProximaFila() {
        if (!fila1.vazia()) return fila1;
        if (!fila2.vazia()) return fila2;
        if (!fila3.vazia()) return fila3;
        return null;
    }

    private void atualizaBloqueados(int slice) {
        List<Processo> remBloqueados = new ArrayList<>();

        for (Processo proc : bloqueados) {
            // Remove o slice de tempo do tempo de bloqueio do processo
            proc.setTempoBloqueado(proc.getTempoBloqueado() - slice);
            
            // Se já passou tempo de E/S do processo, insere de volta na fila em que estava
            if (proc.getTempoBloqueado() <= 0) {
                System.out.println("Processo " + proc.getNome() + " estava bloqueado e foi reinserido na fila " + proc.getUltimaFila().getIndex() + ".");
                proc.setEstado(Estado.PRONTO);
                proc.getUltimaFila().adicionar(proc);
                proc.setSurtoCPUrestante(proc.getSurtoCPU());
                // Não pode remover dentro do forEach da própria collection
                remBloqueados.add(proc);
            }
        }
        // Remove de fato da lista de bloqueados
        remBloqueados.forEach(p -> bloqueados.remove(p));
    }

    private void escalonaProcessos() {
        // Ordena pela prioridade antes de colocar na fila
        todos.sort(Comparator.comparingInt(Processo::getPrioridade));
        todos.forEach(p -> fila1.adicionar(p));
    }
}
