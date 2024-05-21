package ru.maxima.dao;

import org.springframework.stereotype.Component;
import ru.maxima.models.Person;

import javax.swing.text.html.Option;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
            PreparedStatement statement = connection.prepareStatement("select * from person where id= ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
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
        Long id=0L;
        Optional<Long> optional = getAllPeople().stream()
                .map(Person::getId).max(Comparator.naturalOrder());
        if(optional.isPresent()){
            id= optional.get();
        }

        try {
            PreparedStatement statement = connection.prepareStatement
                    ("Insert into person(id,name,email,age) values(?,?,?,?)");
            statement.setLong(1, ++id);
            statement.setString(2, person.getName());
            statement.setString(3, person.getEmail());
            statement.setInt(4, person.getAge());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Person personFromView, Long id) {
        try {
            PreparedStatement statement = connection.prepareStatement
                    ("update person set name= ?, email= ?, age= ? where id= " + id);
            statement.setString(1, personFromView.getName());
            statement.setString(2, personFromView.getEmail());
            statement.setInt(3, personFromView.getAge());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(Long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("delete from person where id= ?");
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
