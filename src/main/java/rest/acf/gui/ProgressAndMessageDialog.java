/*
 * ProgressAndMessageDialog.java
 *
 * (c) by Ollie
 *
 * 08.09.2019
 */
package rest.acf.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import corent.files.Inifile;
import corent.gui.JDialogWithInifile;

/**
 * A dialog which is able to show progress and messages about the generation process.
 *
 * @author ollie
 *
 */
public class ProgressAndMessageDialog extends JDialogWithInifile {

	private static final int HGAP = 3;
	private static final int VGAP = 3;

	private JLabel labelProgress = new JLabel("Progress of Generation");
	private JLabel labelMessages = new JLabel("Messages");
	private JProgressBar progressBar = new JProgressBar(0, 100);
	private JTextArea textAreaMessages = new JTextArea(10, 80);

	public ProgressAndMessageDialog(JFrame f, Inifile ini) {
		super(f, "Generation Progress", ini);
		this.setContentPane(createMainPanel());
		this.pack();
		this.setVisible(true);
		centerDialog();
	}

	private JPanel createMainPanel() {
		JPanel p = new JPanel(new BorderLayout(HGAP, VGAP));
		p.add(createProgressPanel(), BorderLayout.NORTH);
		p.add(createMessagesPanel(), BorderLayout.CENTER);
		return p;
	}

	private JPanel createProgressPanel() {
		JPanel p = new JPanel(new GridLayout(2, 1, HGAP, VGAP));
		p.add(this.labelProgress);
		p.add(this.progressBar);
		return p;
	}

	private JPanel createMessagesPanel() {
		JPanel p = new JPanel(new BorderLayout(HGAP, VGAP));
		p.add(this.labelMessages, BorderLayout.NORTH);
		p.add(this.textAreaMessages, BorderLayout.CENTER);
		return p;
	}

	private void centerDialog() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
	}

}