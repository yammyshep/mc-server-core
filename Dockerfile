FROM alpine:3.19

# Install jre and other dependencies
RUN apk add --no-cache openjdk21-jre

# Install paper server jar
ADD https://api.papermc.io/v2/projects/paper/versions/1.20.6/builds/79/downloads/paper-1.20.6-79.jar paper.jar

# Install built plugin jar
COPY target/servercore-1.0-SNAPSHOT.jar plugins/

# Install plugin dependencies
ADD https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar plugins/
ADD https://download.luckperms.net/1541/bukkit/loader/LuckPerms-Bukkit-5.4.128.jar plugins/
ADD http://ci.extendedclip.com/job/PlaceholderAPI/193/artifact/build/libs/PlaceholderAPI-2.11.6-DEV-193.jar plugins/
ADD https://cdn.modrinth.com/data/BOtP88G0/versions/x9EDRdqn/GuiEngine-1.4.3.jar plugins/

RUN echo "eula=true" > eula.txt

EXPOSE 25565

ENTRYPOINT [ "java", "-jar", "paper.jar" ]
