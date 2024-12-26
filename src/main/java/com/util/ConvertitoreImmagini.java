package com.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConvertitoreImmagini {

    // Metodo per convertire un'immagine in un array di byte
    public static byte[] immagineInByte(String percorsoImmagine) throws IOException {
        Path path = Paths.get(percorsoImmagine);
        return Files.readAllBytes(path);
    }

    // Metodo per convertire un array di byte in un'immagine
    public static void byteInImmagine(byte[] immagineBytes, String percorsoOutput) throws IOException {
        Path path = Paths.get(percorsoOutput);
        Files.write(path, immagineBytes);
    }
}
