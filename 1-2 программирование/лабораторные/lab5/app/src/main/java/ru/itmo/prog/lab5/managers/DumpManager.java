package ru.itmo.prog.lab5.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonParseException;

import java.io.*;
import java.time.LocalDate;
import java.util.Collection;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import ru.itmo.prog.lab5.models.Product;
import ru.itmo.prog.lab5.utility.LocalDateAdapter;
import ru.itmo.prog.lab5.utility.console.Console;

/**
 * Использует файл для сохранения и загрузки коллекции.
 * @author maxbarsukov
 */
public class DumpManager {
  private final Gson gson = new GsonBuilder()
    .setPrettyPrinting()
    .serializeNulls()
    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
    .create();

  private final String fileName;
  private final Console console;

  public DumpManager(String fileName, Console console) {
    if (!(new File(fileName).exists())) {
      fileName = "../" + fileName;
    }
    this.fileName = fileName;
    this.console = console;
  }

  /**
   * Записывает коллекцию в файл.
   * @param collection коллекция
   */
  public void writeCollection(Collection<Product> collection) {
    try (PrintWriter collectionPrintWriter = new PrintWriter(new File(fileName))) {
      collectionPrintWriter.println(gson.toJson(collection));
      console.println("Коллекция успешна сохранена в файл!");
    } catch (IOException exception) {
      console.printError("Загрузочный файл не может быть открыт!");
    }
  }

  /**
   * Считывает коллекцию из файл.
   * @return Считанная коллекция
   */
  public Collection<Product> readCollection() {
    if (fileName != null && !fileName.isEmpty()) {
      try (var fileReader = new FileReader(fileName)) {
        var collectionType = new TypeToken<PriorityQueue<Product>>() {}.getType();
        var reader = new BufferedReader(fileReader);

        var jsonString = new StringBuilder();

        String line;
        while((line = reader.readLine()) != null) {
          line = line.trim();
          if (!line.equals("")) {
            jsonString.append(line);
          }
        }

        if (jsonString.length() == 0) {
          jsonString = new StringBuilder("[]");
        }

        PriorityQueue<Product> collection = gson.fromJson(jsonString.toString(), collectionType);

        console.println("Коллекция успешна загружена!");
        return collection;

      } catch (FileNotFoundException exception) {
        console.printError("Загрузочный файл не найден!");
      } catch (NoSuchElementException exception) {
        console.printError("Загрузочный файл пуст!");
      } catch (JsonParseException exception) {
        console.printError("В загрузочном файле не обнаружена необходимая коллекция!");
      } catch (IllegalStateException | IOException exception) {
        console.printError("Непредвиденная ошибка!");
        System.exit(0);
      }
    } else {
      console.printError("Аргумент командной строки с загрузочным файлом не найден!");
    }
    return new PriorityQueue<>();
  }
}
