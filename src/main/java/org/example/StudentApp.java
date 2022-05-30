package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.example.entity.Request;
import org.example.entity.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class StudentApp implements RequestHandler<Request, Response>
{

    @Override
    public Response handleRequest(Request request, Context context) {
        Response response = new Response();
        try {
            insertData(request,response);
        } catch (SQLException sqlException) {
            response.setId("999");
            response.setMessage("Unable to register "+sqlException);
        }
        return response;
    }

    private void insertData(Request request, Response response) throws SQLException{
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String query = getQuery(request);
        int responseCode = statement.executeUpdate(query);
        if (1 == responseCode){
            response.setId(String.valueOf(responseCode));
            response.setMessage("Successful updated data");
        }
    }

    private String getQuery(Request request){
        String query = "INSERT INTO studentINFO(name, rollno, email, address) VALUES (";
        if (request != null){
            query = query.concat("'"+request.getName()+"','"+request.getRollno()+"','"+request.getEmail()+"','"+request.getAddress()+"')");
        }
        return query;
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/studentdatabase";
        String username = "root";
        String password = "";
        Connection con = DriverManager.getConnection(url, username, password);
        return con;
    }
}
