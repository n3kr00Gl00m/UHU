import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class CanvasGasolinera extends Canvas {
    private class Vehiculo {
        private final int id;
        private final char tipo; // 'C' para coche, 'T' para camión
        private boolean repostando; // Indica si está repostando
        private int posicion; // Posición del vehículo respecto al surtidor

        public Vehiculo(int id, char tipo) {
            this.id = id;
            this.tipo = tipo;
            this.repostando = false;
            this.posicion = -1; // Inicializa en -1 para indicar que no tiene posición asignada
        }

        public int getId() { return id; }
        public char getTipo() { return tipo; }
        public boolean isRepostando() { return repostando; }
        public void setRepostando(boolean repostando) { this.repostando = repostando; }
        public int getPosicion() { return posicion; }
        public void setPosicion(int posicion) { this.posicion = posicion; }
    }

    private final ArrayList<Vehiculo> colaCoches = new ArrayList<>();
    private final ArrayList<Vehiculo> colaCamiones = new ArrayList<>();
    private final ArrayList<Vehiculo> vehiculosRepostando = new ArrayList<>(); // Lista de vehículos repostando
    private BufferedImage imagen; // Imagen para doble buffer
    private Image cocheImg, camionImg, surtidorImg;
    private boolean[] callesOcupadas = new boolean[4]; // true si la calle está ocupada
    private char[] tipoEnCalle = new char[4];          // indica el tipo de vehículo en cada calle ('C' o 'T')

    public CanvasGasolinera(int ancho, int alto) {
        setSize(ancho, alto);
        setBackground(Color.WHITE);
        cargarImagenes(); // Carga de imágenes
        imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
    }

    private void cargarImagenes() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        cocheImg = toolkit.getImage(getClass().getResource("image/coche.png"));
        camionImg = toolkit.getImage(getClass().getResource("image/camion.png"));
        surtidorImg = toolkit.getImage(getClass().getResource("image/surtidor.png"));

        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(cocheImg, 1);
        tracker.addImage(camionImg, 2);
        tracker.addImage(surtidorImg, 3);

        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public synchronized void paint(Graphics g) {
        if (imagen == null) {
            imagen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics gbuf = imagen.getGraphics();
        gbuf.setColor(Color.WHITE);
        gbuf.fillRect(0, 0, getWidth(), getHeight());

        int surtidorIzqX = 100;
        int surtidorDerX = 300;
        int surtidorY = 150;

        gbuf.drawImage(surtidorImg, surtidorIzqX, surtidorY, 60, 60, this);
        gbuf.drawImage(surtidorImg, surtidorDerX, surtidorY, 60, 60, this);

        // Pintar vehículos en repostaje
        for (Vehiculo v : vehiculosRepostando) {
            pintarEnCalle(gbuf, v, surtidorIzqX, surtidorDerX, surtidorY);
        }

        // Pintar vehículos en cola
        pintarEnCola(gbuf, colaCoches, 50, surtidorY + 150);
        pintarEnCola(gbuf, colaCamiones, 400, surtidorY + 150);

        g.drawImage(imagen, 0, 0, this);
    }

    private void pintarEnCola(Graphics g, List<Vehiculo> cola, int inicioX, int inicioY) {
        int offsetX = 0;
        for (Vehiculo v : cola) {
            if (!v.isRepostando()) { // Solo pinta en la cola si no está repostando
                Image vehiculoImg = v.getTipo() == 'C' ? cocheImg : camionImg;
                g.drawImage(vehiculoImg, inicioX + offsetX, inicioY, 50, 50, this);
                g.setColor(Color.BLACK); // Pintar ID en negro
                g.drawString(v.getTipo() + String.valueOf(v.getId()), inicioX + offsetX, inicioY + 60);
                offsetX += 60; // Espacio entre vehículos
            }
        }
    }

    private void pintarEnCalle(Graphics g, Vehiculo vehiculo, int surtidorIzqX, int surtidorDerX, int surtidorY) {
        int posicion = vehiculo.getPosicion();
        int xPos = switch (posicion) {
            case 0 -> surtidorIzqX - 60; // Calle 1
            case 1 -> surtidorIzqX + 70; // Calle 2
            case 2 -> surtidorDerX - 60; // Calle 3
            case 3 -> surtidorDerX + 70; // Calle 4
            default -> -1;
        };

        Image vehiculoImg = vehiculo.getTipo() == 'C' ? cocheImg : camionImg;
        g.drawImage(vehiculoImg, xPos, surtidorY + 10, 50, 50, this);
        g.setColor(Color.BLACK);
        g.drawString(vehiculo.getTipo() + String.valueOf(vehiculo.getId()), xPos, surtidorY);
    }

    private int calcularCalleLibre(Vehiculo vehiculo, boolean enCentrales) {
        if (vehiculo.getTipo() == 'C') {
            // Chequear calles centrales primero
            if (!callesOcupadas[1]) {
                callesOcupadas[1] = true;
                tipoEnCalle[1] = 'C';
                return 1; // Calle 2
            } else if (!callesOcupadas[2]) {
                callesOcupadas[2] = true;
                tipoEnCalle[2] = 'C';
                return 2; // Calle 3
            }
            // Si no hay calles centrales libres, usar laterales
            if (!callesOcupadas[0]) {
                callesOcupadas[0] = true;
                tipoEnCalle[0] = 'C';
                return 0; // Calle 1
            } else if (!callesOcupadas[3]) {
                callesOcupadas[3] = true;
                tipoEnCalle[3] = 'C';
                return 3; // Calle 4
            }

        } else if (vehiculo.getTipo() == 'T') {
            // Chequear calles laterales primero
            if (!callesOcupadas[0]) {
                callesOcupadas[0] = true;
                tipoEnCalle[0] = 'T';
                return 0; // Calle 1
            } else if (!callesOcupadas[3]) {
                callesOcupadas[3] = true;
                tipoEnCalle[3] = 'T';
                return 3; // Calle 4
            }
            // Si no hay laterales libres, usar centrales
            if (!callesOcupadas[1] && !hayCocheEnCalleCentral()) {
                callesOcupadas[1] = true;
                tipoEnCalle[1] = 'T';
                return 1; // Calle 2
            } else if (!callesOcupadas[2] && !hayCocheEnCalleCentral()) {
                callesOcupadas[2] = true;
                tipoEnCalle[2] = 'T';
                return 2; // Calle 3
            }
        }

        // Si no se encuentra calle libre, retornar -1
        return -1;
    }

    // Método auxiliar para verificar si hay un coche en las calles centrales
    private boolean hayCocheEnCalleCentral() {
        return callesOcupadas[2] || callesOcupadas[3];
    }

    public synchronized void agregarVehiculo(int id, char tipo) {
        Vehiculo nuevoVehiculo = new Vehiculo(id, tipo);
        if (tipo == 'C') {
            colaCoches.add(nuevoVehiculo);
        } else {
            colaCamiones.add(nuevoVehiculo);
        }
        repaint();
    }

    public synchronized void quitarVehiculo(int id, char tipo) {
        vehiculosRepostando.removeIf(v -> v.getId() == id);
        if (tipo == 'C') {
            colaCoches.removeIf(v -> v.getId() == id);
        } else {
            colaCamiones.removeIf(v -> v.getId() == id);
        }
        repaint();
    }

    public synchronized void iniciarRepostajeCoche(int id) {
        iniciarRepostaje(id, colaCoches);
    }

    public synchronized void iniciarRepostajeCamion(int id) {
        iniciarRepostaje(id, colaCamiones);
    }

    private void iniciarRepostaje(int id, ArrayList<Vehiculo> cola) {
        Vehiculo vehiculo = cola.stream().filter(v -> v.getId() == id).findFirst().orElse(null);
        if (vehiculo != null) {
            vehiculo.setRepostando(true);
            cola.remove(vehiculo);
            int posicion = calcularCalleLibre(vehiculo, true); // Suponemos que entra a centrales
            if (posicion != -1) {
                vehiculo.setPosicion(posicion);
                vehiculosRepostando.add(vehiculo);
                repaint();
            }
        }
    }

    public synchronized void finalizarRepostajeCoche(int id) {
        finalizarRepostaje(id, colaCoches);
    }

    public synchronized void finalizarRepostajeCamion(int id) {
        finalizarRepostaje(id, colaCamiones);
    }

    private void finalizarRepostaje(int id, ArrayList<Vehiculo> cola) {
        Vehiculo vehiculo = vehiculosRepostando.stream().filter(v -> v.getId() == id).findFirst().orElse(null);
        if (vehiculo != null) {
            vehiculosRepostando.remove(vehiculo);
            int posicion = vehiculo.getPosicion();
            callesOcupadas[posicion] = false; // Liberar la calle ocupada
            tipoEnCalle[posicion] = ' '; // Limpiar el tipo en la calle
            cola.add(vehiculo); // Volver a añadir a la cola
            repaint();
        }
    }
}
