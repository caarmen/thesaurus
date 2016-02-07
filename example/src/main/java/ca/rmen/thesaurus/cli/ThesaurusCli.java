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

import ca.rmen.thesaurus.RogetThesaurusReader;
import ca.rmen.thesaurus.Thesaurus;
import ca.rmen.thesaurus.ThesaurusEntry;
import ca.rmen.thesaurus.WordNetThesaurusReader;

import java.io.File;
import java.io.IOException;

public class ThesaurusCli {
    private static void usage() {
        System.out.println("[-load <file>] [-save <file>] query");
        System.exit(-1);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String word = args[args.length - 1];

        File inputFile = null;
        File outputFile = null;
        for (int i = 0; i < args.length - 1; i++) {
            if ("-load".equals(args[i])) inputFile = new File(args[++i]);
            else if ("-save".equals(args[i])) outputFile = new File(args[++i]);
            else if ("-help".equals(args[i])) usage();
        }

        long before;
        long after;
        if (inputFile != null) {

            System.out.println("Loading thesaurus from disk");
            before = System.currentTimeMillis();
            Thesaurus thesaurus = Thesaurus.load(inputFile);
            after = System.currentTimeMillis();
            System.out.println("Loaded in " + ((float)(after - before)/1000) + " seconds");
            search(thesaurus, word);
        }

        System.out.println("Loading Roget thesaurus");
        before = System.currentTimeMillis();
        Thesaurus rogetThesaurus = RogetThesaurusReader.createThesaurus();
        after = System.currentTimeMillis();
        System.out.println("Loaded in " + ((float)(after - before)/1000) + " seconds");
        search(rogetThesaurus, word);

        System.out.println("Loading WordNet thesaurus");
        before = System.currentTimeMillis();
        Thesaurus wordNetThesaurus = WordNetThesaurusReader.createThesaurus();
        after = System.currentTimeMillis();
        System.out.println("Loaded in " + ((float)(after - before)/1000) + " seconds");
        search(wordNetThesaurus, word);

        if (outputFile != null) {
            System.out.println("Saving Roget thesaurus");

            before = System.currentTimeMillis();
            rogetThesaurus.save(new File(outputFile.getParent(), "roget-" + outputFile.getName()));
            after = System.currentTimeMillis();
            System.out.println("Saved in " + ((float)(after - before)/1000) + " seconds");
            System.out.println("Saving WordNet thesaurus");
            before = System.currentTimeMillis();
            wordNetThesaurus.save(new File(outputFile.getParent(), "wordnet-" + outputFile.getName()));
            after = System.currentTimeMillis();
            System.out.println("Saved in " + ((float)(after - before)/1000) + " seconds");
        }
    }

    private static void search(Thesaurus thesaurus, String query) {
        System.out.println();
        ThesaurusEntry[] entries = thesaurus.getEntries(query);
        System.out.println("Thesaurus: Synonyms for " + query + ": ");
        for (ThesaurusEntry entry : entries) {
            System.out.println("  (" + entry.wordType + "): ");
            System.out.println("     synonyms: " + entry.synonyms);
            System.out.println("     antonyms: " + entry.antonyms);
        }
    }
}
