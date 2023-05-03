package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.MainActivity;
import com.example.roguelikesurvival.R;

public class GameOver extends View implements View.OnClickListener {

        private Context context;

    public GameOver(Context context) {
            super(context);
            this.context = context;
            setOnClickListener(this);
        }

        @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String text = "Game Over";
        float x = 800;
        float y = 200;
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.gameOver);
        paint.setColor(color);
        float textSize = 150;
        paint.setTextSize(textSize);
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
