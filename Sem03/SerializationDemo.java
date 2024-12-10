package Sem03;

import java.io.*;

class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

public class SerializationDemo {
    public static void main(String[] args) {
        // Создаем объект Person
        Person person = new Person("John Doe", 30);

        // Серилизация
        serializePerson(person, "person.ser");

        // Десериализация
        Person deserializedPerson = deserializePerson("person.ser");

        // Вывод результатов
        System.out.println("Оригинальный объект: " + person);
        System.out.println("Десериализованный объект: " + deserializedPerson);
    }

    private static void serializePerson(Person person, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(person);
            System.out.println("Объект успешно сериализован в файл " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Person deserializePerson(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Person person = (Person) ois.readObject();
            System.out.println("Объект успешно десериализован из файла " + filename);
            return person;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

