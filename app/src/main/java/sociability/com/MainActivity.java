package sociability.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cv1 = findViewById(R.id.cv1);
        cv1.setOnClickListener(this);

        CardView cv2 = findViewById(R.id.cv2);
        cv2.setOnClickListener(this);

        CardView cv3 = findViewById(R.id.cv3);
        cv3.setOnClickListener(this);

    }

    private void updateCardImages(){

        if(FirstScreenActivity.short_quiz_completed == 1) {
            ImageView img = findViewById(R.id.image1);
            img.setImageResource(R.drawable.quiz_finished_card_icon);
        }

        if(FirstScreenActivity.long_quiz_completed == 1) {
            ImageView img2 = findViewById(R.id.image2);
            img2.setImageResource(R.drawable.quiz_finished_card_icon);
        }

        if(FirstScreenActivity.personal_quiz_completed == 1) {
            ImageView img3 = findViewById(R.id.image3);
            img3.setImageResource(R.drawable.quiz_finished_card_icon);
        }
    }
    @Override
    public void onClick(View v){
        Intent intent = new Intent(this, QuizActivity.class);

        switch(v.getId()){
            case R.id.cv1:
                //open quiz1
                intent.putExtra("message", "short quiz");
                startActivity(intent);
                break;
            case R.id.cv2:
                //open quiz2
                intent.putExtra("message", "long quiz");
                startActivity(intent);
                break;
            case R.id.cv3:
                //open quiz2
                intent.putExtra("message", "personal quiz");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCardImages();
    }

}
