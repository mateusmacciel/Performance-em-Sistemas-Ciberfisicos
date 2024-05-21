import java.util.ArrayList;
import java.util.Random;

class Produtor extends Thread{
    private ArrayList<Integer> pilha;

    private int id;

    public Produtor(ArrayList<Integer> pilha, int id){
        this.pilha = pilha;
        this.id = id;
    }

    public void run(){
        Random random = new Random();
        while(true){
            int valor = random.nextInt(1000);
            synchronized(pilha){
                if(this.pilha.size() == 10) {
                    System.out.println("Pilha cheia.");
                    try {
                        pilha.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    this.pilha.add(valor);
                    System.out.println("Produtor " + this.id + " adicionou: " + valor);
                    System.out.println(pilha);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }
    }
}

class Consumidor extends Thread{
    private ArrayList<Integer> pilha;

    private int id;

    public Consumidor(ArrayList<Integer> pilha, int id){
        this.pilha = pilha;
        this.id = id;
    }

    public void run(){
        while(true){
            synchronized(pilha){
                if(this.pilha.size() == 0){
                    System.out.println("Pilha vazia.");
                    try {
                        pilha.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    int valor = this.pilha.remove(this.pilha.size() - 1);
                    System.out.println("Consumidor " + this.id + " removeu: " + valor);
                    System.out.println(pilha);
                    pilha.notify();
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}



public class Main {
    public static void main (String[] args){
        ArrayList<Integer> pilha = new ArrayList<Integer>();

        Produtor p1 = new Produtor(pilha, 1);
        Produtor p2 = new Produtor(pilha, 2);
        Produtor p3 = new Produtor(pilha, 3);

        Consumidor c1 = new Consumidor(pilha, 1);
        Consumidor c2 = new Consumidor(pilha, 2);


        p1.start();
        p2.start();
        p3.start();

        c1.start();
        c2.start();
    }
}
