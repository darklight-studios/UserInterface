package com.isaacjg.darklight.plugins.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ijg.darklight.sdk.core.Plugin;
import com.ijg.darklight.sdk.core.PluginHandler;

/*
 * UserInterface - A plugin for Darklight Nova Core
 * Copyright © 2013 Isaac Grant
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

/**
 * UserInterface is a plugin for Darklight Nova Core that is
 * a graphical user interface to visualize progress on a simulation
 * session.
 * 
 * @author Isaac Grant
 */
public class UserInterface extends Plugin {

	private final int WIDTH = 300;
	private final int HEIGHT = 200;
	private final String TITLE = "Darklight UserInterface";
	private final String TOTAL_TEXT = "Total Issues:";
	private final String FOUND_TEXT = "Found Issues:";
	private final String PERCENT_TEXT = "Percent Complete:";
	
	private JFrame frame;
	private JPanel panel;
	private JButton refresh, finish;
	private JLabel totalLabel, foundLabel, percentLabel;
	private JLabel total, found, percent;
	
	public UserInterface(PluginHandler handler) {
		super(handler);
		handler.accessHandler.setAutoUpdate(false);
	}
	
	@Override
	protected void start() {
		setupAndDisplayGUI();
	}
	
	@Override
	protected void kill() {
		frame.dispose();
	}
	
	private void update() {
		if (pluginHandler.accessHandler.isFinished()) {
			found.setForeground(Color.green);
			percent.setForeground(Color.green);
		} else {
			found.setForeground(Color.black);
			percent.setForeground(Color.black);
		}
		
		found.setText("" + pluginHandler.accessHandler.getFixedIssueCount());
		percent.setText(pluginHandler.accessHandler.getPercentComplete());
	}
	
	private void setupAndDisplayGUI() {
		frame = new JFrame(TITLE);
		panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		refresh = new JButton("Refresh");
		refresh.setActionCommand("refresh");
		new UIListener(this, refresh);
		
		finish = new JButton("End Session");
		finish.setActionCommand("finish");
		new UIListener(this, finish);
		
		totalLabel = new JLabel(TOTAL_TEXT);
		foundLabel = new JLabel(FOUND_TEXT);
		percentLabel = new JLabel(PERCENT_TEXT);
		
		total = new JLabel("" + pluginHandler.accessHandler.getTotalIssueCount());
		found = new JLabel("0");
		percent = new JLabel("0%");
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		panel.add(totalLabel, c);
		
		c.gridy = 1;
		panel.add(foundLabel, c);
		
		c.gridy = 2;
		panel.add(percentLabel, c);
		
		c.gridx = 3;
		c.gridy = 0;
		panel.add(total, c);
		
		c.gridy = 1;
		panel.add(found, c);
		
		c.gridy = 2;
		panel.add(percent, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(0, 5, 0, 5);
		panel.add(refresh, c);
		
		c.gridx = 3;
		c.ipadx = 0;
		c.insets = new Insets(0, 5, 0, 5);
		panel.add(finish, c);
		
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocation(500, 500);
		frame.setVisible(true);
		frame.pack();
		update();
	}
	
	private class UIListener implements MouseListener {
		
		private JButton button;
		private UserInterface ui;
		
		public UIListener(UserInterface ui, JButton button) {
			this.button = button;
			this.ui = ui;
			button.addMouseListener(this);
		}
		
		public void mouseClicked(MouseEvent e) {
			if (button.contains(e.getPoint())) {
				switch (button.getActionCommand()) {
				case "refresh":
					ui.pluginHandler.accessHandler.checkIssues();
					ui.update();
					break;
				case "finish":
					ui.pluginHandler.accessHandler.finishSession();
				}
			}
		}
		
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
}
