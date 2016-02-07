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
import java.util.SortedSet;

public class TestRogetThesaurus {

    @Test
    public void testThesaurus() throws IOException {
        Thesaurus thesaurus = RogetThesaurusReader.createThesaurus();
        Set<ThesaurusEntry> entries = thesaurus.getSynonyms("hate");
        Assert.assertNotNull(entries);
        Assert.assertEquals(1, entries.size());
        SortedSet<String> synonyms = entries.iterator().next().synonyms;
        Assert.assertEquals(5, synonyms.size());
        Assert.assertTrue(synonyms.contains("dislike"));
    }
}
