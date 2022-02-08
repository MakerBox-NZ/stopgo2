/*
 * Copyright (C) 2020 Seth Kenlon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package nz.org.makerbox.stopgo;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

public class CreateProject {

	static File dir_images;
    private static File dir_trash;
    private static File dir_export;
    static int returnValue = 0;
       
	public static File main(String msg_operator) {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Choose your Stopgo image directory");

            Date date = new Date();
            DateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            String timestamp = sdf.format(date);  
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                //logger.info("LookandFeel set.");
            } catch(Exception ex) {
                ex.printStackTrace();
                //logger.warn("Failed to set look and feel.");
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
            
            dir_export = new File(dir_images, "Video");
            if (!dir_export.exists()) {
                dir_export.mkdirs();
            }
            
            // this is the project directory
            return dir_images;
    }

    public static File getTrashDir() {
        if (!dir_trash.exists()) {
            dir_trash.mkdirs();
        }

        return dir_trash; 
    }
    
    public static File getExportDir() {
        if (!dir_export.exists()) {
            dir_export.mkdirs();
        }

        return dir_export; 
    }    
}