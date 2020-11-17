package org.makerbox.stopgo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author Seth Kenlon
 *
 */

public class CamView extends JFrame implements Runnable, WebcamListener, 
    WindowListener, UncaughtExceptionHandler, ItemListener, 
    WebcamDiscoveryListener, ActionListener, WebcamImageTransformer {

    private int counter = 0;
    private static final long serialVersionUID = 1L;
    private static BufferedImage onion = null;
    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private WebcamPicker picker = null;
    private Timeline timeline = null;
    private File dir_images;

    @Override
    public void run() {
        GridBagConstraints gbc = new GridBagConstraints();  
    	setTitle("Stopgo");
        GridBagLayout layout = new GridBagLayout();  
        setLayout(layout);  
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gbc.fill = GridBagConstraints.BOTH;
        
        JMenuBar menu_main = new JMenuBar();
        //ImageIcon exitIcon = new ImageIcon("src/resources/exit.png");
        JMenu menu_main_file = new JMenu("File");
        menu_main_file.setMnemonic(KeyEvent.VK_F);
        // New
        JMenuItem menu_mf_new = new JMenuItem("New");//, newIcon);
        menu_mf_new.setMnemonic(KeyEvent.VK_N);
        menu_mf_new.addActionListener(this);//(event) -> CreateProject.main()
        // Open
        JMenuItem menu_mf_open = new JMenuItem("Open");//, openIcon);
        menu_mf_open.setMnemonic(KeyEvent.VK_O);
        menu_mf_open.addActionListener(this);
        // Exit
        JMenuItem menu_mf_exit = new JMenuItem("Exit");//, exitIcon);
        menu_mf_exit.setMnemonic(KeyEvent.VK_E);
        //eMenuItem.setToolTipText("Exit application");
        menu_mf_exit.addActionListener((event) -> System.exit(0));

        menu_main_file.add(menu_mf_new);
        menu_main_file.add(menu_mf_open);
        menu_main_file.add(menu_mf_exit);
        menu_main.add(menu_main_file);
        setJMenuBar(menu_main);
    	Webcam.addDiscoveryListener(this);
        addWindowListener(this);

        picker = new WebcamPicker();
        picker.addItemListener(this);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        add(picker, gbc);
        webcam = picker.getSelectedWebcam();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.addWebcamListener(CamView.this);

        JButton btn_shutter = new JButton("snap");
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.gridy = 0;
        add(btn_shutter, gbc);
        btn_shutter.setText("Snap");
        btn_shutter.addActionListener(this);
        
        //TODO: make this nicer
        if (webcam == null) {
                System.out.println("No webcams found...");
                System.exit(1);
        }

        panel = new WebcamPanel(webcam, false);
        webcam.setImageTransformer(this);
        panel.setFPSDisplayed(true);
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.gridy = 1;
        add(panel, gbc);

        timeline = new Timeline();
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        gbc.gridy = 2;
        add(timeline, gbc);
        
        pack();
        setVisible(true);
            
        Thread t = new Thread() {

@Override
public void run() {
	panel.start();
                }
	};
	t.setName("Stopgo");
	t.setDaemon(true);
	t.setUncaughtExceptionHandler(this);
	t.start();
}

public static void main(String[] args) {
        SwingUtilities.invokeLater(new CamView());   
}

@Override
public void webcamOpen(WebcamEvent we) {
        System.out.println("webcam open");
}

@Override
public void webcamClosed(WebcamEvent we) {
        System.out.println("webcam closed");
}

@Override
public void webcamDisposed(WebcamEvent we) {
        System.out.println("webcam disposed");
}

@Override
public void webcamImageObtained(WebcamEvent we) {
}

@Override
public void windowActivated(WindowEvent e) {
}

@Override
public void windowClosed(WindowEvent e) {
        webcam.close();
}

@Override
public void windowClosing(WindowEvent e) {
}

@Override
public void windowOpened(WindowEvent e) {
}

@Override
public void windowDeactivated(WindowEvent e) {
}

@Override
public void windowDeiconified(WindowEvent e) {
        System.out.println("webcam viewer resumed");
        panel.resume();
}

@Override
public void windowIconified(WindowEvent e) {
        System.out.println("webcam viewer paused");
        panel.pause();
}

@Override
public void uncaughtException(Thread t, Throwable e) {
        System.err.println(String.format("Exception in thread %s", t.getName()));
        e.printStackTrace();
}

@Override
public void actionPerformed(ActionEvent ae) {
    String action = ae.getActionCommand();
    if (action.equals("Snap")) {
        System.out.println("Button pressed.");
        try {
                onion = null;
        	String snapshot = String.format("%07d", counter) + ".png";
		ImageIO.write(webcam.getImage(), "PNG", 
                        new File(this.dir_images + File.separator + snapshot));
		counter++;
                onion = webcam.getImage();
            } catch (IOException e) {
		System.out.println("You must create a project first.");
		e.printStackTrace();
            }
    } else if (action.equals("New") || action.equals("Open")) {
        counter=0;
        this.dir_images = CreateProject.main(action);
        // get image numbers
        File[] files = this.dir_images.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        Arrays.sort(files);
        File lastfile = files[files.length-1];
        String largestnum = lastfile.getName().replaceAll("[^0-9]", "");
        counter = Integer.parseInt(largestnum)+1;
        //counter = Arrays.toString(files[files.length-1]).replaceAll("[^0-9]", ""));
    }
}

@Override
public BufferedImage transform(BufferedImage image) {
	int w = image.getWidth();
	int h = image.getHeight();
	BufferedImage modified = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	Graphics2D g2d = modified.createGraphics();
	g2d.drawImage(image, null, 0, 0);
        
	// TODO make alpha adjustable by user
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
        g2d.drawImage(onion, null, 0, 0);
	g2d.dispose();

	modified.flush();
	return modified;
}

@Override
public void itemStateChanged(ItemEvent e) {
	if (e.getItem() != webcam) {
	if (webcam != null) {

	panel.stop();
	remove(panel);

	webcam.removeWebcamListener(this);
	webcam.close();

	webcam = (Webcam) e.getItem();
	webcam.setViewSize(WebcamResolution.VGA.getSize());
	webcam.addWebcamListener(this);
	System.out.println("selected " + webcam.getName());

	panel = new WebcamPanel(webcam, false);
	panel.setFPSDisplayed(true);

	add(panel, BorderLayout.CENTER);
	pack();

	Thread t = new Thread() {

@Override
public void run() {
	panel.start();
	}
};
	t.setName("example-stopper");
	t.setDaemon(true);
	t.setUncaughtExceptionHandler(this);
	t.start();
	}}
}

@Override
public void webcamFound(WebcamDiscoveryEvent event) {
        if (picker != null) {
                picker.addItem(event.getWebcam());
        }
}

@Override
public void webcamGone(WebcamDiscoveryEvent event) {
        if (picker != null) {
                picker.removeItem(event.getWebcam());
        }
}
}