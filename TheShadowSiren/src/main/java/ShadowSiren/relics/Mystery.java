package ShadowSiren.relics;

import ShadowSiren.ShadowSirenMod;
import ShadowSiren.powers.FreezePower;
import ShadowSiren.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.CurseOfTheBell;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CallingBell;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;
import java.util.HashMap;

import static ShadowSiren.ShadowSirenMod.makeRelicOutlinePath;
import static ShadowSiren.ShadowSirenMod.makeRelicPath;

public class Mystery extends CustomRelic {

    private boolean cardsReceived = true;

    // ID, images, text.
    public static final String ID = ShadowSirenMod.makeID("Mystery");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Mystery.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Mystery.png"));

    public Mystery() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.cardsReceived = false;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        AbstractCard randomCard = AbstractDungeon.returnTrulyRandomCard();
        UnlockTracker.markCardAsSeen(randomCard.cardID);
        group.addToBottom(randomCard.makeCopy());
        AbstractDungeon.gridSelectScreen.openConfirmationGrid(group, this.DESCRIPTIONS[1]);
        //CardCrawlGame.sound.playA("BELL", MathUtils.random(-0.2F, -0.3F));
    }

    public void update() {
        super.update();
        if (!this.cardsReceived) {
            if (!AbstractDungeon.isScreenUp) {
                AbstractDungeon.combatRewardScreen.open();
                AbstractDungeon.combatRewardScreen.rewards.clear();
                AbstractDungeon.overlayMenu.proceedButton.setLabel(this.DESCRIPTIONS[2]);
            }
            ArrayList<AbstractRelic> randomRelicHelper = new ArrayList<>();
            randomRelicHelper.add(AbstractDungeon.returnRandomScreenlessRelic(RelicTier.COMMON));
            randomRelicHelper.add(AbstractDungeon.returnRandomScreenlessRelic(RelicTier.UNCOMMON));
            randomRelicHelper.add(AbstractDungeon.returnRandomScreenlessRelic(RelicTier.RARE));
            randomRelicHelper.add(AbstractDungeon.returnRandomScreenlessRelic(RelicTier.BOSS));
            randomRelicHelper.add(AbstractDungeon.returnRandomScreenlessRelic(RelicTier.SHOP));
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(randomRelicHelper.get(AbstractDungeon.relicRng.random(randomRelicHelper.size() - 1))));
            AbstractDungeon.combatRewardScreen.positionRewards();
            this.cardsReceived = true;
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
        }

        if (this.hb.hovered && InputHelper.justClickedLeft) {
            CardCrawlGame.sound.playA("CARD_OBTAIN", MathUtils.random(-0.2F, -0.3F));
            this.flash();
        }

    }

    public AbstractRelic makeCopy() {
        return new Mystery();
    }

}
