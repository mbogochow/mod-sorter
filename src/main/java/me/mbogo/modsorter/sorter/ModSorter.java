package me.mbogo.modsorter.sorter;

import me.mbogo.modsorter.message.MessageLogger;
import me.mbogo.modsorter.mod.Mod;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ModSorter {
    private static List<Mod> doSorting(@Nonnull final List<Mod> modList, final boolean checkAll) {
        final List<Mod> sortedList = new LinkedList<>(modList);
        final List<Mod> allList = new LinkedList<>();

        // Iterate through each mod in the list
        for (Iterator<Mod> iter = modList.iterator(); iter.hasNext(); ) {
            final Mod mod = iter.next();
            final List<Mod> before = mod.getBeforeList();
            boolean noSort = false;

            // If the mod does not have any other mods in its before list then it
            // will never need to be moved
            if (before.isEmpty()) {
                iter.remove();
                continue;
            }

            if (checkAll) {
                // Check if the mod should go before all mods
                if (before.get(0).getName().equals(Mod.AllString)) {
                    // If it is an ALL mod, it should be sorted separately from the rest
                    // of the mods
                    sortedList.remove(mod);
                    iter.remove();
                    allList.add(mod);
                    noSort = true;
                }
            } else {
                // Check whether any of the mods conflict before sorting
                final ModChecker checker = new ModChecker(mod);
                for (final Mod bMod : before) {
                    if (bMod != null && checker.check(bMod)) {
                        MessageLogger.error(mod.getName() + " conflicts with "
                                + bMod.getName());
                        return null;
                    }
                }
            }

            if (!noSort) {
                // Iterate through the already sorted mods and add in this mod in the
                // earliest possible position
                for (final Mod sortedMod : sortedList) {
                    // Reached itself, already in earliest possible position at the moment
                    if (mod == sortedMod)
                        break;

                    // Swap if the sorted mod is in the before list
                    if (before.contains(sortedMod)) {
                        sortedList.remove(mod);
                        sortedList.add(sortedList.indexOf(sortedMod), mod);
                        iter = modList.iterator(); // restart modList iterator
                        break;
                    }
                } /* for sortedList */
            }
        } /* for modList */

        if (checkAll) {
            final List<Mod> sortedAllList = doSorting(allList, false);
            assert sortedAllList != null;
            sortedList.addAll(0, sortedAllList);
        }

        return sortedList;
    } /* doSorting */

    public static List<Mod> sort(final List<Mod> modList) {
        return doSorting(modList, true);
    } /* sort */
} /* ModSorter class */
