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

import ca.rmen.thesaurus.MemoryThesaurus;
import ca.rmen.thesaurus.RogetThesaurusReader;
import ca.rmen.thesaurus.Thesaurus;
import ca.rmen.thesaurus.ThesaurusEntry;
import ca.rmen.thesaurus.WordNetThesaurusReader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ThesaurusCli {
    private static void usage() {
        System.out.println("Usages:");
        System.out.println("query [-load <file>] <query>");
        System.out.println("exportdb <roget|wordnet> </path/to/db/file>");
        System.out.println("exportbin <roget|wordnet> </path/to/binary/file>");
        System.exit(-1);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length < 2) {
            usage();
        }
        String command = args[0];
        if ("query".equals(command)) {
            String word = args[args.length - 1];
            File binaryFile = null;
            if (args.length == 4) {
                binaryFile = new File(args[2]);
            }
            runQuery(binaryFile, word);

        } else if ("exportdb".equals(command)) {
            if(args.length != 3) usage();
            exportDb(args[1], new File(args[2]));

        } else if ("exportbin".equals(command)) {
            if(args.length != 3) usage();
            exportBin(args[1], new File(args[2]));
        } else {
            usage();
        }
    }

    private static void runQuery(File inputFile, String word) throws IOException, ClassNotFoundException {
        long before;
        long after;
        if (inputFile != null) {

            System.out.println("Loading thesaurus from disk");
            before = System.currentTimeMillis();
            Thesaurus thesaurus = MemoryThesaurus.loadBinary(inputFile);
            after = System.currentTimeMillis();
            System.out.println("Loaded in " + ((float) (after - before) / 1000) + " seconds");
            search(thesaurus, word);
        } else {
            System.out.println("Loading Roget thesaurus");
            before = System.currentTimeMillis();
            MemoryThesaurus rogetThesaurus = RogetThesaurusReader.createThesaurus();
            after = System.currentTimeMillis();
            System.out.println("Loaded in " + ((float) (after - before) / 1000) + " seconds");
            search(rogetThesaurus, word);

            System.out.println("Loading WordNet thesaurus");
            before = System.currentTimeMillis();
            MemoryThesaurus wordNetThesaurus = WordNetThesaurusReader.createThesaurus();
            after = System.currentTimeMillis();
            System.out.println("Loaded in " + ((float) (after - before) / 1000) + " seconds");
            search(wordNetThesaurus, word);

        }
    }

    private static void search(Thesaurus thesaurus, String query) {
        System.out.println();
        ThesaurusEntry[] entries = thesaurus.getEntries(query);
        System.out.println("Thesaurus: Synonyms for " + query + ": ");
        for (ThesaurusEntry entry : entries) {
            System.out.println("  (" + entry.wordType + "): ");
            System.out.println("     synonyms: " + Arrays.toString(entry.synonyms));
            System.out.println("     antonyms: " + Arrays.toString(entry.antonyms));
        }
    }

    private static void exportDb(String thesaurusType, File outputFile) throws IOException {
        MemoryThesaurus thesaurus = "roget".equals(thesaurusType) ? RogetThesaurusReader.createThesaurus() : WordNetThesaurusReader.createThesaurus();
        DbExport.export(thesaurus, outputFile);
    }

    private static void exportBin(String thesaurusType, File outputFile) throws IOException {
        MemoryThesaurus thesaurus = "roget".equals(thesaurusType) ? RogetThesaurusReader.createThesaurus() : WordNetThesaurusReader.createThesaurus();
        long before = System.currentTimeMillis();
        thesaurus.saveBinary(new File(outputFile.getParent(), outputFile.getName()));
        long after = System.currentTimeMillis();
        System.out.println("Saved in " + ((float) (after - before) / 1000) + " seconds");

    }
}
