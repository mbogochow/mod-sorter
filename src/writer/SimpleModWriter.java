package writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import mod.Mod;

public class SimpleModWriter implements ModWriter
{
  protected FileWriter writer;
  
  public SimpleModWriter(String filePath) throws IOException
  {
    writer = new FileWriter(filePath);
  }
  
  public SimpleModWriter(File file) throws IOException
  {
    writer = new FileWriter(file);
  }
  
  @Override
  public void initWrite() throws IOException
  {
  }

  @Override
  public void writeMod(Mod mod) throws IOException
  {
    writer.write(mod.getName() + System.lineSeparator());
  }

  @Override
  public void writeMods(Collection<Mod> mods) throws IOException
  {
    if (mods == null)
    {
      writer.write("mods null");
      return;
    }
    for (Iterator<Mod> iter = mods.iterator(); iter.hasNext();)
    {
      writeMod(iter.next());
    }
    writer.flush();
  }
}
