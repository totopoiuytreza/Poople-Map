package com.devmobile.pooplemap.db.jdbc;

import com.devmobile.pooplemap.db.jdbc.entities.UserProfilePicture;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class DatabaseHandlerImg {
    private Connection connection;
    private final String host = "pooplemap-db.co5tqek4s7w8.us-east-1.rds.amazonaws.com";

    private final String database = "pooplemapimg";
    private final int port = 5432;
    private final String user = "postgres";
    private final String pass = "Azertyuiop456";
    private String url = "jdbc:postgresql://%s:%d/%s";
    private boolean status;


    public DatabaseHandlerImg() {
        this.url = String.format(this.url, this.host, this.port, this.database);
        connect();

    }


    private void connect() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    status = true;
                    System.out.println("Connected: " + status);
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


    public void insertUserProfilePicture(UserProfilePicture userProfilePicture) {
        try {
            // Check if the row already exists
            String query1 = "SELECT * FROM user_profile_picture WHERE id_user = ?";
            PreparedStatement statement1 = connection.prepareStatement(query1);
            statement1.setBigDecimal(1, new BigDecimal(userProfilePicture.getIdUser()));
            ResultSet resultSet = statement1.executeQuery();
            if (resultSet.next()) {
                updateUserProfilePicture(userProfilePicture);
                return;
            }

            String query = "INSERT INTO user_profile_picture (id_user, picture, description) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBigDecimal(1, new BigDecimal(userProfilePicture.getIdUser()));
            statement.setBytes(2, userProfilePicture.getPicture());
            statement.setString(3, userProfilePicture.getDescription());
            statement.executeUpdate();
            statement.close();
            System.out.println("User profile picture inserted successfully.");
        } catch (SQLException e) {
            System.out.println("User profile picture not inserted.");
            e.printStackTrace();
        }
    }

    public CompletableFuture<UserProfilePicture> getUserProfilePictureAsync(BigInteger idUser) {
        return CompletableFuture.supplyAsync(() -> {
            UserProfilePicture userProfilePicture = null;
            try {
                String query = "SELECT * FROM user_profile_picture WHERE id_user = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setBigDecimal(1, new BigDecimal(idUser));
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    byte[] picture = resultSet.getBytes("picture");
                    String description = resultSet.getString("description");
                    userProfilePicture = new UserProfilePicture(idUser, picture, description);
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return userProfilePicture;
        });
    }

    public void updateUserProfilePicture(UserProfilePicture userProfilePicture) {
        try {
            String query = "UPDATE user_profile_picture SET picture = ?, description = ? WHERE id_user = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBytes(1, userProfilePicture.getPicture());
            statement.setString(2, userProfilePicture.getDescription());
            statement.setBigDecimal(3, new BigDecimal(userProfilePicture.getIdUser()));
            statement.executeUpdate();
            statement.close();
            System.out.println("User profile picture updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteUserProfilePicture(BigInteger idUser) {
        try {
            String query = "DELETE FROM user_profile_picture WHERE id_user = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBigDecimal(1, new BigDecimal(idUser));
            statement.executeUpdate();
            statement.close();
            System.out.println("User profile picture deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}