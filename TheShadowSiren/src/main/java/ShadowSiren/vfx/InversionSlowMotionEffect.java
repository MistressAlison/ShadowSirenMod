package ShadowSiren.vfx;

import ShadowSiren.util.GameSpeedController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class InversionSlowMotionEffect extends GameSpeedController.SlowMotionEffect {
    //private final AtlasRegion img = ImageMaster.WHITE_RING;
    private final AtlasRegion img = AbstractPower.atlas.findRegion("128/time");
    private final float originX = img.packedWidth / 2.0F;
    private final float originY = img.packedWidth / 2.0F;
    private final float drawX = Settings.WIDTH / 2.0F;
    private final float drawY = Settings.HEIGHT / 2.0F;
    private float effectiveSpeed;

    public InversionSlowMotionEffect(float speedDivisor, float duration) {
        super(speedDivisor, duration);
        effectiveSpeed = speedDivisor;
        color = Color.WHITE.cpy();
        rotation = MathUtils.random(15f, 100f) * MathUtils.randomSign();
    }

    @Override
    public float getSpeedDivisor() {
        return effectiveSpeed;
    }

    public void update() {
        super.update();
        if (this.duration < this.startingDuration / 2.0F) {
            this.scale = Interpolation.fade.apply(0.01F, 50.0F, this.duration / (this.startingDuration / 2.0F)) * Settings.scale;
            this.effectiveSpeed  = Interpolation.fade.apply(1F, speedDivisor, this.duration / (this.startingDuration / 2.0F));
            this.color.a  = Interpolation.fade.apply(1F, 0F, this.duration / (this.startingDuration / 2.0F));
        } else {
            this.scale = Interpolation.fade.apply(50.0F, 0.01F, (this.duration - this.startingDuration / 2.0F) / (this.startingDuration / 2.0F)) * Settings.scale;
            this.effectiveSpeed = Interpolation.fade.apply(speedDivisor, 1F, (this.duration - this.startingDuration / 2.0F) / (this.startingDuration / 2.0F));
            this.color.a = Interpolation.fade.apply(0F, 1F, (this.duration - this.startingDuration / 2.0F) / (this.startingDuration / 2.0F));
        }
        rotation += Gdx.graphics.getDeltaTime() * getSpeedDivisor() * rotation;
    }

    public void render(SpriteBatch sb) {
        //int src = sb.getBlendSrcFunc();
        //int dst = sb.getBlendDstFunc();
        //sb.setBlendFunction(GL20.GL_ONE_MINUS_DST_COLOR, GL20.GL_ONE_MINUS_SRC_COLOR);
        //sb.setColor(Color.BLACK.cpy());
        //sb.setBlendFunction(GL20.GL_ZERO, GL20.GL_ONE_MINUS_DST_ALPHA);
        sb.setColor(color);
        sb.draw(img, drawX - originX, drawY - originY, originX, originY, img.packedWidth, img.packedHeight, scale, scale, rotation);
        //sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void dispose() {
    }
}
