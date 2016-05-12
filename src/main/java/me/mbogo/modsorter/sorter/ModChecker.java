package me.mbogo.modsorter.sorter;

import com.google.common.collect.Lists;
import me.mbogo.modsorter.message.MessageLogger;
import me.mbogo.modsorter.mod.Mod;

import java.util.List;

public class ModChecker {
    private Mod check;

    public ModChecker(final Mod modToCheck) {
        this.check = modToCheck;
    }

    public boolean check(final Mod mod) {
        return mod != null && doChecking(mod, Lists.newArrayList());
    }

    private boolean doChecking(Mod before, List<Mod> checkedMods) {
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

    private boolean checkBefore(final List<Mod> beforeList, final List<Mod> checkedMods) {
        boolean result = false;
        for (Mod mod : beforeList) {
            if (mod == null)
                continue;
            final boolean res = doChecking(mod, checkedMods);
            if (res) {
                MessageLogger.error("LOOP TRACE: " + mod.getName());
            }
            result |= res;
        }
        return result;
    }
}
