package bupt.astroleander.sodraggable;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SeekBar;

import bupt.astroleander.sodraggable.adapter.RecyclerGridAdapter;
import bupt.astroleander.sodraggable.databinding.ActivityMainBinding;


/**
 * <h1> ${className} </h1>
 * <p>Created by Astroleander on 2018/10/22<p>.
 * <p>
 * <p>
 *
 * @author Astroleander
 * @version 1.0.0
 *  </p>
 */
public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private RGBViewModel rgb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // using data binding replacing standard activity style
        // if you are not using data bind in google.lifecycle package, you must add next line
//        setContentView(R.layout.activity_main);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        // initialize viewmodel and send it to data-binding auto-generate class
        final RGBViewModel vm = new RGBViewModel();
        binding.setRgb(this.rgb = vm);

        // next, It will show you four popular method to register a listener to view or viewmodel

        // I - create method inner class
        Observable.OnPropertyChangedCallback propertyCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                notifyColorChanged();
            }
        };
        rgb.red.addOnPropertyChangedCallback(propertyCallback);
        rgb.blue.addOnPropertyChangedCallback(propertyCallback);
        rgb.green.addOnPropertyChangedCallback(propertyCallback);

        // one-way binding, standard Android style
        SeekBar barRed = findViewById(R.id.seel_bar_red);
        // in old style, we need manually cast class from view to components type
        SeekBar barGreen = (SeekBar) findViewById(R.id.seek_bar_g);
        SeekBar barBlue = findViewById(R.id.seek_bar_b);

        // II - create inner anonymous class
        // its most popular and useful way
        barRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // must using `MainActivity.this` to make out this
                // or this will default refer to nearest context `SeekBar.OnSeekBarChangeListener`
                MainActivity.this.rgb.red.set(progress);
                notifySeekBarChanged();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // III - create listener by implements interface in activity
        // method II and III both can get context(e.g. MainActivity.this) conveniently
        // see line.149 ~ line.160
        barGreen.setOnSeekBarChangeListener(this);

        // IV - create listener by injecting interface
        // its the most powerful way to deliver function thought different context
        // see line.165
        bupt.astroleander.sodraggable.SeekBarChangeListener.SeekBarChange seekBarChange = new SeekBarChangeListener.SeekBarChange() {
            @Override
            public void callback(int progress) {
                MainActivity.this.rgb.blue.set(progress);
                notifySeekBarChanged();
            }
        };
        // send interfaces implements to its constructor to inject method above
        bupt.astroleander.sodraggable.SeekBarChangeListener seekBarListener = new SeekBarChangeListener(seekBarChange);
        barBlue.setOnSeekBarChangeListener(seekBarListener);


        // For principle dry and make code clear
        // , we should extract different functions into functions to make activity ( especially onCreate method ) more readable
        // like that
        initGrid();
        initButton();
    }

    private void initButton() {

    }

    // initialize recyclerView
    private void initGrid() {
        // RecyclerView import in at build.gradle.dependencies

        // 1. set recyclerView
        // one-way bind in tradition way
        final RecyclerView recyclerView = findViewById(R.id.list_view);
        // 2. set recyclerView layout manager
        // generally we choose manager from defaults like GridLayoutManager,LinearLayoutManager,
        // but you can custom it
        final GridLayoutManager layoutManager = new GridLayoutManager(this,5);
        recyclerView.setLayoutManager(layoutManager);
        // 3. set recyclerView adapter
        // @see bupt.astroleander.sodraggable.adapter.RecyclerGridAdapter
        RecyclerGridAdapter adapter = new RecyclerGridAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    // manual notify function
    private void notifyColorChanged() {
        int r = this.rgb.red.get();
        int g = this.rgb.green.get();
        int b = this.rgb.blue.get();
        Log.d("RGB:",r+"\t"+g+"\t"+ b);

        // if we create a private member named mRecyclerAdapter like:
        // ``` private RecyclerGridAdapter mRecyclerAdapter; ```
        // we can call it on everywhere in this class to save your time ( and patient )
        ((RecyclerGridAdapter)((RecyclerView)findViewById(R.id.list_view)).getAdapter()).setColors(new int[]{
                r,g,b
        });
    }

    // manual notify function
    private void notifySeekBarChanged() {
        notifyColorChanged();
    }

    // implement onSeekBar callbacks
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        MainActivity.this.rgb.green.set(progress);
        notifySeekBarChanged();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

// implement onSeekBar callbacks
class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
    private SeekBarChange injectListener;

    interface SeekBarChange{
        void callback(int progress);
    }

    public SeekBarChangeListener(SeekBarChange callback){
        this.injectListener = callback;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
       injectListener.callback(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}