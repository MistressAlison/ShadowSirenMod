package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.FreezePower;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class IceStorm extends CustomRelic {

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("IceStorm");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("IceStorm.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("IceStorm.png"));

    private static final int FREEZE = 1;
    private static final int TURN = 4;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String FREEZE_STAT = DESCRIPTIONS[3];

    public IceStorm() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TURN + DESCRIPTIONS[1] + FREEZE + DESCRIPTIONS[2];
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (!grayscale) {
            ++this.counter;
        }
        if (this.counter == TURN) {
            this.beginLongPulse();
        }

    }

    @Override
    public void onPlayerEndTurn() {
        if (this.counter == TURN) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE.cpy(), true));
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                if (!aM.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(aM, AbstractDungeon.player, new FreezePower(aM, FREEZE), FREEZE, true));
                    aM.tint.color = Color.BLUE.cpy();
                    aM.tint.changeColor(Color.WHITE.cpy());
                    stats.put(FREEZE_STAT, stats.get(FREEZE_STAT) + FREEZE);
                }
            }
            this.stopPulse();
            this.grayscale = true;
            this.counter = -1;
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        IceStorm newRelic = new IceStorm();
        newRelic.stats = this.stats;
        return newRelic;
    }

    public String getStatsDescription() {
        return FREEZE_STAT + stats.get(FREEZE_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(FREEZE_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(FREEZE_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(FREEZE_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }
}
