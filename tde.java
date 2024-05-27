import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

class Biblioteca {
    protected Semaphore[] livros;

    public Biblioteca(int numLivros) {
        livros = new Semaphore[numLivros];
        for (int i = 0; i < numLivros; i++) {
            livros[i] = new Semaphore(1);
        }
    }

    public void acquire(int userId, int livroId) throws InterruptedException {
        livros[livroId].acquire();
        System.out.println("Usuário " + userId + " – Emprestou livro " + livroId);
    }

    public boolean tryAcquire(int userId, int livroId) {
        boolean retorno = livros[livroId].tryAcquire();
        if (retorno) {
            System.out.println("Usuário " + userId + " – Emprestou livro " + livroId);
        } else {
            System.out.println("Usuário " + userId + " – Esperando livro " + livroId + " ficar disponível");
        }
        return retorno;
    }

    public void release(int userId, int livroId) {
        livros[livroId].release();
        System.out.println("Usuário " + userId + " – Devolveu livro " + livroId);
    }
}

class Usuario extends Thread {
    protected int userId;
    protected Biblioteca biblioteca;

    public Usuario(int userId, Biblioteca biblioteca) {
        this.userId = userId;
        this.biblioteca = biblioteca;
    }

    public void run() {
        while (true) {
            int livroId = ThreadLocalRandom.current().nextInt(biblioteca.livros.length);
            try {
                if (biblioteca.tryAcquire(userId, livroId) == false) {
                    biblioteca.acquire(userId, livroId);
                }
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
                biblioteca.release(userId, livroId);
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
            } catch (InterruptedException e) {
                System.out.println("Erro.");
            }
        }
    }
}

class Main {
    public static void main(String[] args) {
        int numLivros = 10;
        int numUsuarios = 3;

        Biblioteca biblioteca = new Biblioteca(numLivros);
        Usuario[] usuarios = new Usuario[numUsuarios];

        for (int i = 0; i < numUsuarios; i++) {
            usuarios[i] = new Usuario(i + 1, biblioteca);
            usuarios[i].start();
        }
    }
}
