package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnRemoveCardFromMasterDeckRelic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class UpArrow extends CustomRelic implements OnRemoveCardFromMasterDeckRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("UpArrow");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("UpArrow.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("UpArrow.png"));

    private static final int BONUS_HP = 3;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String HP_STAT = DESCRIPTIONS[2];

    public UpArrow() {
        super(ID, IMG, OUTLINE, RelicTier.SHOP, LandingSound.MAGICAL);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BONUS_HP + DESCRIPTIONS[1];
    }

    public String getStatsDescription() {
        return HP_STAT + stats.get(HP_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(HP_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(HP_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(HP_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        UpArrow newRelic = new UpArrow();
        newRelic.stats = this.stats;
        return newRelic;
    }

    @Override
    public void onRemoveCardFromMasterDeck(AbstractCard abstractCard) {
        AbstractDungeon.player.increaseMaxHp(BONUS_HP, true);
        stats.put(HP_STAT, stats.get(HP_STAT) + BONUS_HP);
        flash();
    }
}
