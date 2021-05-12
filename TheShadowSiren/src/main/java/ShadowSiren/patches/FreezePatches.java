package ShadowSiren.patches;

import ShadowSiren.powers.FreezePower;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.HexaghostBody;
import com.megacrit.cardcrawl.monsters.exordium.HexaghostOrb;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class FreezePatches {

    //This stuff is reasonable. Reworked from StunPatches from stslib

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "rollMove"
    )
    public static class RollMove {
        public RollMove() {
        }

        public static SpireReturn<?> Prefix(AbstractMonster __instance) {
            return __instance.hasPower(FreezePower.POWER_ID) ? SpireReturn.Return(null) : SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class GetNextAction {
        public GetNextAction() {
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals("com.megacrit.cardcrawl.monsters.AbstractMonster") && m.getMethodName().equals("takeTurn")) {
                        m.replace("if (!m.hasPower("+FreezePower.class.getName()+".POWER_ID)) {$_ = $proceed($$);}");
                    }

                }
            };
        }
    }

    //Cursed stuff begins

    //Replaced with an instrument patch. This was funny though
    /*@SpirePatch(clz = AbstractMonster.class, method = "render")
    public static class StopAnimatingPls {
        @SpireInsertPatch(locator = Locator.class)
        public static void stop(AbstractMonster __instance, SpriteBatch sb) {
            if (__instance.hasPower(FreezePower.POWER_ID)) {
                __instance.state.update(-Gdx.graphics.getDeltaTime());
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AnimationState.class, "update");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }*/

    @SpirePatch(clz = AnimationState.class, method = SpirePatch.CLASS)
    private static class FreezeFlag {
        //Used to hold the charges gained this combat
        public static SpireField<Boolean> isFrozen = new SpireField<>(() -> Boolean.FALSE);
    }

    public static void setFreezeFlag(AnimationState s, boolean bool) {
        FreezeFlag.isFrozen.set(s, bool);
    }

    @SpirePatch(clz = HexaghostBody.class, method = SpirePatch.CLASS)
    private static class HexaghostBodyFreezeFlag {
        //Used to hold the charges gained this combat
        public static SpireField<Boolean> isFrozen = new SpireField<>(() -> Boolean.FALSE);
    }

    public static void setHexaghostBodyFreezeFlag(HexaghostBody s, boolean bool) {
        HexaghostBodyFreezeFlag.isFrozen.set(s, bool);
    }

    @SpirePatch(clz = HexaghostOrb.class, method = SpirePatch.CLASS)
    private static class HexaghostOrbFreezeFlag {
        //Used to hold the charges gained this combat
        public static SpireField<Boolean> isFrozen = new SpireField<>(() -> Boolean.FALSE);
    }

    public static void setHexaghostOrbFreezeFlag(HexaghostOrb s, boolean bool) {
        HexaghostOrbFreezeFlag.isFrozen.set(s, bool);
    }

    @SpirePatch(clz = AnimationState.class, method = "setAnimation", paramtypez = {int.class, Animation.class,boolean.class})
    public static class DoNotChangeAnimationPls {
        public static SpireReturn<?> Prefix(AnimationState __instance, int trackIndex, Animation animation, boolean loop) {
            if (FreezeFlag.isFrozen.get(__instance)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AnimationState.class, method = "addAnimation", paramtypez = {int.class, Animation.class, boolean.class, float.class})
    public static class DoNotAddAnimationsEither {
        public static SpireReturn<?> Prefix(AnimationState __instance, int trackIndex, Animation animation, boolean loop, float delay) {
            if (FreezeFlag.isFrozen.get(__instance)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = HexaghostBody.class, method = "update")
    public static class DoNotAnimateBodyPls {
        public static SpireReturn<?> Prefix(HexaghostBody __instance) {
            if (HexaghostBodyFreezeFlag.isFrozen.get(__instance)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = HexaghostOrb.class, method = "update")
    public static class DoNotAnimateOrbPls {
        public static SpireReturn<?> Prefix(HexaghostOrb __instance) {
            if (HexaghostOrbFreezeFlag.isFrozen.get(__instance)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    //Thanks Gk
    @SpirePatch2(clz = AbstractMonster.class, method = "render")
    public static class StopAnimatingPls {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                //Method call is basically the equivalent of a methodcallmatcher of an insert patch, checks the edit method against every method call in the function you#re patching
                public void edit(MethodCall m) throws CannotCompileException {
                    //If the method is from the class AnimationState and the method is called update
                    if (m.getClassName().equals(AnimationState.class.getName()) && m.getMethodName().equals("update")) {
                        m.replace("{" +
                                //This is usable and refers to the class you#re patching, can be substitued by $0 but that has extra rules
                                "if(this.hasPower("+FreezePower.class.getName()+".POWER_ID)) {" +
                                //$1 refers to the first input parameter of the method, in this case the float that Gdx.graphics.getDeltaTime() returns
                                "$1 = 0;" +
                                "}" +
                                //Call the method as normal
                                "$proceed($$);" +
                                "}");
                    }
                }
            };
        }
    }
}
