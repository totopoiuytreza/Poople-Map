package com.devmobile.pooplemap;
import java.sql.Connection;
import java.sql.DriverManager;
public class DatabaseConnection {
    private Connection connection;
    private final String user;
    private final String pass;
    private final String url;
    private boolean status;

    public DatabaseConnection(String uri, String user, String password)
    {
        this.url = "jdbc:" + uri;
        this.user = user;
        this.pass = password;
        System.out.println("Url:" + url);

        connect();
        //this.disconnect();
        System.out.println("connection status:" + status);
    }

    private void connect()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    status = true;
                    System.out.println("connected:" + status);
                }
                catch (Exception e)
                {
                    status = false;
                    System.out.print(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try
        {
            thread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.status = false;
        }
    }

    public Connection getExtraConnection()
    {
        Connection c = null;
        try
        {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, pass);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return c;
    }
}
