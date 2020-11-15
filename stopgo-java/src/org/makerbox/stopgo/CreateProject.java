package org.makerbox.stopgo;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import javax.swing.plaf.FileChooserUI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateProject {

	static File dir_images = null;
        static int returnValue = 0;
                
	public static File main(String msg_operator) {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Choose your Stopgo image directory");

            Date date = new Date();
            DateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            String timestamp = sdf.format(date);  
            //setSelectedFile does not work for dir names
            //jfc.setSelectedFile( new File("stopgo_" + timestamp) );
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                System.out.println("LookandFeel set. [DEBUG]");
            } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("Failed to set look and feel [WARNING] [DEBUG].");
            }

            if (msg_operator.equals("Open")) {
                returnValue = jfc.showOpenDialog(null);                    
            } else {
                returnValue = jfc.showSaveDialog(null);                    
            }

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                CreateProject.dir_images = jfc.getSelectedFile();
                String str_project = (CreateProject.dir_images.getAbsolutePath());
                String str_default = (FileSystemView.getFileSystemView().getHomeDirectory()).getAbsolutePath();

                // catch no input
                if (str_project.equals(str_default)) {
                    CreateProject.dir_images = new File(str_project + File.separator + "stopgo_" + timestamp);
                } 

                if (! CreateProject.dir_images.exists()) {
                    CreateProject.dir_images.mkdirs();
            }
        }
            return dir_images;
    }
}