package com.lfo.guess;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.tvrank)
    TextView tvrank;
    @BindView(R.id.tvcondition)
    TextView tvcondition;
    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.but1)
    Button but1;
    @BindView(R.id.butregard)
    Button butregard;
    @BindView(R.id.cardLayout)
    LinearLayout cardLayout;
    @BindView(R.id.adView1)
    NativeExpressAdView adView1;
    @BindView(R.id.buttonLayout)
    LinearLayout buttonLayout;

    private int round = 0;//回合數
    private int nowround = 0;//回合數
    private int regardtimes = 0;
    private Player player = new Player();
    float x = 0;//for ValueAnimator
    float y = 0;//for ValueAnimator
    private ValueAnimator animation1;
    private ValueAnimator animation2;

    private Animation animationbut1;
    private int repeattimes = 0;//重複的次數
    private int temp = 0;
    private Context context;
    private AnimatorSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.gameactivity);
        ButterKnife.bind(this);
        try {
            MobileAds.initialize(this, "ca-app-pub-4642124283463221~7540466680");

//            if(BuildConfig.DEBUG){
//                AdRequest request = new AdRequest.Builder()
//                        .addTestDevice("2D224CBCE8B0E73B245DF17363DE4FAA")
//                        .build();
//                adView1.loadAd(request);
//                Log.i("isTestDevice:", "" + request.isTestDevice(this));
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String tag = this.getClass().getName().toString();
        GoogleHandler.Companion.getInstance().sendAnalytics(tag, tag, "Activity");

        Intent intent = this.getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                round = bundle.getInt("round");
                //Log.d("round:",""+round);

            }
        }

        but1.setVisibility(View.VISIBLE);
        butregard.setVisibility(View.VISIBLE);
        butregard.setEnabled(false);
        but1.setEnabled(true);
        imageView1.setEnabled(false);
        imageView2.setEnabled(false);
        imageView3.setEnabled(false);//一開始只能按下開始洗牌but1 離開遊戲 再來一局
        setListener();
        animationbut1 = AnimationUtils.loadAnimation(this, R.anim.scale);
        but1.startAnimation(animationbut1);


    }


    private void rankprocess(int iswin, Player player) {

        int div = player.wincount - player.lostcount;//勝差
        int temp = 0;
        if (iswin == 1) {
            player.wincount++;
            if (player.lastcondition == 1) {//如果上一場是獲勝的就繼續增加player.condition
                player.condition++;
                player.point = player.point + player.condition;//連勝連敗的加成
                temp += player.condition;
            } else {
                player.condition = 0;
            }
            player.lastcondition = 1;

            if (div > 0) {//勝場多直接用勝場加

                player.point += player.wincount;
                temp += player.wincount;

            } else {//敗場多用勝敗差來加
                player.point += Math.abs(div);
                temp += Math.abs(div);
            }
        } else {
            player.lostcount++;
            if (player.lastcondition == 0) {//輸了 如果上一場也是書的 累積敗場condition
                player.condition--;
                player.point += player.condition;//加上負號(condition<0)
                temp += player.condition;
            } else {
                player.condition = 0;
            }
            player.lastcondition = 0;
            if (div < 0) {
                player.point--;//在加扣一
                player.point -= player.lostcount;
                temp -= player.lostcount;

            } else {
                player.point = player.point - 2;//在加扣二
                player.point -= div;//ex: div=2  1-2
                temp -= div;

            }


        }
        tvrank.setText(String.valueOf(player.point));
        if (temp > 0) {
            tvcondition.setText("+" + String.valueOf(temp));
        } else {
            tvcondition.setText(String.valueOf(temp));
        }

        Log.d("div:", String.valueOf(div) + ",wincount:" + String.valueOf(player.wincount) + ",lostcount:" + String.valueOf(player.lostcount) + ",condition:" + String.valueOf(player.condition));
        nowround++;


        if (nowround > round) {
            gameover();

        } else {
            //判斷完是否有下一局之後
            butregard.setEnabled(false);
            but1.setEnabled(true);
        }

    }

    private void gameover() {
        tvcondition.setText("");
        butregard.setEnabled(false);
        but1.setEnabled(false);
        but1.clearAnimation();
        but1.setEnabled(false);
        but1.setVisibility(View.GONE);
        butregard.setVisibility(View.GONE);
        butregard.setEnabled(false);

        tv1.setText("過了" + round + "局 遊戲結束!");
        GoogleHandler.Companion.getInstance().sendAnalytics("gameover()", "" + player.getstrpoint(), "Method");
    }


    private void setalldisabled() {


        imageView1.setEnabled(false);
        imageView2.setEnabled(false);
        imageView3.setEnabled(false);
        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.club2127666_1282707469));
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.diamond2127666_1282707469));
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.spade2127666_1282707469));
        but1.startAnimation(animationbut1);

    }

    private void afterShufflingsetallenabled() {
        temp = 0;
        repeattimes = 0;
        //洗完排了
        butregard.setEnabled(true);
        imageView1.setEnabled(true);
        imageView2.setEnabled(true);
        imageView3.setEnabled(true);

    }


    private void Shuffling() {

        //重選交換  重選速度
        int s = (int) (Math.floor(Math.random() * (2 - 0 + 1)) + 0);//隨機取得介於最大最小值間的"整數"：
        Log.d("random:", "s:" + s);

        int speed = (int) (Math.floor(Math.random() * (50 - 1 + 1)) + 0);
        Log.d("random:速度", "speed:" + String.valueOf(speed));
        int setDurationtime = (speed) * 10;//速度10~500


        switch (s) {
            case 0:
                animateSet(imageView1, imageView2, setDurationtime);
                break;
            case 1:
                animateSet(imageView1, imageView3, setDurationtime);
                break;
            case 2:
                animateSet(imageView2, imageView3, setDurationtime);
                break;
        }

    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void wincomment() {


        int s = (int) (Math.floor(Math.random() * (10 - 0 + 1)) + 0);//隨機取得介於最大最小值間的"整數"：
        String str = "";
        switch (s) {
            case 0:
                str = "好棒!";
                break;
            case 1:
                str = "厲害!";
                break;
            case 2:
                str = "出奇的好啊!";
                break;
            case 3:
                str = "運氣很好!";
                break;
            case 4:
                str = "不錯!";
                break;
            case 5:
                str = "good!";
                break;
            case 6:
                str = "sweet!";
                break;
            case 7:
                str = "神來運轉!";
                break;
            case 8:
                str = "越來越好!";
                break;
            case 9:
                str = "強喔!";
                break;
            case 10:
                str = "高手!";
                break;

        }

        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        //tvreadme.setText(str);

    }


    private void startgame() {

        setalldisabled();
        but1.setEnabled(false);
        //butregard.setEnabled(true);
        //初始化
        tv1.setText("洗牌中");
        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.card_back1));
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.card_back1));
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.card_back1));
        imageView1.setAlpha(1.0f);
        imageView2.setAlpha(1.0f);
        imageView3.setAlpha(1.0f);
        tvcondition.setText(R.string.space);
        //先抽出重複洗牌的個數
        repeattimes = (int) (Math.floor(Math.random() * (10 - 0 + 1)) + 0);//隨機取得介於最大最小值間的"整數"：
        //亂數公式：Math.floor(Math.random() * (max - min + 1)) + min

        Log.d("random:洗牌的次數", "s:" + repeattimes);


        Shuffling();


    }

    private void setListener() {
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageView2.setAlpha(0.6f);
                imageView3.setAlpha(0.6f);
                tv1.setText("猜錯了 呵呵");
                rankprocess(0, player);
                setalldisabled();
                Log.d("setListener()", "imageView1 CLICK");

                String str = "再一次會更好";

                Toast.makeText(context, str, Toast.LENGTH_LONG).show();

            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageView1.setAlpha(0.6f);
                imageView3.setAlpha(0.6f);
                tv1.setText("猜錯了 呵呵");
                rankprocess(0, player);
                setalldisabled();
                Log.d("setListener()", "imageView2 CLICK");
                String str = "再一次會更好";

                Toast.makeText(context, str, Toast.LENGTH_LONG).show();


            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageView2.setAlpha(0.6f);
                imageView1.setAlpha(0.6f);
                tv1.setText("猜對了耶 好棒棒");
                rankprocess(1, player);
                setalldisabled();
                Log.d("setListener()", "imageView3 CLICK");
                wincomment();

            }
        });
        butregard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butregard.setEnabled(false);
                //若玩家選擇放棄 -10不計勝敗
                tv1.setText("明智的選擇!不計勝敗");
                tvcondition.setText("－１０");
                player.point -= 10;
                regardtimes++;
                but1.setText("繼續遊戲");
                but1.clearAnimation();

                tvrank.setText(String.valueOf(player.point));
                nowround++;
                setalldisabled();
                if (nowround > round) {
                    gameover();

                } else {
                    //判斷完是否有下一局之後
                    butregard.setEnabled(false);
                    but1.setEnabled(true);
                }
                Log.d("setListener()", "butregard CLICK");

            }


        });
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//startgame disable rankprocess enable
                startgame();
                but1.clearAnimation();

            }


        });

        tv1.setText("請問黑桃A哪一張?");
    }

    private void animateSet(View ig1, View ig2, int duation) {
        int x1 = (int) ig1.getX();
        int x2 = (int) ig2.getX();
        animation1 = ObjectAnimator.ofFloat(ig1, "x", x1, x2);
        animation2 = ObjectAnimator.ofFloat(ig2, "x", x2, x1);

        set = new AnimatorSet();
        set.setDuration(duation);
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (repeattimes > temp) {
                    temp++;
                    Shuffling();
                    return;
                }

                afterShufflingsetallenabled();
                tv1.setText("請選出黑桃A");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }


        });
        set.playTogether(animation1, animation2);
        set.start();
    }


}

