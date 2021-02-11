package org.makerbox.stopgo;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateProject {

	static File dir_images = null;
    private static File dir_trash = null;
    static int returnValue = 0;
       
	public static File main(String msg_operator) {
            Logger logger = LoggerFactory.getLogger(CreateProject.class);
            
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Choose your Stopgo image directory");

            Date date = new Date();
            DateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            String timestamp = sdf.format(date);  
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                logger.info("LookandFeel set.");
            } catch(Exception ex) {
                //ex.printStackTrace();
                logger.warn("Failed to set look and feel.");
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
            dir_trash = new File(dir_images, "Trash");
            if (!dir_trash.exists()) {
                dir_trash.mkdirs();
            }

            return dir_images;
    }

    public static File getTrash() {
        return dir_trash; 
    }
}