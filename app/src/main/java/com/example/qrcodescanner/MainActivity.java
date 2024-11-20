package com.example.qrcodescanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button btnScan;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        tvResult = findViewById(R.id.tvResult);

        btnScan.setOnClickListener(v -> startQRScanner());
    }

    private void startQRScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String scanContent = result.getContents();
            if (scanContent != null) {
                tvResult.setText(scanContent);
                handleScanResult(scanContent);
            }
        }
    }

    private void handleScanResult(String result) {
        if (result.startsWith("http")) {

            showVisitWebsiteDialog(result);
        }
    }

    private void showVisitWebsiteDialog(String url) {
        new AlertDialog.Builder(this)
                .setTitle("Visit Website?")
                .setMessage("Do you want to visit this website: " + url)
                .setPositiveButton("Yes", (dialog, which) -> openWebsite(url))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openWebsite(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
