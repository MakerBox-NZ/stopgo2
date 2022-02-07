/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.org.makerbox.stopgo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import static nz.org.makerbox.stopgo.Timeline.listFiles;

/** @author Seth Kenlon */

public class saveData {
    private static final Properties prop = new Properties();

    public static void initFile(File dir_images) throws IOException {
        String path = dir_images + File.separator + "stopgo.properties";
        File file = new File(path);
        file.createNewFile();
    }
    
    public static void updateFile(int count) {
        prop.setProperty("image.resume", Integer.toString(count));
    }
    
    public static void saveFile(File dir_images) {
        String path = dir_images + File.separator + "stopgo.properties";
        File file = new File(path);
        try (OutputStream output = new FileOutputStream(file)) {
            prop.store(output, null);
            System.out.println("DEBUG: Saved.");
        } catch (IOException io) {
            System.out.println("DEBUG: Save failed. It's safe to continue.");
        }
        System.out.println("DEBUG: saveFile triggered.");
    }
    
    /**
     * Get latest frame count.
     * @param dir_images: project directory containing images
     * @return largest_int: number assigned to latest image
     */
    public static int getCount(File dir_images) {
        File[] files;
        files = listFiles(dir_images);
        File lastfile = files[files.length-1];
        String largestnum = lastfile.getName().replaceAll("[^0-9]", "");
        int largest_int = Integer.parseInt(largestnum);
        return largest_int;
    }
    
    public static int getResume(File dir_images) {
        String path = dir_images + File.separator + "stopgo.properties";
        
        try (InputStream ingest = new FileInputStream(path)) {
            Properties prop = new Properties();
            prop.load(ingest);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(prop.getProperty("image.resume"));
        String count = prop.getProperty("image.resume");
        System.out.println(count);
        int counter = Integer.valueOf(count);
        System.out.println(counter);
        return counter;
    }
}
