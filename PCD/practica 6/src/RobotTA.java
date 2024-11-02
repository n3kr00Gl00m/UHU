import java.util.Random;
import java.util.concurrent.Semaphore;

public class RobotTA extends Thread{
    private final Semaphore nBlanco, cBlanco;
    private final Semaphore nRojo, cRojo;

    private String id;
    private Random rd;

    public RobotTA(Semaphore nBlanco, Semaphore cBlanco, Semaphore nRojo, Semaphore cRojo, int id) {
        this.nBlanco = nBlanco;
        this.cBlanco = cBlanco;
        this.nRojo = nRojo;
        this.cRojo = cRojo;
        rd = new Random();
        this.id = "ROSA-" + id;
    }

    @Override
    public void run() {
            for(int i = 0; i<6; i++){
            try {
                //Dos blancos
                this.nRojo.acquire();
                System.out.println(id + "-> Coge ROJO ");
                this.cRojo.release();

                this.nRojo.acquire();
                System.out.println(id + "-> Coge ROJO ");
                this.cRojo.release();

                //Uno rojo
                this.nBlanco.acquire();
                System.out.println(id + "-> Coge BLANCO ");
                this.cBlanco.release();

                //Esperar
                sleep((rd.nextInt(1)+1)*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
