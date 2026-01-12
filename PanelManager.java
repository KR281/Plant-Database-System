
package plants_app;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

// Singleton pattern
public class PanelManager extends JFrame {
    private static PanelManager instance;   // Holds an instance of panel manager. Ensures there is only one instance.
    public static JFrame mainFrame;         // Holds the JFrame

    private PanelManager(JFrame frame) {  // Method to create the JFrame
        PanelManager.mainFrame = frame;
        PanelManager.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PanelManager.mainFrame.setSize(800, 600);
        PanelManager.mainFrame.setLocationRelativeTo(null);    
    }
    // If no instance of PanelManger is created, it will create a new one.
    public static PanelManager getInstance(JFrame frame) {
        if (instance == null) {
            instance = new PanelManager(frame);
        }
        return instance;
    }
    
    public void showPanel(JPanel panel) {
        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
    public static void ChangeToUI(String plant) {
        SwingUtilities.invokeLater(() -> {
            try {
                JPanel uiPanel = new UI(plant);
                mainFrame.setContentPane(uiPanel);
                mainFrame.revalidate();
                mainFrame.repaint();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
    public static void ChangeToHome(String orderBy) {
        SwingUtilities.invokeLater(() -> {
            try {
                Home homePanel = new Home(orderBy);
                mainFrame.setContentPane(homePanel);
                mainFrame.revalidate();
                mainFrame.repaint();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}