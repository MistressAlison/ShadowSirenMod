package ShadowSiren.patches;

//import ShadowSiren.cards.interfaces.MultiCardPreviewHack;

public class SCVMultiPreviewPatch {
    /*@SpirePatch(clz = SingleCardViewPopup.class, method = "update")
    public static class UpdateListener {
        @SpirePrefixPatch
        public static void updateListener(SingleCardViewPopup __instance, @ByRef AbstractCard[] ___card) {
            if (___card[0] instanceof MultiCardPreviewHack) {
                AbstractCard c = ((MultiCardPreviewHack) ___card[0]).getCurrentPreview(___card[0]);
                if (___card[0] != c) {
                    ___card[0] = c;
                    //This fixes one issue, but is super inefficient
                    ReflectionHacks.RMethod m = ReflectionHacks.privateMethod(__instance.getClass(), "loadPortraitImg");
                    m.invoke(__instance);
                }
            }
        }
    }*/
}
