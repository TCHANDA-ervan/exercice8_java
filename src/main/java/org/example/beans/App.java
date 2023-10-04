package org.example.beans;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) {
        String cheminFichier = "src/main/resources/personnes.csv";

        try {
            Stream<String> lignes = Files.lines(Paths.get(cheminFichier));

            // Ignorer la première ligne
            Stream<String> lignesSansEnTete = lignes.skip(1);

            // Convertir chaque ligne en un objet Personne
            List<Personne> personnes = lignesSansEnTete
                    .map(App::convertirEnPersonne)
                    .toList();

            // Trier la liste des personnes par âge croissant
            //personnes.stream().sorted((p1, p2) -> Integer.compare(p1.getAge(), p2.getAge()));

            // Créer un comparateur basé sur l'âge
            Comparator<Personne> comparateurParAge = Comparator.comparing(Personne::getAge);

            // Créer un comparateur basé sur l'âge (décroissant) puis sur le prénom (croissant)
            Comparator<Personne> comparateur = Comparator.comparing(Personne::getAge)
                    .reversed()
                    .thenComparing(Comparator.comparing(Personne::getPrenom));

            // Trier la liste des personnes en utilisant le comparateur et collecter le résultat
            List<Personne> personnesTrie = personnes.stream()
                    .sorted(comparateur)
                    .collect(Collectors.toList());

            // Trier la liste des personnes en utilisant le comparateur et collecter le résultat
            List<Personne> personnesTrie2 = personnes.stream()
                    .sorted(comparateurParAge)
                    .collect(Collectors.toList());

            // Afficher les personnes triées
            personnes.forEach(System.out::println);

        } catch (IOException e) {
            log.error("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }

    private static Personne convertirEnPersonne(String ligne) {
        String[] parties = ligne.split(",");

        String prenom = parties[0];
        int age = Integer.valueOf(parties[1]);

        return new Personne(prenom, age);
    }
}
