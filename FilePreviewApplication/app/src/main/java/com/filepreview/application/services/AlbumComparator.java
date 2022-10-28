package com.filepreview.application.services;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;


/**
 * album list compare
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class AlbumComparator implements Comparator<String> {
    private Collator collator = Collator.getInstance(Locale.CHINA);

    @Override
    public int compare(String str1, String str2) {
        boolean chinese1 = isChinese(str1);
        boolean chinese2 = isChinese(str2);
        if (chinese1 || chinese2) {
            CollationKey key1 = collator.getCollationKey(str1);
            CollationKey key2 = collator.getCollationKey(str2);
            if (chinese1) {
                return key1.compareTo(key2);
            } else {
                return Math.abs(key1.compareTo(key2));
            }
        } else {
            return str1.compareTo(str2);
        }
    }

    private boolean isChinese(String str) {
        return str.getBytes().length != str.length();
    }
}
