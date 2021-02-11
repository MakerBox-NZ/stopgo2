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
package org.makerbox.stopgo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Establishes a timeline to display each picture in order.
 * @author Seth Kenlon
 */
public class Timeline extends JPanel implements MouseListener {
    /**
     * Constructor for timeline.
     */
    public Timeline() {
        Logger logger = LoggerFactory.getLogger(Timeline.class);
        this.initUI();
    }

    /** 
     * Create layout for timeline
     */
    private void initUI() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(layout);
        this.setMinimumSize(new Dimension(256,256));
        this.setFocusable(true);
    }
    
      /**
       * populate the timeline with Picture objects
       * @param timeline
       * @param dir_images 
       */  
    public static void pop(Timeline timeline, File dir_images) {
        File[] files;
        files = listFiles(dir_images);
        if (files != null) {
            for (File child : files) {
                JLabel imglabel = new Picture(child.toString(), 180, 128);
                timeline.add(Box.createRigidArea(new Dimension(10, 0)));
                timeline.add(imglabel);
            }
        } else {
            System.out.println("No images found.");
            System.out.println("Cannot open this directory as a Stopgo project.");
        }
        
        timeline.revalidate();
        timeline.repaint();
    }

    public static File[] listFiles(File dir_images) {
        File[] files = dir_images.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        Arrays.sort(files);
        return files;
    }
    
    /**
     * Get latest frame count.
     * @param dir_images: project directory containing images
     * @return largest_int: number assigned to latest image
     */
    public int getResume(File dir_images) {
        File[] files;
        files = listFiles(dir_images);
        File lastfile = files[files.length-1];
        String largestnum = lastfile.getName().replaceAll("[^0-9]", "");
        int largest_int = Integer.parseInt(largestnum);
        return largest_int;
    }
    
    public static void setColor(Timeline timeline, Color c) {
        timeline.setBackground(c);
    }
    
    @Override
    public void mouseReleased(MouseEvent arg0) {
        System.out.println("DEBUG Mouse event on Picture detected.");
        for (Component jc : this.getComponents()) {
            if ( jc instanceof Picture ) {
                Picture i = (Picture) jc;
                i.toggle();
            }
        }
    }
       
    @Override
    public void mousePressed(MouseEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}