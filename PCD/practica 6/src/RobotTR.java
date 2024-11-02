import java.util.Random;
import java.util.concurrent.Semaphore;

public class RobotTR extends Thread{
    private final Semaphore nBlanco, cBlanco;
    private final Semaphore nRojo, cRojo;

    private boolean parar;

    Random rd;

    public RobotTR(Semaphore nBlanco, Semaphore cBlanco, Semaphore nRojo, Semaphore cRojo) {
        this.nBlanco = nBlanco;
        this.cBlanco = cBlanco;
        this.nRojo = nRojo;
        this.cRojo = cRojo;
        this.parar = false;
        rd = new Random();
    }

    public void detener() {
        parar = true;
    }
    @Override
    public void run() {
        while(!parar) {
            try {
                for(int i = 0; i<(rd.nextInt(2)+3); i++) {
                    this.cBlanco.acquire();
                    System.out.println("---------------- Reponiendo 1 BLANCO");
                    this.nBlanco.release();
                }
                for(int i = 0; i<(rd.nextInt(2)+3); i++) {
                    this.cRojo.acquire();
                    System.out.println("---------------- Reponiendo 1 ROJO");
                    this.nRojo.release();
                }

                sleep((rd.nextInt(3)+2)*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
