package ca.georgebrown.game2011.slotmachine;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class MainActivity extends Activity {
    private int playerMoney = 200;
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

    private boolean haveMoney = false;
    private TextView hint;

    private int playTimes = 0;
    private int winTimes = 0;
    private boolean isWin = false;
    private int setWinTime;
    Button spinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super .onCreate(savedInstanceState);
        setContentView(R.layout. activity_main);

        playerBalance = findViewById(R.id.balanceText);
        playerBalance.setText(""+playerMoney);
        jackpotTxt = findViewById(R.id.jackpotText);
        jackpotTxt.setText(""+jackpot);


        Reel01 = findViewById(R.id.Reel1);
        Reel02 = findViewById(R.id.Reel2);
        Reel03 = findViewById(R.id.Reel3);

        loadImages();
        Reel01.setImageBitmap(item[0]);
        Reel02.setImageBitmap(item[0]);
        Reel03.setImageBitmap(item[0]);

        hint = findViewById(R.id.hintText);

        setWinTime = new Random().nextInt(5)+1;

        Button quit = findViewById(R.id.quitBt);
        quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        spinButton = findViewById(R.id.spinBt);
        spinButton.setEnabled(true);
        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin();
            }
        });

        Button reset = findViewById(R.id.resetBt);
        reset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                reset();
                spinButton.setEnabled(true);
            }
        });

        Button resetBet = findViewById(R.id.resetBetBt);
        resetBet.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                bet = 0;
                playerBet.setText(""+bet);
                hint.setText("");
                spinButton.setEnabled(true);
            }
        });

        betting();

    }


    public Bitmap getBitmapFromAssets(String fileName) throws IOException
    {
        AssetManager assetManager = getAssets();
        InputStream istr = assetManager.open(fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        istr.close();
        return bitmap;
    }

    public void reset()
    {
        playerMoney = 200;
        bet = 0;
        playerBalance.setText(""+playerMoney);
        playerBet.setText(""+bet);
        Reel01.setImageBitmap(item[0]);
        Reel02.setImageBitmap(item[0]);
        Reel03.setImageBitmap(item[0]);
        hint.setText("");
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

        if(bet == 0) hint.setText("You gotta place your bet");

        if(haveMoney && playerMoney>=bet && bet>0){
            runSpin();
        }else if(bet>0){
            hint.setText("Not enough money");
            spinButton.setEnabled(false);
        }
        playTimes++;
    }

    public  void runSpin(){

        int random1 = new Random().nextInt(7);
        int random2 = new Random().nextInt(7);
        int random3 = new Random().nextInt(7);

        Reel01.setImageBitmap(item[random1]);
        Reel02.setImageBitmap(item[random2]);
        Reel03.setImageBitmap(item[random3]);

        if(random1==random2 && random2==random3){
            hint.setText("You Won!");
            isWin = true;
        }else isWin = false;

        if(random1+random2+random3==0){
            playerMoney += bet*50;
            hint.setText("You Won Bonus!");     //not working
        }

        //set must win condition
        if(playTimes >= setWinTime && winTimes < 3){
            Reel01.setImageBitmap(item[random1]);
            Reel02.setImageBitmap(item[random1]);
            Reel03.setImageBitmap(item[random1]);
            hint.setText("You Won!");
            isWin = true;
            playTimes = 0;
            setWinTime = new Random().nextInt(setWinTime)+3;

            if (random1 ==0) {
                playerMoney += bet*50;
                hint.setText("You Won Bonus!");     //not working
            }
        }

        if(isWin){
            winTimes++;
            if(winTimes >= 3) winTimes = 0;
            playerMoney += bet;
            playerBalance.setText(""+playerMoney);
        }else{
            playerMoney -= bet;
            if(playerMoney < 0) playerMoney = 0;
            playerBalance.setText(""+playerMoney);
            hint.setText("You Lost!");
        }

        if(playerMoney ==0){
            bet = 0;
            playerBet.setText("" + bet);
        }

        Log.d("setWinTime","setWinTime= " + setWinTime);
        Log.d("playTimes"+"winTimes","play: "+ Integer.toString(playTimes)+" | " + "win: "+ Integer.toString(winTimes));
    }


    public void checkBet()
    {
        if(playerMoney<bet){
            haveMoney = false;
            hint.setText("Not enough money");
            playerBet.setText("" + bet);
            spinButton.setEnabled(false);
        }else{
            haveMoney = true;
            hint.setText("");
            playerBet.setText("" + bet);
            spinButton.setEnabled(true);
        }
    }

    public void betting()
    {
        playerBet = findViewById(R.id.betText);
        playerBet.setText(""+bet);

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
                        switch(finalI) {
                            case 0:
                                bet += 2;
                                checkBet();
                                if(!haveMoney) bet -=2;
                                break;
                            case 1:
                                bet += 5;
                                checkBet();
                                if(!haveMoney) bet -=5;
                                break;
                            case 2:
                                bet += 10;
                                checkBet();
                                if(!haveMoney) bet -=10;
                                break;
                            case 3:
                                bet += 20;
                                checkBet();
                                if(!haveMoney) bet -=20;
                                break;
                            case 4:
                                bet += 50;
                                checkBet();
                                if(!haveMoney) bet -=50;
                                break;
                            case 5:
                                bet += 100;
                                checkBet();
                                if(!haveMoney) bet -=100;
                                break;
                            case 6:
                                bet += 200;
                                checkBet();
                                if(!haveMoney) bet -=200;
                                break;
                            case 7:
                                bet += 500;
                                checkBet();
                                if(!haveMoney) bet -=500;
                                break;
                            default:
                                break;
                        }
                    Log.d("bet ","Bet: "+ Integer.toString(bet));
                }});
        }

    }



}
