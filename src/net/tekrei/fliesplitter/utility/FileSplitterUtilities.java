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
package net.tekrei.fliesplitter.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.channels.FileChannel;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.tekrei.filesplitter.gui.MainFrame;
import net.tekrei.filesplitter.gui.Messages;

public class FileSplitterUtilities {
	private static long SIZE_MULTIPLIER = 1000;
	public static final String[] SIZE_INFO = { "B", //$NON-NLS-1$
			"KB", //$NON-NLS-1$
			"MB", //$NON-NLS-1$
			"GB" };
	private MainFrame mainFrame;
	private JFileChooser fileChooser;
	private File selectedFile;

	public FileSplitterUtilities(MainFrame _anaPencere) {
		this.mainFrame = _anaPencere;
	}

	public void selectFile() {
		// create file chooser if it not created yet
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
		}

		if (fileChooser.showDialog(null,
				Messages.getInstance().getString("DosyaParcalayici.Sec")) == JFileChooser.APPROVE_OPTION) { //$NON-NLS-1$

			// Select file to split/join
			selectedFile = fileChooser.getSelectedFile();
			mainFrame.setSelectedFile(selectedFile.getAbsolutePath());
		}
	}

	public void join() {
		// check extension of selected file
		int selectedFileExtension = getFileExtension(selectedFile.getName());

		if (selectedFileExtension >= 0) {
			mainFrame.operationStarted(Messages.getInstance().getString("DosyaParcalayici.Birlestirme")); //$NON-NLS-1$

			// we should join all files
			// find resulting file name
			String fileName = fileNameToJoin(selectedFile.getName());

			if (fileName != null) {
				String filePath = getPath(selectedFile.getAbsolutePath());

				// join files
				File newFile = new File(filePath + File.separatorChar + fileName);

				try {
					FileOutputStream os = new FileOutputStream(newFile, true);
					FileChannel outCh = os.getChannel();

					long position = 0;

					while (selectedFile.exists()) {
						FileInputStream in = new FileInputStream(selectedFile);
						mainFrame.notify(selectedFile.getName()
								+ Messages.getInstance().getString("DosyaParcalayici.DosyaEkleniyor")); //$NON-NLS-1$

						try {
							FileChannel inCh = in.getChannel();

							// add each part to the end of the new file
							position += outCh.transferFrom(inCh, position, inCh.size());

							inCh.close();
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

						// arrange the next file
						selectedFile = new File(
								filePath + File.separatorChar + fileName + getExtension(++selectedFileExtension));
					}

					outCh.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			mainFrame.operationFinished(Messages.getInstance().getString("DosyaParcalayici.Birlestirme")); //$NON-NLS-1$
		}

		// extension is not a number
		// show error to the user
		JOptionPane.showMessageDialog(mainFrame, Messages.getInstance().getString("DosyaParcalayici.ParcaDosyaHata")); //$NON-NLS-1$
	}

	private String fileNameToJoin(String fileName) {
		// find last position of the dot (.)
		int dotPosition = fileName.lastIndexOf("."); //$NON-NLS-1$

		if (dotPosition >= 0) {
			return fileName.substring(0, dotPosition);
		}

		return null;
	}

	private String getPath(String fileName) {
		// find last position of the path separator and return the path
		int lastSeparatorIndex = fileName.lastIndexOf(File.separatorChar);

		return fileName.substring(0, lastSeparatorIndex);
	}

	private int getFileExtension(String dosyaIsmi) {
		// Bunu nasil kontrol edecegiz?
		// Eger uzantisi int sayiya donusebiliyorsa bu parca dosyadir
		int noktaKonum = dosyaIsmi.lastIndexOf("."); //$NON-NLS-1$

		if (noktaKonum >= 0) {
			String noktadanSonra = dosyaIsmi.substring(noktaKonum + 1);

			try {
				int noktadanSonraInt = Integer.parseInt(noktadanSonra);

				return noktadanSonraInt;
			} catch (NumberFormatException e) {
				return -1;
			}
		}

		return -1;
	}

	public void split(String expectedSize, String sizeType) {
		long sizeLimit = calculateSize(expectedSize, sizeType);

		if (selectedFile.length() <= sizeLimit) {
			JOptionPane.showMessageDialog(mainFrame, Messages.getInstance().getString("DosyaParcalayici.SinirHata")); //$NON-NLS-1$
			return;
		}

		mainFrame.operationStarted(Messages.getInstance().getString("DosyaParcalayici.Parcalama")); //$NON-NLS-1$

		// Find number of pieces
		mainFrame.notify(Messages.getInstance().getString("DosyaParcalayici.DosyaBoyutMesaj") + //$NON-NLS-1$
				selectedFile.length());

		float f = (float) selectedFile.length() / (float) sizeLimit;
		long fileCount = (long) Math.ceil(f);
		mainFrame.notify(Messages.getInstance().getString("DosyaParcalayici.DosyaAdediMesaj") + //$NON-NLS-1$
				fileCount);

		// start splitting the file
		try {
			FileInputStream in = new FileInputStream(selectedFile);
			FileChannel inCh = in.getChannel();

			long fileSize = selectedFile.length();

			for (int i = 0; i < fileCount; i++) {
				File newFile = new File(selectedFile.getAbsolutePath() + getExtension(i));

				if (newFile.exists() && (!fileExistsError(newFile.getAbsolutePath()))) {
					in.close();
					mainFrame.operationFinished(Messages.getInstance().getString("DosyaParcalayici.Parcalama")); //$NON-NLS-1$

					return;
				}

				mainFrame.notify(
						newFile.getName() + Messages.getInstance().getString("DosyaParcalayici.DosyaYaratiliyor")); //$NON-NLS-1$

				try {
					FileOutputStream out = new FileOutputStream(newFile);

					FileChannel outCh = out.getChannel();

					if (fileSize <= sizeLimit) {
						sizeLimit = fileSize;
					}

					fileSize -= outCh.transferFrom(inCh, 0, sizeLimit);

					outCh.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			inCh.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainFrame, Messages.getInstance().getString("DosyaParcalayici.HataMesaj") + //$NON-NLS-1$
					e.getMessage());
		}

		mainFrame.operationFinished(Messages.getInstance().getString("DosyaParcalayici.Parcalama")); //$NON-NLS-1$
	}

	private boolean fileExistsError(String dosyaAdi) {
		JOptionPane.showMessageDialog(mainFrame,
				dosyaAdi + Messages.getInstance().getString("DosyaParcalayici.UstuneYazmaSorgu")); //$NON-NLS-1$

		return false;
	}

	private String getExtension(int i) {
		String extension = " "; //$NON-NLS-1$

		if (i < 10) {
			extension = "00" + //$NON-NLS-1$
					i;
		} else if (i < 100) {
			extension = "0" + //$NON-NLS-1$
					i;
		} else {
			extension = " " + //$NON-NLS-1$
					i;
		}

		return "." + //$NON-NLS-1$
				extension;
	}

	private long calculateSize(String boyut, String boyutTipi) {
		long result = 0;

		if (boyutTipi.equals(SIZE_INFO[0])) {
			result = Long.parseLong(boyut);
		} else if (boyutTipi.equals(SIZE_INFO[1])) {
			result = Long.parseLong(boyut) * SIZE_MULTIPLIER;
		} else if (boyutTipi.equals(SIZE_INFO[2])) {
			result = Long.parseLong(boyut) * SIZE_MULTIPLIER * SIZE_MULTIPLIER;
		} else if (boyutTipi.equals(SIZE_INFO[3])) {
			result = Long.parseLong(boyut) * SIZE_MULTIPLIER * SIZE_MULTIPLIER * SIZE_MULTIPLIER;
		}
		return result;
	}
}
