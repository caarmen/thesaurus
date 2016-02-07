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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordNetThesaurusReader {
    private static final String ROOT_FOLDER = "/dictionary_files/wordnet/";
    private static final String THESAURUS_FILE = ROOT_FOLDER + "th_en_US_v2.dat";
    private WordNetThesaurusReader() {
        // prevent instantiation of a utility class
    }

    public static Thesaurus createThesaurus() throws IOException {
        Thesaurus thesaurus = new Thesaurus();
        InputStream is = WordNetThesaurusReader.class.getResourceAsStream(THESAURUS_FILE);
        Map<String, ThesaurusEntry[]> map = read(is);
        thesaurus.buildIndex(map);
        return thesaurus;
    }

    static Map<String, ThesaurusEntry[]> read(InputStream is) throws IOException {
        Map<String, ThesaurusEntry[]> result = new HashMap<>();
        BufferedReader bufferedReader = null;
        // Ex: (verb)|take hold|let go of (antonym)
        Pattern relatedWordsPattern = Pattern.compile("^\\(([a-z]*)\\)(.*)$");
        // Ex: let go of (antonym)
        Pattern relatedWordPattern = Pattern.compile("^ *([^(]*)\\((.*)\\)");
        // Ex: hold|45
        Pattern entryPattern = Pattern.compile("^([^(|]*)\\|([0-9]*)$");
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String currentWord = null;
            ThesaurusEntry[] currentEntries = null;
            int currentEntryIndex = 0;
            Set<String> synonyms = new HashSet<>();
            Set<String> antonyms = new HashSet<>();
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                if (line.isEmpty()) continue;
                if (line.startsWith(";;;")) continue;
                // Example line to start a new word:
                // hold|45
                // This means the word hold has 45 thesaurus entries
                if (line.charAt(0) != '(') {
                    if (currentWord != null) {
                        result.put(currentWord, currentEntries);
                    }
                    Matcher entryMatcher = entryPattern.matcher(line);
                    if(entryMatcher.matches()) {
                        currentWord = entryMatcher.group(1);
                        int entryCount = Integer.valueOf(entryMatcher.group(2));
                        currentEntries = new ThesaurusEntry[entryCount];
                        currentEntryIndex = 0;
                    }
                } else {
                    // Example entry lines containing related words for "hold":
                    // (verb)|take hold|let go of (antonym)
                    // (verb)|restrain|confine|disable (generic term)|disenable (generic term)|incapacitate (generic term)
                    Matcher relatedWordsMatcher = relatedWordsPattern.matcher(line);
                    if (relatedWordsMatcher.matches()) {
                        // wordType: ex: verb, noun, etc.
                        String wordType = relatedWordsMatcher.group(1).toUpperCase(Locale.US);
                        // tokens: pipe-separated list of related words
                        String tokens = relatedWordsMatcher.group(2);
                        String[] relatedWords = tokens.split("\\|");

                        synonyms.clear();
                        antonyms.clear();

                        for (String relatedWord : relatedWords) {
                            if (!relatedWord.isEmpty()) {
                                // Example related word without any qualifier: "restrain"
                                if(!relatedWord.contains("(")) {
                                    synonyms.add(relatedWord.trim());
                                }
                                // Example related words with qualifiers (we only care about the antonym one for now):
                                // "disable (generic term)"
                                // "let go of (antonym)"
                                else {
                                    Matcher relatedWordMatcher = relatedWordPattern.matcher(relatedWord);
                                    if(relatedWordMatcher.matches()) {
                                        relatedWord = relatedWordMatcher.group(1).trim();
                                        String typeOfWord = relatedWordMatcher.group(2).trim();
                                        // The "antonym" qualifier means this related word goes into the antonyms list.
                                        if("antonym".equals(typeOfWord)) antonyms.add(relatedWord);
                                        // All other qualifiers: the word goes to the synonyms list
                                        else synonyms.add(relatedWord);
                                    }
                                }

                            }
                        }
                        ThesaurusEntry entry = new ThesaurusEntry(ThesaurusEntry.WordType.valueOf(wordType),
                                synonyms.toArray(new String[synonyms.size()]),
                                antonyms.toArray(new String[antonyms.size()]));
                        currentEntries[currentEntryIndex++] = entry;
                    }
                }
            }

        } finally {
            if (bufferedReader != null) bufferedReader.close();
        }
        return result;

    }
}
