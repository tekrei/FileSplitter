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
package net.tekrei.filesplitter.utility;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    // Turkish
    public static final String TR = "tr"; //$NON-NLS-1$
    // English
    public static final String EN = "en"; //$NON-NLS-1$
    //message tags
    public static final String mainFrameTitle = "AnaPencere.Baslik";
    public static final String sizeTag = "AnaPencere.ParcaBoyutuEtiket";
    public static final String sizeInfo = "AnaPencere.DosyaBoyutuEtiket";
    public static final String selectFile = "AnaPencere.DosyaSecEtiket";
    public static final String selectFileTooltip = "AnaPencere.DosyaSecTooltip";
    public static final String splitTooltip = "AnaPencere.ParcalaTooltip";
    public static final String split = "AnaPencere.ParcalaEtiket";
    public static final String sizeWarning = "AnaPencere.BoyutSiniriUyari";
    public static final String join = "AnaPencere.BirlestirEtiket";
    public static final String joinTooltip = "AnaPencere.BirlestirTooltip";
    public static final String exit = "AnaPencere.Cikis";
    public static final String clear = "AnaPencere.Temizle";
    public static final String license = "AnaPencere.Lisans";
    public static final String about = "AnaPencere.Hakkinda";
    public static final String file = "AnaPencere.Dosya";
    public static final String english = "AnaPencere.Ingilizce";
    public static final String turkish = "AnaPencere.Turkce";
    public static final String language = "AnaPencere.Dil";
    public static final String complete = "AnaPencere.IslemTamamlandi";
    public static final String started = "AnaPencere.IslemBasladi";
    static final String select = "DosyaParcalayici.Sec";
    static final String joining = "DosyaParcalayici.Birlestirme";
    static final String overwrite = "DosyaParcalayici.UstuneYazmaSorgu";
    static final String splitting = "DosyaParcalayici.Parcalama";
    static final String error = "DosyaParcalayici.HataMesaj";
    static final String creating = "DosyaParcalayici.DosyaYaratiliyor";
    static final String fileCount = "DosyaParcalayici.DosyaAdediMesaj";
    static final String fileSize = "DosyaParcalayici.DosyaBoyutMesaj";
    static final String limitError = "DosyaParcalayici.SinirHata";
    static final String partError = "DosyaParcalayici.ParcaDosyaHata";
    static final String fileAdding = "DosyaParcalayici.DosyaEkleniyor";
    static final String checksumCreated = "DosyaParcalayici.ChecksumCreated";
    static final String checksumError = "DosyaParcalayici.ChecksumError";
    static final String checksumOK = "DosyaParcalayici.ChecksumOK";
    static final String fileExistsError = "DosyaParcalayici.FileExists";
    private static String LANGUAGE = null;
    private static ResourceBundle RESOURCE_BUNDLE = null;

    private Messages() {
    }

    public static void initialize(String _dil) {
        LANGUAGE = _dil;
        if (LANGUAGE == null) {
            LANGUAGE = TR;
        }

        RESOURCE_BUNDLE = ResourceBundle.getBundle(LANGUAGE);
    }

    public static String getLanguage() {
        return LANGUAGE;
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
