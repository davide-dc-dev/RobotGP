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
public class PauseAnimation extends Animation  implements Serializable{
    public PauseAnimation(long millisec) {
        this.type = Animation.Type.PAUSE;
        this.which = "";
        this.duration = millisec;
    }

    @Override
    public boolean hasFinished(long elapsed) {
        return (elapsed >= duration);
    }    
}
