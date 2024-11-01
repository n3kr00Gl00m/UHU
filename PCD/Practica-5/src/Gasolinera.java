import java.util.LinkedList;
import java.util.Queue;

public class Gasolinera {
    private Queue<Integer> colaCochesEspera;  // Cola de espera para coches
    private Queue<Integer> colaCamionesEspera;  // Cola de espera para camiones
    private int lateralesLibres, centralesLibres;
    CanvasGasolinera canvas;

    public Gasolinera(CanvasGasolinera canvas) {
        this.lateralesLibres = 2;
        this.centralesLibres = 2;
        this.colaCochesEspera = new LinkedList<>();
        this.colaCamionesEspera = new LinkedList<>();
        this.canvas = canvas;
    }

    public void vehiculoALateral(int id) {
        this.lateralesLibres--;
    }

    /**
     * Método para que un coche entre en la gasolinera
     * @return lado indica si entra en un lateral (0) o en un central (1)
     */
    public synchronized int entraCoche(int id) throws InterruptedException {
        int lado = -1;
        colaCochesEspera.add(id);
        this.canvas.agregarVehiculo(id, 'C');
        while (this.lateralesLibres == 0 && this.centralesLibres == 0) {
            System.out.println("Coche " + id + " en espera. Cola de coches: " + colaCochesEspera);
            this.wait();
        }

        // Si hay espacio central disponible
        if (centralesLibres > 0) {
            this.centralesLibres--;
            lado = 1;
        }
        // Si hay espacio lateral disponible
        else if (lateralesLibres > 0) {
            vehiculoALateral(id);
            lado = 0;
        }
        this.canvas.iniciarRepostajeCoche(id);
        colaCochesEspera.remove((Integer) id);  // Eliminamos el coche de la cola cuando entra

        System.out.println("Coche entra en " + (lado == 0 ? "lateral" : "central") + ". Disponibles: Laterales: " + this.lateralesLibres + ", Centrales: " + this.centralesLibres);
        return lado;
    }

    /**
     * Método para que un camión entre en la gasolinera
     * @return lado indica si entra en un lateral (0) o en un central (1)
     */
    public synchronized int entraCamion(int id) throws InterruptedException {
        int lado = -1;
        colaCamionesEspera.add(id);
        this.canvas.agregarVehiculo(id, 'T');

        while (this.lateralesLibres == 0 && this.centralesLibres < 2) {
            System.out.println("Camión " + id + " en espera. Cola de camiones: " + colaCamionesEspera);
            this.wait();
        }

        // Si hay espacio lateral disponible
        if (lateralesLibres > 0) {
            vehiculoALateral(id);
            lado = 0;
        }
        // Si hay 2 espacios centrales libres
        else if (centralesLibres == 2) {
            this.centralesLibres = 0;
            lado = 1;
        }

        this.canvas.iniciarRepostajeCamion(id);

        colaCamionesEspera.remove((Integer)id);  // Eliminamos el camión de la cola cuando entra
        System.out.println("Camión entra en " + (lado == 0 ? "lateral" : "central") + ". Disponibles: Laterales: " + this.lateralesLibres + ", Centrales: " + this.centralesLibres);
        return lado;
    }

    /**
     * Método para que un coche salga de la gasolinera
     */
    public synchronized void saleCoche(int id, int lado) {
        if (lado == 1) {  // Central
            this.centralesLibres++;
        } else if (lado == 0) {  // Lateral
            this.lateralesLibres++;
        }

        this.canvas.finalizarRepostajeCoche(id);
        System.out.println("Coche sale de " + (lado == 0 ? "lateral" : "central") + ". Disponibles: Laterales: " + this.lateralesLibres + ", Centrales: " + this.centralesLibres);
        notifyAll();
    }

    /**
     * Método para que un camión salga de la gasolinera
     */
    public synchronized void saleCamion(int id, int lado) {
        if (lado == 1) {  // Central
            this.centralesLibres = 2;
        } else if (lado == 0) {  // Lateral
            this.lateralesLibres++;
        }
        this.canvas.finalizarRepostajeCamion(id);
        System.out.println("Camión sale de " + (lado == 0 ? "lateral" : "central") + ". Disponibles: Laterales: " + this.lateralesLibres + ", Centrales: " + this.centralesLibres);
        notifyAll();
    }
}
