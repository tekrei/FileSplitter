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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "resources."; //$NON-NLS-1$
	private static String LANGUAGE = null;
	private static ResourceBundle RESOURCE_BUNDLE = null;
	private static Messages _instance = null;

	//special characters
	public static String LINE_END = System.getProperty("line.separator");
	public static String CHECKSUM_EXTENSION = ".checksum.txt";

	//message tags
	public static String mainFrameTitle = "AnaPencere.Baslik";
	public static String sizeTag = "AnaPencere.ParcaBoyutuEtiket";
	public static String selectFile = "AnaPencere.DosyaSecEtiket";
	public static String selectFileTooltip = "AnaPencere.DosyaSecTooltip";
	public static String splitTooltip = "AnaPencere.ParcalaTooltip";
	public static String split = "AnaPencere.ParcalaEtiket";
	public static String sizeWarning = "AnaPencere.BoyutSiniriUyari";
	public static String join = "AnaPencere.BirlestirEtiket";
	public static String joinTooltip = "AnaPencere.BirlestirTooltip";
	public static String exit = "AnaPencere.Cikis";
	public static String clear = "AnaPencere.Temizle";
	public static String license = "AnaPencere.Lisans";
	public static String about = "AnaPencere.Hakkinda";
	public static String file = "AnaPencere.Dosya";
	public static String english = "AnaPencere.Ingilizce";
	public static String turkish = "AnaPencere.Turkce";
	public static String language = "AnaPencere.Dil";
	public static String complete = "AnaPencere.IslemTamamlandi";
	public static String started = "AnaPencere.IslemBasladi";
	public static String select = "DosyaParcalayici.Sec";
	public static String joining = "DosyaParcalayici.Birlestirme";
	public static String overwrite = "DosyaParcalayici.UstuneYazmaSorgu";
	public static String splitting = "DosyaParcalayici.Parcalama";
	public static String error = "DosyaParcalayici.HataMesaj";
	public static String creating = "DosyaParcalayici.DosyaYaratiliyor";
	public static String fileCount = "DosyaParcalayici.DosyaAdediMesaj";
	public static String fileSize = "DosyaParcalayici.DosyaBoyutMesaj";
	public static String limitError = "DosyaParcalayici.SinirHata";
	public static String partError = "DosyaParcalayici.ParcaDosyaHata";
	public static String fileAdding = "DosyaParcalayici.DosyaEkleniyor";
	public static String checksumCreated = "DosyaParcalayici.ChecksumCreated";
	public static String checksumError = "DosyaParcalayici.ChecksumError";
	public static String checksumOK = "DosyaParcalayici.ChecksumOK";

	// Turkish
	public static final String TR = "tr"; //$NON-NLS-1$
	// English
	public static final String EN = "en"; //$NON-NLS-1$

	private Messages() {
	}

	public static Messages getInstance() {
		if (_instance == null) {
			_instance = new Messages();
		}
		return _instance;
	}

	public void initialize(String _dil) {
		LANGUAGE = _dil;
		if (LANGUAGE == null) {
			LANGUAGE = TR;
		}

		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME + LANGUAGE);
	}

	public String getLanguage() {
		return LANGUAGE;
	}

	public String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
