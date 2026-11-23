Utilisation de l’IA — TP 1
1. Compiler le projet avec Java 21
   Problème rencontré
   IntelliJ affichait Java 21, mais la compilation échouait sur les triples guillemets dans Bb.java. Le message d’erreur mentionnait encore -source 8.

Raison de l’utilisation de l’IA
Je voulais comprendre pourquoi le changement de version dans IntelliJ n’avait pas réglé l’erreur. J’ai partagé une capture, puis mon pom.xml.

Prompt utilisé
« tjr ca » (avec une capture de l’erreur de compilation).

Réponse de l’IA — extrait après lecture du pom.xml
« IntelliJ est bien réglé sur Java 21, mais le pom.xml de tp1-java contient encore maven.compiler.source et maven.compiler.target à 8. C’est ce qui provoque l’erreur sur les """ dans Bb.java. »

Ce que j’ai appris
J’avais choisi le JDK 21 dans IntelliJ, mais mon projet demandait toujours à Maven de compiler en Java 8. J’ai compris à quoi sert la ligne <maven.compiler.release>21</maven.compiler.release> dans le pom.xml. Dans le premier test, Gemini indique que la clé est invalide. Dans le deuxième, il précise que le rôle system est refusé dans les messages de la conversation.

J’ai aussi utilisé l’IA pour générer le rôle bonus « La Pink Ward » sur le thème de League of Legends, ainsi que la consigne qui dit à Gemini comment répondre.