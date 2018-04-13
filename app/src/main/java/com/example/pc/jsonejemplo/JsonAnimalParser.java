package com.example.pc.jsonejemplo;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 12/04/2018.
 */

public class JsonAnimalParser {


    public List<Animal> leerFlujoJson(InputStream in) throws IOException {
        // Nueva instancia JsonReader
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            // Leer Array
            return leerArrayAnimales(reader);
        } finally {
            reader.close();
        }

    }



    public List<Animal> leerArrayAnimales(JsonReader reader) throws IOException {
        // Lista temporal
        ArrayList<Animal> animales = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            // Leer objeto
            animales.add(leerAnimal(reader));
        }
        reader.endArray();
        return animales;
    }

    public Animal leerAnimal(JsonReader reader) throws IOException {
        // Variables locales
        String especie = null;
        String descripcion = null;
        String imagen = null;

        // Iniciar objeto
        reader.beginObject();

        /*
        Lectura de cada atributo
         */
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "especie":
                    especie = reader.nextString();

                    break;
                case "descripcion":
                    descripcion = reader.nextString();
                    break;
                case "imagen":
                    imagen = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Animal(especie, descripcion, imagen);
    }

}