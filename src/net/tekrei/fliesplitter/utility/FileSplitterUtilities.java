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

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.security.MessageDigest;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.tekrei.filesplitter.gui.MainFrame;

public class FileSplitterUtilities {
	private static long SIZE_MULTIPLIER = 1000;
	public static final String[] SIZE_INFO = { "B", "KB", "MB", "GB" };
	private MainFrame mainFrame;
	private JFileChooser fileChooser;
	private File selectedFile;
	public static String CHECKSUM_EXTENSION = ".checksum.txt";

	public FileSplitterUtilities(MainFrame _anaPencere) {
		this.mainFrame = _anaPencere;
	}

	public void selectFile() {
		// create file chooser if it not created yet
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
		}

		if (fileChooser.showDialog(mainFrame,
				Messages.getInstance().getString(Messages.select)) == JFileChooser.APPROVE_OPTION) {
			// file to split/join
			selectedFile = fileChooser.getSelectedFile();
			mainFrame.setSelectedFile(selectedFile.getAbsolutePath());
		}
	}

	/**
	 * Source : http://www.rgagnon.com/javadetails/java-0416.html
	 * https://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-
	 * in-java
	 */
	private byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	/**
	 * Source : http://www.rgagnon.com/javadetails/java-0416.html
	 * https://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-
	 * in-java
	 */
	public String getMD5Checksum(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		String result = "";

		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	public void join() {
		// check extension of selected file
		int selectedFileExtension = getFileExtension(selectedFile.getName());

		if (selectedFileExtension >= 0) {
			mainFrame.operationStarted(Messages.getInstance().getString(Messages.joining));

			// find resulting file name
			String fileName = fileNameToJoin(selectedFile.getName());

			if (fileName != null) {
				String filePath = getPath(selectedFile.getAbsolutePath());

				// join files
				File newFile = new File(filePath + File.separatorChar + fileName);
				
				if(newFile.exists()){
					JOptionPane.showMessageDialog(mainFrame, Messages.getInstance().getString(Messages.fileExistsError)+newFile.getAbsolutePath());
					mainFrame.operationFinished(Messages.getInstance().getString(Messages.joining));
					return;
				}

				try {
					FileOutputStream os = new FileOutputStream(newFile, true);
					FileChannel outCh = os.getChannel();

					long position = 0;

					while (selectedFile.exists()) {
						FileInputStream in = new FileInputStream(selectedFile);
						mainFrame
								.notify(selectedFile.getName() + Messages.getInstance().getString(Messages.fileAdding));

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
					// check checksum if file exists
					try {
						File checksumFile = new File(newFile.getAbsolutePath() + CHECKSUM_EXTENSION);
						String checksum = new String(Files.readAllBytes((checksumFile.toPath())));
						String newChecksum = getMD5Checksum(newFile.getAbsolutePath());
						if (!checksum.equals(newChecksum)) {
							mainFrame.notify(Messages.getInstance().getString(Messages.checksumError) + checksum + "|"
									+ newChecksum);
						} else {
							mainFrame.notify(Messages.getInstance().getString(Messages.checksumOK));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			mainFrame.operationFinished(Messages.getInstance().getString(Messages.joining));
		} else {
			// extension is not a number
			// show error to the user
			JOptionPane.showMessageDialog(mainFrame, Messages.getInstance().getString(Messages.partError));
		}
	}

	private String fileNameToJoin(String fileName) {
		// find last position of the dot (.)
		int dotPosition = fileName.lastIndexOf(".");

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

	private int getFileExtension(String fileName) {
		int dotPosition = fileName.lastIndexOf(".");

		if (dotPosition >= 0) {
			String afterDot = fileName.substring(dotPosition + 1);
			try {
				return Integer.parseInt(afterDot);
			} catch (NumberFormatException e) {
				return -1;
			}
		}
		return -1;
	}

	public void split(String expectedSize, String sizeType) {
		long sizeLimit = calculateSize(expectedSize, sizeType);
		if (selectedFile.length() <= sizeLimit) {
			JOptionPane.showMessageDialog(mainFrame, Messages.getInstance().getString(Messages.limitError));
			return;
		}

		mainFrame.operationStarted(Messages.getInstance().getString(Messages.splitting));
		mainFrame.notify(Messages.getInstance().getString(Messages.fileSize) + selectedFile.length());

		// Find number of pieces
		float f = (float) selectedFile.length() / (float) sizeLimit;
		long fileCount = (long) Math.ceil(f);
		mainFrame.notify(Messages.getInstance().getString(Messages.fileCount) + fileCount);

		// start splitting the file
		try {
			FileInputStream in = new FileInputStream(selectedFile);
			FileChannel inCh = in.getChannel();

			long fileSize = selectedFile.length();

			for (int i = 0; i < fileCount; i++) {
				File newFile = new File(selectedFile.getAbsolutePath() + getExtension(i));

				if (newFile.exists()) {
					JOptionPane.showMessageDialog(mainFrame,
							newFile.getAbsolutePath() + Messages.getInstance().getString(Messages.overwrite));
					in.close();
					mainFrame.operationFinished(Messages.getInstance().getString(Messages.splitting));

					return;
				}

				mainFrame.notify(newFile.getName() + Messages.getInstance().getString(Messages.creating));

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
			JOptionPane.showMessageDialog(mainFrame, Messages.getInstance().getString(Messages.error) + e.getMessage());
		}
		mainFrame.operationFinished(Messages.getInstance().getString(Messages.splitting));
		try {
			// find checksum and create checksum file
			String checksum = getMD5Checksum(selectedFile.getAbsolutePath());
			mainFrame.notify("Checksum:" + checksum);
			String checksumFileName = selectedFile.getAbsolutePath() + CHECKSUM_EXTENSION;
			File checksumFile = new File(checksumFileName);
			FileWriter fw = new FileWriter(checksumFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(checksum);
			bw.close();
			mainFrame.notify(Messages.getInstance().getString(Messages.checksumCreated) + checksumFileName);
		} catch (Exception e) {
		}
	}

	private String getExtension(int i) {
		String extension = " ";

		if (i < 10) {
			extension = "00" + i;
		} else if (i < 100) {
			extension = "0" + i;
		} else {
			extension = " " + i;
		}

		return "." + extension;
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
