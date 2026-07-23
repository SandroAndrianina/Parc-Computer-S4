#!/bin/bash

# Définition des variables
APP_NAME="ordinateur"
SRC_DIR="src/main/java"
WEB_DIR="src/main/webapp"
BUILD_DIR="build"
LIB_DIR="lib"
TOMCAT_WEBAPPS="/opt/homebrew/opt/tomcat/libexec/webapps"
SERVLET_API_JAR="/opt/homebrew/opt/tomcat/libexec/lib/servlet-api.jar"
MYSQL_JAR="$LIB_DIR/mysql-connector-j-9.6.0.jar"
GSON_JAR="$LIB_DIR/gson-2.10.1.jar"          # <-- AJOUTÉ

# Nettoyage et création des répertoires
rm -rf $BUILD_DIR
mkdir -p $BUILD_DIR/WEB-INF/classes
mkdir -p $BUILD_DIR/WEB-INF/lib

# Vérification que les drivers/libs existent
if [ ! -f "$MYSQL_JAR" ]; then
    echo "❌ Driver MySQL non trouvé dans $LIB_DIR"
    exit 1
fi
echo "✅ Driver MySQL trouvé"

if [ ! -f "$GSON_JAR" ]; then                # <-- AJOUTÉ
    echo "❌ Gson non trouvé dans $LIB_DIR"
    exit 1
fi
echo "✅ Gson trouvé"

# Compilation des fichiers Java
echo "🔨 Compilation..."
find $SRC_DIR -name "*.java" > sources.txt
javac -cp "$SERVLET_API_JAR:$MYSQL_JAR:$GSON_JAR" -d $BUILD_DIR/WEB-INF/classes @sources.txt   # <-- GSON_JAR ajouté ici
rm sources.txt

# Copier les fichiers web
echo "📁 Copie des fichiers web..."
cp -r $WEB_DIR/* $BUILD_DIR/

# Copier les drivers/libs dans WEB-INF/lib (TRÈS IMPORTANT !)
echo "📦 Copie du driver MySQL dans le WAR..."
cp $MYSQL_JAR $BUILD_DIR/WEB-INF/lib/

echo "📦 Copie de Gson dans le WAR..."      # <-- AJOUTÉ
cp $GSON_JAR $BUILD_DIR/WEB-INF/lib/         # <-- AJOUTÉ

# Générer le fichier WAR
echo "📦 Génération du WAR..."
cd $BUILD_DIR || exit
jar -cvf $APP_NAME.war *
cd ..

# Déploiement dans Tomcat
echo "🚀 Déploiement..."
cp -f $BUILD_DIR/$APP_NAME.war $TOMCAT_WEBAPPS/

echo ""
echo "✅ Déploiement terminé !"
echo "🌐 Accéder à : http://localhost:8080/$APP_NAME"