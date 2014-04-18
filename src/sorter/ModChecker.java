package sorter;

import java.util.List;

import mod.Mod;

import com.google.common.collect.Lists;

public class ModChecker
{
  private Mod check;
  
  public ModChecker(Mod modToCheck)
  {
    this.check = modToCheck;
  }

  public boolean check(Mod mod)
  {
    if (mod == null)
      return false;
    List<Mod> writtenMods = Lists.newArrayList();
    return doChecking(mod, writtenMods);
  }
  
  private boolean doChecking(Mod before, List<Mod> checkedMods)
  {
    List<Mod> beforeList = before.getBeforeList();
    if (beforeList.isEmpty())
      return false;
    if (beforeList.contains(check))
      return true;
    if (checkedMods.contains(before))
      return false;
    checkedMods.add(before);
    return checkBefore(beforeList, checkedMods);
  }
  
  private boolean checkBefore(List<Mod> beforeList, List<Mod> checkednMods)
  {
    boolean result = false;
    for (Mod mod : beforeList)
    {
      if (mod == null)
        continue;
      boolean res = doChecking(mod, checkednMods);
      if (res)
      {
        System.err.println("LOOP TRACE: " + mod.getName());
      }
      result |= res;
    }
    return result;
  }
}
