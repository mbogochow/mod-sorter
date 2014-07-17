package sorter;

import java.util.Iterator;
import java.util.List;

import message.MessageLogger;
import mod.Mod;

import com.google.common.collect.Lists;

public class ModSorter
{
  private static List<Mod> doSorting(List<Mod> modList, boolean checkAll)
  {
    List<Mod> sortedList = Lists.newLinkedList(modList);
    List<Mod> allList = Lists.newLinkedList();
    
    // Iterate through each mod in the list
    for (Iterator<Mod> iter = modList.iterator(); iter.hasNext();)
    {
      Mod mod = iter.next();
      List<Mod> before = mod.getBeforeList();
      boolean noSort = false;
      
      // If the mod does not have any other mods in its before list then it 
      // will never need to be moved
      if (before.isEmpty())
      {
        iter.remove();
        continue;
      }

      if (checkAll)
      {
        // Check if the mod should go before all mods
        if (before.get(0).getName().equals(Mod.AllString))
        {
          // If it is an ALL mod, it should be sorted separately from the rest 
          // of the mods
          sortedList.remove(mod);
          iter.remove();
          allList.add(mod);
          noSort = true;
        }
      }
      
      else
      {
        // Check whether any of the mods conflict before sorting
        ModChecker checker = new ModChecker(mod);
        for (Mod bMod : before)
        { 
          if (bMod != null && checker.check(bMod))
          {
            MessageLogger.error(mod.getName() + " conflicts with "
                + bMod.getName());
            return null;
          }
        }        
      }

      if (!noSort)
      {
        // Iterate through the already sorted mods and add in this mod in the 
        // earliest possible position
        for (Iterator<Mod> sortedIter = sortedList.iterator(); sortedIter
            .hasNext();)
        {
          Mod sortedMod = sortedIter.next();
          
          // Reached itself, already in earliest possible position at the moment
          if (mod == sortedMod)
            break;
          
          // Swap if the sorted mod is in the before list
          if (before.contains(sortedMod))
          {
            sortedList.remove(mod);
            sortedList.add(sortedList.indexOf(sortedMod), mod);
            iter = modList.iterator(); // restart modList iterator
            break;
          }
        } /* for sortedList */        
      }
    } /* for modList */
    
    if (checkAll)
    {
      final List<Mod> sortedAllList = doSorting(allList, false); 
      sortedList.addAll(0, sortedAllList);      
    }
    
    return sortedList;
  } /* doSorting */
  
  public static List<Mod> sort(List<Mod> modList)
  {
    return doSorting(modList, true);
  } /* sort */
} /* ModSorter class */
