package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class BlackKey extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("BlackKey");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BlackKey.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BlackKey.png"));

    HashMap<String, Integer> stats = new HashMap<>();
    private final String USES_STAT = DESCRIPTIONS[1];

    public BlackKey() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        if (room instanceof TreasureRoom) {
            this.flash();
            this.pulse = true;
        } else {
            this.pulse = false;
        }

    }

    @Override
    public void onChestOpen(boolean bossChest) {
        if (!bossChest) {
            if (this.counter == -1) this.counter = 0;
            ++this.counter;
            stats.put(USES_STAT, stats.get(USES_STAT) + 1);
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            if (AbstractDungeon.relicRng.randomBoolean(0.75F)) {
                AbstractDungeon.getCurrRoom().addRelicToRewards(RelicTier.COMMON);
            } else {
                AbstractDungeon.getCurrRoom().addRelicToRewards(RelicTier.UNCOMMON);
            }
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(AbstractDungeon.returnRandomCurse(), (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
        }
    }

    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 40;
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        BlackKey newRelic = new BlackKey();
        newRelic.stats = this.stats;
        return newRelic;
    }

    public String getStatsDescription() {
        return USES_STAT + stats.get(USES_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(USES_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(USES_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(USES_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }
}
