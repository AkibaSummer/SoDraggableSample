package bupt.astroleander.sodraggable;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import bupt.astroleander.sodraggable.utils.Converters;

/**
 * <h1> ${className} </h1>
 * <p>Created by Astroleander on 2018/11/5<p>.
 *
 * <p>
 *
 * @author Astroleander
 * @version 1.0.0
 * @description </p>
 */
public class SecondaryActivity extends AppCompatActivity{
    private EditText editR;
    private EditText editG;
    private EditText editB;

    public static final int CODE_OK = 201;
    public static final int CODE_EMPTY = 400;
    public static final int CODE_FAILURE = 404;

    public static final int CAMERA_REQUEST = 592;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        Log.e(getClass().getName(), "Start Activity");

        initResult();
        initEditTexts();
        initButtons();
        notifyIntentHandler();
    }

    private void initButtons() {
        Button camera;
        camera = findViewById(R.id.btn_camera);
        camera.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });

        Button result;
        result = findViewById(R.id.btn_result);
        result.setOnClickListener(v -> {
            Intent intent = new Intent(SecondaryActivity.this,MainActivity.class);
            intent.putExtra("r",Converters.convertToInt(editR.getText().toString()));
            intent.putExtra("g",Converters.convertToInt(editG.getText().toString()));
            intent.putExtra("b",Converters.convertToInt(editB.getText().toString()));
            setResult(CODE_OK,intent);
            finish();
        });
    }

    private void initResult() {
        setResult(CODE_EMPTY, new Intent());
    }

    private void initEditTexts() {
        editR = findViewById(R.id.edit_r);
        editG = findViewById(R.id.edit_g);
        editB = findViewById(R.id.edit_b);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        notifyIntentHandler();
    }

    private void notifyIntentHandler() {
        Log.e(getClass().getName(), "onNewIntent: Received intent");
        readIntent();
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private void readIntent() {
        Intent intent = getIntent();
        // point to original intent.extras , which is not including data
//        Bundle extra = intent.getExtras();

        Bundle bundle = intent.getBundleExtra("bundle");
        if(bundle!=null) {int red = bundle.getInt("r");
            this.editR.setText(Converters.convertToString(red));
        }

        GParcelable greenParcelable = (GParcelable)intent.getParcelableExtra("parcel");
        if(greenParcelable!= null) {int green = greenParcelable.getG();
            this.editG.setText(Converters.convertToString(green));
        }

        BSerializable blueSerializable =  (BSerializable)intent.getSerializableExtra("serialize");
        if(blueSerializable !=null){int blue = blueSerializable.getB();
            this.editB.setText(Converters.convertToString(blue)); }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST){
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    ImageView imageView = findViewById(R.id.image);
                    imageView.setImageBitmap(photo);

                    long redBucket = 0;
                    long greenBucket = 0;
                    long blueBucket = 0;
                    long pixelCount = 0;

                    for (int y = 0; y < photo.getHeight(); y++)
                    {
                        for (int x = 0; x < photo.getWidth(); x++)
                        {
                            int pixel = photo.getPixel(x, y);

                            pixelCount++;
                            redBucket += (long)Color.red(pixel);
                            greenBucket += (long)Color.green(pixel);
                            blueBucket += (long)Color.blue(pixel);
                            // does alpha matter?
                        }
                    }
                    int red = (int) (redBucket / pixelCount);
                    int green = (int) (greenBucket / pixelCount);
                    int blue = (int) (blueBucket / pixelCount);

                    this.editR.setText(Converters.convertToString(red));
                    this.editG.setText(Converters.convertToString(green));
                    this.editB.setText(Converters.convertToString(blue));
                }
            }catch (Exception exc){
                Snackbar.make(SecondaryActivity.this.findViewById(R.id.view),"Can't get photo",Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
