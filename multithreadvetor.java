import java.util.Random;
import java.util.Scanner;

class InicializaThread extends Thread {
    private Double[] vetor;
    private int inicio;
    private int fim;
    private int Contagem;

    public InicializaThread(Double[] vetor, int inicio, int fim) {
        this.vetor = vetor;
        this.inicio = inicio;
        this.fim = fim;
        this.Contagem = 0;
    }

    public void run() {
        Random rand = new Random();
        for (int i = inicio; i < fim; i++) {
            this.vetor[i] = rand.nextDouble(1);
        }
    }

    public int getContagem() {
        return this.Contagem;
    }
}

class ContagemVet {
    public static int contagem(Double[] vetor) {
        int contagem = 0;
        for (Double value : vetor) {
            if (value > 0.25 && value < 0.75) {
                contagem++;
            }
        }
        return contagem;
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Números de Threads (1 a 4): ");
        int numeroThreads = scanner.nextInt();

        Double[] vetor = new Double[200_000_000];
        int intervaloThread = 200_000_000 / numeroThreads;
        InicializaThread[] threads = new InicializaThread[numeroThreads];

        for (int i = 0; i < numeroThreads; i++) {
            int inicio = i * intervaloThread;
            int fim = (i + 1) * intervaloThread;
            threads[i] = new InicializaThread(vetor, inicio, fim);
            threads[i].start();
        }

        for (InicializaThread thread : threads) {
            thread.join();
        }

        System.out.println("Encerrou.");

        int contagem = ContagemVet.contagem(vetor);
        System.out.println("Quantidade de valores verificados entre 0.25 e 0.75: " + contagem);
    }
}
