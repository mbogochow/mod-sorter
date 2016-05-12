package me.mbogo.modsorter.mod;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

/**
 * Master root me.mbogo.modsorter.mod list.
 *
 * @author Michael Bogochow
 * @version 0.0.2
 * @since March 2014
 */
public class MasterModList {
    protected List<Mod> modList;

    public MasterModList() {
        modList = Lists.newLinkedList();
    }

    /**
     * Check if the master me.mbogo.modsorter.mod list contains a me.mbogo.modsorter.mod with the given name.
     *
     * @param modName the name of the me.mbogo.modsorter.mod to check for
     * @return true if there is a me.mbogo.modsorter.mod in the list with the given name; false
     * otherwise
     */
    public boolean contains(String modName) {
        for (Mod mod : modList) {
            if (mod.getName().equals(modName))
                return true;
        }

        return false;
    }

    /**
     * Check if the given me.mbogo.modsorter.mod is contained within the master me.mbogo.modsorter.mod list.
     *
     * @param mod the me.mbogo.modsorter.mod to check
     * @return true if the me.mbogo.modsorter.mod is contained in the list; false otherwise
     */
    public boolean contains(Mod mod) {
        return modList.contains(mod);
    }

    /**
     * Add the given me.mbogo.modsorter.mod to the list if it is not already present.
     *
     * @param mod the me.mbogo.modsorter.mod to add to the list
     * @return true if the me.mbogo.modsorter.mod was added to the list; false otherwise
     */
    public boolean add(Mod mod) {
        if (modList.add(mod))
            return true;
        return update(mod.getName(), mod);
    }

    public boolean update(String modName, Mod value) {
        Mod mod = getMod(modName);
        if (mod == null)
            return false;
        mod = value;
//    me.mbogo.modsorter.mod.copy(value);
        return true;
    }

    /**
     * Get the me.mbogo.modsorter.mod with the given name from the list if it is present.
     *
     * @param modName the name of the me.mbogo.modsorter.mod to retrieve
     * @return the me.mbogo.modsorter.mod with the given name if it was found in the list or null if
     * the no me.mbogo.modsorter.mod with the given name was found
     */
    public Mod getMod(String modName) {
        for (Mod mod : modList) {
            if (mod.getName().equals(modName))
                return mod;
        }

        return null;
    }

    /**
     * Get the iterator for the me.mbogo.modsorter.mod list
     */
    public Iterator<Mod> getIterator() {
        return modList.iterator();
    }

    /**
     * Get the list.
     */
    public List<Mod> getList() {
        return modList;
    }
}
