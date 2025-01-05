FROM alpine:3.19

# Install jre and other dependencies
RUN apk add --no-cache openjdk21-jre

WORKDIR /srv

# Install paper server jar
ADD https://api.papermc.io/v2/projects/paper/versions/1.21.4/builds/76/downloads/paper-1.21.4-76.jar paper.jar

# Install plugin dependencies
ADD https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar plugins/
ADD https://cdn.modrinth.com/data/Vebnzrzj/versions/cfNN7sys/LuckPerms-Bukkit-5.4.145.jar plugins/

RUN echo "eula=true" > eula.txt
RUN echo -e "server.basic:\n  default: true\n  children:\n    minecraft.command.op: true\n    luckperms.*: true" > permissions.yml

RUN java -jar paper.jar --initSettings

# Install built plugin jar
COPY target/servercore*.jar plugins/

EXPOSE 25565

ENTRYPOINT [ "java", "-jar", "paper.jar" ]
