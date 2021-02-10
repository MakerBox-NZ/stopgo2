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
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

/**
 * @author Seth Kenlon
 *
 */

public class App extends JFrame implements Runnable, WebcamListener, 
    WindowListener, UncaughtExceptionHandler, ItemListener, 
    WebcamDiscoveryListener, ActionListener, WebcamImageTransformer, KeyListener {

    private int counter = 0;
    private static final long serialVersionUID = 1L;
    private static BufferedImage onion = null;
    private Webcam cam = null;
    private WebcamPanel camview = null;
    private WebcamPicker picker = null;
    private Timeline timeline = null;
    private File dir_images;
    private final Color makerboxblue = new Color(79,205,222);
    
    @Override
    public void run() {
    	this.setTitle("Stopgo");
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(960,800));
        
        JMenuBar menu_main = new JMenuBar();
        //ImageIcon exitIcon = new ImageIcon("src/resources/exit.png");
        JMenu menu_main_file = new JMenu("File");
        menu_main_file.setMnemonic(KeyEvent.VK_F);
        // New
        JMenuItem menu_mf_new = new JMenuItem("New");//, newIcon);
        menu_mf_new.setMnemonic(KeyEvent.VK_N);
        menu_mf_new.addActionListener(this);
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

        JMenu menu_main_edit = new JMenu("Edit");
        menu_main_edit.setMnemonic(KeyEvent.VK_E);
        JMenuItem menu_me_delete = new JMenuItem("Delete");
        menu_me_delete.addActionListener(this);

        menu_main_edit.add(menu_me_delete);
        menu_main.add(menu_main_edit);
        
        setJMenuBar(menu_main);
    	Webcam.addDiscoveryListener(this);
        addWindowListener(this);
        
        Container contentPane = this.getContentPane();  
        SpringLayout spring = new SpringLayout();  
        contentPane.setLayout(spring);
        
        picker = new WebcamPicker();
        picker.addItemListener(this);
        contentPane.add(picker);

        JButton btn_shutter = new JButton("snap");
        btn_shutter.setText("Snap");
        btn_shutter.addActionListener(this);
        //btn_shutter.requestFocusInWindow();
        //btn_shutter.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released ENTER"), "released");
        contentPane.add(btn_shutter);

        JButton btn_delete = new JButton("delete");
        btn_delete.setText("Delete");
        btn_delete.addActionListener(this);
        //btn_shutter.requestFocusInWindow();
        //btn_shutter.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released ENTER"), "released");
        contentPane.add(btn_delete);

        cam = picker.getSelectedWebcam();
        cam.setViewSize(WebcamResolution.VGA.getSize());
        cam.addWebcamListener(App.this);
        
        camview = new WebcamPanel(cam, false);
        cam.setImageTransformer(this);
        camview.setFPSDisplayed(true);
        contentPane.add(camview);
        
        timeline = new Timeline();
        //TODO add a preference or dropdown box for color choice
        timeline.setColor(timeline, makerboxblue);
        timeline.setPreferredSize(new Dimension(1920,180));
        JScrollPane scroller = new JScrollPane(timeline);
        contentPane.add(scroller);
        scroller.setMinimumSize(new Dimension(256,256));
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        // <place the edge of...>, <component_0>, <with padding>, <...against the edge of...>, <component_1>   
        spring.putConstraint(SpringLayout.HORIZONTAL_CENTER, camview, 0, SpringLayout.HORIZONTAL_CENTER, contentPane); 
        
        spring.putConstraint(SpringLayout.WEST, picker, 0, SpringLayout.WEST, camview); 
        spring.putConstraint(SpringLayout.WEST, btn_delete, 10, SpringLayout.EAST, picker);
        spring.putConstraint(SpringLayout.WEST, btn_shutter, 10, SpringLayout.EAST, btn_delete);

        spring.putConstraint(SpringLayout.EAST, btn_shutter, 0, SpringLayout.EAST, camview);
        
        spring.putConstraint(SpringLayout.NORTH, picker, 10, SpringLayout.SOUTH, camview);
        spring.putConstraint(SpringLayout.NORTH, btn_delete, 10, SpringLayout.SOUTH, camview); 
        spring.putConstraint(SpringLayout.NORTH, btn_shutter, 10, SpringLayout.SOUTH, camview); 
        
        spring.putConstraint(SpringLayout.SOUTH, scroller, 0, SpringLayout.SOUTH, contentPane); 
        spring.putConstraint(SpringLayout.EAST, scroller, 0, SpringLayout.EAST, contentPane); 
        spring.putConstraint(SpringLayout.WEST, scroller, 0, SpringLayout.WEST, contentPane); 

        pack();
        setVisible(true);
        
        //TODO: make this a popup, and not exit
        if (cam == null) {
                System.out.println("No webcams found...");
                System.exit(1);
        }

        Thread t = new Thread() {

@Override
public void run() {
	camview.start();
                }
	};
	t.setName("Stopgo");
	t.setDaemon(true);
	t.setUncaughtExceptionHandler(this);
	t.start();
}

@Override
public void actionPerformed(ActionEvent ae) {
    String action = ae.getActionCommand();
    if (action.equals("Snap")) {
        System.out.println("Button pressed.");
        try {
                onion = null;
        	String snapshot = String.format("%07d", counter) + ".png";
		ImageIO.write(cam.getImage(), "PNG", 
                        new File(this.dir_images + File.separator + snapshot));
		counter++;
                onion = cam.getImage();
            } catch (IOException e) {
                //TODO make this a popup window
		System.out.println("You must create a project first.");
		e.printStackTrace();
            }
    } else if (action.equals("New")) {
        this.dir_images = CreateProject.main(action);
    } else if (action.equals("Open")) {
        counter=0;
        this.dir_images = CreateProject.main(action);
        // TODO do not restrict to only PNG
        // get image numbers
        timeline.pop(timeline, dir_images);
        timeline.revalidate(); timeline.repaint();
        counter = timeline.getResume(dir_images);
    } else if (action.equals("Delete")) {
        System.out.println("Delete menu selected");
    }
}

@Override
public void uncaughtException(Thread t, Throwable e) {
        System.err.println(String.format("Exception in thread %s", t.getName()));
        e.printStackTrace();
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
	if (e.getItem() != cam) {
	if (cam != null) {

	camview.stop();
	remove(camview);

	cam.removeWebcamListener(this);
	cam.close();

	cam = (Webcam) e.getItem();
	cam.setViewSize(WebcamResolution.VGA.getSize());
	cam.addWebcamListener(this);
	System.out.println("selected " + cam.getName());

	camview = new WebcamPanel(cam, false);
	camview.setFPSDisplayed(true);

	add(camview, BorderLayout.CENTER);
	pack();

	Thread t = new Thread() {

@Override
public void run() {
	camview.start();
	}
                };
	t.setName("stopper");
	t.setDaemon(true);
	t.setUncaughtExceptionHandler(this);
	t.start();
	}}
}

public static void main(String[] args) {
        SwingUtilities.invokeLater(new App());   
}

@Override
public void webcamOpen(WebcamEvent we) {
        System.out.println("camera open");
}

@Override
public void webcamClosed(WebcamEvent we) {
        System.out.println("camera closed");
}

@Override
public void webcamDisposed(WebcamEvent we) {
        System.out.println("camera disposed");
}

@Override
public void webcamImageObtained(WebcamEvent we) {
}

@Override
public void windowActivated(WindowEvent e) {
}

@Override
public void windowClosed(WindowEvent e) {
        cam.close();
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
        camview.resume();
}

@Override
public void windowIconified(WindowEvent e) {
        System.out.println("camera viewer paused");
        camview.pause();
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("i felt that");
    }
}
