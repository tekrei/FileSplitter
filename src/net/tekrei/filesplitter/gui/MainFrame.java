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

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel jcp = null;
    private JButton btnDosyaSec;
    private JTextField txtDosyaIsmi;
    private JButton btnParcala;
    private JButton btnBirlestir;
    private FileSplitterUtilities dosyaParcalayici;
    private JTextArea txtDuyuru;
    private JScrollPane scrDuyuru;
    private JTextField txtBoyut;
    private JComboBox<String> cmbBoyutTipi;

    public MainFrame() {
        super();
        initialize(null);
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

        dosyaParcalayici = new FileSplitterUtilities(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(397, 282);
        this.setResizable(false);
        generateUI();
    }

    private void generateUI() {
        this.setContentPane(getJcp());
        this.setJMenuBar(generateMenuBar());
        this.setTitle(Messages.getInstance().getString("AnaPencere.Baslik")); //$NON-NLS-1$
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
        lblDosyaBoyut.setText(Messages.getInstance().getString("AnaPencere.ParcaBoyutuEtiket")); //$NON-NLS-1$

        return lblDosyaBoyut;
    }

    private void reset() {
        txtBoyut.setText(""); //$NON-NLS-1$
        txtDosyaIsmi.setText(""); //$NON-NLS-1$
        txtDuyuru.setText(""); //$NON-NLS-1$
        cmbBoyutTipi.setSelectedIndex(1);
        btnDosyaSec.grabFocus();
        jcp.updateUI();
    }

    private JButton getBtnDosyaSec() {
        btnDosyaSec = new JButton();
        btnDosyaSec.setBounds(new java.awt.Rectangle(280, 10, 111, 21));
        btnDosyaSec.setText(Messages.getInstance().getString("AnaPencere.DosyaSecEtiket")); //$NON-NLS-1$
        btnDosyaSec.setToolTipText(Messages.getInstance().getString("AnaPencere.DosyaSecTooltip")); //$NON-NLS-1$
        btnDosyaSec.setMnemonic('s');
        btnDosyaSec.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dosyaParcalayici.selectFile();
                }
            });

        return btnDosyaSec;
    }

    private JTextField getTxtDosyaIsmi() {
        txtDosyaIsmi = new JTextField();
        txtDosyaIsmi.setBounds(new java.awt.Rectangle(0, 10, 281, 21));
        txtDosyaIsmi.setEditable(false);

        return txtDosyaIsmi;
    }

    private JButton getBtnParcala() {
        btnParcala = new JButton();
        btnParcala.setBounds(new java.awt.Rectangle(0, 70, 191, 21));
        btnParcala.setToolTipText(Messages.getInstance().getString("AnaPencere.ParcalaTooltip")); //$NON-NLS-1$
        btnParcala.setText(Messages.getInstance().getString("AnaPencere.ParcalaEtiket")); //$NON-NLS-1$
        btnParcala.setMnemonic('p');
        btnParcala.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (txtBoyut.getText().equals("")) { //$NON-NLS-1$
                        JOptionPane.showMessageDialog(null,
                            Messages.getInstance().getString("AnaPencere.BoyutSiniriUyari")); //$NON-NLS-1$
                    } else {
                        dosyaParcalayici.split(txtBoyut.getText(),
                            cmbBoyutTipi.getSelectedItem().toString());
                    }
                }
            });

        return btnParcala;
    }

    private JButton getBtnBirlestir() {
        btnBirlestir = new JButton();
        btnBirlestir.setBounds(new java.awt.Rectangle(200, 70, 191, 21));
        btnBirlestir.setText(Messages.getInstance().getString("AnaPencere.BirlestirEtiket")); //$NON-NLS-1$
        btnBirlestir.setToolTipText(Messages.getInstance().getString("AnaPencere.BirlestirTooltip")); //$NON-NLS-1$
        btnBirlestir.setMnemonic('i');
        btnBirlestir.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dosyaParcalayici.join();
                }
            });

        return btnBirlestir;
    }

    private JScrollPane getTxtDuyuru() {
        txtDuyuru = new JTextArea();
        txtDuyuru.setEditable(false);
        scrDuyuru = new JScrollPane(txtDuyuru);
        scrDuyuru.setBounds(new java.awt.Rectangle(10, 100, 371, 131));

        return scrDuyuru;
    }

    private JTextField getTxtBoyut() {
        txtBoyut = new JTextField();
        txtBoyut.setBounds(new java.awt.Rectangle(120, 40, 191, 21));
        txtBoyut.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        return txtBoyut;
    }

    private JComboBox<String> getCmbBoyutTipi() {
        cmbBoyutTipi = new JComboBox<String>(FileSplitterUtilities.SIZE_INFO);
        cmbBoyutTipi.setBounds(new java.awt.Rectangle(320, 40, 71, 21));
        cmbBoyutTipi.setSelectedIndex(1);

        return cmbBoyutTipi;
    }

    public void notify(String duyuru) {
        txtDuyuru.setText(txtDuyuru.getText() + duyuru + "\n"); //$NON-NLS-1$
        this.getContentPane().update(this.getContentPane().getGraphics());
    }

    public void setSelectedFile(String absolutePath) {
        txtDosyaIsmi.setText(absolutePath);
    }

    public void operationStarted(String islem) {
        notify(islem + " " + //$NON-NLS-1$
            Messages.getInstance().getString("AnaPencere.IslemBasladi")); //$NON-NLS-1$
        btnParcala.setEnabled(false);
    }

    public void operationFinished(String islem) {
        notify(islem + " " + //$NON-NLS-1$
            Messages.getInstance().getString("AnaPencere.IslemTamamlandi")); //$NON-NLS-1$
        btnParcala.setEnabled(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menu = new JMenuBar();
        menu.add(generateDosyaMenu());
        menu.add(generateDilMenu());

        return menu;
    }

    private JMenu generateDilMenu() {
        JMenu mnDil = new JMenu(Messages.getInstance().getString("AnaPencere.Dil")); //$NON-NLS-1$

        ButtonGroup btnGrp = new ButtonGroup();

        JRadioButtonMenuItem item = new JRadioButtonMenuItem(Messages.getInstance()
                                                                     .getString("AnaPencere.Turkce")); //$NON-NLS-1$
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

        item = new JRadioButtonMenuItem(Messages.getInstance().getString("AnaPencere.Ingilizce")); //$NON-NLS-1$

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
        JMenu mnDosya = new JMenu(Messages.getInstance().getString("AnaPencere.Dosya")); //$NON-NLS-1$

        JMenuItem item = new JMenuItem(Messages.getInstance().getString("AnaPencere.Hakkinda")); //$NON-NLS-1$
        item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null,
                        Messages.getInstance().getString("AnaPencere.Lisans")); //$NON-NLS-1$
                }
            });
        mnDosya.add(item);

        item = new JMenuItem(Messages.getInstance().getString("AnaPencere.Temizle")); //$NON-NLS-1$
        item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    reset();
                }
            });
        mnDosya.add(item);

        item = new JMenuItem(Messages.getInstance().getString("AnaPencere.Cikis")); //$NON-NLS-1$
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
} //  @jve:decl-index=0:visual-constraint="10,10"
