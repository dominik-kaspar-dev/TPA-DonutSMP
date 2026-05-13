#!/bin/sh

set -e

PRG="$0"
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done

DIRNAME=`dirname "$PRG"`
SCRIPT_NAME=`basename "$PRG"`
APP_HOME=`cd "$DIRNAME" > /dev/null && pwd -P` || exit 1

if [ ! -d "$APP_HOME/.mvn" ]; then
    echo "Error: Maven wrapper not found in $APP_HOME"
    exit 1
fi

MAVEN_PROJECTBASEDIR="${APP_HOME}"
MAVEN_OPTS="-Xmx1024m"

if [ -f "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties" ]; then
    . "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"
fi

exec java $MAVEN_OPTS -classpath "$APP_HOME/.mvn/wrapper/maven-wrapper.jar" \
    org.apache.maven.wrapper.MavenWrapperMain "$@"
