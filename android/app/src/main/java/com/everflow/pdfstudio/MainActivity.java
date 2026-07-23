package com.everflow.pdfstudio;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.webkit.JavascriptInterface;

import com.getcapacitor.BridgeActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MainActivity extends BridgeActivity {

    // Holds a file (base64) handed in by another app via a VIEW/SEND intent,
    // until the web app asks for it through the AndroidFile JS interface.
    private String pendingJson = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Let the web app pull any file this activity was launched with.
        getBridge().getWebView().addJavascriptInterface(new FileBridge(), "AndroidFile");

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
        if (pendingJson != null && getBridge() != null && getBridge().getWebView() != null) {
            getBridge().getWebView().post(() ->
                getBridge().getWebView().evaluateJavascript(
                    "window.__checkAndroidFile && window.__checkAndroidFile();", null));
        }
    }

    private void handleIntent(Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        Uri uri = null;
        if (Intent.ACTION_VIEW.equals(action)) {
            uri = intent.getData();
        } else if (Intent.ACTION_SEND.equals(action)) {
            uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        }
        if (uri == null) return;
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            in.close();
            String b64 = Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP);
            JSONObject o = new JSONObject();
            o.put("name", displayName(uri));
            o.put("b64", b64);
            pendingJson = o.toString();
        } catch (Exception e) {
            pendingJson = null;
        }
    }

    private String displayName(Uri uri) {
        String name = null;
        try {
            Cursor c = getContentResolver().query(uri, null, null, null, null);
            if (c != null) {
                int idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (c.moveToFirst() && idx >= 0) name = c.getString(idx);
                c.close();
            }
        } catch (Exception ignored) {}
        if (name == null || name.isEmpty()) name = uri.getLastPathSegment();
        if (name == null) name = "document";
        if (!name.contains(".")) {
            String type = getContentResolver().getType(uri);
            name += (type != null && type.contains("epub")) ? ".epub" : ".pdf";
        }
        return name;
    }

    public class FileBridge {
        @JavascriptInterface
        public String getPendingFile() {
            String j = pendingJson;
            pendingJson = null;
            return j;
        }
    }
}
