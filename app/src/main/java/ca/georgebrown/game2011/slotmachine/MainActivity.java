package ca.georgebrown.game2011.slotmachine;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class MainActivity extends Activity {

    private int playerMoney = 300;
    private TextView playerBalance;
    private int jackpot = 10000;
    private TextView jackpotTxt;
    private int bet = 0;
    private TextView playerBet;

    private int itemCount = 7;
    private Bitmap[] item = new Bitmap[itemCount];

    private ImageView Reel01;
    private ImageView Reel02;
    private ImageView Reel03;

    private Button[] betButton = new Button[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super .onCreate(savedInstanceState);
        setContentView(R.layout. activity_main);

        playerBalance = findViewById(R.id.balanceText);
        playerBalance.setText(""+playerMoney);
        jackpotTxt = findViewById(R.id.jackpotText);
        jackpotTxt.setText(""+jackpot);
        playerBet = findViewById(R.id.betText);
        playerBet.setText(""+bet);

        Reel01 = findViewById(R.id.Reel1);
        Reel02 = findViewById(R.id.Reel2);
        Reel03 = findViewById(R.id.Reel3);

        loadImages();
        Reel01.setImageBitmap(item[0]);
        Reel02.setImageBitmap(item[0]);
        Reel03.setImageBitmap(item[0]);

        Button quit = findViewById(R.id.quitBt);
        quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button spin = findViewById(R.id.spinBt);
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                playerBalance.setText("~~~Hello~~~");
                spin();
            }
        });

        betting();


//        Bitmap imageReel1 = null;
//        try {
//            imageReel1 = getBitmapFromAssets("pic/img_item1.png");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Reel01.setImageBitmap(imageReel1);

    }

    public Bitmap getBitmapFromAssets(String fileName) throws IOException
    {
        AssetManager assetManager = getAssets();
        InputStream istr = assetManager.open(fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        istr.close();
        return bitmap;
    }


    public void loadImages(){

        for(int i=0; i<itemCount; ++i)
        {
            item[i] = null;
            try {
                int fileName = i+1;
                String imgPath = "pic/img_item"+fileName+".png";
                item[i]= getBitmapFromAssets(imgPath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void spin() {

        int random1 = new Random().nextInt(8);
        int random2 = new Random().nextInt(8);
        int random3 = new Random().nextInt(8);

        Reel01.setImageBitmap(item[random1]);
        Reel02.setImageBitmap(item[random2]);
        Reel03.setImageBitmap(item[random3]);
    }


    public void betting(){

        for(int i=0; i<betButton.length; ++i)
        {
            String betButtonId = "betBt"+(i+1);
            betButton[i] = findViewById(R.id.betBt1);

            int resID = getResources().getIdentifier(betButtonId, "id", getPackageName());
            betButton[i] = (findViewById(resID));
            final int finalI = i;
            betButton[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    switch(finalI)
                    {
                        case 0:
                            bet += 1;
                            playerBet.setText(""+bet);
                            break;
                        case 1:
                            bet +=5;
                            playerBet.setText(""+bet);
                            break;
                        case 2:
                            bet +=10;
                            playerBet.setText(""+bet);
                            break;
                        case 3:
                            bet +=20;
                            playerBet.setText(""+bet);
                            break;
                        case 4:
                            bet +=50;
                            playerBet.setText(""+bet);
                            break;
                        case 5:
                            bet +=100;
                            playerBet.setText(""+bet);
                            break;
                        case 6:
                            bet +=200;
                            playerBet.setText(""+bet);
                            break;
                        case 7:
                            bet +=500;
                            playerBet.setText(""+bet);
                            break;
                        default:
                            break;
                    }
                }});
        }

    }


}
