package ShadowSiren;

import ShadowSiren.characters.Vivian;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;

public class CustomAnimationListener implements Player.PlayerListener {

    private final Vivian character;

    public CustomAnimationListener(Vivian character) {
        this.character = character;
    }

    public void animationFinished(Animation animation) {
        if (animation.name.equals("hat")) {
            character.playAnimation("hat");
        } else if (!animation.name.equals("idle")) {
            character.resetToIdleAnimation();
        }
    }

    @Override
    public void animationChanged(Animation animation, Animation animation1) {}

    @Override
    public void preProcess(Player player) {}

    @Override
    public void postProcess(Player player) {}

    @Override
    public void mainlineKeyChanged(Mainline.Key key, Mainline.Key key1) {}
}
