package haitong.yht.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haitong on 16/12/21.
 */
public class SnakeView extends View {

    private final int DIRECTION_L = 0x0000;
    private final int DIRECTION_T = 0x0001;
    private final int DIRECTION_R = 0x0002;
    private final int DIRECTION_B = 0x0003;

    private final int CANVAS_REFRESH_INTERVAL = 1000;

    private final int APPLE_STROKE_WIDTH = 40;
    private final int SNAKE_STROKE_WIDTH = 20;

    private final int MOVE_DISTANCE = 100;

    private final Paint applePaint = new Paint();
    private final Paint snakePaint = new Paint();

    private final Point apple = new Point();
    private final List<Point> snakeBody = new ArrayList<>();

    private int direction = DIRECTION_R;

    private final Runnable refresh = new Runnable() {
        @Override
        public void run() {
            SnakeView.this.invalidate();
        }
    };

    public SnakeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initApple();
        initSnake();
    }

    private void initPaint() {
        applePaint.setAntiAlias(true);
        applePaint.setColor(Color.RED);
        applePaint.setStyle(Paint.Style.FILL);
        applePaint.setStrokeWidth(APPLE_STROKE_WIDTH);
        snakePaint.setAntiAlias(true);
        snakePaint.setColor(Color.WHITE);
        snakePaint.setStyle(Paint.Style.FILL);
        snakePaint.setStrokeWidth(SNAKE_STROKE_WIDTH);
    }

    private void initSnake() {
        Point head = new Point(600, 500);
        Point tail = new Point(200, 500);
        snakeBody.add(head);
        snakeBody.add(tail);
    }

    private void initApple() {
        apple.set(900, 200);
    }

    private void updateSnake() {
        Point head = snakeBody.get(0);
        switch (direction) {
            case DIRECTION_L:
                head.x -= MOVE_DISTANCE;
                break;
            case DIRECTION_T:
                head.y -= MOVE_DISTANCE;
                break;
            case DIRECTION_R:
                head.x += MOVE_DISTANCE;
                break;
            case DIRECTION_B:
                head.y += MOVE_DISTANCE;
                break;
            default:
                head.x += MOVE_DISTANCE;
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = snakeBody.size();
        for (int i = 0; i < size - 1; i++) {
            Point p1 = snakeBody.get(i);
            Point p2 = snakeBody.get(i + 1);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, snakePaint);
        }
        canvas.drawPoint(apple.x, apple.y, applePaint);
        this.postDelayed(refresh, CANVAS_REFRESH_INTERVAL);
    }

}
