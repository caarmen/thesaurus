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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Thesaurus {

    private final Map<String, Set<ThesaurusEntry>> entriesMap = new HashMap<>();

    public Set<ThesaurusEntry> getEntries(String word) {
        Set<ThesaurusEntry> entries = entriesMap.get(word);
        if (entries == null) return new HashSet<>();
        return Collections.unmodifiableSet(entries);
    }

    public void buildIndex(Map<String, Set<ThesaurusEntry>> entriesMap) {
        this.entriesMap.clear();
        this.entriesMap.putAll(entriesMap);
    }
}
