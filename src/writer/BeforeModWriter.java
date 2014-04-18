package writer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import mod.Mod;

import com.google.common.collect.Lists;

public class BeforeModWriter extends SimpleModWriter
{
  protected int increment = 2;
  protected int indentation = 0;
  protected int printLevel = 0;
  protected final int maxLevel = 5;

  public BeforeModWriter(String filePath) throws IOException
  {
    super(filePath);
  }

  public BeforeModWriter(File file) throws IOException
  {
    super(file);
  }

  @Override
  public void writeMod(Mod mod) throws IOException
  {
    if (mod == null)
      return;
    List<Mod> writtenMods = Lists.newArrayList();
    doWriting(mod, writtenMods);
    printLevel += 1;
    writeBeforeMods(mod, writtenMods);
    printLevel -= 1;
  }
  
  protected boolean doWriting(Mod mod, List<Mod> writtenMods) throws IOException
  {
    if (mod == null)
      return false;
    indent();
    writer.write(mod.getName() + System.lineSeparator());
    if (writtenMods.contains(mod))
      return false;
    writtenMods.add(mod);
    return true;
  }
  
  protected void writeBeforeMods(Mod mod, List<Mod> writtenMods) throws IOException
  {
    if (mod == null || printLevel >  maxLevel)
      return;
    List<Mod> beforeList = mod.getBeforeList();
    indentation += increment;
    for (Mod bMod : beforeList)
    {
      if (doWriting(bMod, writtenMods))
        writeBeforeMods(bMod, writtenMods);
    }
    indentation -= increment;
  }

  // generate a string of spaces for current indentation level
  private void indent() throws IOException
  {
    for (int i = 0; i < indentation; i++)
    {
      writer.write(' ');
    }
  }
}
