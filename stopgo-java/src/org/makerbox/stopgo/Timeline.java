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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Timeline extends JPanel {
   
   private final JPanel panel;
   
   public Timeline() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.cyan);
        //panel.setPreferredSize(new Dimension(640, 300));
        this.add(panel);
        
//        JLabel imglabel = new JLabel();
//        ImageIcon imageIcon = new ImageIcon(new ImageIcon("/tmp/test.jpg").getImage().getScaledInstance(180, 128, Image.SCALE_DEFAULT));
//        imglabel.setIcon(imageIcon);
        JLabel imglabel = new Picture("/tmp/test.jpg");
        this.add(imglabel);
        
        this.setVisible(true);
   }
   
   public static void main(String args[]) {
       Timeline timeline = new Timeline();
     }
   }