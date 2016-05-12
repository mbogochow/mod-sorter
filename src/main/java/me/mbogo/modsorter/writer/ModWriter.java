package me.mbogo.modsorter.writer;

import me.mbogo.modsorter.mod.Mod;

import java.io.IOException;
import java.util.Collection;

public interface ModWriter {
    void writeHeader() throws IOException;

    void writeFooter() throws IOException;

    void writeMod(Mod mod) throws IOException;

    void writeMods(Collection<Mod> mods) throws IOException;

    void closeFile() throws IOException;
}
