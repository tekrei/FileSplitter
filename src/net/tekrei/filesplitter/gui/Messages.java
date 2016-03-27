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
	private static final String BUNDLE_NAME = "net.tekrei.filesplitter.gui."; //$NON-NLS-1$
	private static String LANGUAGE = null;
	private static ResourceBundle RESOURCE_BUNDLE = null;
	private static Messages _instance = null;

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
