import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class ContadorCaracteres {
    public static void main(String[] args) {
        long inicioExecucao = System.currentTimeMillis();

        File diretorio = new File("/home/teusma/Downloads/todosArquivos");
        int numThreads = 8;

        if (diretorio.exists()) {
            File[] arquivos = diretorio.listFiles();
            int arquivosPorThread = arquivos.length / numThreads;

            Thread[] threads = new Thread[numThreads];
            for (int i = 0; i < numThreads; i++) {
                int inicio = i * arquivosPorThread;
                int fim = (i == numThreads - 1) ? arquivos.length : (i + 1) * arquivosPorThread;

                threads[i] = new Thread(new InicializaThread(arquivos, inicio, fim));
                threads[i].start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (Character character : InicializaThread.contagemCaracteres.keySet()) {
                System.out.println(character + ": " + InicializaThread.contagemCaracteres.get(character));
            }

            long fimExecucao = System.currentTimeMillis();
            System.out.println("Tempo de execução: " + (fimExecucao - inicioExecucao) + " milissegundos");
        }
    }
}

class InicializaThread extends Thread {
    private File[] arquivos;
    private int indiceInicial;
    private int indiceFinal;
    public static Map<Character, Integer> contagemCaracteres = new HashMap<>();

    public InicializaThread(File[] arquivos, int indiceInicial, int indiceFinal) {
        this.arquivos = arquivos;
        this.indiceInicial = indiceInicial;
        this.indiceFinal = indiceFinal;
    }

    public void run() {
        for (int i = indiceInicial; i < indiceFinal; i++) {
            contarCaracteresEmArquivo(arquivos[i]);
        }
    }

    public void contarCaracteresEmArquivo(File arquivo) {
        try (FileReader leitor = new FileReader(arquivo)) {
            int dado;
            while ((dado = leitor.read()) != -1) {
                char caracter = (char) dado;
                if (Character.isLetter(caracter)) {
                    synchronized (InicializaThread.class) {
                        contagemCaracteres.put(caracter, contagemCaracteres.getOrDefault(caracter, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
