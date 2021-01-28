package com.it_intensive.eli3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import java.util.Random;


public class ActivityMain extends AppCompatActivity {
    private final int REQUEST_ACT = 1;
    private float dp;

    private Toolbar myToolbar;
    private Spinner mySpinner;
    private PathView pathView;
    private ImageView imgIconCart;
    private Button[] btnIconTargets;
    private RelativeLayout relativeMap;
    private Button btnSearch;
    private Button btnStop;

    private TextView txtDebugItem;
    private TextView txtDebugGrid;
    private TextView txtDebugCurrent;

    private Handler handleItemReciever = new Handler();
    private Handler handlePathDrawer = new Handler();
    private Handler handleCurrentReciever = new Handler();
    private Runnable runPathDrawer;

    private MediaPlayer playerStart;
    private MediaPlayer playerArrival;
    private boolean isAlreadyPlayed = false;


    private Point gridCurrent = new Point(0, 0);
    private Point gridTarget = new Point(-1, -1);


    Point[] gridStarts = new Point[9 * 5 * 3 + 9 * 3 * 2];
    Point[] gridTargets = new Point[8 * 4];
    int cntStart = 0, cntTarget = 0;
    int nextStart = 0, nextTarget = 0;
    int idxStart = 0;
    Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dp = getResources().getDisplayMetrics().density;
        myToolbar = findViewById(R.id.toolbar);
        mySpinner = findViewById(R.id.spinner);
        pathView = findViewById(R.id.path_view);
        imgIconCart = findViewById(R.id.img_icon_cart);
        relativeMap = findViewById(R.id.relative_map);
        btnSearch = findViewById(R.id.btn_search);
        btnStop = findViewById(R.id.btn_stop);

        playerStart = MediaPlayer.create(ActivityMain.this, R.raw.sound_start);
        playerArrival = MediaPlayer.create(ActivityMain.this, R.raw.sound_arrival);

        txtDebugItem = findViewById(R.id.txt_debug_item);
        txtDebugGrid = findViewById(R.id.txt_debug_grid);
        txtDebugCurrent = findViewById(R.id.txt_debug_current);

        initToolbar();
        btnStop.setVisibility(View.INVISIBLE);
        pathView.setVisibility(View.INVISIBLE);
//        imgIconTarget.setVisibility(View.INVISIBLE);
        debugFunction();

        runPathDrawer = new Runnable() {
            @Override
            public void run() {
                RequestRetriever retrieverGrid = new RequestRetriever("edu4", "co2");
                retrieverGrid.setReceiver(new IReceived() {
                    @Override
                    public void getResponseBody(String msg) {
                        final String strCurrent = parsing(msg);
                        handleCurrentReciever.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] strGrid = strCurrent.split(" ");
                                gridCurrent.x = Integer.parseInt(strGrid[0]);
                                gridCurrent.y = Integer.parseInt(strGrid[1]);
                                txtDebugCurrent.setText(strCurrent);
                            }
                        });
                    }
                });
                retrieverGrid.start();
                try {
                    retrieverGrid.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                setRelativeLocation(imgIconCart, Point.convertCartGridToLocation(gridCurrent));
//                if(gridTarget.x >= 0) drawShortestPath(gridCurrent, gridTarget);
//                handlePathDrawer.postDelayed(runPathDrawer, 500);
//                if(idxStart == cntStart) idxStart = 0;
//                gridCurrent = gridStarts[idxStart++];
//                setRelativeLocation(imgIconCart, Point.convertCartGridToLocation(gridCurrent));
//                handlePathDrawer.postDelayed(runPathDrawer, 1000);
            }
        };
        handlePathDrawer.post(runPathDrawer);

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ActivityMain.this, ActivitySearch.class);
                startActivityForResult(intent, REQUEST_ACT);
                btnSearch.setBackgroundColor(Color.BLUE);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnSearch.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.INVISIBLE);
                stopNavigation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mySpinner.setSelection(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            Toast.makeText(ActivityMain.this, "Request Failed", Toast.LENGTH_SHORT).show();
            return;
        }

        if(requestCode == REQUEST_ACT){
            String strItem = data.getStringExtra("request_item");
            txtDebugItem.setText(strItem);

            RequestController sender = new RequestController(Mart.getInstance().transKorToEng(strItem).toLowerCase());
            sender.start();
            try {
                sender.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            RequestRetriever retrieveItem = new RequestRetriever("edu4","led"); //milk에 관한 데이터 출력
            retrieveItem.setReceiver(new IReceived() {
                public void getResponseBody(final String msg) {
                    final String strItem = parsing(msg);
                    System.out.println("[!]");
                    System.out.println(msg);
                    System.out.println("[?]");
                    System.out.println(strItem);
                    handleItemReciever.post(new Runnable(){
                        @Override
                        public void run(){
                            txtDebugItem.setText(strItem);
                            Item item = Item.parseItem(strItem);
                            gridTarget = item.grid;
                            txtDebugGrid.setText(item.name + " crd: " + item.grid.x + " " + item.grid.y + " weight: " + item.weight + " price: " + item.price);
                        }
                    });

                }
            });
            retrieveItem.start();
            isAlreadyPlayed = false;

            btnStop.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.INVISIBLE);
            playerStart.start();
        }
    }

    private void initToolbar(){
        setSupportActionBar(myToolbar);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ActivityMain.this,
                R.layout.custom_spinner_item,
                getResources().getStringArray(R.array.functions));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ActivityMain.this,
                        mySpinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT)
                        .show();
                if(position == 1){
                    Intent intent = new Intent(ActivityMain.this, ActivitySearch.class);
                    startActivityForResult(intent, REQUEST_ACT);
                }
                else if(position == 2){
                    Intent intent = new Intent(ActivityMain.this, ActivityViewCart.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void debugFunction(){
        for(int i = 0; i < 15; i++)
            for(int j = 0; j < 15; j++){
                if((i / 3) % 2 == 1 && (j / 3) % 2 == 1){
                    if(i % 3 != 1 || j % 3 != 1)
                        gridTargets[cntTarget++] = new Point(i, j);
                }
                else gridStarts[cntStart++] = new Point(i, j);
            }
    }

    private void drawShortestPath(Point gridCurrent, Point gridTarget){
//        imgIconTarget.setVisibility(View.VISIBLE);
//        setRelativeLocation(imgIconTarget, Point.convertTargetGridToLocation(gridTarget));

        Point[] gridPath = Navigation.getInstance().findShortestPath(gridCurrent, gridTarget);
        Point[] crdPath = new Point[gridPath.length];

        for(int i = 0; i < crdPath.length; i++)
            crdPath[i] = Point.convertLocationToCoordinate(
                    Point.convertPathGridToLocation(gridPath[i]), dp);

        pathView.drawLocationPath(crdPath);
        pathView.setVisibility(View.VISIBLE);
        if(isAlreadyPlayed == false){
            if(Math.abs(gridCurrent.x - gridTarget.x) + Math.abs(gridCurrent.y - gridTarget.y) == 1){
                isAlreadyPlayed = true;
                playerArrival.start();
            }
        }
    }

    private void setRelativeLocation(View view, Point locView){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        Point crdView = Point.convertLocationToCoordinate(locView, dp);
        params.leftMargin = crdView.x;
        params.topMargin = crdView.y;
        view.requestLayout();
    }

    private String parsing(String msg){ //db에서 con에 있는 data만 parsing
        int start=msg.indexOf("<con>")+5;
        int end=msg.indexOf("</con>");

        msg=msg.substring(start,end);
        return msg;
    }

    private void stopNavigation(){
        gridTarget.x = -1;
//        imgIconTarget.setVisibility(View.INVISIBLE);
        pathView.setVisibility(View.INVISIBLE);
    }
}
