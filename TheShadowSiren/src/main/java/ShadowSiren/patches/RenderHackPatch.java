package ShadowSiren.patches;

public class RenderHackPatch {

   /* @SpirePatch(clz = CardCrawlGame.class, method = "update")
    public static class renderPatch2 {
        @SpirePrefixPatch
        public static void shaderHack(CardCrawlGame __instance) {
            if (AbstractDungeon.player != null) {
                if (AbstractDungeon.player.stance.ID.equals(VoidStance.STANCE_ID)) {
                    //ShaderContainer.shade.begin();
                    CardCrawlGame.psb.setShader(ShaderContainer.shade);
                } else {
                    //ShaderContainer.shade.end();
                    CardCrawlGame.psb.setShader(null);
                }
            }
        }
    }*/

    /*@SpirePatch(clz = CardCrawlGame.class, method = "render")
    public static class renderPatch1 {
        @SpireInsertPatch(locator = Locator.class)
        public static void shaderHack(CardCrawlGame __instance, @ByRef SpriteBatch[] ___sb) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.stance.ID.equals(VoidStance.STANCE_ID)) {
                //sb.setShader(ShaderContainer.shade);
                ShaderContainer.shade.begin();
                //___sb[0].setShader(ShaderContainer.shade);
                CardCrawlGame.psb.setShader(ShaderContainer.shade);
                //ShaderHelper.setShader(___sb[0], ShaderHelper.Shader.GRAYSCALE);
                //sb.setColor(new Color(100,100,100,0.3f));
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "setColor");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "render")
    public static class renderPatch2 {
        @SpireInsertPatch(locator = Locator.class)
        public static void shaderHack(CardCrawlGame __instance, @ByRef SpriteBatch[] ___sb) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.stance.ID.equals(VoidStance.STANCE_ID)) {
                //sb.setShader(ShaderContainer.shade);
                //ShaderHelper.setShader(___sb[0], ShaderHelper.Shader.DEFAULT);
                ___sb[0].setShader(null);
                ShaderContainer.shade.end();
                //sb.setColor(new Color(100,100,100,0.3f));
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "end");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }*/

    /*
    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class renderPatch1 {
        @SpireInsertPatch(locator = Locator.class)
        public static void shaderHack(AbstractDungeon __instance, @ByRef SpriteBatch[] sb) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.stance.ID.equals(VoidStance.STANCE_ID)) {
                ShaderContainer.shade.begin();
                sb[0].setShader(ShaderContainer.shade);
                ShaderHelper.setShader(sb[0], ShaderHelper.Shader.GRAYSCALE);
                //sb.setColor(new Color(100,100,100,0.3f));
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractScene.class, "renderCombatRoomBg");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class renderPatch2 {
        @SpireInsertPatch(locator = Locator.class)
        public static void shaderHack(AbstractDungeon __instance, @ByRef SpriteBatch[] sb) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.stance.ID.equals(VoidStance.STANCE_ID)) {
                //sb.setShader(ShaderContainer.shade);
                ShaderHelper.setShader(sb[0], ShaderHelper.Shader.DEFAULT);
                ShaderContainer.shade.end();
                //sb.setColor(new Color(100,100,100,0.3f));
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(OverlayMenu.class, "render");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }*/
}
