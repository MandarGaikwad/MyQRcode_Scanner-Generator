package mandar.gauri.myqrapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    //private static final String TAG = "Certificate Scanner";
    TextView barcodeResult;
    public String bhai_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        barcodeResult = findViewById(R.id.textview2);
        isCameraPermissionGranted();
    }

    private boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT>=23){
            if (checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                //Log.v(TAG,"Permission is granted");
                Toast.makeText(this, "Permission is Granted", Toast.LENGTH_SHORT).show();
                return true;
            }else {
                //Log.v(TAG,"Permission is revoked");
                Toast.makeText(this, "Permission is Revoked", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
                return false;
            }
        }else
        {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            //Log.v(TAG,"Permission Granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED);
        //Log.v(TAG,"Permission: " + permissions[0] + "was" + grantResults[0]);
        Toast.makeText(this, "Permission: " + permissions[0] + "was" + grantResults[0], Toast.LENGTH_SHORT).show();
    }

    public void scanBarcode(View view) {
        Intent intent = new Intent(this, ScanQR.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    bhai_value = barcode.displayValue;
                    barcodeResult.setMovementMethod(new ScrollingMovementMethod()); //scroll text view
                    barcodeResult.setText("Barcode Value : " + bhai_value);
                    Toast.makeText(this, bhai_value, Toast.LENGTH_LONG).show();
                } else {
                    barcodeResult.setText("No Barcode Captured");
                }
            }
        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out:
                auth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void GenerateQRcode(View view) {
        Intent intent = new Intent(this, GenerateQR.class);
        startActivityForResult(intent, 0);
    }
}
