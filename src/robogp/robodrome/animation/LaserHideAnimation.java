/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robogp.robodrome.animation;

import java.io.Serializable;

/**
 *
 * @author claudia
 */
public class LaserHideAnimation extends Animation implements Serializable{

    public LaserHideAnimation() {
        this.type = Animation.Type.LASER_HIDE;
        this.which = "";
    }

    @Override
    public boolean hasFinished(long elapsed) {
        return true;
    }
}
