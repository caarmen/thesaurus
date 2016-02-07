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

import java.io.Serializable;
import java.util.Set;

public class ThesaurusEntry implements Serializable {
    public enum WordType {
        ADJ,
        ADV,
        NOUN,
        VERB,
        UNKNOWN
    }
    public final WordType wordType;
    public final Set<String> synonyms;
    public final Set<String> antonyms;

    public ThesaurusEntry(WordType wordType, Set<String> synonyms, Set<String> antonyms) {
        this.wordType = wordType;
        this.synonyms = synonyms;
        this.antonyms = antonyms;
    }

}
