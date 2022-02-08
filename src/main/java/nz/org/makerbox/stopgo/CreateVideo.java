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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import static nz.org.makerbox.stopgo.CreateProject.dir_images;
import org.apache.commons.io.FileUtils;
import org.jcodec.api.awt.AWTSequenceEncoder;

 /**
 *
 * @author Seth Kenlon
 */
public class CreateVideo {
    public static void encode() throws FileNotFoundException, IOException {
        File output = new File(dir_images + "/Video/" + "movie.mp4");
        AWTSequenceEncoder enc = AWTSequenceEncoder.create24Fps(output);

        String[] ext = new String[] { "png" };
        List<File> file_list = (List<File>) FileUtils.listFiles(dir_images, ext, false);
        Collections.sort(file_list);
        
        // iterate through each item in collection
        for (Iterator<File> iterator = file_list.iterator(); iterator.hasNext();) {
            //System.out.println("processing: " + iterator.next());
            BufferedImage bi = ImageIO.read(iterator.next());
            enc.encodeImage(bi);
        }
        enc.finish();
    }
}