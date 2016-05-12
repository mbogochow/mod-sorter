package me.mbogo.modsorter.mod;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

/**
 * Master root mod list.
 *
 * @author Michael Bogochow
 * @version 0.0.2
 * @since March 2014
 */
public class MasterModList {
    protected final List<Mod> modList;

    public MasterModList() {
        modList = Lists.newLinkedList();
    }

    /**
     * Check if the master mod list contains a mod with the given name.
     *
     * @param modName the name of the mod to check for
     * @return true if there is a mod in the list with the given name; false
     * otherwise
     */
    public boolean contains(final String modName) {
        for (final Mod mod : modList) {
            if (mod.getName().equals(modName))
                return true;
        }

        return false;
    }

    /**
     * Check if the given mod is contained within the master mod list.
     *
     * @param mod the mod to check
     * @return true if the mod is contained in the list; false otherwise
     */
    public boolean contains(final Mod mod) {
        return modList.contains(mod);
    }

    /**
     * Add the given mod to the list if it is not already present.
     *
     * @param mod the mod to add to the list
     * @return true if the mod was added to the list; false otherwise
     */
    public boolean add(final Mod mod) {
        return modList.add(mod) || update(mod.getName(), mod);
    }

    public boolean update(final String modName, final Mod value) {
        Mod mod = getMod(modName);
        if (mod == null)
            return false;
        mod = value;
//    mod.copy(value);
        return true;
    }

    /**
     * Get the mod with the given name from the list if it is present.
     *
     * @param modName the name of the mod to retrieve
     * @return the mod with the given name if it was found in the list or null if
     * the no mod with the given name was found
     */
    public Mod getMod(final String modName) {
        for (Mod mod : modList) {
            if (mod.getName().equals(modName))
                return mod;
        }

        return null;
    }

    /**
     * Get the iterator for the mod list
     */
    public Iterator<Mod> iterator() {
        return modList.iterator();
    }

    /**
     * Get the list.
     */
    public List<Mod> getList() {
        return modList;
    }
}
