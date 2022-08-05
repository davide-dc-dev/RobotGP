package robogp.robodrome.animation;

import java.io.Serializable;


public class BeltState implements Serializable {
        private int circleX;
        private int circleY;
        private int circleRad;
        private int currentX;
        private int currentY;
        private boolean visible;
        private boolean fired;
        
        public void setCirclePosition(int x, int y) {
            circleX = x;
            circleY = y;
        }
        
        public void setCurrentPosition(int x, int y) {
            currentX = x;
            currentY = y;
        }

        /**
         * @return the circleX
         */
        public int getCircleX() {
            return circleX;
        }

        /**
         * @return the circleY
         */
        public int getCircleY() {
            return circleY;
        }

        /**
         * @return the circleRad
         */
        public int getCircleRadius() {
            return circleRad;
        }

        /**
         * @param circleRad the circleRad to set
         */
        public void setCircleRadius(int circleRad) {
            this.circleRad = circleRad;
        }

        /**
         * @return the currentX
         */
        public int getCurrentX() {
            return currentX;
        }

        /**
         * @return the currentY
         */
        public int getCurrentY() {
            return currentY;
        }

        public boolean isVisible() {
            return visible;
        }

        /**
         * @param visible the visible to set
         */
        public void setVisible(boolean visible) {
            this.visible = visible;
        }

    }

