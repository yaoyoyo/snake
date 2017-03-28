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
import java.util.Random;

/**
 * Created by haitong on 16/12/21.
 */
public class SnakeView extends View {

    private final int DIRECTION_L = 0x1001;
    private final int DIRECTION_T = 0x0011;
    private final int DIRECTION_R = 0x0110;
    private final int DIRECTION_B = 0x1100;

    private final int CANVAS_REFRESH_INTERVAL = 300;

    private final int APPLE_STROKE_WIDTH = 50;
    private final int SNAKE_STROKE_WIDTH = 50;

    private final int MOVE_DISTANCE = 50;

    private final Paint applePaint = new Paint();
    private final Paint snakePaint = new Paint();

    private final Point apple = new Point();
    private final List<Point> snakeBody = new ArrayList<>();

    private int currDirection = DIRECTION_R;
    private int lastDirection = currDirection;

    private int hCount;
    private int vCount;

    private Random random = new Random();

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
        initBound();
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

    private void initBound() {
        int width = ScreenUtils.getScreenWidth(getContext());
        int height = ScreenUtils.getScreenHeight(getContext());
        hCount = width / MOVE_DISTANCE;
        vCount = height / MOVE_DISTANCE;
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
        Point head = new Point(300, 500);
        Point body = new Point(250, 500);
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
        if (temp.x < MOVE_DISTANCE * 2 || temp.x > MOVE_DISTANCE * hCount
                || temp.y < MOVE_DISTANCE * 2 || temp.y > MOVE_DISTANCE * vCount) {
            //蛇的头部撞到了墙，GAME OVER!
            Toast.makeText(getContext(), "GAME OVER!", Toast.LENGTH_LONG).show();
            reset();
            return;
        }

        snakeBody.add(0, temp);
        lastDirection = currDirection;

        if (snakeBody.contains(apple)) {
            updateApple();
        } else {
            snakeBody.remove(snakeBody.size() - 1);
        }
    }

    private void updateApple() {
        int x = (2 + random.nextInt(hCount)) * MOVE_DISTANCE;
        int y = (2 + random.nextInt(vCount)) * MOVE_DISTANCE;
        apple.set(x, y);
        if (snakeBody.contains(apple)) {
            updateApple();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Point point : snakeBody) {
            canvas.drawPoint(point.x, point.y, snakePaint);
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
