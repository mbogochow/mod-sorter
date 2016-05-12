package me.mbogo.modsorter.sorter;

import com.google.common.collect.Lists;
import me.mbogo.modsorter.message.MessageLogger;
import me.mbogo.modsorter.mod.Mod;

import java.util.List;

public class ModChecker {
    private Mod check;

    public ModChecker(Mod modToCheck) {
        this.check = modToCheck;
    }

    public boolean check(Mod mod) {
        if (mod == null)
            return false;
        List<Mod> writtenMods = Lists.newArrayList();
        return doChecking(mod, writtenMods);
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

    private boolean checkBefore(List<Mod> beforeList, List<Mod> checkedMods) {
        boolean result = false;
        for (Mod mod : beforeList) {
            if (mod == null)
                continue;
            boolean res = doChecking(mod, checkedMods);
            if (res) {
                MessageLogger.error("LOOP TRACE: " + mod.getName());
            }
            result |= res;
        }
        return result;
    }
}
