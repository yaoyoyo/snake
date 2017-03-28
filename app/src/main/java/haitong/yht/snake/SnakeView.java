package haitong.yht.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haitong on 16/12/21.
 */
public class SnakeView extends View {

    private final int DIRECTION_L = 0x1001;
    private final int DIRECTION_T = 0x0011;
    private final int DIRECTION_R = 0x0110;
    private final int DIRECTION_B = 0x1100;

    private final int CANVAS_REFRESH_INTERVAL = 1000;

    private final int APPLE_STROKE_WIDTH = 100;
    private final int SNAKE_STROKE_WIDTH = 100;

    private final int MOVE_DISTANCE = 100;

    private final Paint applePaint = new Paint();
    private final Paint snakePaint = new Paint();

    private final Point apple = new Point();
    private final List<Point> snakeBody = new ArrayList<>();

    private int currDirection = DIRECTION_R;
    private int lastDirection = currDirection;

    private final Runnable refresh = new Runnable() {
        @Override
        public void run() {
            updateSnake();
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

    private void reset() {
        initApple();
        initSnake();
        currDirection = DIRECTION_R;
        lastDirection = DIRECTION_R;
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
        Point head = new Point(400, 500);
        Point body = new Point(300, 500);
        Point tail = new Point(200, 500);
        snakeBody.clear();
        snakeBody.add(head);
        snakeBody.add(body);
        snakeBody.add(tail);
    }

    private void initApple() {
        apple.set(900, 200);
    }

    private void updateSnake() {
        snakeBody.remove(snakeBody.size() - 1);

        Point head = snakeBody.get(0);
        Point temp = new Point(head);
        switch (currDirection) {
            case DIRECTION_L:
                temp.x -= MOVE_DISTANCE;
                break;
            case DIRECTION_T:
                temp.y -= MOVE_DISTANCE;
                break;
            case DIRECTION_R:
                temp.x += MOVE_DISTANCE;
                break;
            case DIRECTION_B:
                temp.y += MOVE_DISTANCE;
                break;
            default:
                temp.x += MOVE_DISTANCE;
                break;
        }
        if (snakeBody.contains(temp)) {
            //蛇的头部撞到了自己，GAME OVER!
            Toast.makeText(getContext(), "GAME OVER!", Toast.LENGTH_LONG).show();
            reset();
            return;
        }
        if (temp.x < 100 || temp.x > 1800 || temp.y < 100 || temp.y > 900) {
            //蛇的头部撞到了墙，GAME OVER!
            Toast.makeText(getContext(), "GAME OVER!", Toast.LENGTH_LONG).show();
            reset();
            return;
        }

        snakeBody.add(0, temp);
        lastDirection = currDirection;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }
        float x = event.getX();
        float y = event.getY();
        int snakeX = snakeBody.get(0).x;
        int snakeY = snakeBody.get(0).y;
        if (Math.abs(x - snakeX) >= Math.abs(y - snakeY)) {
            if (x > snakeX) {
                currDirection = DIRECTION_R;
            } else {
                currDirection = DIRECTION_L;
            }
        } else {
            if (y > snakeY) {
                currDirection = DIRECTION_B;
            } else {
                currDirection = DIRECTION_T;
            }
        }
        if ((currDirection & lastDirection) == 0) {
            currDirection = lastDirection;
        }
        return super.onTouchEvent(event);
    }
}
