import java.io.FileReader;
import java.io.Reader;
import com.google.gson.Gson;

public class App {
    public static void main(String[] args) throws Exception {
        SimulacaoConfig config = carregarConfiguracao("EscalonadorSistemasOperacionais/config.json");
        
        if (config != null) {
            Escalonador e = new Escalonador(
                config.getProcessos(), 
                config.getConfiguracao().getQuantumFila1(), 
                config.getConfiguracao().getQuantumFila2()
            );
            e.simular();
        } else {
            System.out.println("Erro ao carregar o arquivo de configuracao.");
        }
    }

    private static SimulacaoConfig carregarConfiguracao(String caminhoArquivo) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(caminhoArquivo)) {
            return gson.fromJson(reader, SimulacaoConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
