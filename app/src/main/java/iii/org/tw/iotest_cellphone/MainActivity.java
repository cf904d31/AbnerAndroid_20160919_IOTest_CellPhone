package iii.org.tw.iotest_cellphone;

import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private TextView info;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private File droot , app1root , app2root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView) findViewById(R.id.textInfo);

        //sp = getPreferences(MODE_PRIVATE);
        sp = getSharedPreferences("game.dat",MODE_PRIVATE);
        editor = sp.edit();

        //-----取得SD卡現有的狀態
        String state = Environment.getExternalStorageState();
        Log.d("Abner",state);
        if(isExternalStorageReadable()) {
            Log.d("Abner","Read OK");
        }
        if(isExternalStorageWritable()) {
            Log.d("Abner","Write OK");
        }

        droot = Environment.getExternalStorageDirectory();
        Log.d("Abner",droot.getAbsolutePath());

        app1root = new File(droot,"Abner");
        if (!app1root.exists()) {
            if(app1root.mkdirs()) {
                Log.d("Abner","Create File OK");
            } else {
                Log.d("Abner","Create File fail");
            }
        } else {
            Log.d("Abner","File exist");
        }

        app2root = new File(droot,"Android/data" + getPackageName());
        if (!app2root.exists()) {
            if(app2root.mkdirs()) {
                Log.d("Abner","Create File OK");
            } else {
                Log.d("Abner","Create File fail");
            }
        } else {
            Log.d("Abner","File exist");
        }

    }

    //-----MODE_PRIVATE 與 MODE_APPEND 差在 MODE_APPEND示一直接上去
    public void test1(View v) {
        editor.putString("name","Abner");
        editor.putBoolean("sound",false);
        editor.putInt("stage",4);
        editor.commit();
        Toast.makeText(this,"Save OK",Toast.LENGTH_SHORT).show();

    }

    public void test2(View v) {
        String name = sp.getString("name","nobody");
        int stage = sp.getInt("stage",0);
        info.setText(name + ":" + stage);
    }

    public void test3(View v) {
        try {
            FileOutputStream fout = openFileOutput("file1.txt",MODE_APPEND);
            fout.write("Hello World!\nHello Abner!\n".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this,"Save Test3 OK", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,"Exception:" + e.toString() , Toast.LENGTH_SHORT).show();
        }
    }

    public void test4(View v) {
        info.setText("");
        try {
            FileInputStream fin = openFileInput("file1.txt");
            int c;
            while ( (c = fin.read()) != -1) {
                info.append(""+(char)c);
            }
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----這個是SD卡中的內部檔案，意即當此APP被刪除時裡面的資料亦會被刪除
    public void test5(View v) {
        File file1 = new File(app1root,"datatest.txt");

        try {
            FileOutputStream fout = new FileOutputStream(file1);
            fout.write("Test5".getBytes());
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //-----這是將APP放在SD卡中的外部檔案，意即當APP被刪除時，資料不會跟著被移除
    public void test6(View v) {
        File file1 = new File(app2root,"datatest.txt");

        try {
            FileOutputStream fout = new FileOutputStream(file1);
            fout.write("Test6".getBytes());
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void test7(View v) {
        File file = new File(app1root,"datatest.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            int c;
            while ( (c = fis.read()) != -1) {
                info.append(""+(char)c);
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void test8(View v) {
        File file = new File(app2root,"datatest.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            int c;
            while ( (c = fis.read()) != -1) {
                info.append(""+(char)c);
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //-----來自於https://developer.android.com/training/basics/data-storage/files.html
    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
