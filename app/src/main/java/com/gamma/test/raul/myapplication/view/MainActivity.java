package com.gamma.test.raul.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gamma.test.raul.myapplication.R;
import com.gamma.test.raul.myapplication.control.BeerController;
import com.gamma.test.raul.myapplication.model.Beer;
import com.gamma.test.raul.myapplication.util.BeerAdapter;
import com.innovattic.rangeseekbar.RangeSeekBar;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class MainActivity extends BaseActivity {

    private static final String STATE_BEERS = "state_beers";
    private static final String LOW_PH_RANGE = "low_ph_range";
    private static final String HIGH_PH_RANGE = "high_ph_range";
    private static final String PREFERENCE_TIMESTAMP = "preference_timestamp";

    @BindView(R.id.list_beer)
    protected RecyclerView  beerList;

    @BindView(R.id.loading_layer)
    protected View loadingLayer;

    @BindView(R.id.rangeSeekBar)
    protected RangeSeekBar rangeSeekBar;

    private List<Beer> stateBeers = null;

    private BeerController controller;
    private Bundle savedInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance = savedInstanceState;
        if(savedInstanceState != null) {
            Beer[] beers = (Beer[]) savedInstanceState.getParcelableArray(STATE_BEERS);
            if (beers != null) {
                stateBeers = Arrays.asList(beers);
            }
        }

        long timestamp = preferences().getLong(PREFERENCE_TIMESTAMP,0);
        if(timestamp != 0){
            Toast.makeText(getApplicationContext(),
                    "Last Update "+ DateFormat.getDateInstance(DateFormat.LONG).format(new Date(timestamp)),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    void createView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    void createController() {
        controller = new BeerController(this);
    }

    @Override
    void setupView() {
        beerList.setHasFixedSize(true);
        beerList.setLayoutManager(new LinearLayoutManager(this));
        rangeSeekBar.setSeekBarChangeListener(new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {

            }

            @Override
            public void onStoppedSeeking() {
               filterBeerRange();

            }

            @Override
            public void onValueChanged(int i, int i1) {

            }
        });
    }

    @Override
    void refresh() {
        if(stateBeers == null) {
            loadingLayer.setVisibility(View.VISIBLE);
            controller.getBeers(new Subscriber<List<Beer>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    loadingLayer.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,
                            R.string.error_login,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(List<Beer> beers) {
                    preferences().edit().putLong(PREFERENCE_TIMESTAMP,System.currentTimeMillis()).apply();
                    loadingLayer.setVisibility(View.GONE);
                    stateBeers = beers;
                    renderBeers();
                }
            });
        }else {
            renderBeers();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Beer[] beerArray =new Beer[stateBeers.size()];
        for(int i =0; i< stateBeers.size();i++){
            beerArray[i] = stateBeers.get(i);
        }
        outState.putParcelableArray(STATE_BEERS,beerArray);
        outState.putInt(LOW_PH_RANGE,rangeSeekBar.getMinThumbValue());
        outState.putInt(HIGH_PH_RANGE,rangeSeekBar.getMaxThumbValue());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_order_1:
                Collections.sort(stateBeers, new Comparator<Beer>() {
                    @Override
                    public int compare(Beer leftbeer, Beer rightBeer) {
                        return new Float(leftbeer.getPh()).compareTo(new Float(rightBeer.getPh()));
                    }
                });

                filterBeerRange();
                break;

            case R.id.action_order_2:
                Collections.sort(stateBeers, new Comparator<Beer>() {
                    @Override
                    public int compare(Beer leftbeer, Beer rightBeer) {
                        return leftbeer.getFirst_brewed().compareTo(rightBeer.getFirst_brewed());
                    }
                });

                filterBeerRange();
                break;
            default:
                break;
        }

        return true;
    }

    private void renderBeers(){
        beerList.setAdapter(new BeerAdapter(MainActivity.this,stateBeers));

        float maxRange = 0f;
        for(Beer b:stateBeers){
            maxRange = Math.max(b.getPh(),maxRange);
        }

        rangeSeekBar.setMax(Math.round(maxRange*10));

        if(savedInstance != null) {
            rangeSeekBar.setMinThumbValue(savedInstance.getInt(LOW_PH_RANGE));
            rangeSeekBar.setMaxThumbValue(savedInstance.getInt(HIGH_PH_RANGE));
            filterBeerRange();
        }
    }

    private void filterBeerRange(){
        ArrayList<Beer> filteredBeers = new ArrayList<>();
        for(Beer b:stateBeers){
            if(b.getPh()*10 >= rangeSeekBar.getMinThumbValue() &&
                    b.getPh()*10 <= rangeSeekBar.getMaxThumbValue()){
                filteredBeers.add(b);
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        Toast.makeText(this,"PH: "+decimalFormat.format(rangeSeekBar.getMinThumbValue()/10f)+" - "
                +decimalFormat.format(rangeSeekBar.getMaxThumbValue()/10f),Toast.LENGTH_SHORT).show();
        beerList.setAdapter(new BeerAdapter(MainActivity.this,filteredBeers));
    }

    public void onBeerClick(View pView){
        Beer item = (Beer)pView.getTag();
        if(item != null){
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, item.getName());
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, item.getName()+"\n\n"
                    +item.getTagline()+"\n\n"
                    +item.getDescription());

            try {
                startActivity(Intent.createChooser(emailIntent,
                        "Enviar Correo con..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this,
                        "No hay Apps de email.",
                        Toast.LENGTH_SHORT).show();
            }


        }
    }
}
