package me.mbogo.modsorter.writer;

import me.mbogo.modsorter.mod.Mod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class SimpleModWriter implements ModWriter {
    protected FileWriter writer;

    public SimpleModWriter(String filePath) throws IOException {
        writer = new FileWriter(filePath);
    }

    public SimpleModWriter(File file) throws IOException {
        writer = new FileWriter(file);
    }

    @Override
    public void writeHeader() throws IOException {
    }

    @Override
    public void writeFooter() throws IOException {
    }

    @Override
    public void writeMod(Mod mod) throws IOException {
        writer.write(mod.getName() + System.lineSeparator());
    }

    @Override
    public void writeMods(Collection<Mod> mods) throws IOException {
        if (mods == null) {
            writer.write("mods null");
            return;
        }
        for (Iterator<Mod> iter = mods.iterator(); iter.hasNext(); ) {
            writeMod(iter.next());
        }
        writer.flush();
    }

    @Override
    public void closeFile() throws IOException {
        writer.close();
    }
} /* SimpleModWriter class */
