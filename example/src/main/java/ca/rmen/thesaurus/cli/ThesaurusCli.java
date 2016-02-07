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

package ca.rmen.thesaurus.cli;

import ca.rmen.thesaurus.Thesaurus;
import ca.rmen.thesaurus.ThesaurusEntry;
import ca.rmen.thesaurus.ThesaurusReader;

import java.io.IOException;
import java.util.Set;

public class ThesaurusCli {
    public static void main(String[] args) throws IOException {
        String word = args[0];
        Thesaurus thesaurus = ThesaurusReader.createThesaurus();
        Set<ThesaurusEntry> entries = thesaurus.getSynonyms(word);

        System.out.println("Synonyms for " + word + ": ");
        for (ThesaurusEntry entry : entries) {
            System.out.println("  (" + entry.wordType + "): " + entry.synonyms);
        }
    }
}
