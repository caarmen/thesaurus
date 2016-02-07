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

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class TestWordNetThesaurusReader {
    private static final String THESAURUS_FILE = "src/main/resources/dictionary_files/wordnet/th_en_US_new.dat";

    @Test
    public void testLoadThesaurus() throws IOException {
        FileInputStream inputStream = new FileInputStream(THESAURUS_FILE);
        Map<String, ThesaurusEntry[]> map = WordNetThesaurusReader.read(inputStream);
        //Assert.assertEquals(55544, map.size());
        ThesaurusEntry[] entries = map.get("hate");
        Assert.assertNotNull(entries);
        Assert.assertEquals(2, entries.length);
        boolean foundDislike = false;
        for (ThesaurusEntry entry : entries) {
            if (entry.synonyms.contains("dislike")) {
                foundDislike = true;
                break;
            }
        }
        Assert.assertTrue(foundDislike);
    }
}
