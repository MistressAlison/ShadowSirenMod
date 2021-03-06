package ShadowSiren.patches;

import ShadowSiren.powers.interfaces.OnUseEnergyPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class OnUseEnergyPowerPatch {

    @SpirePatch(clz = EnergyPanel.class, method = "useEnergy")
    public static class UseEnergyListener {
        @SpirePrefixPatch
        public static SpireReturn<?> useEnergyListener(int e) {
            boolean canUseEnergy = true;
            for (AbstractPower pow : AbstractDungeon.player.powers) {
                if (pow instanceof OnUseEnergyPower) {
                    canUseEnergy &= ((OnUseEnergyPower) pow).onUseEnergy(e);
                }
            }
            if (!canUseEnergy) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

}
