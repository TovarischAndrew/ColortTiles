package com.example.color2;


        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.util.AttributeSet;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.Nullable;

class Tile {
    int color;
    int left;
    int right;
    int bottom;
    int top;

    Tile(int l, int t, int r, int b, int c) {
        left = l;
        top = t;
        right = r;
        bottom = b;
        color = c;
    }

    int getColor() { // dont forget its int
        return color;
    }

    void setColor(int c) {
        color = c;
    }
}
// переименовать класс
public class NewClass extends View {
    int outline = 5;
    Tile[][] tiles = new Tile[4][4];
    boolean generating = true;
    int width, height; // ширина и высота канвы

    public NewClass(Context context) {
        super(context);
    }

    public NewClass(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // 1) заполнить массив tiles случайными цветами
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();


        int tile_width = width / 4;
        int tile_height = height / 4;
        // 2) отрисовка плиток
        // задать цвет можно, используя кисть
        Paint light = new Paint();
        light.setColor(Color.parseColor("#FBCEB5"));
        Paint dark = new Paint();
        dark.setColor(Color.parseColor("#242240"));

        light.setStyle(Paint.Style.FILL);
        dark.setStyle(Paint.Style.FILL);


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                int left = j * tile_width;
                int top = i * tile_height;
                int right = left + tile_width;
                int bottom = top + tile_height;

                Rect tile = new Rect();
                tile.set(left + outline, top + outline, right - outline, bottom - outline);
// Генерацию случайных цветов для тайлов можно вынести в новую функцию
                int color;
                if (generating) {
                    if (Math.random() > 0.5) { // math random is between 0 and 1.0 by default
                        canvas.drawRect(tile, light);
                        color = 1;
                    } else {
                        canvas.drawRect(tile, dark);
                        color = 0;
                    }
                    tiles[i][j] = new Tile(left, top, right, bottom, color);

                } else {
                    color = tiles[i][j].getColor();
                    if (color == 0) {
                        canvas.drawRect(tile, light);
                        color = 1;
                    } else {
                        canvas.drawRect(tile, dark);
                        color = 0;
                    }
                }
            }
        }
        if (generating) generating = false;
        super.onDraw(canvas); // рисуем на канвасе обращаясь к родительскому классу
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 3) получить координаты касания
        int x = (int) event.getX();
        int y = (int) event.getY();
        // 4) определить тип события
        if (event.getAction() == MotionEvent.ACTION_DOWN)// палец коснулся экрана
        {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {

                    if (tiles[i][j].left < x && tiles[i][j].right > x && tiles[i][j].top < y && tiles[i][j].bottom > y) { //neighbours
// 5) определить, какой из плиток коснулись
                        // изменить её цвет на противоположный
                        int k = i, m = j;

                        for (int ii = 0; ii < 4; ii++) { //change colour
                            for (int jj = 0; jj < 4; jj++) {

                                if (ii == k || jj == m) {
                                    if (tiles[ii][jj].getColor() == 1)
                                        tiles[ii][jj].setColor(0);
                                    else
                                        tiles[ii][jj].setColor(1);
                                }
                            }
                        }
                        break;
                    }
                }
            }
            //check if won
            int sum = 0;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    sum += tiles[i][j].getColor(); // get colors returns 0 or 1
                }
            }
            int color = 0;

            // 6) проверить, не выиграли ли вы (все плитки одного цвета)
            if (sum == 4 * 4 || sum == 0) { // if one color
                Toast toast = Toast.makeText(getContext(), // text view crashed my app
                        "You won!", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
        invalidate(); // заставляет экран перерисоваться
        return true;
    }
}
