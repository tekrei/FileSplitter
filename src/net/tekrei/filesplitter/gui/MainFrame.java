/*
 * DosyaParcalayici (FileSplitter)
 * 
 * Experimental program to split and join big files
 * 
 * Boyutu buyuk dosyalari tasinmayi kolaylastirmak icin belirlenen
 * boyutta parcalara ayirir, daha onceden parcalanmis dosyalari birlestirir
 * 
 * 2006 Tahir Emre KALAYCI
 * 
 * This file is a part of DosyaParcalayici
 * 
 * DosyaParcalayici is a free software: you can redistribute it and/or modify
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
 *
 */
package net.tekrei.filesplitter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.tekrei.fliesplitter.utility.FileSplitterUtilities;
import net.tekrei.fliesplitter.utility.Messages;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel jcp = null;
	private JButton btnSelectFile;
	private JTextField txtFileName;
	private JButton btnSplit;
	private JButton btnJoin;
	private FileSplitterUtilities fileSplitter;
	private JTextArea txtNotification;
	private JScrollPane scrNotification;
	private JTextField txtSize;
	private JComboBox<String> cmbSizeType;

	public MainFrame() {
		super();
		initialize(null);
		this.setResizable(false);
		this.setLocationByPlatform(true);
		this.setVisible(true);
	}

	public MainFrame(String language) {
		super();
		initialize(language);
		this.setVisible(true);
	}

	private void initialize(String language) {
		if (language != null) {
			Messages.getInstance().initialize(language);
		} else {
			Messages.getInstance().initialize(Messages.EN);
		}

		fileSplitter = new FileSplitterUtilities(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(397, 282);
		this.setResizable(false);
		generateUI();
	}

	private void generateUI() {
		this.setContentPane(getJcp());
		this.setJMenuBar(generateMenuBar());
		this.setTitle(Messages.getInstance().getString(Messages.mainFrameTitle));
		reset();
	}

	private void dilDegistir(String language) {
		Messages.getInstance().initialize(language);
		this.remove(jcp);
		jcp = null;
		generateUI();
	}

	private JPanel getJcp() {
		if (jcp == null) {
			jcp = new JPanel();
			jcp.setLayout(null);
			jcp.add(getBtnDosyaSec(), null);
			jcp.add(getTxtDosyaIsmi(), null);
			jcp.add(getBtnParcala(), null);
			jcp.add(getBtnBirlestir(), null);
			jcp.add(getTxtDuyuru(), null);
			jcp.add(getLblDosyaBoyut(), null);
			jcp.add(getTxtBoyut(), null);
			jcp.add(getCmbBoyutTipi(), null);
		}

		return jcp;
	}

	private JLabel getLblDosyaBoyut() {
		JLabel lblDosyaBoyut = new JLabel();
		lblDosyaBoyut.setBounds(new java.awt.Rectangle(0, 40, 111, 21));
		lblDosyaBoyut.setText(Messages.getInstance().getString(Messages.sizeTag));

		return lblDosyaBoyut;
	}

	private void reset() {
		txtSize.setText("100");
		txtFileName.setText("");
		txtNotification.setText("");
		cmbSizeType.setSelectedIndex(2);
		btnSelectFile.grabFocus();
		jcp.updateUI();
	}

	private JButton getBtnDosyaSec() {
		btnSelectFile = new JButton();
		btnSelectFile.setBounds(new java.awt.Rectangle(280, 10, 111, 21));
		btnSelectFile.setText(Messages.getInstance().getString(Messages.selectFile));
		btnSelectFile.setToolTipText(Messages.getInstance().getString(Messages.selectFileTooltip));
		btnSelectFile.setMnemonic('s');
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileSplitter.selectFile();
			}
		});

		return btnSelectFile;
	}

	private JTextField getTxtDosyaIsmi() {
		txtFileName = new JTextField();
		txtFileName.setBounds(new java.awt.Rectangle(0, 10, 281, 21));
		txtFileName.setEditable(false);

		return txtFileName;
	}

	private JButton getBtnParcala() {
		btnSplit = new JButton();
		btnSplit.setBounds(new java.awt.Rectangle(0, 70, 191, 21));
		btnSplit.setToolTipText(Messages.getInstance().getString(Messages.splitTooltip));
		btnSplit.setText(Messages.getInstance().getString(Messages.split));
		btnSplit.setMnemonic('p');
		btnSplit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtSize.getText().equals("")) {
					JOptionPane.showMessageDialog(null, Messages.getInstance().getString(Messages.sizeWarning));
				} else {
					fileSplitter.split(txtSize.getText(), cmbSizeType.getSelectedItem().toString());
				}
			}
		});

		return btnSplit;
	}

	private JButton getBtnBirlestir() {
		btnJoin = new JButton();
		btnJoin.setBounds(new java.awt.Rectangle(200, 70, 191, 21));
		btnJoin.setText(Messages.getInstance().getString(Messages.join));
		btnJoin.setToolTipText(Messages.getInstance().getString(Messages.joinTooltip));
		btnJoin.setMnemonic('i');
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileSplitter.join();
			}
		});

		return btnJoin;
	}

	private JScrollPane getTxtDuyuru() {
		txtNotification = new JTextArea();
		txtNotification.setEditable(false);
		scrNotification = new JScrollPane(txtNotification);
		scrNotification.setBounds(new java.awt.Rectangle(10, 100, 371, 131));

		return scrNotification;
	}

	private JTextField getTxtBoyut() {
		txtSize = new JTextField();
		txtSize.setBounds(new java.awt.Rectangle(120, 40, 191, 21));
		txtSize.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		return txtSize;
	}

	private JComboBox<String> getCmbBoyutTipi() {
		cmbSizeType = new JComboBox<String>(FileSplitterUtilities.SIZE_INFO);
		cmbSizeType.setBounds(new java.awt.Rectangle(320, 40, 71, 21));

		return cmbSizeType;
	}

	public void notify(String duyuru) {
		txtNotification.setText(txtNotification.getText() + duyuru + System.getProperty("line.separator"));
		this.getContentPane().update(this.getContentPane().getGraphics());
	}

	public void setSelectedFile(String absolutePath) {
		txtFileName.setText(absolutePath);
	}

	public void operationStarted(String islem) {
		notify(islem + " " + Messages.getInstance().getString(Messages.started));
		btnSplit.setEnabled(false);
	}

	public void operationFinished(String islem) {
		notify(islem + " " + Messages.getInstance().getString(Messages.complete));
		btnSplit.setEnabled(true);
	}

	private JMenuBar generateMenuBar() {
		JMenuBar menu = new JMenuBar();
		menu.add(generateDosyaMenu());
		menu.add(generateLanguageMenu());

		return menu;
	}

	private JMenu generateLanguageMenu() {
		JMenu mnDil = new JMenu(Messages.getInstance().getString(Messages.language));

		ButtonGroup btnGrp = new ButtonGroup();

		JRadioButtonMenuItem item = new JRadioButtonMenuItem(Messages.getInstance().getString(Messages.turkish));
		btnGrp.add(item);

		if (Messages.getInstance().getLanguage().equals(Messages.TR)) {
			item.setSelected(true);
		}

		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dilDegistir(Messages.TR);
			}
		});
		mnDil.add(item);

		item = new JRadioButtonMenuItem(Messages.getInstance().getString(Messages.english));

		if (Messages.getInstance().getLanguage().equals(Messages.EN)) {
			item.setSelected(true);
		}

		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dilDegistir(Messages.EN);
			}
		});
		btnGrp.add(item);
		mnDil.add(item);

		return mnDil;
	}

	private JMenu generateDosyaMenu() {
		JMenu mnDosya = new JMenu(Messages.getInstance().getString(Messages.file));

		JMenuItem item = new JMenuItem(Messages.getInstance().getString(Messages.about));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, Messages.getInstance().getString(Messages.license));
			}
		});
		mnDosya.add(item);

		item = new JMenuItem(Messages.getInstance().getString(Messages.clear));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		mnDosya.add(item);

		item = new JMenuItem(Messages.getInstance().getString(Messages.exit));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnDosya.add(item);

		return mnDosya;
	}

	public static void main(String[] args) {
		new MainFrame();
	}
} // @jve:decl-index=0:visual-constraint="10,10"
