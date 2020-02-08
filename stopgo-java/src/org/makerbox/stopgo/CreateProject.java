package org.makerbox.stopgo;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

public class CreateProject {

	static File dir_project = null; 
	static File dir_images = null;
        static int returnValue = 0;
                
//	public static File main() {
	public static File main(String msg_operator) {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Stopgo project directory");
		
		Date date = new Date();
	    DateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
	    String timestamp = sdf.format(date);
	    
		jfc.setSelectedFile(new File("stopgo_" + timestamp));

		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                System.out.println("LookandFeel set. [DEBUG]");
	    }catch(Exception ex) {
	        ex.printStackTrace();
	        System.out.println("Look and feel [WARNING] [DEBUG].");
	    }
		
                if (msg_operator.equals("Open")) {
                    returnValue = jfc.showOpenDialog(null);                    
                } else {
                    returnValue = jfc.showSaveDialog(null);                    
                }

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			CreateProject.dir_project = jfc.getSelectedFile();
			String str_project = (CreateProject.dir_project.getAbsolutePath());
			String str_default = (FileSystemView.getFileSystemView().getHomeDirectory()).getAbsolutePath();

			// catch no input
			if (str_project.equals(str_default)) {
				CreateProject.dir_project = new File(str_project + File.separator + "stopgo_" + timestamp);
			} 

			if (CreateProject.dir_project.mkdirs()) {
				CreateProject.dir_images = new File(CreateProject.dir_project + File.separator + "images");
				CreateProject.dir_images.mkdirs();
			} else if (CreateProject.dir_project.exists()) {
                                CreateProject.dir_images = new File(CreateProject.dir_project + File.separator + "images");
                                System.out.println(CreateProject.dir_project + " exists. Setting image dir to " + CreateProject.dir_images);
			} else {
			    System.out.println("Something went wrong.");
			}
		}
                        return dir_images;
	}
}