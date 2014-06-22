package writer;

import java.io.IOException;
import java.util.Collection;

import mod.Mod;

public interface ModWriter
{
  public void writeHeader() throws IOException;
  public void writeFooter() throws IOException;
  public void writeMod(Mod mod) throws IOException;
  public void writeMods(Collection<Mod> mods) throws IOException;
  public void closeFile() throws IOException;
}
