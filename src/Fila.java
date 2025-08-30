public interface Fila {
    
    public void adicionar(Processo p);

    public Processo proximo();

    public boolean vazia();

    public int getQuantum();

    public int getIndex();
}
