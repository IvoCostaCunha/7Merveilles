# 7Merveilles
Le jeu de plateau [7Wonders](https://en.wikipedia.org/wiki/7_Wonders_(board_game)) codé en Java.

## Description
Cette application avec une architecture client-serveur numérise le jeu de plateau [7Wonders](https://en.wikipedia.org/wiki/7_Wonders_(board_game)) en Java avec une interface CLI.
Le jeu sera joué par des bots qui simuleront de vrais joueurs. Chaque client représente un bot et chaque bot a une stratégie differente.

## Dépendances
- [Maven](https://maven.apache.org/) (Testé with Maven@3.9.9)
- [Java](https://adoptium.net/) (Testé with Java@21)

## Build
Maven est utilisé pour build le projet.
```sh
mvn install 
cd lanceur
mvn exec:java
#Ctrl+c to exit.
```
