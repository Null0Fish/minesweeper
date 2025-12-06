import processing.core.PApplet;
public class Timer {
    final private PApplet p;
    // time that the timer started
    private int startTime;
    // time that the timer ended at
    private int endTime;
    // true if timer is paused if it is not paused then is true
    private boolean isPaused;
    // the time at which the timer was paused at
    private int pausedTime;
    // the total time that the timer has been paused
    private int timeWhilePaused;
    // true if timer is running else false

    private boolean isGoing;

    /**
     * @param p the PApplet that the timer will be based on
     */
    public Timer (PApplet p) {
        this.p = p;
        startTime = 0;
        endTime = 0;
        isPaused = false;
        pausedTime = 0;
        isGoing = false;
    }

    /**
     * @return the time that a timer has been running, if a timer has not been started returns 0
     * if a timer has ended returns the total time the timer ran
     */
    public int getTime () {
        if(endTime != 0)
            return endTime - timeWhilePaused - startTime;
        if(startTime == 0)
            return 0;
        if(isPaused)
            return pausedTime - startTime - timeWhilePaused;
        return p.millis() - startTime - timeWhilePaused;
    }

    /**
     * starts a timer with a start time of when this is called
     */
    public void start () {
        startTime = p.millis();
        isGoing = true;
    }

    /**
     * ends the timer, getTime() will now return the time when this was called
     */
    public void end () {
        if(isGoing) {
            endTime = p.millis();
            isGoing = false;
        }
    }

    /**
     * resets the timer setting startTIme and endTIme to the default value of -1
     */
    public void reset () {
        startTime = 0;
        endTime = 0;
    }

    /**
     * pauses the timer
     */
    public void pause(){
        if(isPaused){
            timeWhilePaused += p.millis() - pausedTime;
            pausedTime = 0;
        } else{
            pausedTime = p.millis();
        }
        isPaused = !isPaused;
    }

    public boolean isPaused(){
        return isPaused;
    }
}