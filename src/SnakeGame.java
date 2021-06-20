import java.util.ArrayList;
import java.util.Random;

public class SnakeGame {

    final int width, height;
    boolean isGameOver = false;

    int[] foodPos;
    int[] pPos;
    ArrayList<int[]> tail;
    int pLengeth;
    int direction;

    // for forcing a move
    int moveCount = 0;

    public SnakeGame(int width, int height){
        this.width = width;
        this.height = height;

        genFood();
        pPos = new int[] {width/2, height/2};
        tail = new ArrayList<int []>();
        pLengeth = 1;
        direction = 3;
    }

    private void genFood() {
        final Random random = new Random();
        foodPos = new int[]{random.nextInt(width), random.nextInt(height)};
    }

    void tick() {
        if(isGameOver) return;

        try {
            Thread.sleep(100);
        } catch (final InterruptedException e) {
        }

        step();
    }

    void step(){
        int[] nextStep = {pPos[0], pPos[1]};

        if(direction == 0){
            nextStep[1]--; 
        }else if(direction == 1){
            nextStep[0]++; 
        }else if(direction == 2){
            nextStep[1]++;
        }else{
            nextStep[0]--;
        }

        if(nextStep[0] < 0 || nextStep[0] >= width || nextStep[1] < 0 || nextStep[1] >= height || inTail(nextStep)){
            isGameOver = true;
            System.out.println("GAME OVER");
            return;
        }

        // Eating
        if(nextStep[0] == foodPos[0] && nextStep[1] == foodPos[1]){
            pLengeth++;
            genFood();
        }

        tail.add(pPos);
        if(tail.size() > pLengeth)
            tail.remove(0);
        pPos = nextStep;
    }

    boolean inTail(int[] coords){
        for (int i = 0; i < tail.size(); i++) {
            if(tail.get(i)[0] == coords[0] && tail.get(i)[1] == coords[1])
                return true;
        }
        return false;
    }

    /**
     * @param newDir
     *  <p> 0 if up </p>
     *  <p> 1 if right </p>
     *  <p> 2 if down </p>
     *  <p> 3 if left </p>
     */
    public void turn(final int newDir){
        if(newDir > 3 || newDir < 0) throw new IllegalArgumentException("Direction should be from 0 to 3: "+newDir);

        if((direction == 0 && newDir == 2) ||
           (direction == 1 && newDir == 3) ||
           (direction == 2 && newDir == 0) ||
           (direction == 3 && newDir == 1)) {
                return;
        }
        
        if(direction != newDir){
            direction = newDir;
            step();
        }
    }
}
