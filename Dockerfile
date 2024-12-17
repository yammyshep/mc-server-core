FROM alpine:3.19

# Install jre and other dependencies
RUN apk add --no-cache openjdk21-jre

# Install paper server jar
ADD https://api.papermc.io/v2/projects/paper/versions/1.21.3/builds/81/downloads/paper-1.21.3-81.jar paper.jar

# Install plugin dependencies
ADD https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar plugins/

RUN echo "eula=true" > eula.txt

# Install built plugin jar
COPY target/servercore*.jar plugins/

EXPOSE 25565

ENTRYPOINT [ "java", "-jar", "paper.jar" ]
