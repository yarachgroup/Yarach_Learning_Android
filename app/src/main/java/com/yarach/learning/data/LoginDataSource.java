package com.yarach.learning.data;

import android.os.AsyncTask;

import com.yarach.learning.data.model.LoggedInUser;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    String thisusername = "";
    String thisuserid = "";
    int check_status = 0;

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // handle loggedInUser authentication
            String userId = 0 + "";

            String apiUrl = "https://id.yarach.ru/api/v1/getData.php?id=1&token=tDEJ+uGWrV9CoRWGa7bNXA==&key=sDMN++6ZrVhFpxmHbrbIUw==&inputdata_useremail=" +
                    username + "&inputdata_userpassword=" + password + "&lang=ru";

            //new GetUrlData().execute(apiUrl);

            //TimeUnit.SECONDS.sleep(60);

            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new ByteArrayInputStream(response.toString().getBytes()));

            thisusername = doc.getDocumentElement().getElementsByTagName("user_name").item(0).getTextContent();

            LoggedInUser user =
                    new LoggedInUser(
                            userId,
                            thisusername);

            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    private class GetUrlData extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            thisusername = "Данные обрабатываются...";
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = reader.readLine()) != null)
                    buffer.append(line);

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            thisusername = result + "";

            /*String userId = 0 + "";

            LoggedInUser user =
                    new LoggedInUser(
                            userId,
                            thisusername);

            Result<LoggedInUser>login*/;
        }
    }
}