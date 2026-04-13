import javax.swing.SwingUtilities;
import ui.MainFrame;

public class Main {

    // method utama sebagai entry point program
    public static void main(String[] args) {

        // menjalankan ui pada event dispatch thread (standar swing)
        SwingUtilities.invokeLater(() -> 

            // membuat dan menampilkan window utama aplikasi
            new MainFrame().setVisible(true)
        );
    }
}