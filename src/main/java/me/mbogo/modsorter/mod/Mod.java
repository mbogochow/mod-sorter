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

    public Mod(final String name) {
        this.name = name;
        this.beforeMods = new ArrayList<>();
    }

    /**
     * Construct a Mod with the given name and list of mods which this mod should
     * be sorted before.
     *
     * @param name       the name of the mod
     * @param beforeMods List of mods that this mod should be sorted before
     */
    public Mod(final String name, final List<Mod> beforeMods) {
        this.name = name;
        this.beforeMods = beforeMods;
    }

    /**
     * Copy constructor
     *
     * @param mod mod to copy
     */
    public Mod(final Mod mod) {
        copy(mod);
    }

    /**
     * Shallow copy.
     *
     * @param mod mod to copy
     */
    public void copy(final Mod mod) {
        this.name = mod.name;
        this.beforeMods = mod.beforeMods;
        this.enabled = mod.enabled;
        this.priority = mod.priority;
    }

    public void setExternal(final boolean external) {
        this.external = external;
    }

    public boolean isExternal() {
        return external;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addMod(final Mod mod) {
        beforeMods.add(mod);
    }

    public void addAllMods(final List<Mod> mod) {
        beforeMods.addAll(mod);
    }

    public List<Mod> getBeforeList() {
        return beforeMods;
    }

    public void setBeforeList(final List<Mod> beforeList) {
        this.beforeMods = beforeList;
    }

    public void addBeforeMod(final Mod mod) {
        final int modIndex = beforeMods.indexOf(mod);
        if (modIndex == -1)
            beforeMods.add(mod);
        else
            beforeMods.set(modIndex, mod);
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof Mod && name.equals(((Mod) other).name);
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
    public int compareTo(final Mod otherMod) {
        if (this.equals(otherMod))
            return 0;

        final boolean ourAll = this.beforeMods.contains(Mod.AllMod);
        final boolean otherAll = otherMod.beforeMods.contains(Mod.AllMod);

        if (ourAll && !otherAll)
            return -1;

        if (!ourAll && otherAll)
            return 1;

        // Must be that either both are ALL mods or both are not ALL mods

        final boolean oursBeforeTheirs = beforeMods.contains(otherMod);
        final boolean theirsBeforeOurs = otherMod.beforeMods.contains(this);

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
