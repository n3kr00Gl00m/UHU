import javax.swing.*;

public class GasolineraFrame extends JFrame {
    private final CanvasGasolinera canvas;
    private final Gasolinera gasolinera;

    public GasolineraFrame() {
        this.canvas = new CanvasGasolinera(900, 600); // Colas separadas
        this.gasolinera = new Gasolinera(canvas);

        // Configuración del JFrame
        setTitle("Simulación de Gasolinera");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(canvas);
        setVisible(true);
    }

    public CanvasGasolinera getCanvas() {
        return canvas;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GasolineraFrame::new);
    }
}
