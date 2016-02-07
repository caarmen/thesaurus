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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MemoryThesaurus implements Thesaurus, Externalizable {

    private final Map<String, ThesaurusEntry[]> entriesMap = new HashMap<>();

    public MemoryThesaurus() {

    }

    @Override
    public ThesaurusEntry[] getEntries(String word) {
        ThesaurusEntry[] entries = entriesMap.get(word);
        if (entries == null) return new ThesaurusEntry[0];
        return entries;
    }

    public void buildIndex(Map<String, ThesaurusEntry[]> entriesMap) {
        this.entriesMap.clear();
        this.entriesMap.putAll(entriesMap);
    }

    public void save(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        fos.close();
    }

    public static Thesaurus load(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (Thesaurus) ois.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        int wordCount = entriesMap.keySet().size();
        out.writeInt(wordCount);
        for(String word : entriesMap.keySet()) {
            out.writeInt(word.length());
            out.writeChars(word);
            ThesaurusEntry[] entries = entriesMap.get(word);
            out.writeInt(entries.length);
            for(ThesaurusEntry entry : entries) {
                entry.writeExternal(out);
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int wordCount = in.readInt();
        for(int i=0; i < wordCount; i++) {
            int wordLength = in.readInt();
            char[] chars = new char[wordLength];
            for (int j = 0; j < wordLength; j++) {
                chars[j] = in.readChar();
            }
            String word = String.valueOf(chars);
            int entryCount = in.readInt();
            ThesaurusEntry[] entries =  new ThesaurusEntry[entryCount];
            for (int j=0; j < entryCount; j++) {
                entries[j] = new ThesaurusEntry();
                entries[j].readExternal(in);
            }
            entriesMap.put(word, entries);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemoryThesaurus thesaurus = (MemoryThesaurus) o;

        if (entriesMap == null && thesaurus.entriesMap == null) return true;
        if (entriesMap == null || thesaurus.entriesMap == null) return false;
        if (!entriesMap.keySet().equals(thesaurus.entriesMap.keySet())) return false;
        for (String word : entriesMap.keySet()) {
            ThesaurusEntry[] myEntries = entriesMap.get(word);
            ThesaurusEntry[] otherEntries = thesaurus.entriesMap.get(word);
            if(!Arrays.equals(myEntries, otherEntries)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return entriesMap != null ? entriesMap.hashCode() : 0;
    }
}
