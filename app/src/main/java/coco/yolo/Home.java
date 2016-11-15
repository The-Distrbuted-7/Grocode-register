package coco.yolo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.*;

public class Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Intent i = getIntent();

        String id = i.getStringExtra("UserId");
        String name = i.getStringExtra("Name");
        String email = i.getStringExtra("Email");
        String password = i.getStringExtra("Password");

        TextView info = (TextView) findViewById(R.id.info);
        //info.setText("Welcome " + name + "!");
        info.setText(readMessage());



    }

    public String readMessage() {
        String tmp = "";
        try {
            String Message;
            FileInputStream fileInputStream = openFileInput("info.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            while ((Message=bufferedReader.readLine()) != null) {
                stringBuffer.append(Message + "\n");
            }
            tmp = stringBuffer.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tmp;
    }
}