package me.mbogo.modsorter.mod;

import java.util.Comparator;


public class ModAlphabeticalComparator implements Comparator<Mod> {
    @Override
    public int compare(Mod mod1, Mod mod2) {
        String mod1Name = mod1.getName();
        String mod2Name = mod2.getName();

        return mod1Name.compareToIgnoreCase(mod2Name);
    }
}
