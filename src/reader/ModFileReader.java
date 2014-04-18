package reader;

import java.io.FileNotFoundException;
import java.util.List;

import mod.Mod;

public interface ModFileReader
{
  public List<Mod> readFile(String fileName)
      throws FileNotFoundException;
}
