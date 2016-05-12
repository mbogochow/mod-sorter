package me.mbogo.modsorter.mod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Bogochow
 * @version 0.0.1
 * @since March 2014
 */
public class Mod // implements Comparable<Mod>
{
    private String name;
    private List<Mod> beforeMods;
    private boolean enabled = true;
    private boolean external = false;
    private int priority;

    public static final String AllString = "ALL";
    public static final Mod AllMod = new Mod(AllString);

    public Mod() {
        this.name = null;
        this.beforeMods = new ArrayList<>();
    }

    public Mod(String name) {
        this.name = name;
        this.beforeMods = new ArrayList<>();
    }

    /**
     * Construct a Mod with the given name and list of mods which this me.mbogo.modsorter.mod should
     * be sorted before.
     *
     * @param name       the name of the me.mbogo.modsorter.mod
     * @param beforeMods List of mods that this me.mbogo.modsorter.mod should be sorted before
     */
    public Mod(String name, List<Mod> beforeMods) {
        this.name = name;
        this.beforeMods = beforeMods;
    }

    /**
     * Copy constructor
     *
     * @param mod me.mbogo.modsorter.mod to copy
     */
    public Mod(Mod mod) {
        copy(mod);
    }

    public void copy(Mod mod) {
        this.name = mod.name;
        this.beforeMods = mod.beforeMods;
        this.enabled = mod.enabled;
        this.priority = mod.priority;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    public boolean isExternal() {
        return external;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addMod(Mod mod) {
        beforeMods.add(mod);
    }

    public void addAllMods(List<Mod> mod) {
        beforeMods.addAll(mod);
    }

    public List<Mod> getBeforeList() {
        return beforeMods;
    }

    public void setBeforeList(List<Mod> beforeList) {
        this.beforeMods = beforeList;
    }

    public void addBeforeMod(Mod mod) {
        int modIndex = beforeMods.indexOf(mod);
        if (modIndex == -1)
            beforeMods.add(mod);

        else
            beforeMods.set(modIndex, mod);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object other) {
        boolean areEqual = false;

        if (other instanceof Mod) {
            if (name.equals(((Mod) other).name))
                areEqual = true;
        }

        return areEqual;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * WARNING: This method does not work for sorting because the final condition
     * makes the comparison indeterminate in relation to other mods.
     * <p>
     * i.e. A < B < C but C may contain A in its before mods. This would break a
     * sort with these three mods.
     */
    @Deprecated
    public int compareTo(Mod otherMod) {
        if (this.equals(otherMod))
            return 0;

        boolean ourAll = this.beforeMods.contains(Mod.AllMod);
        boolean otherAll = otherMod.beforeMods.contains(Mod.AllMod);

        if (ourAll && !otherAll)
            return -1;

        if (!ourAll && otherAll)
            return 1;

        // Must be that either both are ALL mods or both are not ALL mods

        boolean oursBeforeTheirs = beforeMods.contains(otherMod);
        boolean theirsBeforeOurs = otherMod.beforeMods.contains(this);

        if (oursBeforeTheirs && theirsBeforeOurs)
            throw new IllegalArgumentException();

        if (oursBeforeTheirs)
            return -1;

        if (theirsBeforeOurs)
            return 1;

        // No relation to each other. Sort based on name.
        return this.name.compareTo(otherMod.name);
    }
}
