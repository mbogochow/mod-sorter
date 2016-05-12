package me.mbogo.modsorter.writer;

import me.mbogo.modsorter.mod.Mod;

import java.io.IOException;
import java.util.Collection;

public interface ModWriter {
    public void writeHeader() throws IOException;

    public void writeFooter() throws IOException;

    public void writeMod(Mod mod) throws IOException;

    public void writeMods(Collection<Mod> mods) throws IOException;

    public void closeFile() throws IOException;
}
