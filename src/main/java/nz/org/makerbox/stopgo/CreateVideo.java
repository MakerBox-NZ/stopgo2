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
import java.util.Collections;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import javax.imageio.ImageIO;
import static nz.org.makerbox.stopgo.CreateProject.dir_images;
import org.apache.commons.io.FileUtils;
import org.jcodec.api.awt.AWTSequenceEncoder;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import java.awt.EventQueue;
import java.util.List;

public class CreateVideo extends JFrame {

    private static final String START = "0%";
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel label = new JLabel(START, JLabel.CENTER);

    public CreateVideo() {
        this.setLayout(new GridLayout(0, 1));
        this.setTitle("Exporting images as movie");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.add(progressBar);
        this.add(label);
        this.setSize(161, 100);
        this.setLocationRelativeTo(getParent());
        this.setVisible(true);
    }

    public void runCalc() {
        progressBar.setIndeterminate(true);
        EncodeVideo task = new EncodeVideo();
        task.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("progress".equals(e.getPropertyName())) {
                    progressBar.setIndeterminate(false);
                    progressBar.setValue((Integer) e.getNewValue());
                }
            }
        });
        task.execute();
    }

    private class EncodeVideo extends SwingWorker<Integer, Integer> {
        private int count;
        private int incr;

        @Override
        protected Integer doInBackground() throws Exception {
            String[] ext = new String[] { "png" };
            List<File> file_list = (List<File>) FileUtils.listFiles(dir_images, ext, false);
            progressBar.setMaximum(file_list.size());
            
            File output = new File(dir_images + "/Video/" + "movie.mp4");
            AWTSequenceEncoder enc = AWTSequenceEncoder.create24Fps(output);
            Collections.sort(file_list);
            
            count = 0;
            // iterate through each item in collection
            for (Iterator<File> i = file_list.iterator(); i.hasNext();) {
                BufferedImage bi = ImageIO.read(i.next());
                enc.encodeImage(bi);
                incr = (int)((count / (float) file_list.size() ) * 100);
                publish(incr); //send updates to process method
                count++;
            }
            enc.finish();
            incr = 100;
            setProgress(incr);
            publish(incr);
            return 0;
    }

        @Override
        protected void process(List<Integer> chunks) {
            for (int i : chunks) {
                setProgress(i);
                //System.out.println("count: " + i + " of " + Integer.toString(progressBar.getMaximum()));
                    label.setText(Integer.toString(i) +"%");
            }
        }
    }

    public static void main() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                CreateVideo t = new CreateVideo();
                t.runCalc();
            }
        });      
    }
}