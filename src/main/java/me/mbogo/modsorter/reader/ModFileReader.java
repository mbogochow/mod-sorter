package me.mbogo.modsorter.reader;

import me.mbogo.modsorter.mod.Mod;

import java.io.FileNotFoundException;
import java.util.List;

public interface ModFileReader {
    List<Mod> readFile(final String fileName)
            throws FileNotFoundException;
}
