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
import ca.rmen.thesaurus.ThesaurusEntry;
import ca.rmen.thesaurus.WordNetThesaurusReader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeSet;

public class DbExport {
    private static final int BATCH_INSERT_SIZE = 1000;

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: <roget|wordnet> /path/to/db/file");
            System.exit(-1);
            return;
        }
        String thesaurusType = args[0];
        String dbFile = args[1];
        MemoryThesaurus thesaurus = "roget".equals(thesaurusType) ? RogetThesaurusReader.createThesaurus() : WordNetThesaurusReader.createThesaurus();
        export(thesaurus, new File(dbFile));
    }

    public static void export(MemoryThesaurus thesaurus, File file) {

        Connection connection;
        try {
            file.delete();
            connection = getDbConnection(file.getAbsolutePath());
            Statement createTableStatement = connection.createStatement();
            createTableStatement.execute("CREATE TABLE thesaurus (word, word_type, synonyms, antonyms)");
            createTableStatement.close();
            Set<String> words = new TreeSet<>();
            words.addAll(thesaurus.getWords());
            PreparedStatement insertStatement =
                    connection.prepareStatement("INSERT INTO thesaurus (word, word_type, synonyms, antonyms) VALUES (?, ?, ?, ?)");
            int row = 0;
            int totalWords = words.size();
            int wordIndex = 0;
            long before = System.currentTimeMillis();
            for (String word : words) {
                ThesaurusEntry[] entries = thesaurus.getEntries(word);
                for (ThesaurusEntry entry : entries) {
                    int column = 1;
                    insertStatement.setString(column++, word);
                    insertStatement.setString(column++, entry.wordType.name());
                    insertStatement.setString(column++, join(entry.synonyms));
                    insertStatement.setString(column++, join(entry.antonyms));
                    insertStatement.addBatch();
                    row++;
                    if (row % BATCH_INSERT_SIZE == 0) {
                        System.out.println("Executing batch...");
                        insertStatement.executeBatch();
                        System.out.println("Executed batch");
                    }
                }
                wordIndex++;
                if (wordIndex % 1000 == 0) {
                    long after = System.currentTimeMillis();
                    System.out.println("Wrote word " + wordIndex + " of " + totalWords + " in " + ((float)(after - before)/1000) + " seconds");
                    before = after;
                }
            }
            insertStatement.executeBatch();
            insertStatement.close();


        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Couldn't export the db to " + file + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Connection getDbConnection(String dbPath) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    private static String join(String[] strings) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.length - 1; i++) {
            builder.append(strings[i]).append(',');
        }
        if (strings.length > 0) builder.append(strings[strings.length - 1]);
        return builder.toString();
    }
}
