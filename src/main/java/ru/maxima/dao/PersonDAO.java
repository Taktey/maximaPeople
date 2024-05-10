package ru.maxima.dao;

import org.springframework.stereotype.Component;
import ru.maxima.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class PersonDAO {
    private Long PEOPLE_COUNT = 0L;


    private final String URL = "jdbc:postgresql://localhost:5432/maxima";
    private final String USERNAME = "postgres";
    private final String PASSWORD = "postgres";
    private Connection connection;

    {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Person> getAllPeople() {
        List<Person> people = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "select * from person;";
            ResultSet resultSet = statement.executeQuery(SQLQuery);
            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getLong("id"));
                person.setName(resultSet.getString("name"));
                person.setEmail(resultSet.getString("email"));
                person.setAge(resultSet.getInt("age"));
                people.add(person);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    public Person findById(final Long id) {
        Person person = new Person();
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "select * from person where id=  " + id + ";";
            ResultSet resultSet = statement.executeQuery(SQLQuery);
            while (resultSet.next()) {
                person.setId(resultSet.getLong("id"));
                person.setName(resultSet.getString("name"));
                person.setEmail(resultSet.getString("email"));
                person.setAge(resultSet.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public void save(Person person) {
        Long id = getAllPeople().stream()
                .map(Person::getId).max(Comparator.naturalOrder()).get();
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "Insert into person(id,name,email,age) values(" + ++id + ", '"
                    + person.getName() + "', '" + person.getEmail() + "', " + person.getAge() + ")";
            statement.executeUpdate(SQLQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Person personFromView, Long id) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "update person " +
                    "set name= '" + personFromView.getName() + "', email= '" + personFromView.getEmail() + "', "
                    + "age= " + personFromView.getAge() + " where id=  " + id + ";";
            statement.executeUpdate(SQLQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(Long id) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "delete from person where id= "+id;
            statement.executeUpdate(SQLQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
