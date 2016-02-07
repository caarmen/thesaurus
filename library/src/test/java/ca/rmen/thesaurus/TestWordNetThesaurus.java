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

import java.io.IOException;
import java.util.Set;

public class TestWordNetThesaurus {

    @Test
    public void testThesaurus() throws IOException {
        Thesaurus thesaurus = WordNetThesaurusReader.createThesaurus();
        Set<ThesaurusEntry> entries = thesaurus.getEntries("hold");
        Assert.assertNotNull(entries);
        Assert.assertEquals(45, entries.size());
        assertHasSynonym("hold", entries, "handle");
        assertHasSynonym("hold", entries, "intermission");
        assertHasAntonym("hold", entries, "let go of");
    }

    private void assertHasSynonym(String word, Set<ThesaurusEntry> entries, String synonym) {
        for (ThesaurusEntry entry : entries) {
            if (entry.synonyms.contains(synonym)) return;
        }
        Assert.assertTrue(word + " should have" + synonym + " as a synonym", false);
    }

    private void assertHasAntonym(String word, Set<ThesaurusEntry> entries, String antonym) {
        for (ThesaurusEntry entry : entries) {
            if (entry.antonyms.contains(antonym)) return;
        }
        Assert.assertTrue(word + " should have" + antonym + " as an antonym", false);
    }
}
