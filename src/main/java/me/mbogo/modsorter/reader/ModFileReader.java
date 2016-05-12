package me.mbogo.modsorter.reader;

import me.mbogo.modsorter.mod.Mod;

import java.io.FileNotFoundException;
import java.util.List;

public interface ModFileReader {
    public List<Mod> readFile(String fileName)
            throws FileNotFoundException;
}
