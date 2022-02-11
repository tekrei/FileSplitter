/*
 * DosyaParcalayici (FileSplitter)
 * 
 * Experimental program to split and join big files
 * 
 * Boyutu buyuk dosyalari tasinmayi kolaylastirmak icin belirlenen
 * boyutta parcalara ayirir, daha onceden parcalanmis dosyalari birlestirir
 * 
 * 2006
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
package filesplitter.gui;

import filesplitter.utility.FileSplitterUtilities;
import filesplitter.utility.Messages;

import javax.swing.*;
import java.io.File;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton btnSelectFile;
    private JTextField txtFileName;
    private JLabel lblFileSize;
    private JButton btnSplit;
    private FileSplitterUtilities fileSplitter;
    private JTextArea txtNotification;
    private JTextField txtSize;
    private JComboBox<String> cmbSizeType;
    private JPanel jcp;

    private MainFrame() {
        this(null);
    }

    private MainFrame(String language) {
        super();
        initialize(language);
        this.setResizable(false);
        this.setLocationByPlatform(true);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame();
    }

    private void initialize(String language) {
        if (language != null) {
            Messages.initialize(language);
        } else {
            Messages.initialize(Messages.EN);
        }

        fileSplitter = new FileSplitterUtilities(this);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setResizable(false);
        generateUI();
    }

    private void generateUI() {
        this.setContentPane(getUI());
        this.setJMenuBar(generateMenuBar());
        this.setTitle(Messages.getString(Messages.mainFrameTitle));
        reset();
    }

    private void changeLanguage(String language) {
        Messages.initialize(language);
        generateUI();
    }

    private JPanel getUI() {
        jcp = new JPanel(null);

        txtFileName = new JTextField();
        txtFileName.setBounds(new java.awt.Rectangle(5, 5, 270, 20));
        txtFileName.setEditable(false);
        jcp.add(txtFileName);

        btnSelectFile = new JButton();
        btnSelectFile.setBounds(new java.awt.Rectangle(275, 5, 120, 20));
        btnSelectFile.setText(Messages.getString(Messages.selectFile));
        btnSelectFile.setToolTipText(Messages.getString(Messages.selectFileTooltip));
        btnSelectFile.setMnemonic('s');
        btnSelectFile.addActionListener(e -> fileSplitter.selectFile());
        jcp.add(btnSelectFile);

        lblFileSize = new JLabel();
        lblFileSize.setBounds(new java.awt.Rectangle(5, 25, 390, 20));
        lblFileSize.setText(String.format(Messages.getString(Messages.sizeInfo), ""));
        jcp.add(lblFileSize);

        btnSplit = new JButton();
        btnSplit.setBounds(new java.awt.Rectangle(5, 70, 191, 20));
        btnSplit.setToolTipText(Messages.getString(Messages.splitTooltip));
        btnSplit.setText(Messages.getString(Messages.split));
        btnSplit.setMnemonic('p');
        btnSplit.addActionListener(e -> {
            if (txtSize.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, Messages.getString(Messages.sizeWarning));
            } else {
                fileSplitter.split(txtSize.getText(), cmbSizeType.getItemAt(cmbSizeType.getSelectedIndex()));
            }
        });
        jcp.add(btnSplit);

        JButton btnJoin = new JButton();
        btnJoin.setBounds(new java.awt.Rectangle(200, 70, 191, 20));
        btnJoin.setText(Messages.getString(Messages.join));
        btnJoin.setToolTipText(Messages.getString(Messages.joinTooltip));
        btnJoin.setMnemonic('i');
        btnJoin.addActionListener(e -> fileSplitter.join());
        jcp.add(btnJoin);

        txtNotification = new JTextArea();
        txtNotification.setEditable(false);
        JScrollPane scrNotification = new JScrollPane(txtNotification);
        scrNotification.setBounds(new java.awt.Rectangle(10, 100, 371, 131));
        jcp.add(scrNotification);

        JLabel lblDosyaBoyut = new JLabel();
        lblDosyaBoyut.setBounds(new java.awt.Rectangle(5, 45, 111, 20));
        lblDosyaBoyut.setText(Messages.getString(Messages.sizeTag));
        jcp.add(lblDosyaBoyut);

        txtSize = new JTextField();
        txtSize.setBounds(new java.awt.Rectangle(120, 45, 191, 20));
        txtSize.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jcp.add(txtSize);

        cmbSizeType = new JComboBox<>(FileSplitterUtilities.SIZE_INFO);
        cmbSizeType.setBounds(new java.awt.Rectangle(320, 45, 71, 20));
        jcp.add(cmbSizeType);

        return jcp;
    }

    private void reset() {
        txtSize.setText("100");
        txtFileName.setText("");
        txtNotification.setText("");
        lblFileSize.setText(String.format(Messages.getString(Messages.sizeInfo), ""));
        cmbSizeType.setSelectedIndex(2);
        btnSelectFile.grabFocus();
        jcp.updateUI();
    }

    public void notify(String duyuru) {
        txtNotification.setText(txtNotification.getText() + duyuru + System.getProperty("line.separator"));
        this.getContentPane().update(this.getContentPane().getGraphics());
    }

    public void setSelectedFile(File selectedFile) {
        txtFileName.setText(selectedFile.getAbsolutePath());
        lblFileSize.setText(String.format(Messages.getString(Messages.sizeInfo), (selectedFile.length() / 1000000) + " Mb"));
    }

    public void operationStarted(String islem) {
        notify(islem + " " + Messages.getString(Messages.started));
        btnSplit.setEnabled(false);
    }

    public void operationFinished(String islem) {
        notify(islem + " " + Messages.getString(Messages.complete));
        btnSplit.setEnabled(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menu = new JMenuBar();
        menu.add(generateDosyaMenu());
        menu.add(generateLanguageMenu());

        return menu;
    }

    private JMenu generateLanguageMenu() {
        JMenu mnDil = new JMenu(Messages.getString(Messages.language));

        ButtonGroup btnGrp = new ButtonGroup();

        JRadioButtonMenuItem item = new JRadioButtonMenuItem(Messages.getString(Messages.turkish));
        btnGrp.add(item);

        if (Messages.getLanguage().equals(Messages.TR)) {
            item.setSelected(true);
        }

        item.addActionListener(e -> changeLanguage(Messages.TR));
        mnDil.add(item);

        item = new JRadioButtonMenuItem(Messages.getString(Messages.english));

        if (Messages.getLanguage().equals(Messages.EN)) {
            item.setSelected(true);
        }

        item.addActionListener(e -> changeLanguage(Messages.EN));
        btnGrp.add(item);
        mnDil.add(item);

        return mnDil;
    }

    private JMenu generateDosyaMenu() {
        JMenu mnDosya = new JMenu(Messages.getString(Messages.file));

        JMenuItem item = new JMenuItem(Messages.getString(Messages.about));
        item.addActionListener(e -> JOptionPane.showMessageDialog(null, Messages.getString(Messages.license)));
        mnDosya.add(item);

        item = new JMenuItem(Messages.getString(Messages.clear));
        item.addActionListener(e -> reset());
        mnDosya.add(item);

        item = new JMenuItem(Messages.getString(Messages.exit));
        item.addActionListener(e -> System.exit(0));
        mnDosya.add(item);

        return mnDosya;
    }
}