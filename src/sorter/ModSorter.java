package sorter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import message.MessageLogger;
import mod.Mod;

import com.google.common.collect.Lists;

public class ModSorter
{
  public static List<Mod> sort(List<Mod> modList)
  {
    List<Mod> sortedList = Lists.newLinkedList(modList);
    List<Mod> allList = Lists.newLinkedList();
    for (Iterator<Mod> iter = modList.iterator(); iter.hasNext();)
    {
      Mod mod = iter.next();
      List<Mod> before = mod.getBeforeList();
      if (before.isEmpty())
      {
//        System.err.println(mod.getName() + " has no before mods");
        iter.remove();
        continue;
      }

      ModChecker checker = new ModChecker(mod);
      for (Mod bMod : before)
      {
        if (bMod == null)
          continue;
//        System.err.println("Checking " + mod.getName() + " for "
//            + bMod.getName());
        
        if (checker.check(bMod))
        {
          MessageLogger.error(mod.getName() + " conflicts with "
              + bMod.getName());
          return null;
        }
      }

      // Check if the mod should go before all mods
      if (before.get(0).getName().equals(Mod.AllString))
      {
        sortedList.remove(mod);
        iter.remove();
        allList.add(mod);
        continue;
      }

      for (Iterator<Mod> sortedIter = sortedList.iterator(); sortedIter
          .hasNext();)
      {
        Mod sortedMod = sortedIter.next();

        if (mod == sortedMod)
          break;

        // Swap if the sorted mod is in the before list
        if (before.contains(sortedMod))
        {
          sortedList.remove(mod);
          sortedList.add(sortedList.indexOf(sortedMod), mod);
          iter = modList.iterator(); // restart iterator
          break;
        }
      }

    }
    Collections.sort(allList);
    sortedList.addAll(0, allList);
    return modList = sortedList;
  }
}
