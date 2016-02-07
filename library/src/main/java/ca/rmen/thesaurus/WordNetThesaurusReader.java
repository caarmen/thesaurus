/*
 * Copyright (c) 2016 Carmen Alvarez
 *
 * This file is part of Thesaurus.
 *
 * Thesaurus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Thesaurus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Thesaurus.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.rmen.thesaurus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordNetThesaurusReader {
    private static final String ROOT_FOLDER = "/dictionary_files/wordnet/";
    private static final String THESAURUS_FILE = ROOT_FOLDER + "th_en_US_new.dat";
    private WordNetThesaurusReader() {
        // prevent instantiation of a utility class
    }

    public static Thesaurus createThesaurus() throws IOException {
        Thesaurus thesaurus = new Thesaurus();
        InputStream is = WordNetThesaurusReader.class.getResourceAsStream(THESAURUS_FILE);
        Map<String, Set<ThesaurusEntry>> map = read(is);
        thesaurus.buildIndex(map);
        return thesaurus;
    }

    static Map<String, Set<ThesaurusEntry>> read(InputStream is) throws IOException {
        Map<String, Set<ThesaurusEntry>> result = new HashMap<>();
        BufferedReader bufferedReader = null;
        Pattern pattern = Pattern.compile("^\\(([a-z]*)\\)(.*)$");
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String currentWord = null;
            Set<ThesaurusEntry> currentEntries = new HashSet<>();
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                if (line.isEmpty()) continue;
                if (line.startsWith(";;;")) continue;
                if (line.charAt(0) != '(') {
                    currentWord = line.replaceAll("\\|.*$", "");
                    currentEntries = new HashSet<>();
                    result.put(currentWord, currentEntries);
                } else {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        String wordType = matcher.group(1).toUpperCase(Locale.US);
                        String tokens = matcher.group(2);
                        String[] synonyms = tokens.split("\\|");
                        SortedSet<String> sortedSynonyms = new TreeSet<>();
                        for (String synonym: synonyms) {
                            if (!synonym.isEmpty()) sortedSynonyms.add(synonym);
                        }
                        ThesaurusEntry entry = new ThesaurusEntry(ThesaurusEntry.WordType.valueOf(wordType), sortedSynonyms);
                        currentEntries.add(entry);
                    }
                }
            }

        } finally {
            if (bufferedReader != null) bufferedReader.close();
        }
        return result;

    }
}
