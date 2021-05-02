package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class LetterP extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("LetterP");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("LetterP.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("LetterP.png"));

    //private static final ArrayList<AbstractMonster> pList = new ArrayList<>();
    private static final int DAMAGE_PERCENTILE = 25;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String DAMAGE_STAT = DESCRIPTIONS[5];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[6];
    private final String ACTIVATION_STAT = DESCRIPTIONS[7];

    public LetterP() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[2] + DAMAGE_PERCENTILE + DESCRIPTIONS[3];
    }

    @Override
    public void atPreBattle() {
        //pList.clear();
        boolean activated = false;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.name.contains(DESCRIPTIONS[1].toLowerCase()) || m.name.contains(DESCRIPTIONS[1].toUpperCase())) {
                this.addToBot(new LoseHPAction(m, AbstractDungeon.player, m.maxHealth * DAMAGE_PERCENTILE/100, AbstractGameAction.AttackEffect.FIRE));
                m.name = m.name.replace(DESCRIPTIONS[1].toLowerCase(),"");
                m.name = m.name.replace(DESCRIPTIONS[1].toUpperCase(),"");
                activated = true;
                //pList.add(m);
                stats.put(DAMAGE_STAT, stats.get(DAMAGE_STAT) + m.maxHealth * DAMAGE_PERCENTILE/100);
                stats.put(ACTIVATION_STAT, stats.get(ACTIVATION_STAT) + 1);
            }
        }
        if (activated) {
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        LetterP newRelic = new LetterP();
        newRelic.stats = this.stats;
        return newRelic;
    }

    public String getStatsDescription() {
        return DAMAGE_STAT + stats.get(DAMAGE_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(DAMAGE_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        builder.append(ACTIVATION_STAT).append(stats.get(ACTIVATION_STAT));
        return builder.toString();
    }

    public void resetStats() {
        stats.put(DAMAGE_STAT, 0);
        stats.put(ACTIVATION_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(DAMAGE_STAT));
        statsToSave.add(stats.get(ACTIVATION_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(DAMAGE_STAT, jsonArray.get(0).getAsInt());
            stats.put(ACTIVATION_STAT, jsonArray.get(1).getAsInt());
        } else {
            resetStats();
        }
    }
}
