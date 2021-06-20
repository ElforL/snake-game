import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class App extends Application {

    final int cellWidth = 10;

    SnakeGame game = new SnakeGame(50, 50);
    Canvas canvas = new Canvas(game.width * cellWidth, game.height * cellWidth);


    @Override
    public void start(final Stage primaryStage) {

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, canvas.getWidth(), canvas.getHeight());

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Snake");
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        
        root.add(canvas, 0, 0);

        final AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                paint();
            }
        };

        Thread gameThread = new Thread(() -> {
            while (!game.isGameOver) {
                game.tick();
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case UP: game.turn(0); break;
                    case RIGHT: game.turn(1); break;
                    case DOWN: game.turn(2); break;
                    case LEFT: game.turn(3); break;
                    default: break;
                }
            }
        });

        timer.start();
        gameThread.start();
    }

    void paint(){
        final var gc = canvas.getGraphicsContext2D();

        if(game.isGameOver){
            gc.setFill(Color.RED);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(new Font(canvas.getHeight() / 10));
            gc.fillText("GAME OVER", canvas.getWidth()/2 , canvas.getHeight()/2);
            return;
        }

        for (int i = 0; i < game.width; i++) {
            for (int j = 0; j < game.height; j++) {
                int startX = j * cellWidth, startY = i * cellWidth;
                if((game.foodPos[0] == j && game.foodPos[1] == i) ||
                    (game.pPos[0] == j && game.pPos[1] == i) ||
                    game.inTail(new int[]{j, i})){

                    gc.setFill(Color.WHITE);
                    gc.fillRect(startX, startY, cellWidth, cellWidth);
                    gc.strokeRect(startX, startY, cellWidth, cellWidth);
                }else{
                    gc.setFill(Color.BLACK);
                    gc.fillRect(startX, startY, cellWidth, cellWidth);
                }
            }
        }
    }

    public static void main(final String[] args) { launch(args); }
}