package com.devmobile.pooplemap.db.jdbc;


import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseImg {

    private Connection connection;

    // For Amazon Postgresql
    // private final String host = "ssprojectinstance.csv2nbvvgbcb.us-east-2.rds.amazonaws.com"

    // For Google Cloud Postgresql
    // private final String host = "35.44.16.169";

    // For Local PostgreSQL
    private final String host = "pooplemap-db.co5tqek4s7w8.us-east-1.rds.amazonaws.com";

    private final String database = "pooplemapimg";
    private final int port = 5432;
    private final String user = "postgres";
    private final String pass = "Azertyuiop456";
    private String url = "jdbc:postgresql://%s:%d/%s";
    private boolean status;

    public DatabaseImg()
    {
        this.url = String.format(this.url, this.host, this.port, this.database);
        connect();
        //this.disconnect();
    }

    private void connect()
    {
        new Thread(new Runnable() {
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
        }).start();
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
