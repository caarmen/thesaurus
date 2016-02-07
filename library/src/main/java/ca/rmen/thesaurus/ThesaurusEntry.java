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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class ThesaurusEntry implements Externalizable {
    public enum WordType {
        ADJ,
        ADV,
        NOUN,
        VERB,
        UNKNOWN
    }

    public WordType wordType;
    public String[] synonyms;
    public String[] antonyms;

    public ThesaurusEntry(WordType wordType, String[] synonyms, String[] antonyms) {
        this.wordType = wordType;
        this.synonyms = synonyms;
        this.antonyms = antonyms;
    }

    public ThesaurusEntry() {

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(wordType.ordinal());
        writeStringArray(out, synonyms);
        writeStringArray(out, antonyms);
    }

    private void writeStringArray(ObjectOutput out, String[] strings) throws IOException {
        out.writeInt(strings.length);
        for (String string : strings) {
            out.writeInt(string.length());
            out.writeChars(string);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        wordType = WordType.values()[in.readInt()];
        synonyms = readStringArray(in);
        antonyms = readStringArray(in);
    }

    private String[] readStringArray(ObjectInput in) throws IOException {
        int numStrings = in.readInt();
        String[] result = new String[numStrings];
        for (int i = 0; i < numStrings; i++) {
            int stringLength = in.readInt();
            char[] chars = new char[stringLength];
            for (int j = 0; j < stringLength; j++) {
                chars[j] = in.readChar();
            }
            result[i] = String.valueOf(chars);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThesaurusEntry that = (ThesaurusEntry) o;

        if (wordType != that.wordType) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(synonyms, that.synonyms)) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if(!Arrays.equals(antonyms, that.antonyms)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = wordType != null ? wordType.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(synonyms);
        result = 31 * result + Arrays.hashCode(antonyms);
        return result;
    }


}
