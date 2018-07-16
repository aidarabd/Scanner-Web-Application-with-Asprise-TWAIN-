
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.*;
import java.util.Properties;

public class HideToSystemTray extends JFrame {

    private JFrame frame;
    TrayIcon trayIcon;
    SystemTray tray;
    /**
     * Launch the application.
     */
    public void main() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HideToSystemTray window = new HideToSystemTray();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public HideToSystemTray() throws AWTException, IOException {
            System.out.println("creating instance");
            try{
                System.out.println("setting look and feel");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }catch(Exception e){
                System.out.println("Unable to set LookAndFeel");
            }
            if(SystemTray.isSupported()){
                System.out.println("system tray supported");
                tray=SystemTray.getSystemTray();

                Image image=Toolkit.getDefaultToolkit().getImage("scanner.png");
                ActionListener exitListener=new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Exiting....");
                        System.exit(0);
                    }
                };
                PopupMenu popup=new PopupMenu();
                MenuItem defaultItem=new MenuItem("Exit");
                defaultItem.addActionListener(exitListener);
                popup.add(defaultItem);
                defaultItem=new MenuItem("Open");
                defaultItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        setVisible(true);
                        setExtendedState(JFrame.NORMAL);
                    }
                });
                popup.add(defaultItem);
                trayIcon=new TrayIcon(image, "SystemTray Demo", popup);
                trayIcon.setImageAutoSize(true);
            }else{
                System.out.println("system tray not supported");
            }
            addWindowStateListener(new WindowStateListener() {
                public void windowStateChanged(WindowEvent e) {
                    if(e.getNewState()==ICONIFIED){
                        try {
                            tray.add(trayIcon);
                            setVisible(false);
                            System.out.println("added to SystemTray");
                        } catch (AWTException ex) {
                            System.out.println("unable to add to tray");
                        }
                    }
                    if(e.getNewState()==7){
                        try{
                            tray.add(trayIcon);
                            setVisible(false);
                            System.out.println("added to SystemTray");
                        }catch(AWTException ex){
                            System.out.println("unable to add to system tray");
                        }
                    }
                    if(e.getNewState()==MAXIMIZED_BOTH){
                        tray.remove(trayIcon);
                        setVisible(true);
                        System.out.println("Tray icon removed");
                    }
                    if(e.getNewState()==NORMAL){
                        tray.remove(trayIcon);
                        setVisible(true);
                        System.out.println("Tray icon removed");
                    }
                }
            });
        setIconImage(Toolkit.getDefaultToolkit().getImage("scanner.png"));

        setVisible(true);
            setBounds(100, 100, 450, 300);

            JButton jbd=new JButton("очистить");
            jbd.setSize( 177, 50);
            getContentPane().add(jbd);
            jbd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {





                System.out.println("pressed");

                Properties prop = System.getProperties();
                InputStream input = null;

                OutputStream out=null;
                try {
                    input = new FileInputStream("config.properties") {  
                    };
                    System.out.println("working...");
                    // load a properties file
                    prop.load(input);

                    out= new FileOutputStream("config.properties");
                    // get the property value and print it out
                    prop.remove("scannerType");
                    prop.store(out, null);
                    out.close();
                    JOptionPane.showMessageDialog(null,"записи удалены");
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
            jbd.setBounds(158, 184, 89, 23);

        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.setBounds( 129, 77, 143, 20);
        add(comboBox);
        comboBox.addItem("item");
        comboBox.addItem("item");
        comboBox.addItem("item");
        comboBox.addItem("item");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //  initialize();

    }

    /**
     * Initialize the contents of the frame.
     */

}
