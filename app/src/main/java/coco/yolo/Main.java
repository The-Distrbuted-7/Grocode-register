package coco.yolo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main extends Activity {

    EditText email, password;
    String Email, Password;
    Context ctx=this;
    String NAME=null, PASSWORD=null, EMAIL=null, ID=null;
    String file_name = "info.txt";

    public String getEmail(String s) {
        String[] parts = s.split(";");

        return parts[0];
    }

    public String getPassword(String s) {
        String[] parts = s.split(";");

        return parts[1];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        email = (EditText) findViewById(R.id.main_name);
        password = (EditText) findViewById(R.id.main_password);

        Toast.makeText(this, readMessage(), Toast.LENGTH_SHORT).show();

        if(readMessage() != "") {
            String Message = readMessage();
            String emailStr = getEmail(Message);
            String passStr = getPassword(Message);
            BackGround b = new BackGround();
            b.execute(emailStr, passStr);
        }
    }

    public void main_login(View v){
        Email = email.getText().toString();
        Password = password.getText().toString();
        BackGround b = new BackGround();
        b.execute(Email, Password);
    }

    public void main_register(View v){
        startActivity(new Intent(this,Register.class));
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://sahragard.com/grocode/login.php");
                String urlParams = "email="+email+"&password="+password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err=null;
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                ID = user_data.getString("UserId");
                NAME = user_data.getString("Name");
                PASSWORD = user_data.getString("Password");
                EMAIL = user_data.getString("Email");

                //Save data
                writeMessage("BLYAT", "YOLO");
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }

            Intent i = new Intent(ctx, Home.class);
            i.putExtra("UserId", ID);
            i.putExtra("Name", NAME);
            i.putExtra("Password", PASSWORD);
            i.putExtra("Email", EMAIL);
            i.putExtra("err", err);
            startActivity(i);

        }
    }

    public void writeMessage(String s, String s2) {
        String Message = "apa";
        try {
            FileOutputStream fileOutputStream = openFileOutput(file_name,MODE_PRIVATE);
            fileOutputStream.write(Message.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Message saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readMessage() {
        String tmp = "";
        try {
            String Message;
            FileInputStream fileInputStream = openFileInput(file_name);
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

        Toast.makeText(getApplicationContext(), tmp, Toast.LENGTH_SHORT).show();
        return tmp;
    }


}