FROM alpine:3.19

# Install jre and other dependencies
ARG JAVA_VERSION=21
RUN apk add --no-cache openjdk${JAVA_VERSION}-jre

WORKDIR /srv

# Install paper server jar
ARG PAPER_VERSION=1.21.4
ADD https://mcutils.com/api/server-jars/paper/${PAPER_VERSION}/download paper.jar

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
