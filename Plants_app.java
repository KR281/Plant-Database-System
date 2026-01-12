
package plants_app;


import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Plants_app {
    public static void main(String[] args) {
        try {
            Database.createConnection();
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Plant Database");
                PanelManager panelManager = PanelManager.getInstance(frame);
                try {
                    panelManager.showPanel(new Home("Name"));
                } catch (SQLException ex) {
                    Logger.getLogger(Plants_app.class.getName()).log(Level.SEVERE, null, ex);
                }
                frame.setVisible(true);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}