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
