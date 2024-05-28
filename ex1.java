class Main {
    public static void main(String[] args) {
        Contador contador = new Contador(0);

        Thread t1 = new minhaThread(contador);
        Thread t2 = new minhaThread(contador);
        Thread t3 = new minhaThread(contador);
        Thread t4 = new minhaThread(contador);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(contador.getValor());
    }
}

class Contador {
    private int valor;

    public Contador(int valor){
        this.valor = valor;
    }

    public synchronized void incrementar(){
        valor++;
    }

    public synchronized void decrementa(){
        valor--;
    }

    public int getValor(){
        return valor;
    }
}

class minhaThread extends Thread{
    private Contador contador;

    public minhaThread(Contador contador){
        this.contador = contador;
    }

    public void run() {
        try {
            for(int i = 0; i < 100; i++){
                contador.incrementar();
                Thread.sleep(100);
            }

            for (int i = 0; i < 100; i++){
                contador.decrementa();
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
